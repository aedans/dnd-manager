package io.github.aedans.dnd.view.form

import io.github.aedans.dnd.controller.Database
import io.github.aedans.dnd.controller.Util
import io.github.aedans.dnd.model.Campaign
import io.github.aedans.dnd.model.Location
import javafx.scene.control.SelectionMode
import tornadofx.*

class NewCampaignForm : Form<Campaign>() {
    override val root = vbox {
        val name = textfield()

        val locations = listview<Location> {
            selectionModel.selectionMode = SelectionMode.SINGLE
            cellFormat { text = it.name }
            Database.list<Location>().subscribe { items.add(it) }
        }

        button("Create") {
            shortcut("Enter")
            action {
                val name = Util.standardizeName(name.text)
                val location = locations.selectedItem ?: Location.default.also { Database.write(it) }
                val campaign = Campaign(name, location.name)
                Database.write(campaign)
                onSuccess(campaign)
            }
        }
    }
}