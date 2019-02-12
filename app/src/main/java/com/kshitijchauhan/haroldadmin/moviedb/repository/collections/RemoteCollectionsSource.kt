package com.kshitijchauhan.haroldadmin.moviedb.repository.collections

import com.kshitijchauhan.haroldadmin.moviedb.repository.data.Resource
import com.kshitijchauhan.haroldadmin.moviedb.repository.data.remote.service.account.AccountService
import com.kshitijchauhan.haroldadmin.moviedb.repository.data.remote.service.discover.DiscoveryService
import com.kshitijchauhan.haroldadmin.moviedb.repository.data.remote.utils.NetworkResponse
import com.kshitijchauhan.haroldadmin.moviedb.utils.extensions.toMovie
import io.reactivex.Flowable
import io.reactivex.Single

class RemoteCollectionsSource(
    private val discoveryService: DiscoveryService,
    private val accountService: AccountService
) {

    fun getCollectionFlowable(accountId: Int, type: CollectionType): Flowable<Collection> {
        return when (type) {
            CollectionType.Favourite -> getFavouritesCollectionFlowable(accountId)
            CollectionType.Watchlist -> getWatchlistedCollectionFlowable(accountId)
            CollectionType.Popular -> getPopularCollectionFlowable()
            CollectionType.TopRated -> getTopRatedCollectionFlowable()
            CollectionType.InTheatres -> getInTheatresCollectionFlowable()
        }
    }

    fun getCollection(accountId: Int, type: CollectionType): Single<Collection> {
        return when (type) {
            CollectionType.Favourite -> getFavouritesCollection(accountId)
            CollectionType.Watchlist -> getWatchlistedCollection(accountId)
            CollectionType.Popular -> getPopularCollection()
            CollectionType.TopRated -> getTopRatedCollection()
            CollectionType.InTheatres -> getInTheatresCollection()
        }
    }

    private fun getFavouritesCollectionFlowable(accountId: Int): Flowable<Collection> {
        return accountService.getFavouriteMovies(accountId)
            .flatMapPublisher { favouriteMoviesResponse ->
                Flowable.just(
                    Collection(
                        CollectionType.Favourite.name,
                        favouriteMoviesResponse.results.map { generalMovieResponse -> generalMovieResponse.id })
                        .apply {
                            movies = favouriteMoviesResponse.results.map { generalMovieResponse ->
                                generalMovieResponse.toMovie()
                            }
                        }
                )
            }
    }

    private fun getFavouritesCollection(accountId: Int): Single<Collection> {
        return accountService.getFavouriteMovies(accountId)
            .flatMap { favouriteMoviesResponse ->
                Single.just(
                    Collection(
                        CollectionType.Favourite.name,
                        favouriteMoviesResponse.results.map { generalMovieResponse -> generalMovieResponse.id })
                        .apply {
                            movies = favouriteMoviesResponse.results.map { generalMovieResponse ->
                                generalMovieResponse.toMovie()
                            }
                        }
                )
            }
    }

    private fun getWatchlistedCollectionFlowable(accountId: Int): Flowable<Collection> {
        return accountService.getMoviesWatchList(accountId)
            .flatMapPublisher { watchlistResponse ->
                Flowable.just(
                    Collection(
                        CollectionType.Watchlist.name,
                        watchlistResponse.results.map { generalMovieResponse -> generalMovieResponse.id })
                        .apply {
                            movies = watchlistResponse.results.map { generalMovieResponse ->
                                generalMovieResponse.toMovie()
                            }
                        }
                )
            }
    }

    private fun getWatchlistedCollection(accountId: Int): Single<Collection> {
        return accountService.getMoviesWatchList(accountId)
            .flatMap { watchlistResponse ->
                Single.just(
                    Collection(
                        CollectionType.Watchlist.name,
                        watchlistResponse.results.map { generalMovieResponse -> generalMovieResponse.id })
                        .apply {
                            movies = watchlistResponse.results.map { generalMovieResponse ->
                                generalMovieResponse.toMovie()
                            }
                        }
                )
            }
    }

    private fun getPopularCollectionFlowable(): Flowable<Collection> {
        return discoveryService.getPopularMovies()
            .flatMapPublisher { popularMoviesResponse ->
                Flowable.just(
                    Collection(
                        CollectionType.Popular.name,
                        popularMoviesResponse.results.map { generalMovieResponse -> generalMovieResponse.id })
                        .apply {
                            movies = popularMoviesResponse.results.map { generalMovieResponse ->
                                generalMovieResponse.toMovie()
                            }
                        }
                )
            }
    }

    private fun getPopularCollection(): Single<Collection> {
        return discoveryService.getPopularMovies()
            .flatMap { popularMoviesResponse ->
                Single.just(
                    Collection(
                        CollectionType.Popular.name,
                        popularMoviesResponse.results.map { generalMovieResponse -> generalMovieResponse.id })
                        .apply {
                            movies = popularMoviesResponse.results.map { generalMovieResponse ->
                                generalMovieResponse.toMovie()
                            }
                        }
                )
            }
    }

    private fun getTopRatedCollectionFlowable(): Flowable<Collection> {
        return discoveryService.getTopRatedMovies()
            .flatMapPublisher { topRatedResponse ->
                Flowable.just(
                    Collection(
                        CollectionType.TopRated.name,
                        topRatedResponse.results.map { generalMovieResponse -> generalMovieResponse.id })
                        .apply {
                            movies = topRatedResponse.results.map { generalMovieResponse ->
                                generalMovieResponse.toMovie()
                            }
                        }
                )
            }
    }

    private fun getTopRatedCollection(): Single<Collection> {
        return discoveryService.getTopRatedMovies()
            .flatMap { topRatedResponse ->
                Single.just(
                    Collection(
                        CollectionType.TopRated.name,
                        topRatedResponse.results.map { generalMovieResponse -> generalMovieResponse.id })
                        .apply {
                            movies = topRatedResponse.results.map { generalMovieResponse ->
                                generalMovieResponse.toMovie()
                            }
                        }
                )
            }
    }

    private fun getInTheatresCollectionFlowable(): Flowable<Collection> {

//        return discoveryService.getMoviesInTheatre(region = "IN")
//            .flatMapPublisher { response ->
//                Flowable.just(
//                    when (response) {
//                        is NetworkResponse.Success -> {
//                            Resource.success(
//                                Collection(CollectionType.InTheatres.name, response.body.results.map { it.id })
//                                    .apply {
//                                        movies = response.body.results.map { it.toMovie() }
//                                    })
//                        }
//                        is NetworkResponse.ServerError -> {
//                            Resource.error(response.body?.statusMessage ?: "Server error")
//                        }
//                        is NetworkResponse.NetworkError -> {
//                            Resource.error(response.error.localizedMessage ?: "Network error")
//                        }
//                    }
//                )
//            }

        return discoveryService.getMoviesInTheatre(region = "IN")
            .flatMapPublisher { topRatedResponse ->
                Flowable.just(
                    Collection(
                        CollectionType.InTheatres.name,
                        topRatedResponse.results.map { generalMovieResponse -> generalMovieResponse.id })
                        .apply {
                            movies = topRatedResponse.results.map { generalMovieResponse ->
                                generalMovieResponse.toMovie()
                            }
                        }
                )
            }
    }

    private fun getInTheatresCollection(): Single<Collection> {
        return discoveryService.getMoviesInTheatre(region = "IN")
            .flatMap { topRatedResponse ->
                Single.just(
                    Collection(
                        CollectionType.InTheatres.name,
                        topRatedResponse.results.map { generalMovieResponse -> generalMovieResponse.id })
                        .apply {
                            movies = topRatedResponse.results.map { generalMovieResponse ->
                                generalMovieResponse.toMovie()
                            }
                        }
                )
            }
    }
}