package com.kshitijchauhan.haroldadmin.moviedb.repository.collections

import io.reactivex.Flowable
import io.reactivex.Single

class LocalCollectionsSource(
    private val collectionsDao: CollectionDao
) {

    fun getCollectionFlowable(type: CollectionType): Flowable<Collection> {
        return when (type) {
            CollectionType.Favourite -> collectionsDao.getFavouriteMoviesFlowable()
            CollectionType.Watchlist -> collectionsDao.getWatchlistedMoviesFlowable()
            CollectionType.Popular -> collectionsDao.getPopularMoviesFlowable()
            CollectionType.TopRated -> collectionsDao.getTopRatedMoviesFlowable()
            CollectionType.InTheatres -> collectionsDao.getMoviesInTheatresFlowable()
        }
    }

    fun getCollection(type: CollectionType): Single<Collection> {
        return when (type) {
            CollectionType.Favourite -> collectionsDao.getFavouriteMovies()
            CollectionType.Watchlist -> collectionsDao.getWatchlistedMovies()
            CollectionType.Popular -> collectionsDao.getPopularMovies()
            CollectionType.TopRated -> collectionsDao.getTopRatedMovies()
            CollectionType.InTheatres -> collectionsDao.getMoviesInTheatres()
        }
    }

    fun saveCollection(collection: Collection) {
        collectionsDao.saveCollection(collection)
    }

    fun isCollectionInDatabase(type: CollectionType): Single<Int> {
        return collectionsDao.isCollectionInDatabase(type.name)
    }
}