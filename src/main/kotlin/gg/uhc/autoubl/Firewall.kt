package gg.uhc.autoubl

import gg.uhc.autoubl.events.ParsedEvent
import gg.uhc.autoubl.events.RequestUuidLookupEvent
import gg.uhc.autoubl.events.UuidFailedLookupEvent
import gg.uhc.autoubl.events.UuidLookupEvent
import org.bukkit.entity.Player
import org.bukkit.event.Event
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.AsyncPlayerPreLoginEvent
import org.bukkit.event.player.PlayerPreLoginEvent
import java.lang.reflect.Field
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import java.util.logging.Level
import java.util.logging.Logger

class Firewall (
    private val uuidField: Field?,
    private val banMatcher: BanMatcher,
    private val logger: Logger,
    private val banMessageFormatter: BanMessageFormatter,
    private val uninitializedMessage: String,
    private val failedToLookupUuidMessage: String,
    private val getOnlinePlayers: () -> Array<Player>,
    private val callEvent: (Event) -> Unit
) : Listener {

    // these 2 extension pull the correct UUID if UUID is supported otherwise attempts to pull the UUID from the cache
    private val AsyncPlayerPreLoginEvent.safeUuid: UUID?
        get() = if (uuidField != null) uuidField.get(this) as UUID else ignToUuidCache[this.name]
    private val Player.safeUuid: UUID?
        get() = if (uuidField != null) this.uniqueId else ignToUuidCache[this.name]

    // only used when there is no UUID support
    private val ignToUuidCache: MutableMap<String, UUID> = ConcurrentHashMap()

    // Checks the list of players against the ban list and kicks them if required
    private fun kickBannedPlayers(toCheck: Iterable<Player>) = toCheck
        .mapNotNull {
            val maybeUuid = it.safeUuid ?: return@mapNotNull null // we don't know their uuid yet so we want to skip them (will be picked up once UUID lookup request is complete)

            val bans = banMatcher.checkUuidStatus(maybeUuid)

            if (bans.isEmpty())
                return@mapNotNull null // Skip those without any bans

            it to bans // return pair of player -> non empty list of bans
        }
        .forEach { (player, bans) ->
            player.kickPlayer(banMessageFormatter.format(bans.maxBy { it.expires }!!))
        }

    // when parsed event comes in kick every online player on the ban list where we can
    // runs at high priority so it runs after the ban matcher has been updated with new data
    @EventHandler(priority = EventPriority.HIGH) fun on(event: ParsedEvent) {
        kickBannedPlayers(getOnlinePlayers().asIterable())
    }

    // When the UUID lookup succeeds update the internal IGN cache and then kick every player in the lookup (that is still online) that was in the lookup
    @EventHandler fun on(event: UuidLookupEvent) {
        // Update internal IGN cache
        ignToUuidCache.putAll(event.mapping)

        // Trigger checks on the UUIDs for every player in the lookup
        kickBannedPlayers(getOnlinePlayers().filter { event.mapping.containsKey(it.name) })
    }

    // If there is a failure to lookup UUIDs kick every online player in the batch
    @EventHandler fun on(event: UuidFailedLookupEvent) {
        getOnlinePlayers()
            .filter { event.requested.contains(it.name) }
            .forEach {
                it.kickPlayer(failedToLookupUuidMessage)
            }
    }

    @EventHandler fun on(event: AsyncPlayerPreLoginEvent) {
        // Don't let anyone in until the ban list is populated the first time
        if (!banMatcher.initialized)
            event.disallow(PlayerPreLoginEvent.Result.KICK_OTHER, uninitializedMessage)

        val maybeUuid = event.safeUuid

        // If we don't know their UUID (no uuid support and their uuid has not been cached yet)
        // we want to trigger a request to lookup their UUID. Once their UUID is known
        // we will kick them if they are banned
        if (maybeUuid == null) {
            event.allow()
            callEvent(RequestUuidLookupEvent(event.name))
            return
        }

        // we know their UUID so check the ban list now
        val bans = banMatcher.checkUuidStatus(maybeUuid)

        if (bans.isEmpty()) {
            // not banned
            event.allow()
            return
        }

        logger.log(Level.INFO, "Denied login for ${event.name}, ban details: $bans")

        val chosenOne = bans.maxBy { it.expires }!!

        event.disallow(PlayerPreLoginEvent.Result.KICK_OTHER, banMessageFormatter.format(chosenOne))
    }
}