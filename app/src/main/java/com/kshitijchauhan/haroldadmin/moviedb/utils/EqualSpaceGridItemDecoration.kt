package com.kshitijchauhan.haroldadmin.moviedb.utils

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class EqualSpaceGridItemDecoration(val spaceInPx: Int): RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)
        outRect.apply {
            left = spaceInPx
            top = spaceInPx
            right = spaceInPx
            bottom = spaceInPx
        }
    }
}