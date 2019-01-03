package com.kshitijchauhan.haroldadmin.moviedb.ui.common

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.kshitijchauhan.haroldadmin.moviedb.R
import kotlinx.android.synthetic.main.item_moviegrid.view.*

class MoviesListAdapter(
    private val glide: RequestManager,
    private val clickListener: (movieId: Int, transitionName: String, sharedView: View) -> Unit
) : ListAdapter<MovieGridItem, MoviesListAdapter.ViewHolder>(MoviesDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_moviegrid, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position), clickListener)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var transitionName: String = ""

        fun bind(movie: MovieGridItem, onClick: (Int, String, View) -> Unit) {
            glide.load(movie.posterPath)
                .transition(DrawableTransitionOptions.withCrossFade())
                .apply(
                    RequestOptions()
                        .centerCrop()
                )
                .into(itemView.ivPoster)

            transitionName = "poster-${movie.id}"
                .also { name ->
                    ViewCompat.setTransitionName(itemView, name)
                }

            itemView.setOnClickListener {
                onClick.invoke(movie.id, transitionName, itemView)
            }
        }
    }

}