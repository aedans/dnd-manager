package io.github.aedans.dnd

import io.github.aedans.dnd.view.HomeView
import tornadofx.App
import tornadofx.launch

class DndApp : App(HomeView::class) {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) = launch<DndApp>(args)
    }
}