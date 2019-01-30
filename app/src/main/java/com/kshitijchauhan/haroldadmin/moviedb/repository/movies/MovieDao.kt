package com.kshitijchauhan.haroldadmin.moviedb.repository.movies

import androidx.room.*
import io.reactivex.Flowable
import io.reactivex.Single

@Dao
interface MovieDao {

    /**
     * Methods that return [Flowable] should be used where data needs to be continuously observed, and notified of
     * changes. For one off requests, methods that return [Single] should be used.
     */

    @Query("SELECT * FROM movies")
    fun getAllMovies(): Flowable<List<Movie>>

    @Query("SELECT * FROM account_states")
    fun getAllAccountStates(): Flowable<List<AccountState>>

    @Query("SELECT * FROM casts")
    fun getAllCasts(): Flowable<List<Cast>>

    @Query("SELECT * FROM movie_trailers")
    fun getAllTrailers(): Flowable<List<MovieTrailer>>

    @Query("SELECT * FROM movies WHERE id = :id")
    fun getMovieFlowable(id: Int): Flowable<Movie>

    @Query("SELECT * FROM account_states where movie_id = :movieId")
    fun getAccountStatesForMovieFlowable(movieId: Int): Flowable<AccountState>

    @Query("SELECT * FROM casts WHERE movie_id = :movieId")
    fun getCastForMovieFlowable(movieId: Int): Flowable<Cast>

    @Query("SELECT * FROM movie_trailers WHERE movie_id = :movieId")
    fun getTrailerForMovieFlowable(movieId: Int): Flowable<MovieTrailer>

    @Query("SELECT * FROM movies WHERE id = :id")
    fun getMovie(id: Int): Single<Movie>

    @Query("SELECT * FROM account_states WHERE movie_id = :movieId")
    fun getAccountStatesForMovie(movieId: Int): Single<AccountState>

    @Query("SELECT * FROM casts WHERE movie_id = :movieId")
    fun getCastForMovie(movieId: Int): Single<Cast>

    @Query("SELECT * FROM movie_trailers WHERE movie_id = :movieId")
    fun getTrailerForMovie(movieId: Int): Single<MovieTrailer>

    @Query("SELECT COUNT(*) FROM movies WHERE id = :id")
    fun isMovieInDatabase(id: Int): Single<Int>

    @Query("SELECT COUNT(*) FROM account_states WHERE movie_id = :movieId")
    fun isAccountStateInDatabase(movieId: Int): Single<Int>

    @Query("SELECT COUNT(*) FROM casts WHERE movie_id = :movieId")
    fun isCastInDatabase(movieId: Int): Single<Int>

    @Query("SELECT COUNT(*) FROM movie_trailers WHERE movie_id = :movieId")
    fun isMovieTrailerInDatabase(movieId: Int): Single<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveMovie(movie: Movie)

    @Insert(onConflict = OnConflictStrategy.FAIL)
    fun saveAccountState(accountState: AccountState)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveCast(cast: Cast)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveMovieTrailer(movieTrailer: MovieTrailer)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveMovies(vararg movie: Movie)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveAccountStates(vararg accountState: AccountState)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveCasts(vararg cast: Cast)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveMovieTrailers(vararg movieTrailer: MovieTrailer)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveAllMovies(movies: List<Movie>)

    /**
     * Collections have an incomplete movie model. We do not want to overwrite complete movie models with incomplete
     * ones. Therefore, we adopt the IGNORE strategy for conflicts.
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun saveAllMoviesFromCollection(movies: List<Movie>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveAllAccountStates(accountStates: List<AccountState>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveAllCasts(casts: List<Cast>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveAllMovieTrailers(trailers: List<MovieTrailer>)

    @Update
    fun updateMovie(movie: Movie)

    @Update
    fun updateAccountState(accountState: AccountState)

    @Update
    fun updateCast(cast: Cast)

    @Update
    fun updateMovieTrailer(movieTrailer: MovieTrailer)

    @Update
    fun updateMovies(vararg movie: Movie)

    @Update
    fun updateAccountStates(vararg accountState: AccountState)

    @Update
    fun updateCasts(vararg cast: Cast)

    @Update
    fun updateTrailers(vararg trailer: MovieTrailer)

    @Update
    fun updateAllMovies(movies: List<Movie>)

    @Update
    fun updateAllAccountStates(accountStates: List<AccountState>)

    @Update
    fun updateAllCasts(casts: List<Cast>)

    @Update
    fun updateAllTrailers(trailers: List<MovieTrailer>)

    @Delete
    fun deleteMovie(movie: Movie)

    @Delete
    fun deleteAccountState(accountState: AccountState)

    @Delete
    fun deleteCast(cast: Cast)

    @Delete
    fun deleteMovieTrailer(movieTrailer: MovieTrailer)

    @Delete
    fun deleteMovies(vararg movie: Movie)

    @Delete
    fun deleteAccountStates(vararg accountState: AccountState)

    @Delete
    fun deleteCasts(vararg cast: Cast)

    @Delete
    fun deleteMovieTrailers(vararg movieTrailer: MovieTrailer)

    @Delete
    fun deleteAllMovies(movies: List<Movie>)

    @Delete
    fun deleteAllAccountStates(accountStates: List<AccountState>)

    @Delete
    fun deleteAllCasts(casts: List<Cast>)

    @Delete
    fun deleteAllMovieTrailers(movieTrailers: List<MovieTrailer>)

}