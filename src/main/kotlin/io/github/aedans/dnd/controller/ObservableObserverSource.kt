package io.github.aedans.dnd.controller

import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.Observer

interface ObservableObserverSource<T> : ObservableSource<T>, Observer<T> {
    val wrap: Observable<T> get() = Observable.wrap(this)
}