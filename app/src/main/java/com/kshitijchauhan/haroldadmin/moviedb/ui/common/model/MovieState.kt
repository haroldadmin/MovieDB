package com.kshitijchauhan.haroldadmin.moviedb.ui.common.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MovieState(
    val watchlisted: Boolean,
    val favourited: Boolean): Parcelable