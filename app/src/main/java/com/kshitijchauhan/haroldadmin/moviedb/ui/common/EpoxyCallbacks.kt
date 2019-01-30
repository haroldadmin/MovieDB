package com.kshitijchauhan.haroldadmin.moviedb.ui.common

import android.view.View

interface EpoxyCallbacks {
    fun onMovieItemClicked(id: Int, transitionName: String, sharedView: View?)
}