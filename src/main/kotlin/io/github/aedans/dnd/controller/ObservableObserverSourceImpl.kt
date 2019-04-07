package io.github.aedans.dnd.controller

import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.disposables.Disposable

class ObservableObserverSourceImpl<T> : ObservableObserverSource<T> {
    val wrap: Observable<T> get() = Observable.wrap(this)

    private val observers: MutableList<Observer<in T>> = mutableListOf()

    override fun subscribe(observer: Observer<in T>) {
        observers += observer
    }

    override fun onComplete() {
        observers.forEach { it.onComplete() }
    }

    override fun onSubscribe(d: Disposable) {
        observers.forEach { it.onSubscribe(d) }
    }

    override fun onNext(t: T) {
        observers.forEach { it.onNext(t) }
    }

    override fun onError(e: Throwable) {
        observers.forEach { it.onError(e) }
    }
}