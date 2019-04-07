package io.github.aedans.dnd.view.list

import io.github.aedans.dnd.controller.Database
import io.reactivex.Observable
import javafx.scene.control.ListView
import tornadofx.cellFormat

abstract class List<T>(clazz: Class<T>) : ListView<T>() {
    abstract fun text(t: T): String

    init {
        cellFormat { text = text(it) }
        Database.list(clazz).subscribe { items.add(it) }
    }
}