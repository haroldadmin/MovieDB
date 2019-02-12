package com.kshitijchauhan.haroldadmin.moviedb.repository.movies

import com.kshitijchauhan.haroldadmin.moviedb.repository.actors.Actor
import com.kshitijchauhan.haroldadmin.moviedb.repository.actors.ActorsDao
import com.kshitijchauhan.haroldadmin.moviedb.utils.extensions.log

class LocalMoviesSource(private val moviesDao: MovieDao,
                        private val actorsDao: ActorsDao) {

    fun getMovieFlowable(id: Int) = moviesDao.getMovieFlowable(id)

    fun getAccountStateForMovieFlowable(movieId: Int) = moviesDao.getAccountStatesForMovieFlowable(movieId)

    fun getCastForMovieFlowable(movieId: Int) = moviesDao.getCastForMovieFlowable(movieId)

    fun getMovieTrailerFlowable(movieId: Int) = moviesDao.getTrailerForMovieFlowable(movieId)

    fun getMovie(id: Int) = moviesDao.getMovie(id)

    fun getAccountStatesForMovie(movieId: Int) = moviesDao.getAccountStatesForMovie(movieId)

    fun getCastForMovie(movieId: Int) = moviesDao.getCastForMovie(movieId)

    fun getMovieTrailer(movieId: Int) = moviesDao.getTrailerForMovie(movieId)

    fun isMovieInDatabase(id: Int) = moviesDao.isMovieInDatabase(id)

    fun isAccountStateInDatabase(movieId: Int) = moviesDao.isAccountStateInDatabase(movieId)

    fun isCastInDatabase(movieId: Int) = moviesDao.isCastInDatabase(movieId)

    fun isMovieTrailerInDatabase(movieId: Int) = moviesDao.isMovieTrailerInDatabase(movieId)

    fun saveMovieToDatabase(movie: Movie) = moviesDao.saveMovie(movie)

    fun saveAccountStateToDatabase(accountState: AccountState) = moviesDao.saveAccountState(accountState)

    fun saveCastToDatabase(cast: Cast) {
        log("Saving cast to database: $cast")
        moviesDao.saveCast(cast)
        cast.castMembers?.let {
            log("Saving cast actors to database: $it")
            actorsDao.saveAllActors(it)
        }
    }

    fun saveMovieTrailerToDatabase(movieTrailer: MovieTrailer) = moviesDao.saveMovieTrailer(movieTrailer)

    fun saveMoviesToDatabase(movies: List<Movie>) = moviesDao.saveAllMovies(movies)

    fun saveAccountStatesToDatabase(accountStates: List<AccountState>) = moviesDao.saveAllAccountStates(accountStates)

    fun saveCastsToDatabase(casts: List<Cast>) {
        moviesDao.saveAllCasts(casts)
        casts.forEach { cast ->
            cast.castMembers?.let {
                actorsDao.saveAllActorsFromCast(it)
            }
        }
    }

    fun saveActorsToDatabase(actors: List<Actor>) = actorsDao.saveAllActors(actors)

    fun saveMovieTrailersInDatabase(movieTrailers: List<MovieTrailer>) = moviesDao.saveAllMovieTrailers(movieTrailers)

    fun updateMovieInDatabase(movie: Movie) = moviesDao.updateMovie(movie)

    fun updateAccountStatesInDatabase(accountState: AccountState) = moviesDao.updateAccountState(accountState)

    fun updateCastInDatabase(cast: Cast) = moviesDao.updateCast(cast)

    fun updateMovieTrailerInDatabase(movieTrailer: MovieTrailer) = moviesDao.updateMovieTrailer(movieTrailer)

    fun getActorsForMovie(actorIds: List<Int>) = moviesDao.getActorsForMovie(actorIds)

}

