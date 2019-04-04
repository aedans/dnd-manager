package io.github.aedans.dnd.model

import io.github.aedans.dnd.controller.Gson
import io.github.aedans.dnd.controller.Util
import io.reactivex.Observable
import java.io.File

data class Character(val name: String) {
    companion object {
        fun read(characterName: String) =
            Gson.read<Character>(File(Util.charactersFile, "$characterName.json"))

        fun write(character: Character) =
            Gson.write(character, File(Util.charactersFile, "${character.name}.json"))

        fun delete(characterName: String) =
            File(Util.charactersFile, "$characterName.json").delete()

        fun list(): Observable<Character> {
            val sequence = Util.charactersFile.listFiles().asSequence()
            return Observable.fromIterable(sequence.asIterable())
                .map { Character.read(it.nameWithoutExtension) }
        }
    }
}