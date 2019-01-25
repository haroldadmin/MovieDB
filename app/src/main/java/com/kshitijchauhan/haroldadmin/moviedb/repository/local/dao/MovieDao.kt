package com.kshitijchauhan.haroldadmin.moviedb.repository.local.dao

import androidx.room.*
import com.kshitijchauhan.haroldadmin.moviedb.repository.local.model.Movie
import io.reactivex.Flowable
import io.reactivex.Single

@Dao
interface MovieDao {

    @Query("SELECT * FROM movies")
    fun getAllMovies(): Flowable<List<Movie>>

    @Query("SELECT * FROM movies WHERE id = :id")
    fun getMovie(id: Int): Flowable<Movie>

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