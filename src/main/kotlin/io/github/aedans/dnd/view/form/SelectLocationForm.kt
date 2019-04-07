package io.github.aedans.dnd.view.form

import io.github.aedans.dnd.model.Location
import io.github.aedans.dnd.view.list.NamedList
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

        val locations = NamedList(Location::class.java)
        this += locations

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