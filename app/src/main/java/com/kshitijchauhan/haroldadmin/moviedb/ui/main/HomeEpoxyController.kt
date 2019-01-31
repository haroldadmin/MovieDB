package com.kshitijchauhan.haroldadmin.moviedb.ui.main

import com.airbnb.epoxy.AutoModel
import com.airbnb.epoxy.Typed2EpoxyController
import com.kshitijchauhan.haroldadmin.moviedb.repository.movies.Movie
import com.kshitijchauhan.haroldadmin.moviedb.ui.common.*

class HomeEpoxyController(
    private val callbacks: EpoxyCallbacks
) : Typed2EpoxyController<List<Movie>, List<Movie>>() {

    @AutoModel
    lateinit var emptyPopularListModel: InfoTextModel_

    @AutoModel
    lateinit var emptyTopRatedListModel: InfoTextModel_

    override fun buildModels(popularMovies: List<Movie>?, topRatedMovies: List<Movie>?) {
        header {
            id("popular")
            title("Popular")
            spanSizeOverride { totalSpanCount, _, _ -> totalSpanCount }
        }

        if (popularMovies.isNullOrEmpty()) {
            emptyPopularListModel
                .text("No movies are popular right now. The world has changed, people don't like movies.")
                .spanSizeOverride { totalSpanCount, _, _ -> totalSpanCount }
                .also { add(it) }

        } else {
            popularMovies.forEach { popularMovie ->
                movie {
                    id(popularMovie.id)
                    movieId(popularMovie.id)
                    posterUrl(popularMovie.posterPath)
                    transitionName("poster-${popularMovie.id}")
                    clickListener { model, _, clickedView, _ ->
                        callbacks.onMovieItemClicked(model.movieId!!, model.transitionName(), clickedView)
                    }
                }
            }
        }

        header {
            id("top-rated")
            title("Top Rated")
            spanSizeOverride { totalSpanCount, _, _ -> totalSpanCount }
        }

        if (topRatedMovies.isNullOrEmpty()) {
            emptyTopRatedListModel
                .text("No movies have good ratings right now.")
                .spanSizeOverride { totalSpanCount, _, _ -> totalSpanCount }
                .also { add(it) }
        } else {
            topRatedMovies.forEach { topRatedMovie ->
                movie {
                    id(topRatedMovie.id)
                    movieId(topRatedMovie.id)
                    posterUrl(topRatedMovie.posterPath)
                    transitionName("poster-${topRatedMovie.id}")
                    clickListener { model, _, clickedView, _ ->
                        callbacks.onMovieItemClicked(model.movieId!!, model.transitionName(), clickedView)
                    }
                }
            }
        }
    }
}