package gg.uhc.autoubl

import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender

class RefreshCommand(private val onTrigger: () -> Unit) : CommandExecutor {
    private val permission = "uhc.ubl.refresh"

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        if (!sender.hasPermission(permission)) {
            sender.sendMessage("${ChatColor.RED}${ChatColor.BOLD} You do not have permission to use this. ($permission)")
            return true
        }

        onTrigger()
        return true
    }

}