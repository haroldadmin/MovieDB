package com.kshitijchauhan.haroldadmin.moviedb.repository.remote.service.account

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

enum class MediaTypes(val mediaName: String) {
    MOVIE("movie"), TV("tv")
}

@Parcelize
data class MarkMediaAsFavoriteRequest(
    @field:Json(name = "media_type") val mediaType: String,
    @field:Json(name = "media_id") val mediaId: Int,
    @field:Json(name = "favorite") val favorite: Boolean
) : Parcelable

@Parcelize
data class AddMediaToWatchlistRequest(
    @field:Json(name = "media_type") val mediaType: String,
    @field:Json(name = "media_id") val mediaId: Int,
    @field:Json(name = "watchlist") val watchlist: Boolean
) : Parcelable