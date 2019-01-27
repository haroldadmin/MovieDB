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

    @Query("SELECT * FROM movies WHERE id = :id")
    fun getMovieFlowable(id: Int): Flowable<Movie>

    @Query("SELECT * FROM account_states where movie_id = :movieId")
    fun getAccountStatesForMovieFlowable(movieId: Int): Flowable<AccountState>

    @Query("SELECT * FROM movies WHERE id = :id")
    fun getMovie(id: Int): Single<Movie>

    @Query("SELECT * FROM account_states WHERE movie_id = :movieId")
    fun getAccountStatesForMovie(movieId: Int): Single<AccountState>

    @Query("SELECT * FROM movies WHERE id = :id")
    fun getMovieBlocking(id: Int): Movie

    @Query("SELECT COUNT(*) FROM movies WHERE id = :id")
    fun isMovieInDatabase(id: Int): Single<Int>

    @Query("SELECT COUNT(*) FROM account_states WHERE movie_id = :movieId")
    fun isAccountStateInDatabase(movieId: Int): Single<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveMovie(movie: Movie)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveAccountState(accountState: AccountState)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveMovies(vararg movie: Movie)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveAccountStates(vararg accountState: AccountState)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveAllMovies(movies: List<Movie>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveAllAccountStates(accountStates: List<AccountState>)

    @Update
    fun updateMovie(movie: Movie)

    @Update
    fun updateAccountState(accountState: AccountState)

    @Update
    fun updateMovies(vararg movie: Movie)

    @Update
    fun updateAccountStates(vararg accountState: AccountState)

    @Update
    fun updateAllMovies(movies: List<Movie>)

    @Update
    fun updateAllAccountStates(accountStates: List<AccountState>)

    @Delete
    fun deleteMovie(movie: Movie)

    @Delete
    fun deleteAccountState(accountState: AccountState)

    @Delete
    fun deleteMovies(vararg movie: Movie)

    @Delete
    fun deleteAccountStates(vararg accountState: AccountState)

    @Delete
    fun deleteAllMovies(movies: List<Movie>)

    @Delete
    fun deleteAllAccountStates(accountStates: List<AccountState>)

}