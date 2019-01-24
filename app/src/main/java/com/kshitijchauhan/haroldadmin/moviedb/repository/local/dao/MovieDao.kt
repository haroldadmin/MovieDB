package com.kshitijchauhan.haroldadmin.moviedb.repository.local.dao

import androidx.room.*
import com.kshitijchauhan.haroldadmin.moviedb.repository.local.model.Movie
import io.reactivex.Observable

@Dao
interface MovieDao {

    @Query("SELECT * FROM movies")
    fun getAllMovies(): Observable<List<Movie>>

    @Query("SELECT * FROM movies WHERE id = :id")
    fun getMovie(id: Int): Observable<Movie>

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