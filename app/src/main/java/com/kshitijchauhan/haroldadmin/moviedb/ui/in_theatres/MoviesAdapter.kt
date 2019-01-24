package com.kshitijchauhan.haroldadmin.moviedb.ui.in_theatres

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.kshitijchauhan.haroldadmin.moviedb.R
import com.kshitijchauhan.haroldadmin.moviedb.repository.remote.service.common.GeneralMovieResponse
import com.kshitijchauhan.haroldadmin.moviedb.utils.Constants
import kotlinx.android.synthetic.main.item_movielist.view.*
import java.text.SimpleDateFormat

class MoviesAdapter(
    val glide: RequestManager,
    private var moviesList: MutableList<GeneralMovieResponse>,
    private val clickListener: (movieId: Int) -> Unit
): RecyclerView.Adapter<MoviesAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_movielist, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = moviesList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(moviesList[position])
    }

    fun updateList(pair: Pair<List<GeneralMovieResponse>, DiffUtil.DiffResult>) {
        this.moviesList.apply {

            // This is just a hack to prevent a recycler view bug
            notifyItemRangeRemoved(0, size)
            // End messy hack

            clear()
            addAll(pair.first)
        }.also {
            pair.second.dispatchUpdatesTo(this)
        }
    }


    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bind(movieSearchResult: GeneralMovieResponse) {
            println("Poster: ${movieSearchResult.posterPath}")
            println("Backdrop: ${movieSearchResult.backdropPath}")
            with(movieSearchResult) {
                itemView.apply {
                    tvTitle.text = title
                    chipReleaseYear.text = SimpleDateFormat("yyyy").format(releaseDate)
                    chipGenre.text = genreIds.takeIf { it.isNotEmpty() }?.let { Constants.Genres[genreIds[0]] } ?: "N/A"
                    glide
                        .load(movieSearchResult.posterPath)
                        .transition(DrawableTransitionOptions.withCrossFade(200))
                        .apply(RequestOptions()
                            .centerCrop()
                        )
                        .into(ivPoster)

                    setOnClickListener {
                        clickListener.invoke(movieSearchResult.id)
                    }
                }
            }
        }
    }

}