package io.github.aedans.dnd.form

import io.github.aedans.dnd.controller.SingleImpl
import io.github.aedans.dnd.controller.SingleObserverSource
import io.github.aedans.dnd.model.Setting
import io.reactivex.Single
import javafx.scene.control.SelectionMode
import tornadofx.*

class SelectSettingFragment : Fragment(), SingleObserverSource<Setting> by SingleImpl() {
    override val root = vbox {
        button("New Setting") {
            useMaxWidth = true
            action {
                val newSetting = find<NewSettingFragment>()
                Single.wrap(newSetting).subscribe { x ->
                    onSuccess(x)
                    close()
                }
                newSetting.openWindow()
            }
        }

        val list = listview<Setting> {
            selectionModel.selectionMode = SelectionMode.SINGLE
            cellFormat { text = it.name }
            Setting.list().subscribe { items.add(it) }
        }

        button("Select") {
            shortcut("Enter")
            action {
                val setting = list.selectedItem!!
                onSuccess(setting)
                close()
            }
        }
    }
}