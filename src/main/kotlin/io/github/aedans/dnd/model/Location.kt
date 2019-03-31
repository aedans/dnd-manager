package io.github.aedans.dnd.model

import io.github.aedans.dnd.controller.Gson
import io.github.aedans.dnd.controller.Util
import io.reactivex.Observable
import java.io.File

data class Location(val name: String, val localeNames: List<String>) {
    companion object {
        val default = Location("World", emptyList())

        fun read(locationName: String) =
            Gson.read<Location>(File(Util.locationsFile, "$locationName.json"))

        fun write(location: Location) =
            Gson.write(location, File(Util.locationsFile, "${location.name}.json"))

        fun delete(locationName: String) =
            File(Util.locationsFile, "$locationName.json").delete()

        fun list(): Observable<Location> {
            val sequence = Util.locationsFile.listFiles().asSequence()
            return Observable.fromIterable(sequence.asIterable())
                .map { Location.read(it.nameWithoutExtension) }
        }
    }
}