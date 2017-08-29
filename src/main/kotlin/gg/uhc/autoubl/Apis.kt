package gg.uhc.autoubl

import com.github.kittinunf.fuel.core.FuelError
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.fuel.httpPost
import com.github.kittinunf.result.Result
import com.github.salomonbrys.kotson.toJsonArray
import gg.uhc.autoubl.events.FetchFailedEvent
import gg.uhc.autoubl.events.FetchedEvent
import gg.uhc.autoubl.events.RequestFetchEvent
import org.bukkit.event.Event
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import java.util.logging.Level
import java.util.logging.Logger

class Apis(
    private val ublEndpoint: String,
    private val mojangUuidEndpoint: String,
    private val runAsync: (() -> Unit) -> Unit,
    private val callEvent: (Event) -> Unit,
    private val logger: Logger
) : Listener {

    fun getCurrentRawUbl(handler: (Result<String, FuelError>) -> Unit) =
        ublEndpoint.httpGet().responseString { _, _, result ->
            handler(result)
        }

    fun getUuidsForNames(names: List<String>, handler: (Result<String, FuelError>) -> Unit) {
        if (names.size > 100)
            throw IllegalArgumentException("Cannot request more than 100 names at once")

        mojangUuidEndpoint
            .httpPost()
            .body(names.toJsonArray().toString())
            .header("Content-Type" to "application/json")
            .responseString { _, _, result ->
                handler(result)
            }
    }

    @EventHandler
    fun onRequestForUpdate(event: RequestFetchEvent) {
        runAsync {
            logger.info("Refreshing UBL listing from server")

            getCurrentRawUbl {
                when (it) {
                    is Result.Success -> {
                        logger.info("Updated UBL from server")
                        callEvent(FetchedEvent(it.value))
                    }
                    is Result.Failure -> {
                        logger.log(Level.WARNING, "Failed to load UBL from server", it.error)
                        callEvent(FetchFailedEvent(it.error))
                    }
                }
            }
        }
    }
}