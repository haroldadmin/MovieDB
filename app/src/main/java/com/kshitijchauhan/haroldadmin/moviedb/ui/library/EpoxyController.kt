package com.kshitijchauhan.haroldadmin.moviedb.ui.library

import android.view.View
import com.airbnb.epoxy.AutoModel
import com.airbnb.epoxy.Typed3EpoxyController
import com.kshitijchauhan.haroldadmin.moviedb.repository.movies.Movie

class AllMoviesController(
    private val callbacks: Callbacks
) : Typed3EpoxyController<List<Movie>, List<Movie>, Boolean>() {

    @AutoModel
    lateinit var emptyListModelFavourited: EmptyListModel_
    @AutoModel
    lateinit var emptyListModelWatchlist: EmptyListModel_
    @AutoModel
    lateinit var needToLoginModel: NeedToLoginModel_

    interface Callbacks {
        fun onMovieItemClicked(id: Int, transitionName: String, sharedView: View?)
    }

    override fun buildModels(popularMovies: List<Movie>?, topRatedMovies: List<Movie>?, isAuthenticated: Boolean?) {
        if (isAuthenticated == false) {
            needToLoginModel
                .spanSizeOverride { totalSpanCount, _, _ ->  totalSpanCount}
                .also { add(it) }
        } else {
            header {
                id("popular")
                title("Favourites")
                spanSizeOverride { totalSpanCount, _, _ -> totalSpanCount }
            }

            if (popularMovies.isNullOrEmpty()) {
                emptyListModelFavourited
                    .text("You have not favourited any movies yet.")
                    .spanSizeOverride { totalSpanCount, _, _ -> totalSpanCount }
                    .also { this.add(it) }
            } else {
                popularMovies.forEach { popularMovie: Movie ->
                    movie {
                        id(popularMovie.id)
                        movieId(popularMovie.id)
                        posterUrl(popularMovie.posterPath)
                        transitionName("poster-${popularMovie.id}")
                        clickListener { model, _, clickedView, position ->
                            callbacks.onMovieItemClicked(model.movieId!!, model.transitionName, clickedView)
                        }
                    }
                }
            }

            header {
                id("top-rated")
                title("Watchlist")
                spanSizeOverride { totalSpanCount, _, _ -> totalSpanCount }
            }

            if (topRatedMovies.isNullOrEmpty()) {
                emptyListModelWatchlist
                    .text("You have not watchlisted any movies yet")
                    .spanSizeOverride { totalSpanCount, _, _ -> totalSpanCount }
                    .also { this.add(it) }
            } else {
                topRatedMovies.forEach { topRatedMovie ->
                    movie {
                        id(topRatedMovie.id)
                        movieId(topRatedMovie.id)
                        posterUrl(topRatedMovie.posterPath)
                        transitionName("poster-${topRatedMovie.id}")
                        clickListener { model, _, clickedView, position ->
                            callbacks.onMovieItemClicked(model.movieId!!, "poster-${model.id()}", clickedView)
                        }
                    }
                }
            }
        }
    }

}