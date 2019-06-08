package com.haroldadmin.tmdb_repository.collections

import androidx.room.Dao
import androidx.room.Query
import com.haroldadmin.tmdb_repository.movie.local.models.Movie

@Dao
internal interface CollectionDao {

    @Query("SELECT * FROM movies WHERE is_favourite == 1")
    suspend fun getFavouritedMovies(): List<Movie>

    @Query("SELECT is_favourite FROM movies WHERE id == :movieId")
    suspend fun isMovieFavourite(movieId: Int): Boolean

    @Query("SELECT * FROM movies WHERE is_watchlisted == 1")
    suspend fun getWatchlistedMovies(): List<Movie>

    @Query("SELECT is_watchlisted FROM movies WHERE id == :movieId")
    suspend fun isMovieWatchlisted(movieId: Int): Boolean
}