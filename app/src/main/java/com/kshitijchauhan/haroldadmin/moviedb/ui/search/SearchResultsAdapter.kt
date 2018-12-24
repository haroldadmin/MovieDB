package com.kshitijchauhan.haroldadmin.moviedb.ui.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.kshitijchauhan.haroldadmin.moviedb.model.Movie

class SearchResultsAdapter(private var searchResults: List<Movie>): RecyclerView.Adapter<SearchResultsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view = LayoutInflater.from(parent.context).inflate(android.R.layout.simple_list_item_1, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = searchResults.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(searchResults[position])
    }

    fun updateList(update: Pair<List<Movie>, DiffUtil.DiffResult>) {
        this.searchResults = update.first
        update.second.dispatchUpdatesTo(this)
    }


    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bind(movie: Movie) {
            (itemView as TextView).text = movie.title
        }
    }
}