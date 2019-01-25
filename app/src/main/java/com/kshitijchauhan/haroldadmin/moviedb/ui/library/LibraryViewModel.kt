package com.kshitijchauhan.haroldadmin.moviedb.ui.library

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kshitijchauhan.haroldadmin.moviedb.repository.data.remote.ApiManager
import com.kshitijchauhan.haroldadmin.moviedb.ui.MovieItemType
import com.kshitijchauhan.haroldadmin.moviedb.ui.common.model.MovieGridItem
import com.kshitijchauhan.haroldadmin.moviedb.utils.extensions.disposeWith
import com.kshitijchauhan.haroldadmin.moviedb.utils.extensions.getPosterUrl
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class LibraryViewModel(private val apiManager: ApiManager) : ViewModel() {

    private val _favouriteMoviesUpdate = MutableLiveData<List<MovieGridItem>>()
    private val _watchlistedMoviesUpdate = MutableLiveData<List<MovieGridItem>>()
    private val compositeDisposable = CompositeDisposable()

    val favouriteMoviesUpdate: LiveData<List<MovieGridItem>>
        get() = _favouriteMoviesUpdate

    val watchListMoviesUpdate: LiveData<List<MovieGridItem>>
        get() = _watchlistedMoviesUpdate

    fun getFavouriteMoves(accountId: Int) {
        apiManager
            .getFavouriteMovies(accountId)
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.computation())
            .map { response ->
                response.results
            }
            .flatMapObservable { list ->
                Observable.fromIterable(list)
            }
            .map { movie ->
                with(movie) {
                    MovieGridItem(
                        id,
                        title,
                        this.posterPath.getPosterUrl(),
                        MovieItemType.LibraryType.Favourite
                    )
                }
            }
            .toList()
            .doOnSuccess {
                _favouriteMoviesUpdate.postValue(it)
            }
            .subscribe()
            .disposeWith(compositeDisposable)
    }

    fun getWatchlistedeMovies(accountId: Int) {
        apiManager
            .getMoviesWatchList(accountId)
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.computation())
            .map { response ->
                response.results
            }
            .flatMapObservable { list ->
                Observable.fromIterable(list)
            }
            .map { movie ->
                with(movie) {
                    MovieGridItem(
                        id,
                        title,
                        this.posterPath.getPosterUrl(),
                        MovieItemType.LibraryType.Watchlisted
                    )
                }
            }
            .toList()
            .doOnSuccess {
                _watchlistedMoviesUpdate.postValue(it)
            }
            .subscribe()
            .disposeWith(compositeDisposable)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}
