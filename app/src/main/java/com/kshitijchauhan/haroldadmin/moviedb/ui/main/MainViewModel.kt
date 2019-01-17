package com.kshitijchauhan.haroldadmin.moviedb.ui.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.kshitijchauhan.haroldadmin.moviedb.MovieDBApplication
import com.kshitijchauhan.haroldadmin.moviedb.ui.UIState
import com.kshitijchauhan.haroldadmin.moviedb.ui.common.BottomNavManager
import com.kshitijchauhan.haroldadmin.moviedb.ui.common.ProgressBarManager
import com.kshitijchauhan.haroldadmin.moviedb.ui.common.model.LoadingTask
import com.kshitijchauhan.haroldadmin.moviedb.ui.common.model.ProgressBarNotification
import com.kshitijchauhan.haroldadmin.moviedb.utils.Constants
import com.kshitijchauhan.haroldadmin.moviedb.utils.SharedPreferencesDelegate
import com.kshitijchauhan.haroldadmin.moviedb.utils.SingleLiveEvent
import com.kshitijchauhan.haroldadmin.moviedb.utils.extensions.disposeWith
import com.kshitijchauhan.haroldadmin.moviedb.utils.extensions.log
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class MainViewModel(application: Application) : AndroidViewModel(application) {

    @Inject
    lateinit var bottomNavManager: BottomNavManager

    @Inject
    lateinit var progressBarManager: ProgressBarManager

    private val compositeDisposable = CompositeDisposable()

    init {
        getApplication<MovieDBApplication>()
            .appComponent
            .inject(this)

        progressBarManager.getProgressBarRelay()
            .subscribe { notification ->
                _progressBarNotification.value = notification
            }
            .disposeWith(compositeDisposable)
    }

    private val _state = SingleLiveEvent<UIState>()
    private val _snackbar = SingleLiveEvent<String>()
    private val _clearBackStack = SingleLiveEvent<Unit>()
    private val _toolbarTitle = SingleLiveEvent<String>()
    private val _progressBarNotification = MutableLiveData<ProgressBarNotification>()
    private var _isAuthenticated by SharedPreferencesDelegate(application, Constants.KEY_IS_AUTHENTICATED, false)
    private var _sessionId by SharedPreferencesDelegate(application, Constants.KEY_SESSION_ID, "")
    private var _accountId by SharedPreferencesDelegate(application, Constants.KEY_ACCOUNT_ID, -1)

    val state: LiveData<UIState>
        get() = _state

    val isAuthenticated: Boolean
        get() = _isAuthenticated

    val sessionId: String
        get() = _sessionId

    val accountId: Int
        get() = _accountId

    val snackbar: LiveData<String>
        get() = _snackbar

    val progressBarNotification: LiveData<ProgressBarNotification>
        get() = _progressBarNotification

    val clearBackStack: LiveData<Unit>
        get() = _clearBackStack

    val toolbarTitle: LiveData<String>
        get() = _toolbarTitle

    fun <T : UIState> updateStateTo(state: T) {
        log("Updating view to: ${state::class.java.simpleName}")
        _state.postValue(state)
    }

    fun showSnackbar(message: String) {
        _snackbar.postValue(message)
    }

    fun setAuthenticationStatus(status: Boolean) {
        _isAuthenticated = status
    }

    fun setSessionId(sessionId: String) {
        _sessionId = sessionId
    }

    fun addLoadingTask(task: LoadingTask) {
        progressBarManager.addTask(task)
    }

    fun completeLoadingTask(tag: String) {
        progressBarManager.completeTaskByTag(tag)
    }

    fun findLoadingTask(tag: String): LoadingTask? {
        return progressBarManager.findTaskByTag(tag)
    }

    fun peekState() = _state.value

    fun signalClearBackstack() = _clearBackStack.call()

    fun updateToolbarTitle(title: String) {
        _toolbarTitle.value = title
    }

    fun setAccountId(newId: Int) {
        _accountId = newId
    }

    fun updateBottomNavManagerState(state: UIState) {
        this.bottomNavManager.setBottomNavActiveState(state)
    }
}