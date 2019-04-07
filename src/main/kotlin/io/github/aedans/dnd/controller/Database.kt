package io.github.aedans.dnd.controller

import io.github.aedans.dnd.model.Named
import io.reactivex.Observable
import java.io.File

object Database {
    inline fun <reified T> directory() = File(Util.dataFile, T::class.simpleName).apply { mkdirs() }
    inline fun <reified T> file(name: String) = File(directory<T>(), "$name.json")

    val writes = mutableMapOf<Class<out Named>, ObservableObserverSourceImpl<Named>>()

    @Suppress("UNCHECKED_CAST")
    inline fun <reified T : Named> writes() = writes.getOrPut(T::class.java) { ObservableObserverSourceImpl() } as ObservableObserverSourceImpl<T>
    inline fun <reified T : Named> write(t: T) {
        writes<T>().onNext(t)
        Gson.write(t, file<T>(t.name))
    }

    val deletes = mutableMapOf<Class<out Named>, ObservableObserverSourceImpl<String>>()

    inline fun <reified T : Named> deletes() = deletes.getOrPut(T::class.java) { ObservableObserverSourceImpl() }
    inline fun <reified T : Named> delete(name: String) {
        deletes<T>().onNext(name)
        file<T>(name).delete()
    }

    inline fun <reified T : Named> read(name: String) = Gson.read<T>(file<T>(name))

    inline fun <reified T : Named> list(): Observable<T> {
        val sequence = directory<T>().listFiles().asSequence()
        return Observable.fromIterable(sequence.asIterable())
            .map { read<T>(it.nameWithoutExtension) }
    }
}