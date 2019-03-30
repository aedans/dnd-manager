package io.github.aedans.dnd.view

import io.github.aedans.dnd.model.Setting
import tornadofx.*

class SettingView : Fragment() {
    val setting: Setting by param()

    val name = label(setting.name)

    override val root = vbox {
        this += name
    }
}