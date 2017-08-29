package gg.uhc.autoubl.events

import org.bukkit.event.Event
import org.bukkit.event.HandlerList

class RequestUuidLookupEvent(val ign: String) : Event() {
    override fun getHandlers(): HandlerList = Companion.handlers

    companion object {
        private val handlers = HandlerList()

        @JvmStatic
        fun getHandlerList(): HandlerList = handlers
    }
}