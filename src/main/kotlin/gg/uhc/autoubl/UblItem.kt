package gg.uhc.autoubl

import java.util.*

data class UblItem(
    val id: Long,
    val ign: String,
    val uuid: UUID,
    val reason: String,
    val createdBy: String,
    val link: String,
    val created: Date,
    val expires: Date
)