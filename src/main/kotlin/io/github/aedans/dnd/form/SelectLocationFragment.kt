package io.github.aedans.dnd.form

import io.github.aedans.dnd.controller.Database
import io.github.aedans.dnd.controller.SingleImpl
import io.github.aedans.dnd.controller.SingleObserverSource
import io.github.aedans.dnd.model.Location
import io.reactivex.Single
import javafx.scene.control.SelectionMode
import tornadofx.*

class SelectLocationFragment : Fragment(), SingleObserverSource<Location> by SingleImpl() {
    override val root = vbox {
        button("New Location") {
            useMaxWidth = true
            action {
                val newLocation = find<NewLocationFragment>()
                Single.wrap(newLocation).subscribe { x ->
                    onSuccess(x)
                    close()
                }
                newLocation.openWindow()
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