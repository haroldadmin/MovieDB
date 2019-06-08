package com.haroldadmin.tmdb_repository.actor.remote

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
@JsonClass(generateAdapter = true)
internal data class PersonResponse(
    @Json(name = "id") val id: Int,
    @Json(name = "profile_path") val profilePath: String?,
    @Json(name = "name") val name: String,
    @Json(name = "birthday") val birthday: Date?,
    @Json(name = "biography") val biography: String
) : Parcelable