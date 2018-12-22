package com.kshitijchauhan.haroldadmin.moviedb.auth

import android.app.Application
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.AndroidViewModel
import com.kshitijchauhan.haroldadmin.moviedb.MovieDBApplication
import com.kshitijchauhan.haroldadmin.moviedb.remote.ApiManager
import com.kshitijchauhan.haroldadmin.moviedb.utils.Constants
import com.kshitijchauhan.haroldadmin.moviedb.utils.SharedPreferencesDelegate
import com.kshitijchauhan.haroldadmin.moviedb.utils.disposeWith
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class AuthenticationViewModel(application: Application): AndroidViewModel(application) {

    private val compositeDisposable = CompositeDisposable()
    private var sessionId by SharedPreferencesDelegate(application, Constants.KEY_SESSION_ID, "")

    @Inject
    lateinit var apiManager: ApiManager

    init {
        getApplication<MovieDBApplication>()
            .appComponent
            .inject(this)
        println("$apiManager in AuthVM")
    }

    fun guestLogin() {
        apiManager
            .createGuestSession()
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.computation())
            .doOnSuccess { response ->
                if (response.success)
                    sessionId = response.id
            }
            .subscribe()
            .disposeWith(compositeDisposable)
    }
}