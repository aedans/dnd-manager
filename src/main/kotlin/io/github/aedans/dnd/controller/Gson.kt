package io.github.aedans.dnd.controller

import com.google.gson.Gson
import java.io.File
import java.io.Reader
import java.io.Writer

object Gson {
    val gson = Gson()

    inline fun <reified T> read(reader: Reader) = gson.fromJson(reader, T::class.java)
    inline fun <reified T> write(t: T, writer: Writer) = gson.toJson(t, writer)

    inline fun <reified T> read(file: File): T = file.bufferedReader().use { read(it) }
    inline fun <reified T> write(t: T, file: File) = file.bufferedWriter().use { write(t, it) }
}