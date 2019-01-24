package com.kshitijchauhan.haroldadmin.moviedb.repository.local.dao

import androidx.room.*
import com.kshitijchauhan.haroldadmin.moviedb.repository.local.model.Collection
import io.reactivex.Observable

@Dao
interface CollectionDao {

    @Query("SELECT * FROM collections WHERE name = 'favourites'")
    fun getFavouriteMovies(): Observable<Collection>

    @Query("SELECT *FROM collections WHERE name = 'watchlisted'")
    fun getWatchlistedMovies(): Observable<Collection>

    @Query("SELECT * FROM collections WHERE name = 'top-rated'")
    fun getTopRatedMovies(): Observable<Collection>

    @Query("SELECT * FROM collections WHERE name = 'popular'")
    fun getPopularMovies(): Observable<Collection>

    @Query("SELECT * FROM collections WHERE name = 'in-theatres'")
    fun getMoviesInTheatres(): Observable<Collection>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveCollection(collection: Collection)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveCollections(vararg collection: Collection)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveAllCollections(collections: List<Collection>)

    @Update
    fun updateCollection(collection: Collection)

    @Update
    fun updateCollections(vararg collection: Collection)

    @Update
    fun updateAllCollections(collections: List<Collection>)

    @Delete
    fun deleteCollection(collection: Collection)
}