package com.kshitijchauhan.haroldadmin.moviedb.remote.service.search

import android.os.Parcelable
import com.kshitijchauhan.haroldadmin.moviedb.model.Movie
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SearchResponse(
    @field:Json(name="page") val page: Int,
    @field:Json(name="results") val results: List<Movie>,
    @field:Json(name="total_results") val totalResults: Int,
    @field:Json(name="total_pages") val totalPages: Int): Parcelable