package com.kshitijchauhan.haroldadmin.moviedb.ui.auth

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kshitijchauhan.haroldadmin.moviedb.core.Resource
import com.kshitijchauhan.haroldadmin.moviedb.core.extensions.disposeWith
import com.kshitijchauhan.haroldadmin.moviedb.core.extensions.log
import com.kshitijchauhan.haroldadmin.moviedb.repository.data.remote.ApiManager
import com.kshitijchauhan.haroldadmin.moviedb.repository.data.remote.service.account.AccountDetailsResponse
import com.kshitijchauhan.haroldadmin.moviedb.repository.data.remote.service.auth.CreateSessionRequest
import com.kshitijchauhan.haroldadmin.moviedb.repository.data.remote.utils.SessionIdInterceptor
import com.kshitijchauhan.haroldadmin.moviedb.utils.SharedPreferencesDelegate
import com.kshitijchauhan.haroldadmin.moviedb.utils.SingleLiveEvent
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import java.io.IOException
import java.util.concurrent.TimeoutException

class AuthenticationViewModel(sharedPreferences: SharedPreferences,
                              private val sessionIdInterceptor: SessionIdInterceptor,
                              private val apiManager: ApiManager) : ViewModel() {

    private val _requestToken = MutableLiveData<Resource<String>>()
    private val _sessionId = MutableLiveData<Resource<String>>()
    private val _authSuccess = SingleLiveEvent<Boolean>()
    private val _accountDetails = MutableLiveData<Resource<AccountDetailsResponse>>()
    private val _message = SingleLiveEvent<String>()
    private val compositeDisposable = CompositeDisposable()
    private var sessionIdPref by SharedPreferencesDelegate(sharedPreferences, com.kshitijchauhan.haroldadmin.moviedb.core.Constants.KEY_SESSION_ID, "")
    private var isAuthenticatedPref by SharedPreferencesDelegate(sharedPreferences, com.kshitijchauhan.haroldadmin.moviedb.core.Constants.KEY_IS_AUTHENTICATED, false)
    private var accountIdPref by SharedPreferencesDelegate(sharedPreferences, com.kshitijchauhan.haroldadmin.moviedb.core.Constants.KEY_ACCOUNT_ID, -1)

    val requestToken: LiveData<Resource<String>>
        get() = _requestToken

    val sessionId: LiveData<Resource<String>>
        get() = _sessionId

    val authSuccess: LiveData<Boolean>
        get() = _authSuccess

    val accountDetails: LiveData<Resource<AccountDetailsResponse>>
        get() = _accountDetails

    val message: LiveData<String>
        get() = _message

    fun getRequestToken() {
        apiManager
            .getRequestToken()
            .subscribeOn(Schedulers.io())
            .subscribeBy(
                onSuccess = { token -> _requestToken.postValue(token) },
                onError = { error -> handleError(error, "get-request-token")}
            )
            .disposeWith(compositeDisposable)
    }

    fun createSession(request: CreateSessionRequest) {
        apiManager
            .createSession(request)
            .subscribeOn(Schedulers.io())
            .doOnSuccess {
                this.getAccountDetails()
            }
            .subscribeBy(
                onSuccess = { sessionIdResource ->
                    _sessionId.postValue(sessionIdResource)
                    if (sessionIdResource is Resource.Success) {
                        sessionIdPref = sessionIdResource.data
                        isAuthenticatedPref = true
                        _authSuccess.postValue(true)
                        setNewSessionIdToInterceptor(sessionIdResource.data)
                    }
                },
                onError = { error -> handleError(error, "create-session")}
            )
            .disposeWith(compositeDisposable)
    }

    fun getAccountDetails() {
        apiManager
            .getAccountDetails()
            .subscribeOn(Schedulers.io())
            .subscribeBy(
                onSuccess = { accountDetails ->
                    _accountDetails.postValue(accountDetails)
                    if (accountDetails is Resource.Success) {
                        accountIdPref = accountDetails.data.id
                    }
                },
                onError = { error -> handleError(error, "get-account-details") }
            )
            .disposeWith(compositeDisposable)
    }

    fun setNewSessionIdToInterceptor(newId: String) {
        sessionIdInterceptor.setSessionId(newId)
    }

    private fun handleError(error: Throwable, caller: String) {
        error.localizedMessage?.let {
            log("ERROR $caller -> $it")
        } ?: log("ERROR $caller ->")
            .also {
                error.printStackTrace()
            }
        when (error) {
            is IOException -> _message.postValue("Please check your internet connection")
            is TimeoutException -> _message.postValue("Request timed out")
            else -> _message.postValue("An error occurred")
        }
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}