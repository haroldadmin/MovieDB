package com.kshitijchauhan.haroldadmin.moviedb.remote.service.movie

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
data class GenrePair(
    @field:Json(name="id") val id: Int,
    @field:Json(name="name") val name: String): Parcelable

@Parcelize
data class Movie (
    @field:Json(name="adult") val isAdult: Boolean,
    @field:Json(name="backdrop_path") var backdropPath: String?,
    @field:Json(name="budget") val budget: Int,
    @field:Json(name="genres") val genres: List<GenrePair>,
    @field:Json(name="homepage") val homepage: String?,
    @field:Json(name="id") val id: Int,
    @field:Json(name="imdb_id") val imdbId: String?,
    @field:Json(name="original_language") val originalLanguage: String,
    @field:Json(name="original_title") val originalTitle: String,
    @field:Json(name="overview") val overview: String?,
    @field:Json(name="popularity") val popularity: Double,
    @field:Json(name="poster_path") var posterPath: String?,
    @field:Json(name="release_date") var releaseDate: String,
    @field:Json(name="revenue") val revenue: Int,
    @field:Json(name="runtime") val runtime: Int?,
    @field:Json(name="title") val title: String,
    @field:Json(name="vote_average") var voteAverage: Double,
    @field:Json(name="vote_count") val voteCount: Int): Parcelable