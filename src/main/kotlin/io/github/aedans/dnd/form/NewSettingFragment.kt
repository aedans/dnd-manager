package io.github.aedans.dnd.form

import io.github.aedans.dnd.controller.SingleImpl
import io.github.aedans.dnd.controller.SingleObserverSource
import io.github.aedans.dnd.controller.Util
import io.github.aedans.dnd.model.Campaign
import io.github.aedans.dnd.model.Setting
import io.reactivex.SingleObserver
import io.reactivex.SingleSource
import tornadofx.*

class NewSettingFragment : Fragment(), SingleObserverSource<Setting> by SingleImpl() {
    override val root = vbox {
        val name = textfield()

        button("Create") {
            shortcut("Enter")
            action {
                val name = Util.standardizeName(name.text)
                val setting = Setting(Util.standardizeName(name), emptyList())
                Setting.write(setting)
                onSuccess(setting)
                close()
            }
        }
    }
}