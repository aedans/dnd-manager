package io.github.aedans.dnd.model

import io.github.aedans.dnd.controller.Gson
import io.github.aedans.dnd.controller.Util
import io.reactivex.Observable
import java.io.File

data class Setting(val name: String, val localeNames: List<String>) {
    companion object {
        val default = Setting("World", emptyList())

        fun read(settingName: String) =
            Gson.read<Setting>(File(Util.settingsFile, "$settingName.json"))

        fun write(setting: Setting) =
            Gson.write(setting, File(Util.settingsFile, "${setting.name}.json"))

        fun delete(settingName: String) =
            File(Util.settingsFile, "$settingName.json").delete()

        fun list(): Observable<Setting> {
            val sequence = Util.settingsFile.listFiles().asSequence()
            return Observable.fromIterable(sequence.asIterable())
                .map { Setting.read(it.nameWithoutExtension) }
        }
    }
}