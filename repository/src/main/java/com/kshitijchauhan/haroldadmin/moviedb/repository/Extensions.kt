package com.kshitijchauhan.haroldadmin.moviedb.repository

import com.kshitijchauhan.haroldadmin.moviedb.repository.actors.Actor
import com.kshitijchauhan.haroldadmin.moviedb.repository.data.remote.service.common.GeneralMovieResponse
import com.kshitijchauhan.haroldadmin.moviedb.repository.data.remote.service.movie.CastMember
import com.kshitijchauhan.haroldadmin.moviedb.repository.data.remote.service.movie.MovieResponse
import com.kshitijchauhan.haroldadmin.moviedb.repository.data.remote.service.movie.MovieStatesResponse
import com.kshitijchauhan.haroldadmin.moviedb.repository.data.remote.service.movie.MovieVideo
import com.kshitijchauhan.haroldadmin.moviedb.repository.data.remote.service.people.PersonResponse
import com.kshitijchauhan.haroldadmin.moviedb.repository.data.remote.utils.Config
import com.kshitijchauhan.haroldadmin.moviedb.repository.movies.AccountState
import com.kshitijchauhan.haroldadmin.moviedb.repository.movies.Movie
import com.kshitijchauhan.haroldadmin.moviedb.repository.movies.MovieTrailer

fun GeneralMovieResponse.toMovie(): Movie {
    return Movie(
        this.id,
        this.title,
        this.posterPath.getPosterUrl(),
        this.backdropPath.getBackdropUrl(),
        this.overview,
        this.voteAverage,
        this.releaseDate,
        this.genreIds,
        this.isAdultMovie,
        null,
        null,
        null,
        isModelComplete = false
    )
}

fun MovieResponse.toMovie(): Movie {
    return Movie(
        id = this.id,
        title = this.title,
        posterPath = this.posterPath.getPosterUrl(),
        backdropPath = this.backdropPath.getBackdropUrl(),
        overview = this.overview ?: "",
        voteAverage = this.voteAverage,
        releaseDate = this.releaseDate,
        genreIds = this.genres.map { genrePair -> genrePair.id },
        isAdult = this.isAdult,
        budget = this.budget,
        revenue = this.revenue,
        genres = this.genres.map { genrePair -> genrePair.name },
        isModelComplete = true
    )
}

fun CastMember.toActor(): Actor {
    return Actor(
        this.id,
        this.profilePath.getProfilePictureUrl(),
        this.name,
        null,
        null,
        false
    )
}

fun PersonResponse.toActor(): Actor {
    return Actor(
        this.id,
        this.profilePath.getProfilePictureUrl(),
        this.name,
        this.birthday,
        this.biography,
        true
    )
}

fun MovieVideo.toMovieTrailer(movieId: Int): MovieTrailer {
    return MovieTrailer(
        movieId,
        this.key
    )
}

fun MovieStatesResponse.toAccountState(movieId: Int): AccountState {
    return AccountState(
        isWatchlisted = this.isWatchlisted ?: false,
        isFavourited = this.isFavourited ?: false,
        movieId = movieId
    )
}

fun String.getYoutubeUrl() = "https://www.youtube.com/watch?v=$this"

fun String?.getPosterUrl(): String {
    return this?.let {
        "${Config.BASE_IMAGE_URL}${Config.DEFAULT_POSTER_SIZE}$it"
    } ?: ""
}

fun String?.getBackdropUrl(): String {
    return this?.let {
        "${Config.BASE_IMAGE_URL}${Config.DEFAULT_BACKDROP_SIZE}${this}"
    } ?: ""
}

fun String?.getProfilePictureUrl(): String {
    return this?.let {
        "${Config.BASE_IMAGE_URL}${Config.SMALL_PROFILE_PICTURE_SIZE}$this"
    } ?: ""
}