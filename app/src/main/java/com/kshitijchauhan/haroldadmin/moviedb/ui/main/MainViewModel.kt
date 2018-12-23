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

    private val _state = SingleLiveEvent<Pair<UIState?, UIState>>()
    private var sessionId by SharedPreferencesDelegate(getApplication(), Constants.KEY_SESSION_ID, "")

    val state: LiveData<Pair<UIState?, UIState>>
        get() = _state

    fun updateState(state: Pair<UIState?, UIState>?) {
        log("Update state from ${state?.first} to ${state?.second}")
        _state.postValue(state)
    }

    fun peekState() = _state.value
}