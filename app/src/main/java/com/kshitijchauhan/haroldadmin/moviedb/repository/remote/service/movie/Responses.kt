package com.kshitijchauhan.haroldadmin.moviedb.repository.remote.service.movie

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class GenrePair(
    @field:Json(name="id") val id: Int,
    @field:Json(name="name") val name: String): Parcelable

@Parcelize
data class MovieResponse (
    @field:Json(name="adult") val isAdult: Boolean,
    @field:Json(name="backdrop_path") var backdropPath: String?,
    @field:Json(name="budget") val budget: Long,
    @field:Json(name="genres") val genres: List<GenrePair>,
    @field:Json(name="homepage") val homepage: String?,
    @field:Json(name="id") val id: Int,
    @field:Json(name="imdb_id") val imdbId: String?,
    @field:Json(name="original_language") val originalLanguage: String,
    @field:Json(name="original_title") val originalTitle: String,
    @field:Json(name="overview") val overview: String?,
    @field:Json(name="popularity") val popularity: Double,
    @field:Json(name="poster_path") var posterPath: String?,
    @field:Json(name="release_date") var releaseDate: Date,
    @field:Json(name="revenue") val revenue: Long,
    @field:Json(name="runtime") val runtime: Int?,
    @field:Json(name="title") val title: String,
    @field:Json(name="vote_average") var voteAverage: Double,
    @field:Json(name="vote_count") val voteCount: Int): Parcelable

@Parcelize
data class MovieStatesResponse(
    @field:Json(name="favorite") val isFavourited: Boolean?,
    @field:Json(name="watchlist") val isWatchlisted: Boolean?): Parcelable

@Parcelize
data class MovieVideo(
    @field:Json(name="id") val id: String,
    @field:Json(name="key") val key: String,
    @field:Json(name="name") val name: String,
    @field:Json(name="site") val site: String,
    @field:Json(name="size") val size: Int,
    @field:Json(name="type") val type: String): Parcelable

@Parcelize
data class MovieVideosResponse(
    @field:Json(name="id") val id: Int,
    @field:Json(name="results") val results: List<MovieVideo>): Parcelable

@Parcelize
data class CastMember(
    @field:Json(name="cast_id") val castId: Int,
    @field:Json(name="character") val character: String,
    @field:Json(name="credit_id") val creditId: String,
    @field:Json(name="gender") val gender: Int?,
    @field:Json(name="id") val id: Int,
    @field:Json(name="name") val name: String,
    @field:Json(name="order") val order: Int,
    @field:Json(name="profile_path") val profilePath: String?): Parcelable

@Parcelize
data class MovieCreditsResponse(
    @field:Json(name="id") val id: Int,
    @field:Json(name="cast") val cast: List<CastMember>): Parcelable