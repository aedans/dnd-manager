package io.github.aedans.dnd.view

import io.github.aedans.dnd.form.NewSettingFragment
import io.github.aedans.dnd.form.SelectSettingFragment
import io.github.aedans.dnd.model.Campaign
import io.github.aedans.dnd.model.Setting
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

    val settingDisplay = Pane()

    val settingsTree = treeview<String> {
        root = TreeItem(campaign.settingName)

        cellFormat { name ->
            text = name

            onUserSelect {
                if (treeItem?.value?.equals(it) == true) {
                    val settingView = find<SettingView>(mapOf(SettingView::setting to Setting.read(name)))
                    settingDisplay.replaceChildren(settingView.root)
                }
            }
        }

        contextmenu {
            item("New") {
                action {
                    val newSetting = find<NewSettingFragment>()
                    Single.wrap(newSetting).subscribe { locale ->
                        val selected = selectionModel.selectedItem
                        val addRoot = TreeItem(locale.name)
                        selected.children.add(addRoot)
                        populateTree(addRoot, { TreeItem(it) }) { parent -> Setting.read(parent.value).localeNames.asIterable() }
                        val setting = Setting.read(selected.value)
                        Setting.write(setting.copy(localeNames = setting.localeNames + locale.name))
                    }
                    newSetting.openWindow()
                }
            }

            item("Remove") {
                action {
                    val selected = selectionModel.selectedItem
                    val parent = selected.parent
                    parent.children.remove(selected)
                    val setting = Setting.read(parent.value)
                    Setting.write(setting.copy(localeNames = setting.localeNames - selected.value))
                }
            }

            item("Add") {
                action {
                    val selectSetting = find<SelectSettingFragment>()
                    Single.wrap(selectSetting).subscribe { locale ->
                        val selected = selectionModel.selectedItem
                        val addRoot = TreeItem(locale.name)
                        selected.children.add(addRoot)
                        populateTree(addRoot, { TreeItem(it) }) { parent -> Setting.read(parent.value).localeNames.asIterable() }
                        val setting = Setting.read(selected.value)
                        Setting.write(setting.copy(localeNames = setting.localeNames + locale.name))
                    }
                    selectSetting.openWindow()
                }
            }

            item("Delete") {
                action {
                    val selected = selectionModel.selectedItem
                    val parent = selected.parent
                    parent.children.remove(selected)
                    val setting = Setting.read(parent.value)
                    Setting.write(setting.copy(localeNames = setting.localeNames - selected.value))
                    Setting.delete(selected.value)
                }
            }
        }

        populate { parent -> Setting.read(parent.value).localeNames.asIterable() }
    }

    val settings = hbox {
        this += settingsTree
        this += settingDisplay
    }

    override val root = vbox {
        this += menu
        this += settings
    }
}