package io.github.aedans.dnd.controller

import java.io.File

object Util {
    val dataFile = File("data").apply { mkdir() }
    val campaignsFile = File(dataFile, "campaigns").apply { mkdir() }
    val settingsFile = File(dataFile, "settings").apply { mkdir() }

    val spacePattern = "\\s+".toPattern()

    fun standardizeName(name: String) = name
        .split(spacePattern)
        .joinToString(" ", "", "") { it.capitalize() }
}