package com.kshitijchauhan.haroldadmin.moviedb.remote.service.auth

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CreateSessionRequest(
    @field:Json(name="request_token") val requestToken: String): Parcelable

@Parcelize
data class DeleteSessionRequest(
    @field:Json(name="session_id") val sessionId: String): Parcelable