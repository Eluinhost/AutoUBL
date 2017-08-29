package gg.uhc.autoubl

import gg.uhc.autoubl.events.ParsedEvent
import gg.uhc.autoubl.events.RequestFetchEvent
import org.bukkit.ChatColor
import org.bukkit.event.Listener
import org.bukkit.event.player.AsyncPlayerPreLoginEvent
import org.bukkit.plugin.java.JavaPlugin
import java.lang.reflect.Field
import java.util.logging.Level

class Entry : JavaPlugin(), Listener {
    private fun runAsync(block: () -> Unit) {
        server.scheduler.scheduleAsyncDelayedTask(this, block)
    }

    private val callEvent = server.pluginManager::callEvent

    private val uuidField: Field? = try {
        AsyncPlayerPreLoginEvent::class.java.getField("uniqueId").apply {
            this.isAccessible = true
            logger.log(Level.INFO, "Detected UUID support, will ban by UUID")
        }
    } catch (ex: Exception) {
        logger.log(Level.INFO, "Could not detect UUID support, will ban by IGN")
        null
    }

    override fun onDisable() {}

    override fun onEnable() {
        if (!this.dataFolder.exists())
            this.dataFolder.mkdir()

        config.options().copyDefaults(true)
        config.save(this.dataFolder.resolve("config.yml"))

        if (config.getBoolean("run update checker")) {
            val updateManager = UpdateManager(this)
            updateManager.start()
        }

        val ublEndpoint = config.getString("ubl endpoint")
        val mojangUuidEndpoint = config.getString("mojang uuid endpoint")
        val backupFile = this.dataFolder.resolve("backup.json")
        val banMessageTemplate = ChatColor.translateAlternateColorCodes('&', config.getString("ban template"))
        val uninitializedMessage = ChatColor.translateAlternateColorCodes('&', config.getString("uninitialized message"))
        val failedToLookupUuidMessage = ChatColor.translateAlternateColorCodes('&', config.getString("failed to lookup uuid message"))
        val refreshMinutes = config.getLong("minutes per refresh")

        val apis = Apis(
            ublEndpoint = ublEndpoint,
            mojangUuidEndpoint = mojangUuidEndpoint,
            logger = logger,
            callEvent = callEvent,
            runAsync = this::runAsync
        )

        val parser = Parser(
            logger = logger,
            runAsync = this::runAsync,
            callEvent = callEvent
        )

        // If there is no UUID support setup a UUID lookup service
        if (uuidField == null) {
            val uuidLookup = UuidLookup(
                apis = apis,
                callEvent = callEvent,
                parser = parser,
                logger = logger
            )

            server.pluginManager.registerEvents(uuidLookup, this)

            server.scheduler.scheduleAsyncRepeatingTask(this, {
                uuidLookup.flush()
            }, 0, 20 * 5) // Every 5 seconds run UUID lookups if there is any to process
        }

        val banMatcher = BanMatcher()

        val backups = Backups(
            file = backupFile,
            runAsync = this::runAsync,
            logger = logger
        )

        val banMessageFormatter = BanMessageFormatter(banMessageTemplate)

        val firewall = Firewall(
            uuidField = uuidField,
            banMessageFormatter = banMessageFormatter,
            banMatcher = banMatcher,
            getOnlinePlayers = server::getOnlinePlayers,
            logger = logger,
            uninitializedMessage = uninitializedMessage,
            callEvent = callEvent,
            failedToLookupUuidMessage = failedToLookupUuidMessage
        )

        listOf(apis, backups, banMatcher, firewall, parser).forEach {
            server.pluginManager.registerEvents(it, this)
        }

        server.getPluginCommand("ublrefresh").executor = RefreshCommand {
            server.pluginManager.callEvent(RequestFetchEvent())
        }

        runAsync {
            // Attempt to parse from backups before starting network request updates

            try {
                // we parse manually instead of relying on FetchEvent -> ParsedEvent e.t.c. to avoid saving back to the file
                callEvent(ParsedEvent(parser.parseUblItems(backups.get())))
            } catch (ex: Exception) {
                logger.log(Level.SEVERE, "Failed to load from backup JSON file", ex)
            }

            // Start loop for auto-refreshing
            server.scheduler.scheduleSyncRepeatingTask(this, {
                callEvent(RequestFetchEvent())
            }, 0, 20 * 60 * refreshMinutes)
        }
    }
}