package io.github.aedans.dnd.model

data class Location(
    override val name: String,
    val localeNames: List<String>,
    val characterNames: List<String>
) : Named {
    companion object {
        val default = Location("World", emptyList(), emptyList())
    }
}