package com.kshitijchauhan.haroldadmin.moviedb.ui.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kshitijchauhan.haroldadmin.moviedb.R
import com.kshitijchauhan.haroldadmin.moviedb.repository.movies.Movie
import com.kshitijchauhan.haroldadmin.moviedb.ui.common.MoviesDiffUtil

class SearchResultsAdapter(val onItemClick: (movieId: Int) -> Unit) :
    ListAdapter<Movie, SearchResultsAdapter.ViewHolder>(MoviesDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_search_suggestion, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position).also {
            holder.bind(it)
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(movieSearchResult: Movie) {
            (itemView as TextView).text = movieSearchResult.title
            itemView.setOnClickListener { onItemClick.invoke(movieSearchResult.id) }
        }
    }
}