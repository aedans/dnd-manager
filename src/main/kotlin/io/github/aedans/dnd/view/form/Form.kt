package io.github.aedans.dnd.view.form

import io.github.aedans.dnd.controller.SingleObserverSource
import io.github.aedans.dnd.controller.SingleObserverSourceImpl
import tornadofx.Fragment

abstract class Form<T> : Fragment(), SingleObserverSource<T> by SingleObserverSourceImpl<T>() {
    fun open() = openSubscribe {  }

    fun openSubscribe(fn: (T) -> Unit) {
        openWindow()
        wrap.subscribe(fn)
        close()
    }
}