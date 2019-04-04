package io.github.aedans.dnd.view

import io.github.aedans.dnd.model.Character
import tornadofx.*

class CharacterView : Fragment() {
    val character: Character by param()

    val name = label(character.name)
    val description = label(character.description)

    override val root = vbox {
        this += name
        this += description
    }
}