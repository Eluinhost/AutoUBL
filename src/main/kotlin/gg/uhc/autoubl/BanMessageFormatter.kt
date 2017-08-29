package gg.uhc.autoubl

import org.bukkit.ChatColor
import java.text.SimpleDateFormat

class BanMessageFormatter(private val template: String) {
    private val dateFormat = SimpleDateFormat("YYYY-MM-DD")

    fun format(item: UblItem): String = ChatColor.translateAlternateColorCodes('&', template
        .replace("{ign}", item.ign, true)
        .replace("{uuid}", item.uuid.toString(), true)
        .replace("{reason}", item.reason, true)
        .replace("{link}", item.link, true)
        .replace("{created}", dateFormat.format(item.created), true)
        .replace("{expires}", dateFormat.format(item.expires), true)
    )
}
