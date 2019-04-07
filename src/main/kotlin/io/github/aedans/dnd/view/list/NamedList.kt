package io.github.aedans.dnd.view.list

import io.github.aedans.dnd.model.Named
import io.reactivex.Observable

class NamedList<T : Named>(clazz: Class<T>) : List<T>(clazz) {
    override fun text(t: T) = t.name
}