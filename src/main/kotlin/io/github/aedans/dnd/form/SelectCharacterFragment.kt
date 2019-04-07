package io.github.aedans.dnd.form

import io.github.aedans.dnd.controller.Database
import io.github.aedans.dnd.controller.SingleObserverSourceImpl
import io.github.aedans.dnd.controller.SingleObserverSource
import io.github.aedans.dnd.model.Character
import io.reactivex.Single
import javafx.scene.control.SelectionMode
import tornadofx.*

class SelectCharacterFragment : Fragment(), SingleObserverSource<Character> by SingleObserverSourceImpl() {
    override val root = vbox {
        button("New Character") {
            useMaxWidth = true
            action {
                val newCharacter = find<NewCharacterFragment>()
                newCharacter.wrap.subscribe { x ->
                    onSuccess(x)
                    close()
                }
                newCharacter.openWindow()
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