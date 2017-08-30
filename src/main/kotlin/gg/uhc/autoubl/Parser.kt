package gg.uhc.autoubl

import com.github.salomonbrys.kotson.fromJson
import com.google.gson.Gson
import gg.uhc.autoubl.events.FetchedEvent
import gg.uhc.autoubl.events.ParsedEvent
import org.bukkit.event.Event
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import java.util.*
import java.util.logging.Level
import java.util.logging.Logger

class Parser(
    private val logger: Logger,
    private val callEvent: (Event) -> Unit,
    private val runAsync: (() -> Unit) -> Unit
): Listener {
    private val gson = Gson()

    private val noDashUuidRegex = "^([a-f0-9]{8})([a-f0-9]{4})([a-f0-9]{4})([a-f0-9]{4})([a-f0-9]{12})$".toRegex(RegexOption.IGNORE_CASE)

    fun parseUblItems(raw: String): List<UblItem> = gson.fromJson(raw)

    fun parseUuidsResponse(raw: String): Map<String, UUID> =
        gson
            .fromJson<List<UuidResponseItem>>(raw)
            .mapNotNull {
                val uuid = try {
                    when (it.id.length) {
                        // In know there is more efficient ways to do this, but whatever
                        32 -> UUID.fromString(it.id.replace(noDashUuidRegex, "$1-$2-$3-$4-$5"))
                        36 -> UUID.fromString(it.id)
                        else -> throw IllegalArgumentException("invalid input length")
                    }
                } catch (ex: Exception) {
                    logger.log(Level.SEVERE, "UNABLE TO PARSE UUID FROM MOJANG: $it", ex)
                    null
                }

                if (uuid == null) {
                    null
                } else {
                    it.name to uuid
                }
            }
            .toMap()

    @EventHandler
    fun receiveRawJson(event: FetchedEvent) {
        runAsync {
            logger.info("Parsing incoming data")

            try {
                callEvent(ParsedEvent(parseUblItems(event.raw)))
                logger.info("Parsed incoming data")
            } catch (ex: Exception) {
                logger.warning("Failed to parse incoming data")
                ex.printStackTrace()
            }
        }
    }
}