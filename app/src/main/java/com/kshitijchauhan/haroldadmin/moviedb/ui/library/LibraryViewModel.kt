package com.kshitijchauhan.haroldadmin.moviedb.ui.library

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.kshitijchauhan.haroldadmin.moviedb.MovieDBApplication
import com.kshitijchauhan.haroldadmin.moviedb.remote.ApiManager
import com.kshitijchauhan.haroldadmin.moviedb.ui.MovieItemType
import com.kshitijchauhan.haroldadmin.moviedb.ui.common.model.MovieGridItem
import com.kshitijchauhan.haroldadmin.moviedb.utils.extensions.disposeWith
import com.kshitijchauhan.haroldadmin.moviedb.utils.extensions.getPosterUrl
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class LibraryViewModel(application: Application) : AndroidViewModel(application) {

    private val _isFavouritesLoading = MutableLiveData<Boolean>()
    private val _isWatchlistLoading = MutableLiveData<Boolean>()
    private val _isLoading = MediatorLiveData<Boolean>()
    private val _favouriteMoviesUpdate = MutableLiveData<List<MovieGridItem>>()
    private val _watchlistedMoviesUpdate = MutableLiveData<List<MovieGridItem>>()
    private val compositeDisposable = CompositeDisposable()

    @Inject
    lateinit var apiManager: ApiManager

    val favouriteMoviesUpdate: LiveData<List<MovieGridItem>>
        get() = _favouriteMoviesUpdate

    val watchListMoviesUpdate: LiveData<List<MovieGridItem>>
        get() = _watchlistedMoviesUpdate

    val isLoading: LiveData<Boolean>
        get() = _isLoading

    init {
        getApplication<MovieDBApplication>()
            .appComponent
            .inject(this)

        _isLoading.apply {
            addSource(_isFavouritesLoading) { isLoading ->
                _isLoading.value = (_isLoading.value ?: false) || isLoading
            }
            addSource(_isWatchlistLoading) { isLoading ->
                _isLoading.value = (_isLoading.value ?: false) || isLoading
            }
        }
    }

    fun getFavouriteMoves(accountId: Int) {
        _isFavouritesLoading.value = true
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
                        getPosterUrl(posterPath),
                        MovieItemType.LibraryType.Favourite
                    )
                }
            }
            .toList()
            .doOnSuccess {
                _isFavouritesLoading.postValue(false)
                _favouriteMoviesUpdate.postValue(it)
            }
            .subscribe()
            .disposeWith(compositeDisposable)
    }

    fun getWatchlistedeMovies(accountId: Int) {
        _isFavouritesLoading.value = true
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
                        getPosterUrl(posterPath),
                        MovieItemType.LibraryType.Watchlisted
                    )
                }
            }
            .toList()
            .doOnSuccess {
                _isWatchlistLoading.postValue(false)
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
