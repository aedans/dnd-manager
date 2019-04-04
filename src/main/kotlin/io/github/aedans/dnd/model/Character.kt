package io.github.aedans.dnd.model

data class Character(
    override val name: String,
    val description: String
) : Named