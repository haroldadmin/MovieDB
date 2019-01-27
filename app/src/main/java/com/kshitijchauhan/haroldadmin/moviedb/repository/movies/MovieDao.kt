package com.kshitijchauhan.haroldadmin.moviedb.repository.movies

import androidx.room.*
import io.reactivex.Flowable
import io.reactivex.Single

@Dao
interface MovieDao {

    @Query("SELECT * FROM movies")
    fun getAllMovies(): Flowable<List<Movie>>

    @Query("SELECT * FROM movies WHERE id = :id")
    fun getMovieFlowable(id: Int): Flowable<Movie>

    /**
     * This method can be used where changes to a movie object don't need to be observed continuously.
     */
    @Query("SELECT * FROM movies WHERE id = :id")
    fun getMovie(id: Int): Single<Movie>

    @Query("SELECT * FROM movies WHERE id = :id")
    fun getMovieBlocking(id: Int): Movie

    @Query("SELECT COUNT(*) FROM movies WHERE id = :id")
    fun isMovieInDatabase(id: Int): Single<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveMovie(movie: Movie)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveMovies(vararg movie: Movie)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveAllMovies(movies: List<Movie>)

    @Update
    fun updateMovie(movie: Movie)

    @Update
    fun updateMovies(vararg movie: Movie)

    @Update
    fun updateAllMovies(movies: List<Movie>)

    @Delete
    fun deleteMovie(movie: Movie)

    @Delete
    fun deleteMovies(vararg movie: Movie)

    @Delete
    fun deleteAllMovies(movies: List<Movie>)

}