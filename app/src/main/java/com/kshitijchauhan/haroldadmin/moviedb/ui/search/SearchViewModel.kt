package com.kshitijchauhan.haroldadmin.moviedb.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.DiffUtil
import com.kshitijchauhan.haroldadmin.moviedb.remote.ApiManager
import com.kshitijchauhan.haroldadmin.moviedb.remote.service.common.GeneralMovieResponse
import com.kshitijchauhan.haroldadmin.moviedb.utils.RxDiffUtil
import com.kshitijchauhan.haroldadmin.moviedb.utils.extensions.disposeWith
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class SearchViewModel(private val apiManager: ApiManager) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()
    private val _searchUpdate = MutableLiveData<Pair<List<GeneralMovieResponse>, DiffUtil.DiffResult>>()

    val searchUpdate: LiveData<Pair<List<GeneralMovieResponse>, DiffUtil.DiffResult>>
        get() = _searchUpdate

    private var currentQuery: Disposable? = null

    fun getMoviesForQuery(query: String) {

        // Cancel the last query, we don't want it anymore
        currentQuery?.dispose()

        currentQuery = apiManager
            .search(query)
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.computation())
            .map { response ->
                response.results
            }
            .flatMapObservable { movies ->
                Observable.fromIterable(movies)
            }
            .toList()
            .toObservable()
            .compose(RxDiffUtil.calculateDiff { oldList, newList ->
                SearchDiffUtil(oldList, newList)
            })
            .doOnNext {
                _searchUpdate.postValue(it)
            }
            .subscribe()

        currentQuery?.disposeWith(compositeDisposable)

    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}
