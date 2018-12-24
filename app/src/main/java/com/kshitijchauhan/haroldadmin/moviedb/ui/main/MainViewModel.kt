package com.kshitijchauhan.haroldadmin.moviedb.ui.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.kshitijchauhan.haroldadmin.moviedb.ui.UIState
import com.kshitijchauhan.haroldadmin.moviedb.utils.Constants
import com.kshitijchauhan.haroldadmin.moviedb.utils.SharedPreferencesDelegate
import com.kshitijchauhan.haroldadmin.moviedb.utils.SingleLiveEvent
import com.kshitijchauhan.haroldadmin.moviedb.utils.log

class MainViewModel(application: Application): AndroidViewModel(application) {

    private val _state = SingleLiveEvent<UIState>()
    private var sessionId by SharedPreferencesDelegate(getApplication(), Constants.KEY_SESSION_ID, "")

    val state: LiveData<UIState>
        get() = _state

    fun updateStateTo(state: UIState) {
        log("Updating view to: ${UIState::class.java.simpleName}")
        _state.postValue(state)
    }

    fun peekState() = _state.value
}