package com.kshitijchauhan.haroldadmin.moviedb.repository.data.remote.service.account

import android.os.Parcelable
import com.kshitijchauhan.haroldadmin.moviedb.repository.data.remote.service.common.GeneralMovieResponse
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

@Parcelize
internal data class MovieWatchlistResponse(
    @field:Json(name="page") val page: Int,
    @field:Json(name="results") val results: List<GeneralMovieResponse>,
    @field:Json(name="total_pages") val totalPages: Int,
    @field:Json(name="total_results") val totalResults: Int): Parcelable

@Parcelize
internal data class FavouriteMoviesResponse(
    @field:Json(name="page") val page: Int,
    @field:Json(name="results") val results: List<GeneralMovieResponse>,
    @field:Json(name="total_pages") val totalPages: Int,
    @field:Json(name="total_results") val totalResults: Int): Parcelable

@Parcelize
data class ToggleFavouriteResponse(
    @field:Json(name="status_code") val statusCode: Int,
    @field:Json(name="status_message") val statusMessage: String): Parcelable

@Parcelize
data class ToggleWatchlistResponse(
    @field:Json(name="status_code") val statusCode: Int,
    @field:Json(name="status_message") val statusMessage: String): Parcelable
