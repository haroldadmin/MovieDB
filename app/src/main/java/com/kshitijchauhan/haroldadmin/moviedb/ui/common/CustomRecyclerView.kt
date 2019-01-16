package com.kshitijchauhan.haroldadmin.moviedb.ui.common

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.kshitijchauhan.haroldadmin.moviedb.utils.extensions.gone
import com.kshitijchauhan.haroldadmin.moviedb.utils.extensions.log
import com.kshitijchauhan.haroldadmin.moviedb.utils.extensions.visible


/**
 * This RecyclerView has the ability to automatically show/hide a View when the dataset of the underlying adapter is empty.
 */
class CustomRecyclerView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : RecyclerView(context, attrs, defStyleAttr) {

    private var emptyView: View? = null

    private val adapterDataObserver = object : AdapterDataObserver() {
        /**
         * We have to override every method in this observer because DiffUtil does not call just onChanged,
         * but it calls the other methods too depending on the changes. If we just override onChanged, then our empty
         * view will not be update when, for example, new items are added to a previously empty list
         */
        override fun onChanged() {
            super.onChanged()
            updateEmptyViewState()
        }

        override fun onItemRangeChanged(positionStart: Int, itemCount: Int) {
            super.onItemRangeChanged(positionStart, itemCount)
            updateEmptyViewState()
        }

        override fun onItemRangeChanged(positionStart: Int, itemCount: Int, payload: Any?) {
            super.onItemRangeChanged(positionStart, itemCount, payload)
            updateEmptyViewState()
        }

        override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
            super.onItemRangeInserted(positionStart, itemCount)
            updateEmptyViewState()
        }

        override fun onItemRangeMoved(fromPosition: Int, toPosition: Int, itemCount: Int) {
            super.onItemRangeMoved(fromPosition, toPosition, itemCount)
            updateEmptyViewState()
        }

        override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
            super.onItemRangeRemoved(positionStart, itemCount)
            updateEmptyViewState()
        }
    }


    override fun setAdapter(newAdapter: Adapter<*>?) {
        adapter?.unregisterAdapterDataObserver(adapterDataObserver).also {
            newAdapter?.registerAdapterDataObserver(adapterDataObserver)
        }
        super.setAdapter(newAdapter)
        updateEmptyViewState()
    }

    private fun updateEmptyViewState() {
        emptyView?.let { eV ->
            adapter?.let {
                if (it.itemCount == 0) {
                    this.gone()
                    eV.visible()
                    return
                }
                /*
                It is important to hide the empty view after making the recyclew view visible, because otherwise
                the empty view does not disappear even after the list is populated
                 */
                this.visible()
                eV.gone()
            }
        }
    }

    fun setEmptyView(emptyView: View) {
        this.emptyView = emptyView
    }
}