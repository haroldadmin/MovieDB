package com.kshitijchauhan.haroldadmin.moviedb.core.extensions

import android.app.Activity
import android.content.Context
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.FragmentManager

fun Activity.hideKeyboard() {
    val inputManager = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    inputManager.hideSoftInputFromWindow(
        if (this.currentFocus == null) {
            null
        } else {
            this.currentFocus?.windowToken
        }, InputMethodManager.HIDE_NOT_ALWAYS
    )
}

fun FragmentManager.clearBackStack() = repeat(this.backStackEntryCount) {
    this.popBackStack()
}