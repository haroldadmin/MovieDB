package com.kshitijchauhan.haroldadmin.moviedb.repository.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.kshitijchauhan.haroldadmin.moviedb.repository.actors.ActorsDao
import com.kshitijchauhan.haroldadmin.moviedb.repository.collections.CollectionDao
import com.kshitijchauhan.haroldadmin.moviedb.repository.actors.Actor
import com.kshitijchauhan.haroldadmin.moviedb.repository.collections.Collection
import com.kshitijchauhan.haroldadmin.moviedb.repository.data.local.Converters
import com.kshitijchauhan.haroldadmin.moviedb.repository.movies.*

@Database(
    entities = [Movie::class, Actor::class, Collection::class, AccountState::class, Cast::class, MovieTrailer::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class MovieDBDatabase : RoomDatabase() {

    internal abstract fun moviesDao(): MovieDao
    internal abstract fun actorsDao(): ActorsDao
    internal abstract fun collectionsDao(): CollectionDao
}
