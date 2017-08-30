package gg.uhc.autoubl

import gg.uhc.autoubl.events.ParsedEvent
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import java.util.*

class BanMatcher: Listener {
    private var bansByUuid = mapOf<UUID, List<UblItem>>()
    var initialized = false
        private set

    fun updateBans(list: List<UblItem>) {
        bansByUuid = list.groupBy(UblItem::uuid)
        initialized = true
    }

    fun checkUuidStatus(uuid: UUID): List<UblItem> {
        if (!initialized)
            throw IllegalStateException("First load of bans has not happened yet")

        val now = Date()
        return bansByUuid[uuid].orEmpty().filter { it.expires > now }
    }
}
