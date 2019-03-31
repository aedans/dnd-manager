package io.github.aedans.dnd.view

import io.github.aedans.dnd.form.NewLocationFragment
import io.github.aedans.dnd.form.SelectLocationFragment
import io.github.aedans.dnd.model.Campaign
import io.github.aedans.dnd.model.Location
import io.reactivex.Single
import javafx.scene.control.TreeItem
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

    val locationDisplay = Pane()

    val locationsTree = treeview<String> {
        root = TreeItem(campaign.locationName)

        cellFormat { name ->
            text = name

            onUserSelect {
                if (treeItem?.value?.equals(it) == true) {
                    val settingView = find<LocationView>(mapOf(LocationView::location to Location.read(name)))
                    locationDisplay.replaceChildren(settingView.root)
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

        populate { parent -> Location.read(parent.value).localeNames.asIterable() }
    }

    val locations = hbox {
        this += locationsTree
        this += locationDisplay
    }

    override val root = vbox {
        this += menu
        this += locations
    }
}