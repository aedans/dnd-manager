package io.github.aedans.dnd.form

import io.github.aedans.dnd.controller.Database
import io.github.aedans.dnd.model.Location
import javafx.scene.control.SelectionMode
import tornadofx.*

class SelectLocationForm : Form<Location>() {
    override val root = vbox {
        button("New Location") {
            useMaxWidth = true
            action {
                find<NewLocationForm>().openSubscribe { x ->
                    onSuccess(x)
                }
            }
        }

        val locations = listview<Location> {
            selectionModel.selectionMode = SelectionMode.SINGLE
            cellFormat { text = it.name }
            Database.list<Location>().subscribe { items.add(it) }
        }

        button("Select") {
            shortcut("Enter")
            action {
                val location = locations.selectedItem!!
                onSuccess(location)
                close()
            }
        }
    }
}