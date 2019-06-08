package com.haroldadmin.tmdb_repository.movie.remote

import com.haroldadmin.tmdb_repository.actor.local.Actor
import com.haroldadmin.tmdb_repository.mapToResource
import com.haroldadmin.tmdb_repository.movie.local.models.Movie
import com.haroldadmin.tmdb_repository.movie.local.models.Trailer
import com.haroldadmin.tmdb_repository.movie.remote.models.MovieStatesResponse
import com.haroldadmin.tmdb_repository.movie.toActor
import com.haroldadmin.tmdb_repository.movie.toMovie
import com.haroldadmin.tmdb_repository.movie.toTrailer
import com.haroldadmin.tmdb_repository.toResource
import com.kshitijchauhan.haroldadmin.moviedb.core.Resource

class RemoteMoviesSource(private val movieService: MovieService) {

    suspend fun getMovieDetails(movieId: Int): Resource<Movie> {
        val movie = movieService.getMovieDetails(movieId).await()
        return movie.mapToResource { toMovie() }
    }

    suspend fun getMovieAccountState(movieId: Int): Resource<MovieStatesResponse> {
        val state = movieService.getAccountStatesForMovie(movieId).await()
        return state.toResource()
    }

    suspend fun getActorsInMovie(movieId: Int): Resource<List<Actor>> {
        val credits = movieService.getCreditsForMovie(movieId).await()
        return credits.mapToResource { this.cast.map { it.toActor() } }
    }

    suspend fun getMovieTrailer(movieId: Int): Resource<Trailer?> {
        val trailer = movieService.getVideosForMovie(movieId).await()
        return trailer.mapToResource { toTrailer(movieId) }
    }
}