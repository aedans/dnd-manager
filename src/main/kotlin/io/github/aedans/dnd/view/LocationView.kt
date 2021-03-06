package io.github.aedans.dnd.view

import io.github.aedans.dnd.view.form.NewCharacterForm
import io.github.aedans.dnd.view.form.SelectCharacterForm
import io.github.aedans.dnd.model.Character
import io.github.aedans.dnd.model.Location
import io.github.aedans.dnd.controller.Database
import io.reactivex.Observable
import javafx.scene.control.SelectionMode
import javafx.scene.input.KeyEvent
import javafx.scene.input.MouseEvent
import tornadofx.*

class LocationView : Fragment() {
    val location: Location by param()

    val name = label(location.name)

    val characters = listview<String> {
        fun characters(location: Location): Observable<String> = Observable
            .fromIterable(location.characterNames)
            .concatWith(Observable.fromIterable(location.localeNames)
                .flatMap { characters(Database.read(it)) })

        characters(location).subscribe { items.add(it) }

        Observable.wrap(Database.deletes<Character>()).subscribe { items.remove(it) }

        selectionModel.selectionMode = SelectionMode.SINGLE
        cellFormat { name ->
            text = name

            fun action(name: String) {
                val campaignView = find<CampaignView>()
                if (item?.equals(name) == true) {
                    val characterView = find<CharacterView>(mapOf(CharacterView::character to Database.read<Character>(name)))
                    campaignView.characterDisplay.replaceChildren(characterView.root)
                }
            }

            addEventFilter(MouseEvent.MOUSE_CLICKED) { event ->
                val selectedItem = selectedItem
                if (selectedItem != null && event.target.isInsideRow()) action(selectedItem)
            }

            addEventFilter(KeyEvent.KEY_PRESSED) { event ->
                val selectedItem = selectedItem
                if (!event.isMetaDown && selectedItem != null) action(selectedItem)
            }
        }

        contextmenu {
            item("New") {
                action {
                    val newCharacter = find<NewCharacterForm>()
                    newCharacter.wrap.subscribe { character ->
                        this@listview.items.add(character.name)
                        Database.write(character)
                        Database.write(location.copy(characterNames = location.characterNames + character.name))
                    }
                    newCharacter.openWindow()
                }
            }

            item("Remove") {
                action {
                    val selected = selectionModel.selectedItem
                    this@listview.items.remove(selected)
                    Database.write(location.copy(characterNames = location.characterNames - selected))
                }
            }

            item("Add") {
                action {
                    find<SelectCharacterForm>().openSubscribe { character ->
                        this@listview.items.add(character.name)
                        Database.write(location.copy(characterNames = location.characterNames + character.name))
                    }
                }
            }

            item("Delete") {
                action {
                    val selected = selectionModel.selectedItem
                    this@listview.items.remove(selected)
                    Database.write(location.copy(characterNames = location.characterNames - selected))
                    Database.delete<Character>(selected)
                }
            }
        }
    }

    override val root = vbox {
        this += name
        this += characters
    }
}