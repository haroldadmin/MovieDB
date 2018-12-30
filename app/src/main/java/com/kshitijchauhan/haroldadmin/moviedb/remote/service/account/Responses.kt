package com.kshitijchauhan.haroldadmin.moviedb.remote.service.account

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Gravatar(
    @field:Json(name="hash") val hash: String): Parcelable

@Parcelize
data class Avatar(
    @field:Json(name="gravatar") val gravatar: Gravatar): Parcelable

@Parcelize
data class AccountDetailsResponse(
    @field:Json(name="avatar") val avatar: Avatar,
    @field:Json(name="id") val id: Int,
    @field:Json(name="name") val name: String,
    @field:Json(name="include_adult") val includeAdult: Boolean,
    @field:Json(name="username") val username: String): Parcelable