package com.kshitijchauhan.haroldadmin.moviedb.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.kshitijchauhan.haroldadmin.moviedb.R
import com.kshitijchauhan.haroldadmin.moviedb.remote.service.common.GeneralMovieResponse
import kotlinx.android.synthetic.main.item_moviegrid.view.*

class MoviesAdapter(
    private var moviesList: List<GeneralMovieResponse>,
    private val glide: RequestManager,
    private val onClick: (movieId: Int) -> Unit
) : RecyclerView.Adapter<MoviesAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_moviegrid, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = moviesList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(moviesList[position])
    }

    fun updateList(newList: List<GeneralMovieResponse>) {
        this.moviesList = newList
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(movie: GeneralMovieResponse) {
            glide.load(movie.posterPath)
                .transition(DrawableTransitionOptions.withCrossFade())
                .apply(RequestOptions()
                    .centerCrop()
                )
                .into(itemView.ivPoster)

            itemView.setOnClickListener { onClick.invoke(movie.id) }
        }
    }

}