package com.kshitijchauhan.haroldadmin.moviedb.repository.collections

import androidx.room.*
import com.kshitijchauhan.haroldadmin.moviedb.repository.movies.Movie
import io.reactivex.Flowable
import io.reactivex.Single

@Dao
interface CollectionDao {

    @Query("SELECT * FROM collections WHERE name = '${CollectionNames.FAVOURITES_NAME}'")
    fun getFavouriteMoviesFlowable(): Flowable<Collection>

    @Query("SELECT * FROM collections WHERE name = '${CollectionNames.FAVOURITES_NAME}'")
    fun getFavouriteMovies(): Single<Collection>

    @Query("SELECT *FROM collections WHERE name = '${CollectionNames.WATCHLIST_NAME}'")
    fun getWatchlistedMoviesFlowable(): Flowable<Collection>

    @Query("SELECT *FROM collections WHERE name = '${CollectionNames.WATCHLIST_NAME}'")
    fun getWatchlistedMovies(): Single<Collection>

    @Query("SELECT * FROM collections WHERE name = '${CollectionNames.TOP_RATED_NAME}'")
    fun getTopRatedMoviesFlowable(): Flowable<Collection>

    @Query("SELECT * FROM collections WHERE name = '${CollectionNames.TOP_RATED_NAME}'")
    fun getTopRatedMovies(): Single<Collection>

    @Query("SELECT * FROM collections WHERE name = '${CollectionNames.POPULAR_NAME}'")
    fun getPopularMoviesFlowable(): Flowable<Collection>

    @Query("SELECT * FROM collections WHERE name = '${CollectionNames.POPULAR_NAME}'")
    fun getPopularMovies(): Single<Collection>

    @Query("SELECT * FROM collections WHERE name = '${CollectionNames.IN_THEATRES_NAME}'")
    fun getMoviesInTheatresFlowable(): Flowable<Collection>

    @Query("SELECT * FROM collections WHERE name = '${CollectionNames.IN_THEATRES_NAME}'")
    fun getMoviesInTheatres(): Single<Collection>

    @Query("SELECT COUNT(*) FROM collections WHERE name = :name")
    fun isCollectionInDatabase(name: String): Single<Int>

    @Query("SELECT * FROM movies WHERE id IN (:ids)")
    fun getMoviesForCollection(ids: List<Int>): Single<List<Movie>>

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