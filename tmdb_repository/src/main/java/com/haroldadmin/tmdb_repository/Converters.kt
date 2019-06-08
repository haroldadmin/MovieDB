package com.haroldadmin.tmdb_repository

import androidx.room.TypeConverter
import java.util.*

internal class Converters {

    // Date Converter
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time?.toLong()
    }

    // List<Int> Converter for IDs
    @TypeConverter
    fun stringToListOfInt(value: String?): List<Int>? {
        val list = mutableListOf<Int>()
        value.takeIf { !it.isNullOrBlank() }
            ?.split(",")
            ?.map { id: String ->
                list.add(id.toInt())
            }
        return list
    }

    @TypeConverter
    fun listOfIntToString(value: List<Int>?): String? {
        return value?.joinToString(separator = ",")
    }

    // List<String> converter used for genres etc.
    @TypeConverter
    fun stringToListOfString(value: String?): List<String>? {
        return value.takeIf { !it.isNullOrBlank() }
            ?.split(",")
            ?.fold(listOf()) { list, s ->
                list + s
            }
    }

    @TypeConverter
    fun listOfStringToString(value: List<String>?): String? {
        return value?.joinToString(separator = ",")
    }

}