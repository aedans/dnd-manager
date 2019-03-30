package io.github.aedans.dnd.form

import io.github.aedans.dnd.controller.SingleImpl
import io.github.aedans.dnd.controller.SingleObserverSource
import io.github.aedans.dnd.controller.Util
import io.github.aedans.dnd.model.Campaign
import io.github.aedans.dnd.model.Setting
import javafx.scene.control.SelectionMode
import tornadofx.*

class NewCampaignFragment : Fragment(), SingleObserverSource<Campaign> by SingleImpl() {
    override val root = vbox {
        val name = textfield()

        val settings = listview<Setting> {
            selectionModel.selectionMode = SelectionMode.SINGLE
            cellFormat { text = it.name }
            Setting.list().subscribe { items.add(it) }
        }

        button("Create") {
            shortcut("Enter")
            action {
                val name = Util.standardizeName(name.text)
                val setting = settings.selectedItem ?: Setting.default.also { Setting.write(it) }
                val campaign = Campaign(name, setting.name)
                Campaign.write(campaign)
                onSuccess(campaign)
                close()
            }
        }
    }
}