package com.kshitijchauhan.haroldadmin.moviedb.ui.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.kshitijchauhan.haroldadmin.moviedb.R
import com.kshitijchauhan.haroldadmin.moviedb.model.Movie
import com.kshitijchauhan.haroldadmin.moviedb.utils.log

class SearchResultsAdapter(private var searchResults: MutableList<Movie>) :
    RecyclerView.Adapter<SearchResultsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_search_suggestion, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = searchResults.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(searchResults[position])
    }

    fun updateList(update: Pair<List<Movie>, DiffUtil.DiffResult>) {

        // This is just a hack to avoid a recyclerview bug
        notifyItemRangeRemoved(0, searchResults.size)
        // End messy hack

        this.searchResults.clear()
        this.searchResults.addAll(update.first)
        update.second.dispatchUpdatesTo(this)
        log("Dispatching updates to search adapter")
        log("Updating adapter list")
    }


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(movie: Movie) {
            (itemView as TextView).text = movie.title
        }
    }
}