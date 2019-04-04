package io.github.aedans.dnd.view

import io.github.aedans.dnd.form.NewCharacterFragment
import io.github.aedans.dnd.form.NewLocationFragment
import io.github.aedans.dnd.form.SelectLocationFragment
import io.github.aedans.dnd.model.Campaign
import io.github.aedans.dnd.model.Character
import io.github.aedans.dnd.model.Location
import io.reactivex.Single
import javafx.scene.control.SelectionMode
import javafx.scene.control.TreeItem
import javafx.scene.input.KeyCode
import javafx.scene.input.KeyEvent
import javafx.scene.input.MouseEvent
import javafx.scene.layout.Pane
import tornadofx.*

class CampaignView : View() {
    val campaign: Campaign by param()

    val menu = menubar {
        menu("File") {
            item("Return") {
                action {
                    replaceWith(find<HomeView>())
                }
            }
        }
    }

    val display = Pane()

    val locationsTree = treeview<String> {
        root = TreeItem(campaign.locationName)

        populate { parent -> Location.read(parent.value).localeNames.asIterable() }

        cellFormat { name ->
            text = name

            onUserSelect {
                if (treeItem?.value?.equals(it) == true) {
                    val locationView = find<LocationView>(mapOf(LocationView::location to Location.read(name)))
                    display.replaceChildren(locationView.root)
                }
            }
        }

        contextmenu {
            item("New") {
                action {
                    val newLocation = find<NewLocationFragment>()
                    Single.wrap(newLocation).subscribe { locale ->
                        val selected = selectionModel.selectedItem
                        val addRoot = TreeItem(locale.name)
                        selected.children.add(addRoot)
                        populateTree(addRoot, { TreeItem(it) }) { parent -> Location.read(parent.value).localeNames.asIterable() }
                        val location = Location.read(selected.value)
                        Location.write(location.copy(localeNames = location.localeNames + locale.name))
                    }
                    newLocation.openWindow()
                }
            }

            item("Remove") {
                action {
                    val selected = selectionModel.selectedItem
                    val parent = selected.parent
                    parent.children.remove(selected)
                    val location = Location.read(parent.value)
                    Location.write(location.copy(localeNames = location.localeNames - selected.value))
                }
            }

            item("Add") {
                action {
                    val selectLocation = find<SelectLocationFragment>()
                    Single.wrap(selectLocation).subscribe { locale ->
                        val selected = selectionModel.selectedItem
                        val addRoot = TreeItem(locale.name)
                        selected.children.add(addRoot)
                        populateTree(addRoot, { TreeItem(it) }) { parent -> Location.read(parent.value).localeNames.asIterable() }
                        val setting = Location.read(selected.value)
                        Location.write(setting.copy(localeNames = setting.localeNames + locale.name))
                    }
                    selectLocation.openWindow()
                }
            }

            item("Delete") {
                action {
                    val selected = selectionModel.selectedItem
                    val parent = selected.parent
                    parent.children.remove(selected)
                    val location = Location.read(parent.value)
                    Location.write(location.copy(localeNames = location.localeNames - selected.value))
                    Location.delete(selected.value)
                }
            }
        }
    }

    val characters = listview<String> {
        Character.list().subscribe { items.add(it.name) }

        selectionModel.selectionMode = SelectionMode.SINGLE
        cellFormat { name ->
            text = name

            fun action(name: String) {
                if (item?.equals(name) == true) {
                    val characterView = find<CharacterView>(mapOf(CharacterView::character to Character.read(name)))
                    display.replaceChildren(characterView.root)
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
                    val newCharacter = find<NewCharacterFragment>()
                    Single.wrap(newCharacter).subscribe { character ->
                        this@listview.items.add(character.name)
                        Character.write(character)
                    }
                    newCharacter.openWindow()
                }
            }

            item("Delete") {
                action {
                    val name = selectionModel.selectedItem
                    this@listview.items.remove(name)
                    Character.delete(name)
                }
            }
        }
    }

    val displayTree = hbox {
        vbox {
            this += locationsTree
            this += characters
        }
        this += display
    }

    override val root = vbox {
        this += menu
        this += displayTree
    }
}