package com.kshitijchauhan.haroldadmin.mvrxlite.base

import io.reactivex.Observable
import io.reactivex.disposables.Disposable

interface MVRxLiteStateStore<S : Any> : Disposable {
    val state: S
    fun get(block: (S) -> Unit)
    fun set(stateReducer: S.() -> S)
    val observable: Observable<S>
}
