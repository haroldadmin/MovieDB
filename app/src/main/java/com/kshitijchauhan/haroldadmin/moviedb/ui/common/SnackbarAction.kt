package com.kshitijchauhan.haroldadmin.moviedb.ui.common

import android.view.View
import androidx.annotation.StringRes
import com.google.android.material.snackbar.Snackbar

data class SnackbarAction(
    @StringRes val message: Int,
    @StringRes val actionText: Int? = null,
    val length: Int = Snackbar.LENGTH_SHORT,
    val action: View.OnClickListener? = null
)