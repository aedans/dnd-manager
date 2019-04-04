package io.github.aedans.dnd.form

import io.github.aedans.dnd.controller.Database
import io.github.aedans.dnd.controller.SingleObserverSourceImpl
import io.github.aedans.dnd.controller.SingleObserverSource
import io.github.aedans.dnd.controller.Util
import io.github.aedans.dnd.model.Character
import tornadofx.*

class NewCharacterFragment : Fragment(), SingleObserverSource<Character> by SingleObserverSourceImpl() {
    override val root = vbox {
        val name = textfield()

        val description = textarea()

        button("Create") {
            shortcut("Enter")
            action {
                val character = Character(Util.standardizeName(name.text), description.text)
                Database.write(character)
                onSuccess(character)
                close()
            }
        }
    }
}