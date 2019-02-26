package com.kshitijchauhan.haroldadmin.moviedb.ui.common

import android.view.View
import com.google.android.material.snackbar.Snackbar

data class SnackbarAction(
    val message: String,
    val actionText: String? = null,
    val length: Int = Snackbar.LENGTH_SHORT,
    val action: View.OnClickListener? = null
)