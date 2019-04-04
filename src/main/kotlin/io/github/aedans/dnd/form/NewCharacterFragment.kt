package io.github.aedans.dnd.form

import io.github.aedans.dnd.controller.Database
import io.github.aedans.dnd.controller.SingleImpl
import io.github.aedans.dnd.controller.SingleObserverSource
import io.github.aedans.dnd.controller.Util
import io.github.aedans.dnd.model.Character
import tornadofx.*

class NewCharacterFragment : Fragment(), SingleObserverSource<Character> by SingleImpl() {
    override val root = vbox {
        val name = textfield()

        button("Create") {
            shortcut("Enter")
            action {
                val name = Util.standardizeName(name.text)
                val character = Character(name)
                Database.write(character)
                onSuccess(character)
                close()
            }
        }
    }
}