package com.kshitijchauhan.haroldadmin.moviedb.ui.search

import androidx.recyclerview.widget.DiffUtil
import com.kshitijchauhan.haroldadmin.moviedb.remote.service.search.MovieSearchResult

class SearchDiffUtil(val oldList: List<MovieSearchResult>,
                     val newList: List<MovieSearchResult>): DiffUtil.Callback() {

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id == newList[newItemPosition].id
    }

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}