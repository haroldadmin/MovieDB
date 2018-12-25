package com.kshitijchauhan.haroldadmin.moviedb.ui.search

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.DiffUtil
import com.kshitijchauhan.haroldadmin.moviedb.MovieDBApplication
import com.kshitijchauhan.haroldadmin.moviedb.model.Movie
import com.kshitijchauhan.haroldadmin.moviedb.remote.ApiManager
import com.kshitijchauhan.haroldadmin.moviedb.utils.RxDiffUtil
import com.kshitijchauhan.haroldadmin.moviedb.utils.disposeWith
import com.kshitijchauhan.haroldadmin.moviedb.utils.log
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class SearchViewModel(application: Application) : AndroidViewModel(application) {

    private val compositeDisposable = CompositeDisposable()
    private val _searchUpdate = MutableLiveData<Pair<List<Movie>, DiffUtil.DiffResult>>()
    private val _searchResults = MutableLiveData<List<Movie>>()

    val searchUpdate: LiveData<Pair<List<Movie>, DiffUtil.DiffResult>>
        get() = _searchUpdate

    val searchResults: LiveData<List<Movie>>
        get() = _searchResults

    @Inject
    lateinit var apiManager: ApiManager
    private var currentQuery: Disposable? = null

    init {
        (application as MovieDBApplication)
            .appComponent
            .inject(this)
    }

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
//            .toObservable()
//            .compose(RxDiffUtil.calculateDiff { oldList, newList ->
//                SearchDiffUtil(oldList, newList)
//            })
//            .observeOn(AndroidSchedulers.mainThread())
            .doOnSuccess {
                _searchResults.postValue(it)
            }
            .doOnDispose {
                log("Observable for query $query is being disposed")
            }
            .subscribe()

        currentQuery?.disposeWith(compositeDisposable)

    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}
