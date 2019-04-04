package io.github.aedans.dnd.view

import io.github.aedans.dnd.form.NewCharacterFragment
import io.github.aedans.dnd.form.SelectCharacterFragment
import io.github.aedans.dnd.model.Character
import io.github.aedans.dnd.model.Location
import io.reactivex.Observable
import io.reactivex.Single
import javafx.scene.control.SelectionMode
import javafx.scene.control.TreeItem
import tornadofx.*

class LocationView : Fragment() {
    val location: Location by param()

    val name = label(location.name)

    val characters = listview<String> {
        Observable.fromIterable(location.characterNames).subscribe { items.add(it) }

        selectionModel.selectionMode = SelectionMode.SINGLE
        cellFormat { name ->
            text = name
        }

        contextmenu {
            item("New") {
                action {
                    val newCharacter = find<NewCharacterFragment>()
                    Single.wrap(newCharacter).subscribe { character ->
                        this@listview.items.add(character.name)
                        Character.write(character)
                        Location.write(location.copy(characterNames = location.characterNames + character.name))
                    }
                    newCharacter.openWindow()
                }
            }

            item("Remove") {
                action {
                    val selected = selectionModel.selectedItem
                    this@listview.items.remove(selected)
                    Location.write(location.copy(characterNames = location.characterNames - selected))
                }
            }

            item("Add") {
                action {
                    val selectCharacter = find<SelectCharacterFragment>()
                    Single.wrap(selectCharacter).subscribe { character ->
                        this@listview.items.add(character.name)
                        Location.write(location.copy(characterNames = location.characterNames + character.name))
                    }
                    selectCharacter.openWindow()
                }
            }

            item("Delete") {
                action {
                    val selected = selectionModel.selectedItem
                    this@listview.items.remove(selected)
                    Location.write(location.copy(characterNames = location.characterNames - selected))
                    Character.delete(selected)
                }
            }
        }
    }

    override val root = vbox {
        this += name
        this += characters
    }
}