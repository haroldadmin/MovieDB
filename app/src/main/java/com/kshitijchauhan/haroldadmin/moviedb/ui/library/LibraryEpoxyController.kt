package com.kshitijchauhan.haroldadmin.moviedb.ui.library

import com.airbnb.epoxy.AutoModel
import com.airbnb.epoxy.Typed3EpoxyController
import com.kshitijchauhan.haroldadmin.moviedb.repository.movies.Movie
import com.kshitijchauhan.haroldadmin.moviedb.ui.common.*

class LibraryEpoxyController(
    private val callbacks: EpoxyCallbacks
) : Typed3EpoxyController<List<Movie>, List<Movie>, Boolean>() {

    @AutoModel
    lateinit var emptyListModelFavourited: InfoTextModel_
    @AutoModel
    lateinit var emptyListModelWatchlist: InfoTextModel_
    @AutoModel
    lateinit var needToLoginModel: NeedToLoginModel_

    override fun buildModels(favouriteMovies: List<Movie>?, watchlistedMovies: List<Movie>?, isAuthenticated: Boolean?) {
        if (isAuthenticated == false) {
            needToLoginModel
                .spanSizeOverride { totalSpanCount, _, _ ->  totalSpanCount}
                .also { add(it) }
        } else {
            header {
                id("favourites")
                title("Favourites")
                spanSizeOverride { totalSpanCount, _, _ -> totalSpanCount }
            }

            if (favouriteMovies.isNullOrEmpty()) {
                emptyListModelFavourited
                    .text("You have not favourited any movies yet.")
                    .spanSizeOverride { totalSpanCount, _, _ -> totalSpanCount }
                    .also { this.add(it) }
            } else {
                favouriteMovies.forEach { favouriteMovie ->
                    movie {
                        id(favouriteMovie.id)
                        movieId(favouriteMovie.id)
                        posterUrl(favouriteMovie.posterPath)
                        transitionName("poster-${favouriteMovie.id}")
                        clickListener { model, _, clickedView, position ->
                            callbacks.onMovieItemClicked(model.movieId!!, model.transitionName(), clickedView)
                        }
                    }
                }
            }

            header {
                id("watchlist")
                title("Watchlist")
                spanSizeOverride { totalSpanCount, _, _ -> totalSpanCount }
            }

            if (watchlistedMovies.isNullOrEmpty()) {
                emptyListModelWatchlist
                    .text("You have not watchlisted any movies yet")
                    .spanSizeOverride { totalSpanCount, _, _ -> totalSpanCount }
                    .also { this.add(it) }
            } else {
                watchlistedMovies.forEach { watchlistedMovie ->
                    movie {
                        id(watchlistedMovie.id)
                        movieId(watchlistedMovie.id)
                        posterUrl(watchlistedMovie.posterPath)
                        transitionName("poster-${watchlistedMovie.id}")
                        clickListener { model, _, clickedView, _ ->
                            callbacks.onMovieItemClicked(model.movieId!!, model.transitionName(), clickedView)
                        }
                    }
                }
            }
        }
    }

}