package com.kshitijchauhan.haroldadmin.moviedb.ui.common

import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.google.android.material.button.MaterialButton
import com.kshitijchauhan.haroldadmin.moviedb.R

/**
 * This class is used for buttons in the MovieResponse Details Fragment to enable its buttons to change their colour when
 * the current movie is already added to Watchlist or Favourites list, or when the user is currently unauthenticated.
 */
class CustomMaterialButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : MaterialButton(context, attrs, defStyleAttr) {

    private var stateRemoveFromList = false
    private var stateUnauthenticated = false
    private val REMOVE_FROM_LIST_STATE: IntArray = intArrayOf(R.attr.state_remove_from_list)
    private val UNAUTHENTICATED_STATE: IntArray = intArrayOf(R.attr.state_unauthenticated)

    override fun onCreateDrawableState(extraSpace: Int): IntArray {
        var state = super.onCreateDrawableState(2)
        if (stateRemoveFromList) {
            state = View.mergeDrawableStates(state, REMOVE_FROM_LIST_STATE)
        } else if (stateUnauthenticated) {
            state = View.mergeDrawableStates(state, UNAUTHENTICATED_STATE)
        }
        return state
    }

    fun setRemoveFromListState(value: Boolean) {
        this.stateRemoveFromList = value
        refreshDrawableState()
    }

    fun setUnauthenticatedState(value: Boolean) {
        this.stateUnauthenticated = value
        refreshDrawableState()
    }
}