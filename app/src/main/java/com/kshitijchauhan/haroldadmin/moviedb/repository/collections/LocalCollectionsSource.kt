package com.kshitijchauhan.haroldadmin.moviedb.repository.collections

import com.kshitijchauhan.haroldadmin.moviedb.repository.movies.Movie
import com.kshitijchauhan.haroldadmin.moviedb.repository.movies.MovieDao
import com.kshitijchauhan.haroldadmin.moviedb.utils.extensions.log
import io.reactivex.Flowable
import io.reactivex.Single

class LocalCollectionsSource(
    private val collectionsDao: CollectionDao,
    private val moviesDao: MovieDao
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

    fun getMoviesForCollection(collection: Collection): Single<List<Movie>> {
        log("Querying DB for collection movies: ${collection.contents}")
        return collectionsDao.getMoviesForCollection(collection.contents)
    }

    fun saveMoviesInCollection(collection: Collection) {
        log("Saving movies in collection")
        collection.movies?.let {
            moviesDao.saveAllMovies(it)
        }
    }

    fun saveCollection(collection: Collection) {
        collectionsDao.saveCollection(collection)
        this.saveMoviesInCollection(collection)
    }

    fun isCollectionInDatabase(type: CollectionType): Single<Int> {
        return collectionsDao.isCollectionInDatabase(type.name)
    }
}