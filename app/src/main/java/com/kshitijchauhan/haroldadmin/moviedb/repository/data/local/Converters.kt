package com.kshitijchauhan.haroldadmin.moviedb.repository.data.local

import androidx.room.TypeConverter
import java.util.*

class Converters {

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
    fun fromIntList(value: String?): List<Int>? {
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

}