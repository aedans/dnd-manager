package io.github.aedans.dnd.form

import io.github.aedans.dnd.controller.SingleImpl
import io.github.aedans.dnd.controller.SingleObserverSource
import io.github.aedans.dnd.model.Character
import io.reactivex.Single
import javafx.scene.control.SelectionMode
import tornadofx.*

class SelectCharacterFragment : Fragment(), SingleObserverSource<Character> by SingleImpl() {
    override val root = vbox {
        button("New Character") {
            useMaxWidth = true
            action {
                val newCharacter = find<NewCharacterFragment>()
                Single.wrap(newCharacter).subscribe { x ->
                    onSuccess(x)
                    close()
                }
                newCharacter.openWindow()
            }
        }

        val characters = listview<Character> {
            selectionModel.selectionMode = SelectionMode.SINGLE
            cellFormat { text = it.name }
            Character.list().subscribe { items.add(it) }
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