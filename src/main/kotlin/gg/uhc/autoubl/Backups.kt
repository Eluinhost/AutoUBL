package gg.uhc.autoubl

import gg.uhc.autoubl.events.FetchedEvent
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import java.io.File
import java.util.logging.Logger

class Backups(
    private val file: File,
    private val runAsync: (() -> Unit) -> Unit,
    private val logger: Logger
): Listener {

    fun save(data: String) = file.writeText(data)
    fun get() = file.readText()

    @EventHandler
    fun receiveRawJson(event: FetchedEvent) {
        runAsync {
            logger.info("Saving backup to backup.json")
            save(event.raw)
            logger.info("Backup saved")
        }
    }
}