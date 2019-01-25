package com.kshitijchauhan.haroldadmin.moviedb.repository.movie

import com.kshitijchauhan.haroldadmin.moviedb.repository.local.dao.MovieDao
import com.kshitijchauhan.haroldadmin.moviedb.repository.local.model.Movie

class LocalMoviesSource(private val moviesDao: MovieDao) {

    fun getMovie(id: Int) = moviesDao.getMovie(id)

    fun getMovieBlocking(id: Int) = moviesDao.getMovieBlocking(id)

    fun isMovieInDatabase(id: Int) = moviesDao.isMovieInDatabase(id)

    fun saveMovieToDatabase(movie: Movie) = moviesDao.saveMovie(movie)

    fun updateMovieInDatabase(movie: Movie) = moviesDao.updateMovie(movie)

}

