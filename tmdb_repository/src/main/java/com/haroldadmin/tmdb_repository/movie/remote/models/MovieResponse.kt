package com.haroldadmin.tmdb_repository.movie.remote.models

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
@JsonClass(generateAdapter = true)
data class MovieResponse (
    @Json(name="adult") val isAdult: Boolean,
    @Json(name="backdrop_path") var backdropPath: String?,
    @Json(name="budget") val budget: Long,
    @Json(name="genres") val genres: List<GenrePair>,
    @Json(name="homepage") val homepage: String?,
    @Json(name="id") val id: Int,
    @Json(name="imdb_id") val imdbId: String?,
    @Json(name="original_language") val originalLanguage: String,
    @Json(name="original_title") val originalTitle: String,
    @Json(name="overview") val overview: String?,
    @Json(name="popularity") val popularity: Double,
    @Json(name="poster_path") var posterPath: String?,
    @Json(name="release_date") var releaseDate: Date,
    @Json(name="revenue") val revenue: Long,
    @Json(name="runtime") val runtime: Int?,
    @Json(name="title") val title: String,
    @Json(name="vote_average") var voteAverage: Double,
    @Json(name="vote_count") val voteCount: Int): Parcelable