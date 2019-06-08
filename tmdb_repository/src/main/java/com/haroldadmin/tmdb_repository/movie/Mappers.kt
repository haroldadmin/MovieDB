package com.haroldadmin.tmdb_repository.movie

import com.haroldadmin.tmdb_repository.actor.local.Actor
import com.haroldadmin.tmdb_repository.movie.local.models.Movie
import com.haroldadmin.tmdb_repository.movie.local.models.Trailer
import com.haroldadmin.tmdb_repository.movie.remote.models.*

internal fun ShortMovieResponse.toMovie() = Movie(
    budget = null,
    isAdult = this.isAdultMovie,
    isFavourite = null,
    isWatchlisted = null,
    revenue = null,
    id = this.id,
    backdropPath = this.backdropPath,
    title = this.originalTitle,
    posterPath = this.posterPath,
    overview = this.overview,
    voteAverage = this.voteAverage,
    releaseDate = this.releaseDate
)

internal fun MovieResponse.toMovie() = Movie(
    id = this.id,
    title = this.title,
    posterPath = this.posterPath,
    backdropPath = this.backdropPath,
    overview = this.overview,
    voteAverage = this.voteAverage,
    releaseDate = this.releaseDate,
    isAdult = this.isAdult,
    isFavourite = null,
    isWatchlisted = null,
    budget = this.budget,
    revenue = this.revenue
)

internal fun CastMember.toActor() = Actor(
    id = this.id,
    profilePictureUrl = this.profilePath,
    name = this.name,
    birthday = null,
    biography = null
)

internal fun MovieVideo.toTrailer(movieId: Int): Trailer {
    return Trailer(
        id = this.id,
        site = this.site,
        key = this.key,
        movieId = movieId
    )
}

internal fun MovieVideosResponse.toTrailer(movieId: Int): Trailer? {
   return this.results
       .asSequence()
       .filter { it.site == "YouTube" }
       .firstOrNull()
       ?.toTrailer(movieId)
}