package com.kshitijchauhan.haroldadmin.moviedb.ui

import androidx.core.view.postDelayed
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import com.kshitijchauhan.haroldadmin.moviedb.core.extensions.log

abstract class BaseFragment : Fragment() {

    protected abstract val initialState: UIState

    protected abstract fun updateToolbarTitle()

    private var isTransitionPostponed = false

    override fun onStart() {
        super.onStart()
        // If the transition remains postponed even after 1 second, we should resume it
        if (isTransitionPostponed) {
            view?.postDelayed(1000) {
                if (lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)) {
                    log("Force resuming enter transition for ${this::class.java.simpleName}")
                    startPostponedEnterTransition()
                }
            }
        }
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