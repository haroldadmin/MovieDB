package com.kshitijchauhan.haroldadmin.moviedb.ui.in_theatres

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.kshitijchauhan.haroldadmin.moviedb.R
import com.kshitijchauhan.haroldadmin.moviedb.repository.data.remote.service.common.GeneralMovieResponse
import com.kshitijchauhan.haroldadmin.moviedb.repository.movies.Movie
import com.kshitijchauhan.haroldadmin.moviedb.ui.common.MoviesDiffUtil
import com.kshitijchauhan.haroldadmin.moviedb.utils.Constants
import kotlinx.android.synthetic.main.item_movielist.view.*
import java.text.SimpleDateFormat

class MoviesAdapter(
    val glide: RequestManager,
    private val clickListener: (movieId: Int) -> Unit
): ListAdapter<Movie, MoviesAdapter.ViewHolder>(MoviesDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_movielist, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bind(movie: Movie) {
            println("Poster: ${movie.posterPath}")
            println("Backdrop: ${movie.backdropPath}")
            with(movie) {
                itemView.apply {
                    tvTitle.text = title
                    chipReleaseYear.text = SimpleDateFormat("yyyy").format(releaseDate)
                    chipGenre.text = genreIds.takeIf { it.isNotEmpty() }?.let { Constants.Genres[genreIds[0]] } ?: "N/A"
                    glide
                        .load(movie.posterPath)
                        .transition(DrawableTransitionOptions.withCrossFade(200))
                        .apply(RequestOptions()
                            .centerCrop()
                        )
                        .into(ivPoster)

                    setOnClickListener {
                        clickListener.invoke(movie.id)
                    }
                }
            }
        }
    }

}