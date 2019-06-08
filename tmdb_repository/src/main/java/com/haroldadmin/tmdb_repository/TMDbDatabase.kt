package com.haroldadmin.tmdb_repository

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.haroldadmin.tmdb_repository.actor.local.Actor
import com.haroldadmin.tmdb_repository.actor.local.Cast
import com.haroldadmin.tmdb_repository.movie.local.MovieDao
import com.haroldadmin.tmdb_repository.movie.local.models.Genre
import com.haroldadmin.tmdb_repository.movie.local.models.Movie
import com.haroldadmin.tmdb_repository.movie.local.models.MovieGenre
import com.haroldadmin.tmdb_repository.movie.local.models.Trailer

@Database(
    entities = [Movie::class, Genre::class, Trailer::class, MovieGenre::class, Actor::class, Cast::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class TMDbDatabase : RoomDatabase() {
    abstract fun moviesDao(): MovieDao
}