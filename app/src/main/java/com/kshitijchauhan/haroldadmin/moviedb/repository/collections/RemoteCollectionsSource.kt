package com.kshitijchauhan.haroldadmin.moviedb.repository.collections

import com.kshitijchauhan.haroldadmin.moviedb.repository.data.remote.service.account.AccountService
import com.kshitijchauhan.haroldadmin.moviedb.repository.data.remote.service.common.GeneralMovieResponse
import com.kshitijchauhan.haroldadmin.moviedb.repository.data.remote.service.discover.DiscoveryService
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.flowables.ConnectableFlowable

class RemoteCollectionsSource(
    private val discoveryService: DiscoveryService,
    private val accountService: AccountService
) {

    fun getCollectionFlowable(accountId: Int, type: CollectionType): Flowable<Collection> {
        return when(type) {
            CollectionType.Favourite -> getFavouritesCollectionFlowable(accountId)
            CollectionType.Watchlist -> getWatchlistedCollectionFlowable(accountId)
            CollectionType.Popular -> getPopularCollectionFlowable()
            CollectionType.TopRated -> getTopRatedCollectionFlowable()
            CollectionType.InTheatres -> getInTheatresCollectionFlowable()
        }
    }

    fun getCollection(accountId: Int, type: CollectionType): Single<Collection> {
        return when(type) {
            CollectionType.Favourite -> getFavouritesCollection(accountId)
            CollectionType.Watchlist -> getWatchlistedCollection(accountId)
            CollectionType.Popular -> getPopularCollection()
            CollectionType.TopRated -> getTopRatedCollection()
            CollectionType.InTheatres -> getInTheatresCollection()
        }
    }

    private fun getFavouritesCollectionMovies(accountId: Int): ConnectableFlowable<GeneralMovieResponse> {
        return accountService.getFavouriteMovies(accountId)
            .flatMapPublisher { response ->
                Flowable.fromIterable(response.results)
            }
            .publish()
    }

    private fun getFavouritesCollectionIds(accountId: Int) {
        getFavouritesCollectionMovies(accountId)
            .map { generalMoviesResponse ->
                generalMoviesResponse.id
            }
            .toList()
            .flatMapPublisher { listOfIds ->
                Flowable.just(Collection(CollectionType.Favourite.name,))
            }
    }

    private fun getFavouritesCollectionFlowable(accountId: Int): Flowable<Collection> {
        return accountService.getFavouriteMovies(accountId)
            .flatMapPublisher { response ->
                Flowable.fromIterable(response.results)
            }
            .publish()
            .map { generalMovieResponse: GeneralMovieResponse ->
                generalMovieResponse.id
            }
            .toList()
            .flatMapPublisher { listOfIds ->
                Flowable.just(Collection(CollectionNames.FAVOURITES_NAME, listOfIds))
            }
    }

    private fun getFavouritesCollection(accountId: Int): Single<Collection> {
        return accountService.getFavouriteMovies(accountId)
            .flatMapPublisher { response ->
                Flowable.fromIterable(response.results)
            }
            .map { generalMovieResponse: GeneralMovieResponse ->
                generalMovieResponse.id
            }
            .toList()
            .flatMap { listOfIds ->
                Single.just(Collection(CollectionNames.FAVOURITES_NAME, listOfIds))
            }
    }

    private fun getWatchlistedCollectionFlowable(accountId: Int): Flowable<Collection> {
        return accountService.getMoviesWatchList(accountId)
            .flatMapPublisher { response ->
                Flowable.fromIterable(response.results)
            }
            .map { generalMovieResponse: GeneralMovieResponse ->
                generalMovieResponse.id
            }
            .toList()
            .flatMapPublisher { listOfIds ->
                Flowable.just(Collection(CollectionNames.WATCHLIST_NAME, listOfIds))
            }
    }

    private fun getWatchlistedCollection(accountId: Int): Single<Collection> {
        return accountService.getMoviesWatchList(accountId)
            .flatMapPublisher { response ->
                Flowable.fromIterable(response.results)
            }
            .map { generalMovieResponse: GeneralMovieResponse ->
                generalMovieResponse.id
            }
            .toList()
            .flatMap { listOfIds ->
                Single.just(Collection(CollectionNames.WATCHLIST_NAME, listOfIds))
            }
    }

    private fun getPopularCollectionFlowable(): Flowable<Collection> {
        return discoveryService.getPopularMovies()
            .flatMapPublisher { response ->
                Flowable.fromIterable(response.results)
            }
            .map { generalMovieResponse: GeneralMovieResponse ->
                generalMovieResponse.id
            }
            .toList()
            .flatMapPublisher { listOfIds ->
                Flowable.just(Collection(CollectionNames.POPULAR_NAME, listOfIds))
            }
    }

    private fun getPopularCollection(): Single<Collection> {
        return discoveryService.getPopularMovies()
            .flatMapPublisher { response ->
                Flowable.fromIterable(response.results)
            }
            .map { generalMovieResponse: GeneralMovieResponse ->
                generalMovieResponse.id
            }
            .toList()
            .flatMap { listOfIds ->
                Single.just(Collection(CollectionNames.POPULAR_NAME, listOfIds))
            }
    }

    private fun getTopRatedCollectionFlowable(): Flowable<Collection> {
        return discoveryService.getTopRatedMovies()
            .flatMapPublisher { response ->
                Flowable.fromIterable(response.results)
            }
            .map { generalMovieResponse: GeneralMovieResponse ->
                generalMovieResponse.id
            }
            .toList()
            .flatMapPublisher { listOfIds ->
                Flowable.just(Collection(CollectionNames.TOP_RATED_NAME, listOfIds))
            }
    }

    private fun getTopRatedCollection(): Single<Collection> {
        return discoveryService.getTopRatedMovies()
            .flatMapPublisher { response ->
                Flowable.fromIterable(response.results)
            }
            .map { generalMovieResponse: GeneralMovieResponse ->
                generalMovieResponse.id
            }
            .toList()
            .flatMap { listOfIds ->
                Single.just(Collection(CollectionNames.TOP_RATED_NAME, listOfIds))
            }
    }

    private fun getInTheatresCollectionFlowable(): Flowable<Collection> {
        return discoveryService.getMoviesInTheatre(region="IN")
            .flatMapPublisher { response ->
                Flowable.fromIterable(response.results)
            }
            .map { generalMovieResponse: GeneralMovieResponse ->
                generalMovieResponse.id
            }
            .toList()
            .flatMapPublisher { listOfIds ->
                Flowable.just(Collection(CollectionNames.IN_THEATRES_NAME, listOfIds))
            }
    }

    private fun getInTheatresCollection(): Single<Collection> {
        return discoveryService.getMoviesInTheatre(region="IN")
            .flatMapPublisher { response ->
                Flowable.fromIterable(response.results)
            }
            .map { generalMovieResponse: GeneralMovieResponse ->
                generalMovieResponse.id
            }
            .toList()
            .flatMap{ listOfIds ->
                Single.just(Collection(CollectionNames.IN_THEATRES_NAME, listOfIds))
            }
    }
}