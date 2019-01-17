package com.kshitijchauhan.haroldadmin.moviedb.ui.common

import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.google.android.material.button.MaterialButton
import com.kshitijchauhan.haroldadmin.moviedb.R

/**
 * This class is used for buttons in the Movie Details Fragment to enable its buttons to change their colour when
 * the current movie is already added to Watchlist or Favourites list.
 * Two extra files [res/color/color_states_movie_details_buttons] and [res/values/attrs/RemoveFromListState]
 * have also been created to allow these changes.
 */
class CustomMaterialButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : MaterialButton(context, attrs, defStyleAttr) {

    private var stateRemoveFromList = false
    private val REMOVE_FROM_LIST_STATE: IntArray = intArrayOf(R.attr.state_remove_from_list)

    override fun onCreateDrawableState(extraSpace: Int): IntArray {
        var state = super.onCreateDrawableState(extraSpace + 1)
        if (stateRemoveFromList) {
            state = View.mergeDrawableStates(state, REMOVE_FROM_LIST_STATE)
        }
        return state
    }

    fun setRemoveFromListState(value: Boolean) {
        this.stateRemoveFromList = value
        refreshDrawableState()
    }
}