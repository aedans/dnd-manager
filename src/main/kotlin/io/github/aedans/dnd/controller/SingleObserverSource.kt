package io.github.aedans.dnd.controller

import io.reactivex.Single
import io.reactivex.SingleObserver
import io.reactivex.SingleSource

interface SingleObserverSource<T> : SingleSource<T>, SingleObserver<T> {
    val wrap: Single<T> get() = Single.wrap(this)
}