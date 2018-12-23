package com.kshitijchauhan.haroldadmin.moviedb.ui.discover

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.RequestOptions
import com.kshitijchauhan.haroldadmin.moviedb.R
import com.kshitijchauhan.haroldadmin.moviedb.model.Movie
import kotlinx.android.synthetic.main.item_moviegrid.view.*
import kotlin.math.roundToInt

class MoviesAdapter(
    val glide: RequestManager,
    private var moviesList: MutableList<Movie>
): RecyclerView.Adapter<MoviesAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_moviegrid, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = moviesList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(moviesList[position])
    }

    fun updateList(pair: Pair<List<Movie>, DiffUtil.DiffResult>) {
        this.moviesList.apply {
            clear()
            addAll(pair.first)
        }.also {
            pair.second.dispatchUpdatesTo(this)
        }
    }


    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bind(movie: Movie) {
            with(movie) {
                itemView.apply {
                    tvMovieName.text = title
                    rbMovieRating.rating = voteAverage.toFloat()
                    glide.load(movie.posterPath).apply(RequestOptions().centerCrop()).into(ivPoster)
                }
            }
        }
    }

}