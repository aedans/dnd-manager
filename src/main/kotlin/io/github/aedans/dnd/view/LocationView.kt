package io.github.aedans.dnd.view

import io.github.aedans.dnd.model.Location
import tornadofx.*

class LocationView : Fragment() {
    val location: Location by param()

    val name = label(location.name)

    override val root = vbox {
        this += name
    }
}