package io.github.aedans.dnd.form

import io.github.aedans.dnd.controller.Database
import io.github.aedans.dnd.controller.SingleImpl
import io.github.aedans.dnd.controller.SingleObserverSource
import io.github.aedans.dnd.controller.Util
import io.github.aedans.dnd.model.Location
import tornadofx.*

class NewLocationFragment : Fragment(), SingleObserverSource<Location> by SingleImpl() {
    override val root = vbox {
        val name = textfield()

        button("Create") {
            shortcut("Enter")
            action {
                val name = Util.standardizeName(name.text)
                val location = Location(Util.standardizeName(name), emptyList(), emptyList())
                Database.write(location)
                onSuccess(location)
                close()
            }
        }
    }
}