package gg.uhc.autoubl.events

import org.bukkit.event.Event
import org.bukkit.event.HandlerList
import java.util.*

class UuidFailedLookupEvent(val requested: Set<String>, val cause: Exception) : Event() {
    override fun getHandlers(): HandlerList = Companion.handlers

    companion object {
        private val handlers = HandlerList()

        @JvmStatic
        fun getHandlerList(): HandlerList = handlers
    }
}