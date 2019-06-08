package com.haroldadmin.tmdb_repository

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ErrorResponse(
    @Json(name="status_code") val statusCode: String,
    @Json(name="status_message") val statusMessage: String): Parcelable