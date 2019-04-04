package io.github.aedans.dnd.model

data class Campaign(
    override val name: String,
    val locationName: String
) : Named