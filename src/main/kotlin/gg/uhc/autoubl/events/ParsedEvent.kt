package gg.uhc.autoubl.events

import gg.uhc.autoubl.UblItem
import org.bukkit.event.Event
import org.bukkit.event.HandlerList

class ParsedEvent(val bans: List<UblItem>) : Event() {
    override fun getHandlers(): HandlerList = Companion.handlers

    companion object {
        private val handlers = HandlerList()

        @JvmStatic
        fun getHandlerList(): HandlerList = handlers
    }
}