package com.haroldadmin.tmdb_repository.movie.local

import com.haroldadmin.tmdb_repository.actor.local.Actor
import com.haroldadmin.tmdb_repository.actor.local.ActorDao
import com.haroldadmin.tmdb_repository.actor.local.Cast
import com.haroldadmin.tmdb_repository.movie.local.models.Movie
import com.haroldadmin.tmdb_repository.movie.local.models.Trailer
import com.haroldadmin.tmdb_repository.movie.remote.models.MovieStatesResponse
import com.haroldadmin.tmdb_repository.toResource
import com.kshitijchauhan.haroldadmin.moviedb.core.Resource

class LocalMoviesSource(private val moviesDao: MovieDao,
                        private val actorDao: ActorDao) {

    suspend fun getMovieDetails(movieId: Int): Resource<Movie> {
        return moviesDao.getMovie(movieId).toResource()
    }

    suspend fun saveMovie(movie: Movie) {
        moviesDao.saveMovie(movie)
    }

    suspend fun getMovieAccountState(movieId: Int): Resource<MovieStatesResponse> {
        val movie = moviesDao.getMovie(movieId)
        val state = MovieStatesResponse(isFavourited = movie.isFavourite, isWatchlisted = movie.isWatchlisted)
        return state.toResource()
    }

    suspend fun updateMovieState(movieId: Int, movieState: MovieStatesResponse) {
        val movie = moviesDao.getMovie(movieId)
        movie.copy(isWatchlisted = movieState.isWatchlisted, isFavourite = movieState.isFavourited)
            .let { updatedMovie ->
                moviesDao.saveMovie(updatedMovie)
            }
    }

    suspend fun getActorsInMovie(movieId: Int): Resource<List<Actor>> {
        val actors = moviesDao.getActorsForMovie(movieId)
        return actors.toResource()
    }

    suspend fun saveActorsInMovie(movieId: Int, actors: List<Actor>) {
        actorDao.saveActors(*actors.toTypedArray())
        actors.map { Cast(movieId, it.id) }.let { casts ->
            moviesDao.saveAllCasts(*casts.toTypedArray())
        }
    }
}