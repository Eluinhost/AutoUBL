package gg.uhc.autoubl

import com.github.kittinunf.result.Result
import gg.uhc.autoubl.events.RequestUuidLookupEvent
import gg.uhc.autoubl.events.UuidFailedLookupEvent
import gg.uhc.autoubl.events.UuidLookupEvent
import org.bukkit.event.Event
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import java.util.concurrent.ConcurrentLinkedQueue
import java.util.logging.Logger

class UuidLookup(
    private val apis: Apis,
    private val parser: Parser,
    private val callEvent: (Event) -> Unit,
    private val logger: Logger
): Listener {
    private val queue = ConcurrentLinkedQueue<String>()

    @EventHandler fun on(event: RequestUuidLookupEvent) {
        queue.add(event.ign)
    }

    fun flush() {
        val igns = mutableSetOf<String>()

        // Grab up to 100 unique items from the queue
        while (igns.size < 100) {
            val item = queue.poll() ?: break

            igns.add(item)
        }

        // Don't run any network requests if there is no requested IGNs
        if (igns.isEmpty())
            return

        logger.info("Looking up UUIDs for IGNS $igns")

        apis.getUuidsForNames(igns.toList()) {
            when (it) {
                is Result.Success ->
                    try {
                        val mapping = parser.parseUuidsResponse(it.value)

                        logger.info("Found matching UUIDs: $mapping")

                        callEvent(UuidLookupEvent(mapping))
                    } catch (ex: Exception) {
                        callEvent(UuidFailedLookupEvent(igns, ex))
                    }
                is Result.Failure ->
                    callEvent(UuidFailedLookupEvent(igns, it.error))
            }

        }
    }
}