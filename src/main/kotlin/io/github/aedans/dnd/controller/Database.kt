package io.github.aedans.dnd.controller

import io.github.aedans.dnd.model.Named
import io.reactivex.Observable
import java.io.File

object Database {
    inline fun <reified T> directory() = File(Util.dataFile, T::class.simpleName).apply { mkdirs() }
    inline fun <reified T> file(name: String) = File(directory<T>(), "$name.json")

    inline fun <reified T : Named> read(name: String) = Gson.read<T>(file<T>(name))
    inline fun <reified T : Named> write(t: T) = Gson.write(t, file<T>(t.name))
    inline fun <reified T : Named> delete(name: String) = file<T>(name).delete()

    inline fun <reified T : Named> list(): Observable<T> {
        val sequence = directory<T>().listFiles().asSequence()
        return Observable.fromIterable(sequence.asIterable())
            .map { read<T>(it.nameWithoutExtension) }
    }
}