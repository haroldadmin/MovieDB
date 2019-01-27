package com.kshitijchauhan.haroldadmin.moviedb.repository.movies

class LocalMoviesSource(private val moviesDao: MovieDao) {

    fun getMovieFlowable(id: Int) = moviesDao.getMovieFlowable(id)

    fun getAccountStateForMovieFlowable(movieId: Int) = moviesDao.getAccountStatesForMovieFlowable(movieId)

    fun getMovie(id: Int) = moviesDao.getMovie(id)

    fun getAccountStatesForMovie(movieId: Int) = moviesDao.getAccountStatesForMovie(movieId)

    fun getMovieBlocking(id: Int) = moviesDao.getMovieBlocking(id)

    fun isMovieInDatabase(id: Int) = moviesDao.isMovieInDatabase(id)

    fun isAccountStateInDatabase(movieId: Int) = moviesDao.isAccountStateInDatabase(movieId)

    fun saveMovieToDatabase(movie: Movie) = moviesDao.saveMovie(movie)

    fun saveAccountStateToDatabase(accountState: AccountState) = moviesDao.saveAccountState(accountState)

    fun saveMoviesToDatabase(movies: List<Movie>) = moviesDao.saveAllMovies(movies)

    fun saveAccountStatesToDatabase(accountStates: List<AccountState>) = moviesDao.saveAllAccountStates(accountStates)

    fun updateMovieInDatabase(movie: Movie) = moviesDao.updateMovie(movie)

    fun updateAccountStatesInDatabase(accountState: AccountState) = moviesDao.updateAccountState(accountState)

}

