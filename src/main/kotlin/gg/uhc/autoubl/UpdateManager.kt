package gg.uhc.autoubl

import gg.uhc.githubreleasechecker.ReleaseChecker
import gg.uhc.githubreleasechecker.deserialization.LatestReleaseQueryer
import gg.uhc.githubreleasechecker.zafarkhaja.semver.Version
import org.bukkit.ChatColor
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerLoginEvent
import org.bukkit.plugin.Plugin
import java.util.logging.Level

class UpdateManager(private val plugin: Plugin) {
    private val queryer = LatestReleaseQueryer("Eluinhost", "AutoUBL")
    private val checker = ReleaseChecker(plugin, queryer, true)

    private var latestVersion: Version? = null
    private var chatMessage: String? = null
    private var started: Boolean = false

    private val loginListener = object: Listener {
        @EventHandler fun on(event: PlayerLoginEvent) {
            if (chatMessage != null && event.player.isOp) {
                event.player.sendMessage(chatMessage)
            }
        }
    }

    fun start() {
        if (started)
            throw IllegalArgumentException("already started")

        started = true
        plugin.server.pluginManager.registerEvents(loginListener, plugin)
        plugin.server.scheduler.scheduleAsyncRepeatingTask(plugin, {
            checkForUpdate()
        }, 0, 20 * 60 * 60 * 2) // run every couple of hours
    }

    fun checkForUpdate() {
        try {
            val response = checker.checkForUpdate()

            if (!response.hasUpdate())
                return

            val update = response.updateDetails

            if (update.version == latestVersion)
                return

            latestVersion = update.version

            val message = "An update is available (${response.installed} -> ${response.updateDetails.version}). More info: ${response.updateDetails.url}"
            chatMessage = "${ChatColor.RED}${ChatColor.BOLD}[AutoUBL]$message"

            plugin.logger.info(message)

            plugin.server.onlinePlayers.filter { it.isOp }.map { it.sendMessage(chatMessage) }
        } catch (ex: Exception) {
            plugin.logger.log(Level.SEVERE, "Error checking for AutoUBL plugin update", ex)
        }
    }

}
