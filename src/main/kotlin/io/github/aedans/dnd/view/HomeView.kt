package io.github.aedans.dnd.view

import io.github.aedans.dnd.controller.Database
import io.github.aedans.dnd.form.NewCampaignFragment
import io.github.aedans.dnd.model.Campaign
import javafx.scene.control.SelectionMode
import tornadofx.*

class HomeView : View() {

    val menu = menubar {
        menu("File") {
            item("New Campaign") {
                action {
                    find<NewCampaignFragment>().openWindow()
                }
            }
        }
    }

    val campaignList = listview<String> {
        selectionModel.selectionMode = SelectionMode.SINGLE

        Database.writes<Campaign>().wrap.subscribe { campaign ->
            items.remove(campaign.name)
            items.add(campaign.name)
        }

        cellFormat {
            text = it

            onDoubleClick {
                replaceWith(find<CampaignView>(mapOf(CampaignView::campaign to Database.read<Campaign>(it))))
            }
        }

        contextmenu {
            item("Delete") {
                action {
                    val selected = selectionModel.selectedItem
                    Database.delete<Campaign>(selected)
                    this@listview.items.remove(selected)
                }
            }
        }

        Database.list<Campaign>().subscribe { items.add(it.name) }
    }

    override val root = vbox {
        setMinSize(1080.0, 720.0)

        this += menu
        this += campaignList
    }
}