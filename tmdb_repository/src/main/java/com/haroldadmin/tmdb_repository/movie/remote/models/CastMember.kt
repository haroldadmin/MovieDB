package com.haroldadmin.tmdb_repository.movie.remote.models

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class CastMember(
    @Json(name = "cast_id") val castId: Int,
    @Json(name = "character") val character: String,
    @Json(name = "credit_id") val creditId: String,
    @Json(name = "gender") val gender: Int?,
    @Json(name = "id") val id: Int,
    @Json(name = "name") val name: String,
    @Json(name = "order") val order: Int,
    @Json(name = "profile_path") val profilePath: String?
) : Parcelable
