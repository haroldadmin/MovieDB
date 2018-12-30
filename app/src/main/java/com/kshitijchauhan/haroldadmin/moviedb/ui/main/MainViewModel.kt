package com.kshitijchauhan.haroldadmin.moviedb.ui.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.kshitijchauhan.haroldadmin.moviedb.ui.UIState
import com.kshitijchauhan.haroldadmin.moviedb.utils.Constants
import com.kshitijchauhan.haroldadmin.moviedb.utils.SharedPreferencesDelegate
import com.kshitijchauhan.haroldadmin.moviedb.utils.SingleLiveEvent
import com.kshitijchauhan.haroldadmin.moviedb.utils.extensions.log

class MainViewModel(application: Application): AndroidViewModel(application) {

    private val _state = SingleLiveEvent<UIState>()
    private val _snackbar = SingleLiveEvent<String>()
    private var _isAuthenticated by SharedPreferencesDelegate(application, Constants.KEY_IS_AUTHENTICATED, false)
    private var _sessionId by SharedPreferencesDelegate(application, Constants.KEY_SESSION_ID, "")

    val state: LiveData<UIState>
        get() = _state

    val isAuthenticated: Boolean
        get() = _isAuthenticated

    val sessionId: String
        get() = _sessionId

    val snackbar: LiveData<String>
        get() = _snackbar

    fun <T: UIState> updateStateTo(state: T) {
        log("Updating view to: ${state::class.java.simpleName}")
        _state.postValue(state)
    }

    fun showSnackbar(message: String) {
        _snackbar.postValue(message)
    }

    fun setAuthenticationStatus(status: Boolean) {
        _isAuthenticated = status
    }

    fun peekState() = _state.value
}