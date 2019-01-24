package com.kshitijchauhan.haroldadmin.moviedb.repository.local

import com.kshitijchauhan.haroldadmin.moviedb.repository.local.dao.MovieDao
import com.kshitijchauhan.haroldadmin.moviedb.repository.local.model.Movie
import com.kshitijchauhan.haroldadmin.moviedb.repository.remote.service.movie.*
import com.kshitijchauhan.haroldadmin.moviedb.utils.extensions.firstOrDefault
import com.kshitijchauhan.haroldadmin.moviedb.utils.extensions.getBackdropUrl
import com.kshitijchauhan.haroldadmin.moviedb.utils.extensions.getPosterUrl
import com.kshitijchauhan.haroldadmin.moviedb.utils.extensions.log
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

class MoviesRepository(
    private val moviesDao: MovieDao,
    private val movieService: MovieService,
    private val isAuthenticated: Boolean
) : Repository<Movie>() {

    override fun get(id: Int): Observable<Movie> {

        lateinit var movieResponse: MovieResponse
        lateinit var creditsResponse: MovieCreditsResponse
        lateinit var statesResponse: MovieStatesResponse
        lateinit var videosResponse: MovieVideosResponse

        movieService.getMovieDetails(id)
            .doOnSubscribe {
                log("Subscribing to movie details")
            }
            .doOnSuccess {
                log("Retrieved movie details")
                movieResponse = it
            }
            .flatMap {
                movieService.getCreditsForMovie(id)
            }
            .doOnSubscribe {
                log("Subscribing to movie credits")
            }
            .doOnSuccess {
                log("Retrieved movie credits")
                creditsResponse = it
            }
            .flatMap {
                movieService.getVideosForMovie(id)
            }
            .doOnSubscribe {
                log("Subscribing to movie videos")
            }
            .doOnSuccess {
                log("Retrieved movie videos")
                videosResponse = it
            }
            .flatMap {
                if (isAuthenticated) {
                    movieService.getAccountStatesForMovie(id)
                } else {
                    Single.just(MovieStatesResponse(isFavourited = null, isWatchlisted = null))
                }
            }
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.single())
            .doOnSubscribe {
                log("Subscribing to movie details")
            }
            .doOnSuccess {
                log("Retrieved movie states")
                statesResponse = it
                val movie = mapResponsesToMovieModel(movieResponse, statesResponse, videosResponse, creditsResponse)
                this.save(movie)
            }
            .subscribe()

        return moviesDao.getMovie(id)
    }

    override fun getAll(): Observable<List<Movie>> {
        return moviesDao.getAllMovies()
    }

    override fun save(t: Movie) {
        moviesDao.saveMovie(t)
    }

    override fun saveMultiple(vararg t: Movie) {
        moviesDao.saveMovies(*t)
    }

    override fun saveAll(ts: List<Movie>) {
        moviesDao.saveAllMovies(ts)
    }

    override fun update(t: Movie) {
        moviesDao.updateMovie(t)
    }

    override fun updateMultiple(vararg t: Movie) {
        moviesDao.updateMovies(*t)
    }

    override fun updateAll(ts: List<Movie>) {
        moviesDao.updateAllMovies(ts)
    }

    override fun delete(t: Movie) {
        moviesDao.deleteMovie(t)
    }

    override fun deleteMultiple(vararg t: Movie) {
        moviesDao.deleteMovies(*t)
    }

    override fun deleteAll(ts: List<Movie>) {
        moviesDao.deleteAllMovies(ts)
    }

    private fun mapResponsesToMovieModel(
        movieResponse: MovieResponse,
        accountStatesResponse: MovieStatesResponse,
        videosResponse: MovieVideosResponse,
        creditsResponse: MovieCreditsResponse
    ): Movie {
        return Movie(
            movieResponse.id,
            movieResponse.title,
            movieResponse.posterPath.getPosterUrl(),
            movieResponse.backdropPath.getBackdropUrl(),
            movieResponse.overview ?: "N/A",
            movieResponse.voteAverage,
            movieResponse.releaseDate,
            movieResponse.genres.first().name,
            accountStatesResponse.isWatchlisted,
            accountStatesResponse.isFavourited,
            videosResponse.results
                .filter { movieVideo ->
                    movieVideo.site == "YouTube" && movieVideo.type == "Trailer"
                }
                .map { movieVideo ->
                    movieVideo.key
                }
                .firstOrDefault(""),
            creditsResponse.cast.map { it.castId }
        )
    }
}