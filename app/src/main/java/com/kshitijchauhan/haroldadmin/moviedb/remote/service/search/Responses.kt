package com.kshitijchauhan.haroldadmin.moviedb.remote.service.search

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SearchResponse(
    @field:Json(name="page") val page: Int,
    @field:Json(name="results") val results: List<MovieSearchResult>,
    @field:Json(name="total_results") val totalResults: Int,
    @field:Json(name="total_pages") val totalPages: Int): Parcelable

@Parcelize
data class MovieSearchResult(
    @field:Json(name="adult") val isAdultMovie: Boolean,
    @field:Json(name="poster_path") var posterPath: String?,
    @field:Json(name="overview") val overview: String,
    @field:Json(name="release_date") var releaseDate: String,
    @field:Json(name="genre_ids") val genreIds: List<Int>,
    @field:Json(name="id") val id: Int,
    @field:Json(name="original_title") val originalTitle: String,
    @field:Json(name="original_language") val originalLanguage: String,
    @field:Json(name="title") val title: String,
    @field:Json(name="backdrop_path") val backdropPath: String?,
    @field:Json(name="popularity") val popularity: Double,
    @field:Json(name="vote_count") val voteCount: Int,
    @field:Json(name="video") val video: Boolean,
    @field:Json(name="vote_average") var voteAverage: Double): Parcelable