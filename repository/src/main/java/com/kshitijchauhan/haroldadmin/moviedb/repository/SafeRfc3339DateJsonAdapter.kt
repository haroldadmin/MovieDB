package com.kshitijchauhan.haroldadmin.moviedb.repository

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter
import com.squareup.moshi.adapters.Rfc3339DateJsonAdapter
import java.util.*

/**
 * This class is an extension for the Rfc3339 Date adapter in Moshi.
 * It handles cases where dates are empty strings too.
 */
class SafeRfc3339DateJsonAdapter(private val delegate: Rfc3339DateJsonAdapter): JsonAdapter<Date>() {

    override fun fromJson(reader: JsonReader): Date? {
        return try {
            delegate.fromJson(reader)
        } catch (ex: Exception) {
            Date(0L)
        }
    }

    override fun toJson(writer: JsonWriter, value: Date?) {
        delegate.toJson(writer, value)
    }

}