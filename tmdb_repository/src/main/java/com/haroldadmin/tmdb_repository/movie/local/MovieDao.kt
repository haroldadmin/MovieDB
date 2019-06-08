package com.haroldadmin.tmdb_repository.movie.local

import androidx.room.*
import com.haroldadmin.tmdb_repository.actor.local.Actor
import com.haroldadmin.tmdb_repository.actor.local.Cast
import com.haroldadmin.tmdb_repository.movie.local.models.Movie
import com.haroldadmin.tmdb_repository.movie.local.models.MovieTrailer

@Dao
interface MovieDao {

    @Query("SELECT * FROM movies WHERE id = :movieId")
    suspend fun getMovie(movieId: Int): Movie

    @Query("SELECT * FROM actors WHERE id IN (SELECT actorId FROM casts WHERE movieId = :movieId)")
    suspend fun getActorsForMovie(movieId: Int): List<Actor>

    @Query("SELECT name FROM genres WHERE id IN (SELECT genreId FROM movie_genre WHERE movieId = :movieId)")
    suspend fun getGenresForMovie(movieId: Int): List<String>

    @Query("SELECT * FROM movies WHERE id = :movieId")
    suspend fun getTrailersForMovie(movieId: Int): List<MovieTrailer>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveMovie(movie: Movie)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveMovies(vararg movie: Movie)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveCast(cast: Cast)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveAllCasts(vararg cast: Cast)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateMovie(movie: Movie)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateMovies(vararg movie: Movie)

    @Delete
    suspend fun deleteMovie(movie: Movie)

    @Delete
    suspend fun deleteMovies(vararg movie: Movie)
}
