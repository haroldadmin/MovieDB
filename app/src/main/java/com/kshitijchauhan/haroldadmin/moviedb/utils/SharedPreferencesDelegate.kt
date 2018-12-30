package com.kshitijchauhan.haroldadmin.moviedb.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import com.kshitijchauhan.haroldadmin.moviedb.R
import com.kshitijchauhan.haroldadmin.moviedb.utils.extensions.log
import kotlin.reflect.KProperty

class SharedPreferencesDelegate<T>(private val context: Context, private val key: String, private val defaultValue: T) {
    private val prefs: SharedPreferences by lazy { context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE) }

    operator fun getValue(thisRef: Any?, property: KProperty<*>): T {
        return findPreferences(key, defaultValue)
    }

    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        savePreference(key, value)
    }

    @Suppress("UNCHECKED_CAST")
    private fun findPreferences(key: String, defaultValue: T): T {
        with(prefs)
        {
            val result: Any = when (defaultValue) {
                is Boolean -> getBoolean(key, defaultValue)
                is Int -> getInt(key, defaultValue)
                is Long -> getLong(key, defaultValue)
                is Float -> getFloat(key, defaultValue)
                is String -> getString(key, defaultValue)
                else -> throw IllegalArgumentException()
            }
            return result as T
        }
    }

    @SuppressLint("CommitPrefEdits")
    private fun savePreference(key: String, value: T) {
        with(prefs.edit())
        {
            when (value) {
                is Boolean -> putBoolean(key, value)
                is Int -> putInt(key, value)
                is Long -> putLong(key, value)
                is Float -> putFloat(key, value)
                is String -> putString(key, value)
                else -> throw IllegalArgumentException()
            }.apply()
        }
    }
}