package io.github.aedans.dnd.controller

import io.github.aedans.dnd.model.Named
import io.reactivex.Observable
import java.io.File

@Suppress("UNCHECKED_CAST")
object Database {
    fun <T> directory(clazz: Class<T>) = File(Util.dataFile, clazz.name).apply { mkdirs() }
    fun <T> file(clazz: Class<T>, name: String) = File(directory(clazz), "$name.json")

    inline fun <reified T> directory() = directory(T::class.java)
    inline fun <reified T> file(name: String) = file(T::class.java, name)

    private val writes = mutableMapOf<Class<out Named>, ObservableObserverSourceImpl<Named>>()

    fun <T : Named> writes(clazz: Class<T>) = writes.getOrPut(clazz) { ObservableObserverSourceImpl() } as ObservableObserverSourceImpl<T>
    fun <T : Named> write(clazz: Class<T>, t: T) {
        writes(clazz).onNext(t)
        file(clazz, t.name).writer().use { Gson.write(t, it) }
    }

    inline fun <reified T : Named> writes() = writes(T::class.java)
    inline fun <reified T : Named> write(t: T) = write(T::class.java, t)

    private val deletes = mutableMapOf<Class<out Named>, ObservableObserverSourceImpl<String>>()

    fun <T : Named> deletes(clazz: Class<T>) = deletes.getOrPut(clazz) { ObservableObserverSourceImpl() }
    fun <T : Named> delete(clazz: Class<T>, name: String) {
        deletes(clazz).onNext(name)
        file(clazz, name).delete()
    }

    inline fun <reified T : Named> deletes() = deletes(T::class.java)
    inline fun <reified T : Named> delete(name: String) = delete(T::class.java, name)

    fun <T> read(clazz: Class<T>, name: String) = file(clazz, name).reader().use { Gson.read(clazz, it) }
    inline fun <reified T> read(name: String) = read(T::class.java, name)

    fun <T> list(clazz: Class<T>): Observable<T> {
        val sequence = directory(clazz).listFiles().asSequence()
        return Observable.fromIterable(sequence.asIterable())
            .map { read(clazz, it.nameWithoutExtension) }
    }

    inline fun <reified T : Named> list() = list(T::class.java)
}