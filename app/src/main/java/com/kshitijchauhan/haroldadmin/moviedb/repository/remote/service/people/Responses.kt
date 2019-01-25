package com.kshitijchauhan.haroldadmin.moviedb.repository.remote.service.people

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PersonResponse(
    @field:Json(name = "id") val id: Int,
    @field:Json(name = "profile_path") val profilePath: String?,
    @field:Json(name = "name") val name: String
) : Parcelable