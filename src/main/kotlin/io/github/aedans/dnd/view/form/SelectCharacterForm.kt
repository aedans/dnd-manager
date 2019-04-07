package io.github.aedans.dnd.view.form

import io.github.aedans.dnd.model.Character
import io.github.aedans.dnd.view.list.NamedList
import tornadofx.*

class SelectCharacterForm : Form<Character>() {
    override val root = vbox {
        button("New Character") {
            useMaxWidth = true
            action {
                find<NewCharacterForm>().openSubscribe { x ->
                    onSuccess(x)
                }
            }
        }

        val characters = NamedList(Character::class.java)
        this += characters

        button("Select") {
            shortcut("Enter")
            action {
                val character = characters.selectedItem!!
                onSuccess(character)
                close()
            }
        }
    }
}