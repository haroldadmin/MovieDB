package com.kshitijchauhan.haroldadmin.moviedb.ui.in_theatres

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.kshitijchauhan.haroldadmin.moviedb.R
import com.kshitijchauhan.haroldadmin.moviedb.core.extensions.disposeWith
import com.kshitijchauhan.haroldadmin.moviedb.repository.collections.CollectionType
import com.kshitijchauhan.haroldadmin.moviedb.repository.collections.CollectionsRepository
import com.kshitijchauhan.haroldadmin.moviedb.ui.UIState
import com.kshitijchauhan.haroldadmin.moviedb.ui.common.SnackbarAction
import com.kshitijchauhan.haroldadmin.moviedb.core.extensions.log
import com.kshitijchauhan.haroldadmin.mvrxlite.base.MVRxLiteViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import java.io.IOException
import java.util.concurrent.TimeoutException

class InTheatresViewModel(
    private val collectionsRepository: CollectionsRepository,
    initialState: UIState.InTheatresScreenState
) : MVRxLiteViewModel<UIState.InTheatresScreenState>(initialState) {

    private val _message = MutableLiveData<SnackbarAction>()
    private val compositeDisposable = CompositeDisposable()

    val message: LiveData<SnackbarAction>
        get() = _message

    fun getMoviesInTheatres() {
        withState { state ->
            collectionsRepository.getCollectionFlowable(type = CollectionType.InTheatres, region = state.countryCode)
                .init(compositeDisposable)
                .subscribeOn(Schedulers.io())
                .subscribeBy(
                    onNext = { inTheatreMovies ->
                        setState { copy(inTheatresMoviesResource = inTheatreMovies) }
                    },
                    onError = { error -> handleError(error, "get-movies-in-theatres") }
                )
                .disposeWith(compositeDisposable)
        }
    }

    fun forceRefreshInTheatresCollection() {
        withState { state ->
            collectionsRepository.forceRefreshCollection(type = CollectionType.InTheatres, region = state.countryCode)
                .init(compositeDisposable)
                .subscribeOn(Schedulers.io())
        }
    }

    fun changeCountryCode(code: String) {
        withState { currentState ->
            if (currentState.countryCode != code) {
                setState { copy(countryCode = code) }
            }
        }
    }

    fun changeCountryName(name: String) {
        withState { currentState ->
            if (currentState.countryName != name) {
                setState { copy(countryName = name) }
            }
        }
    }

    private fun handleError(error: Throwable, caller: String) {
        error.localizedMessage?.let {
            log("ERROR $caller -> $it")
        } ?: log("ERROR $caller ->")
            .also {
                error.printStackTrace()
            }
        when (error) {
            is IOException -> _message.postValue(SnackbarAction(R.string.error_check_internet))
            is TimeoutException -> _message.postValue(SnackbarAction(R.string.error_timeout))
            else -> _message.postValue(SnackbarAction(R.string.error_general_error))
        }
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}