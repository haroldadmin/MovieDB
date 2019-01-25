package com.kshitijchauhan.haroldadmin.moviedb.repository.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.kshitijchauhan.haroldadmin.moviedb.repository.actors.ActorsDao
import com.kshitijchauhan.haroldadmin.moviedb.repository.collections.CollectionDao
import com.kshitijchauhan.haroldadmin.moviedb.repository.movies.MovieDao
import com.kshitijchauhan.haroldadmin.moviedb.repository.data.local.model.Actor
import com.kshitijchauhan.haroldadmin.moviedb.repository.data.local.model.Collection
import com.kshitijchauhan.haroldadmin.moviedb.repository.data.local.model.Converters
import com.kshitijchauhan.haroldadmin.moviedb.repository.data.local.model.Movie

@Database(entities = [Movie::class, Actor::class, Collection::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class MovieDBDatabase : RoomDatabase() {

    abstract fun moviesDao(): MovieDao
    abstract fun actorsDao(): ActorsDao
    abstract fun collectionsDao(): CollectionDao
}
