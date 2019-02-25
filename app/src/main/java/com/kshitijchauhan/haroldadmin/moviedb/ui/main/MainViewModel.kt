package com.kshitijchauhan.haroldadmin.moviedb.ui.main

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kshitijchauhan.haroldadmin.moviedb.ui.UIState
import com.kshitijchauhan.haroldadmin.moviedb.ui.common.BackPressListener
import com.kshitijchauhan.haroldadmin.moviedb.utils.Constants
import com.kshitijchauhan.haroldadmin.moviedb.utils.SharedPreferencesDelegate
import com.kshitijchauhan.haroldadmin.moviedb.utils.SingleLiveEvent

class MainViewModel(
    sharedPreferences: SharedPreferences
) : ViewModel() {

    private val _state = SingleLiveEvent<UIState>()
    private val _snackbar = SingleLiveEvent<String>()
    private val _toolbarTitle = SingleLiveEvent<String>()
    private var _isAuthenticated by SharedPreferencesDelegate(sharedPreferences, Constants.KEY_IS_AUTHENTICATED, false)
    private var _sessionId by SharedPreferencesDelegate(sharedPreferences, Constants.KEY_SESSION_ID, "")
    private var _accountId by SharedPreferencesDelegate(sharedPreferences, Constants.KEY_ACCOUNT_ID, -1)
    private var _backPressListener = MutableLiveData<BackPressListener>()

    val state: LiveData<UIState>
        get() = _state

    val isAuthenticated: Boolean
        get() = _isAuthenticated

    val accountId: Int
        get() = _accountId

    val snackbar: LiveData<String>
        get() = _snackbar

    val toolbarTitle: LiveData<String>
        get() = _toolbarTitle

    val backPressListener: LiveData<BackPressListener>
        get() = _backPressListener

    fun showSnackbar(message: String) {
        _snackbar.postValue(message)
    }

    fun setAuthenticationStatus(status: Boolean) {
        _isAuthenticated = status
    }

    fun setSessionId(sessionId: String) {
        _sessionId = sessionId
    }

    fun updateToolbarTitle(title: String) {
        _toolbarTitle.value = title
    }

    fun setBackPressListener(listener: BackPressListener?) = _backPressListener.postValue(listener)
}