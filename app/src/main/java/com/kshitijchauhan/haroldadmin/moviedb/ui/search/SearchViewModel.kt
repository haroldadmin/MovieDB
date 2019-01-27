package com.kshitijchauhan.haroldadmin.moviedb.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kshitijchauhan.haroldadmin.moviedb.repository.data.remote.ApiManager
import com.kshitijchauhan.haroldadmin.moviedb.repository.data.remote.service.common.GeneralMovieResponse
import com.kshitijchauhan.haroldadmin.moviedb.repository.movies.Movie
import com.kshitijchauhan.haroldadmin.moviedb.ui.MovieItemType
import com.kshitijchauhan.haroldadmin.moviedb.ui.common.model.MovieGridItem
import com.kshitijchauhan.haroldadmin.moviedb.utils.extensions.disposeWith
import com.kshitijchauhan.haroldadmin.moviedb.utils.extensions.getPosterUrl
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class SearchViewModel(private val apiManager: ApiManager) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()
    private val _searchResults = MutableLiveData<List<Movie>>()

    val searchResults: LiveData<List<Movie>>
        get() = _searchResults

    private var currentQuery: Disposable? = null

    fun getMoviesForQuery(query: String) {

        // Cancel the last query, we don't want it anymore
        currentQuery?.dispose()

        currentQuery = apiManager
            .search(query)
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.computation())
            .flatMapObservable { response ->
                Observable.fromIterable(response.results)
            }
            .map { generalMovieResponse: GeneralMovieResponse ->
                MovieGridItem(
                    generalMovieResponse.id,
                    generalMovieResponse.title,
                    generalMovieResponse.posterPath.getPosterUrl(),
                    MovieItemType.MovieType.SearchResult
                )
            }
            .toList()
            .doOnSuccess {
//                _searchResults.postValue(it)
                TODO("Update search view model to use a different data type")
            }
            .subscribe()

        currentQuery?.disposeWith(compositeDisposable)

    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}
