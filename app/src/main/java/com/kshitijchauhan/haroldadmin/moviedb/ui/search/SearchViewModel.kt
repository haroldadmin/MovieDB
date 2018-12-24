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
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class SearchViewModel(application: Application) : AndroidViewModel(application) {

    private val compositeDisposable = CompositeDisposable()
    private val _searchUpdate = MutableLiveData<Pair<List<Movie>, DiffUtil.DiffResult>>()

    val searchUpdate: LiveData<Pair<List<Movie>, DiffUtil.DiffResult>>
        get() = _searchUpdate

    @Inject
    lateinit var apiManager: ApiManager

    init {
        (application as MovieDBApplication)
            .appComponent
            .inject(this)
    }

    fun getMoviesForQuery(query: String) {
        compositeDisposable.clear()
        apiManager
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
            .disposeWith(compositeDisposable)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}
