package com.kshitijchauhan.haroldadmin.moviedb.ui.common

interface BackPressListener {
    /**
     * This method should be implemented by an object which wants to intercept back press events.
     *
     * It is useful for detecting backpresses in fragments. Implementations of this method should return true or false
     * to indicate whether the enclosing activity should call its super.onBackPressed method or not.
     */
    fun onBackPressed(): Boolean
}