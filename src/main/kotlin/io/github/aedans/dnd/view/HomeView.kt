package io.github.aedans.dnd.view

import io.github.aedans.dnd.form.NewCampaignFragment
import io.github.aedans.dnd.model.Campaign
import io.github.aedans.dnd.controller.Database
import io.github.aedans.dnd.model.Named
import io.reactivex.Single
import javafx.scene.control.SelectionMode
import tornadofx.*

class HomeView : View() {
    val menu = menubar {
        menu("File") {

        }
    }

    val campaignList = listview<String> {
        selectionModel.selectionMode = SelectionMode.SINGLE

        cellFormat {
            text = it

            onDoubleClick {
                replaceWith(find<CampaignView>(mapOf(CampaignView::campaign to Database.read<Campaign>(it))))
            }
        }

        contextmenu {
            item("New") {
                action {
                    val newCampaign = find<NewCampaignFragment>()
                    Single.wrap(newCampaign).subscribe { campaign -> this@listview.items.add(campaign.name) }
                    newCampaign.openWindow()
                }
            }

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