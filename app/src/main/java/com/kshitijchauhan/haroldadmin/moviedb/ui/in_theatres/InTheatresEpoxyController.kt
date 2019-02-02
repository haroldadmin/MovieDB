package com.kshitijchauhan.haroldadmin.moviedb.ui.in_theatres

import com.airbnb.epoxy.TypedEpoxyController
import com.kshitijchauhan.haroldadmin.moviedb.repository.movies.Movie
import com.kshitijchauhan.haroldadmin.moviedb.ui.common.EpoxyCallbacks
import com.kshitijchauhan.haroldadmin.moviedb.ui.common.header
import com.kshitijchauhan.haroldadmin.moviedb.ui.common.infoText
import com.kshitijchauhan.haroldadmin.moviedb.ui.common.movie

class InTheatresEpoxyController(
    private val callbacks: EpoxyCallbacks
): TypedEpoxyController<List<Movie>>() {

    override fun buildModels(movies: List<Movie>?) {

        header {
            id("in-theatres-header")
            title("Movies in theatres")
            spanSizeOverride { totalSpanCount, _, _ -> totalSpanCount }
        }

        if(movies.isNullOrEmpty()) {
            infoText {
                id("in-theatres-info")
                text("We can't find any movies in theatres near you")
                spanSizeOverride { totalSpanCount, _, _ -> totalSpanCount }
            }
        } else {
            movies.forEach { inTheatreMovie ->
                movie {
                    id(inTheatreMovie.id)
                    movieId(inTheatreMovie.id)
                    posterUrl(inTheatreMovie.posterPath)
                    transitionName("poster-${inTheatreMovie.id}")
                    clickListener { model, _, clickedView, _ ->
                        callbacks.onMovieItemClicked(model.movieId!!, model.transitionName, clickedView)
                    }
                }
            }
        }

    }

}