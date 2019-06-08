package com.haroldadmin.tmdb_repository.movie.remote.models

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class SimilarMoviesResponse(
    @Json(name="page") val page: Int,
    @Json(name="results") val results: List<ShortMovieResponse>,
    @Json(name="total_results") val totalResults: Int,
    @Json(name="total_pages") val totalPages: Int): Parcelable