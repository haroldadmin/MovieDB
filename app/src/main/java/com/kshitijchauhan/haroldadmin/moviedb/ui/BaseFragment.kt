package com.kshitijchauhan.haroldadmin.moviedb.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.postDelayed
import androidx.fragment.app.Fragment
import com.kshitijchauhan.haroldadmin.moviedb.utils.extensions.log

abstract class BaseFragment: Fragment() {

    protected abstract val associatedUIState: UIState

    protected abstract fun notifyBottomNavManager()

    protected abstract fun updateToolbarTitle()

    private var isTransitionPostponed = false

    override fun onStart() {
        super.onStart()
        // If the transition remains postponed even after 1 second, we should resume it
        if (isTransitionPostponed) {
            view?.postDelayed(1000) {
                log("Force resuming enter transition for ${this::class.java.simpleName}")
                startPostponedEnterTransition()
            }
        }
    }

    override fun onResume() {
        notifyBottomNavManager()
        super.onResume()
    }

    override fun postponeEnterTransition() {
        super.postponeEnterTransition()
        log("Postponing enter transition for ${this::class.java.simpleName}")
        isTransitionPostponed = true
    }

    override fun startPostponedEnterTransition() {
        super.startPostponedEnterTransition()
        log("Resuming enter transition for ${this::class.java.simpleName}")
        isTransitionPostponed = false
    }
}