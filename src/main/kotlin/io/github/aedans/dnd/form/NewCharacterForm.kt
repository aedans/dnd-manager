package io.github.aedans.dnd.form

import io.github.aedans.dnd.controller.Database
import io.github.aedans.dnd.controller.SingleObserverSourceImpl
import io.github.aedans.dnd.controller.SingleObserverSource
import io.github.aedans.dnd.controller.Util
import io.github.aedans.dnd.model.Character
import tornadofx.*

class NewCharacterForm : Form<Character>() {
    override val root = vbox {
        val name = textfield()
        val race = textfield()

        val description = textarea()

        button("Create") {
            shortcut("Enter")
            action {
                val character = Character(Util.standardizeName(name.text), race.text, description.text)
                Database.write(character)
                onSuccess(character)
                close()
            }
        }
    }
}