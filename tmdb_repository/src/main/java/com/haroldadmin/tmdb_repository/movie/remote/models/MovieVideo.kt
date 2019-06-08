package com.haroldadmin.tmdb_repository.movie.remote.models

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class MovieVideo(
    @Json(name="id") val id: String,
    @Json(name="key") val key: String,
    @Json(name="name") val name: String,
    @Json(name="site") val site: String,
    @Json(name="size") val size: Int,
    @Json(name="type") val type: String): Parcelable
