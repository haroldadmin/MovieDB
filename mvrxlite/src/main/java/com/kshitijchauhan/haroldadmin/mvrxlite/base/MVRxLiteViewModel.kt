package com.kshitijchauhan.haroldadmin.mvrxlite.base

import androidx.annotation.CallSuper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

abstract class MVRxLiteViewModel<S : MVRxLiteState>(
    initialState: S,
    private val stateStore: MVRxLiteStateStore<S> = StateStore(
        initialState
    )
) : ViewModel() {

    private val TAG = this::class.java.simpleName

    private val compositeDisposable = CompositeDisposable()
    private val _state = MutableLiveData<S>()

    val state: LiveData<S>
        get() = _state

    init {
        stateStore.observable
            .subscribe { state: S -> _state.postValue(state) }
            .disposeBy(compositeDisposable)
    }

    protected fun setState(reducer: S.() -> S) {
        stateStore.set(reducer)
    }

    protected fun withState(block: (state: S) -> Unit) {
        stateStore.get(block)
    }

    @CallSuper
    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

    private fun Disposable.disposeBy(compositeDisposable: CompositeDisposable) {
        compositeDisposable.add(this)
    }
}