package com.kshitijchauhan.haroldadmin.mvrxlite.base

interface MVRxLiteView <T: MVRxLiteState> {
    fun renderState(state: T)
}