package io.github.aedans.dnd.controller

import com.google.gson.Gson
import java.io.Reader
import java.io.Writer

object Gson {
    private val gson = Gson()

    fun <T> read(clazz: Class<T>, reader: Reader) = gson.fromJson(reader, clazz)
    fun <T> write(t: T, writer: Writer) = gson.toJson(t, writer)

    inline fun <reified T> read(reader: Reader) = read(T::class.java, reader)
}