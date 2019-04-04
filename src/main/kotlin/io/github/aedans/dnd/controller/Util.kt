package io.github.aedans.dnd.controller

import java.io.File

object Util {
    val dataFile = File("data").apply { mkdir() }

    val spacePattern = "\\s+".toPattern()

    fun standardizeName(name: String) = name
        .split(spacePattern)
        .joinToString(" ", "", "") { it.capitalize() }
}