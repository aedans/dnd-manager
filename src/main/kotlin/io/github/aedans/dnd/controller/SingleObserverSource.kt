package io.github.aedans.dnd.controller

import io.reactivex.SingleObserver
import io.reactivex.SingleSource

interface SingleObserverSource<T> : SingleSource<T>, SingleObserver<T>