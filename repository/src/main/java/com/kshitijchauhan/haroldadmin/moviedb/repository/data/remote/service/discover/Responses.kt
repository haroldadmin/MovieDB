package com.kshitijchauhan.haroldadmin.moviedb.repository.data.remote.service.discover

import android.os.Parcelable
import com.kshitijchauhan.haroldadmin.moviedb.repository.data.remote.service.common.GeneralMovieResponse
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
internal data class DiscoverMoviesResponse(
    @field:Json(name="page") val page: Int,
    @field:Json(name="results") val results: List<GeneralMovieResponse>,
    @field:Json(name="total_results") val totalResults: Int,
    @field:Json(name="total_pages") val totalPages: Int): Parcelable