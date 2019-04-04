package io.github.aedans.dnd.controller

import io.reactivex.ObservableSource
import io.reactivex.Observer

interface ObservableObserverSource<T> : ObservableSource<T>, Observer<T>