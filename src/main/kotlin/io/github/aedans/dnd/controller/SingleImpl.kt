package io.github.aedans.dnd.controller

import io.reactivex.SingleObserver
import io.reactivex.disposables.Disposable

class SingleImpl<T> : SingleObserverSource<T> {
    private val observers: MutableList<SingleObserver<in T>> = mutableListOf()

    override fun subscribe(observer: SingleObserver<in T>) {
        observers += observer
    }

    override fun onSuccess(t: T) {
        observers.forEach { it.onSuccess(t) }
    }

    override fun onSubscribe(d: Disposable) {
        observers.forEach { it.onSubscribe(d) }
    }

    override fun onError(e: Throwable) {
        observers.forEach { it.onError(e) }
    }
}