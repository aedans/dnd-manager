package io.github.aedans.dnd.view.form

import io.github.aedans.dnd.controller.Database
import io.github.aedans.dnd.model.Character
import javafx.scene.control.SelectionMode
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

        val characters = listview<Character> {
            selectionModel.selectionMode = SelectionMode.SINGLE
            cellFormat { text = it.name }
            Database.list<Character>().subscribe { items.add(it) }
        }

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