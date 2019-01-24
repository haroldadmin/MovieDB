package com.kshitijchauhan.haroldadmin.moviedb.repository.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.kshitijchauhan.haroldadmin.moviedb.repository.local.dao.ActorsDao
import com.kshitijchauhan.haroldadmin.moviedb.repository.local.dao.CollectionDao
import com.kshitijchauhan.haroldadmin.moviedb.repository.local.dao.MovieDao
import com.kshitijchauhan.haroldadmin.moviedb.repository.local.model.Actor
import com.kshitijchauhan.haroldadmin.moviedb.repository.local.model.Collection
import com.kshitijchauhan.haroldadmin.moviedb.repository.local.model.Converters
import com.kshitijchauhan.haroldadmin.moviedb.repository.local.model.Movie

@Database(entities = [Movie::class, Actor::class, Collection::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class MovieDBDatabase : RoomDatabase() {

    abstract fun moviesDao(): MovieDao
    abstract fun actorsDao(): ActorsDao
    abstract fun collectionsDao(): CollectionDao
}
