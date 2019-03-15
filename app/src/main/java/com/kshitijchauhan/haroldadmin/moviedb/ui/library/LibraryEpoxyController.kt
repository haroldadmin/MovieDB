package com.kshitijchauhan.haroldadmin.moviedb.ui.library

import android.os.Handler
import com.airbnb.epoxy.AutoModel
import com.airbnb.epoxy.TypedEpoxyController
import com.bumptech.glide.RequestManager
import com.kshitijchauhan.haroldadmin.moviedb.core.Resource
import com.kshitijchauhan.haroldadmin.moviedb.core.extensions.safe
import com.kshitijchauhan.haroldadmin.moviedb.repository.movies.Movie
import com.kshitijchauhan.haroldadmin.moviedb.ui.UIState
import com.kshitijchauhan.haroldadmin.moviedb.ui.common.*

class LibraryEpoxyController(
    private val callbacks: EpoxyCallbacks,
    private val glide: RequestManager,
    epoxyHandler: Handler
) : TypedEpoxyController<UIState.LibraryScreenState>(epoxyHandler, epoxyHandler) {

    @AutoModel
    lateinit var emptyListModelFavourited: InfoTextModel_
    @AutoModel
    lateinit var emptyListModelWatchlist: InfoTextModel_
    @AutoModel
    lateinit var needToLoginModel: NeedToLoginModel_

    override fun buildModels(state: UIState.LibraryScreenState) {
        with(state) {
            if (!isAuthenticated) {
                buildNeedToLoginModel()
            } else {
                buildFavouritesModel(favouriteMoviesResource)
                buildWatchlistModel(watchlistedMoviesResource)
            }
        }
    }

    private fun buildNeedToLoginModel() {
        needToLoginModel
            .spanSizeOverride { totalSpanCount, _, _ -> totalSpanCount }
            .also { add(it) }
    }

    private fun buildFavouritesModel(favouriteMoviesResource: Resource<List<Movie>>) {
        header {
            id("favourites")
            title("Favourites")
            spanSizeOverride { totalSpanCount, _, _ -> totalSpanCount }
        }

        when (favouriteMoviesResource) {
            is Resource.Success -> {
                when {
                    favouriteMoviesResource.data.isEmpty() -> {
                        emptyListModelFavourited
                            .text("You have not favourited any movies yet.")
                            .spanSizeOverride { totalSpanCount, _, _ -> totalSpanCount }
                            .also { this.add(it) }
                        Unit
                    }

                    else -> favouriteMoviesResource.data.forEach { favouriteMovie ->
                        movie {
                            id(favouriteMovie.id)
                            movieId(favouriteMovie.id)
                            glide(glide)
                            posterUrl(favouriteMovie.posterPath)
                            transitionName("poster-${favouriteMovie.id}")
                            clickListener { model, _, clickedView, _ ->
                                callbacks.onMovieItemClicked(
                                    model.movieId!!,
                                    model.transitionName(),
                                    clickedView
                                )
                            }
                        }
                    }
                }
            }
            is Resource.Error -> {
                infoText {
                    id("error-favourite-movies")
                    text("Error getting Favourite movies")
                    spanSizeOverride { totalSpanCount, _, _ -> totalSpanCount }
                }
            }
            is Resource.Loading -> {
                loading {
                    id("load-favourite-movies")
                    description("Loading Favourite movies")
                    spanSizeOverride { totalSpanCount, _, _ -> totalSpanCount }
                }
            }
        }.safe
    }

    private fun buildWatchlistModel(watchlistedMoviesResource: Resource<List<Movie>>) {
        header {
            id("watchlist")
            title("Watchlist")
            spanSizeOverride { totalSpanCount, _, _ -> totalSpanCount }
        }

        when (watchlistedMoviesResource) {
            is Resource.Success -> {
                when {
                    watchlistedMoviesResource.data.isEmpty() -> {
                        emptyListModelWatchlist
                            .text("You have not watchlisted any movies yet")
                            .spanSizeOverride { totalSpanCount, _, _ -> totalSpanCount }
                            .also { this@LibraryEpoxyController.add(it) }
                        Unit
                    }
                    else -> watchlistedMoviesResource.data.forEach { watchlistedMovie ->
                        movie {
                            id(watchlistedMovie.id)
                            movieId(watchlistedMovie.id)
                            glide(glide)
                            posterUrl(watchlistedMovie.posterPath)
                            transitionName("poster-${watchlistedMovie.id}")
                            clickListener { model, _, clickedView, _ ->
                                callbacks.onMovieItemClicked(
                                    model.movieId!!,
                                    model.transitionName(),
                                    clickedView
                                )
                            }
                        }
                    }

                }
            }
            is Resource.Error -> {
                infoText {
                    id("error-watchlist-movies")
                    text("Error getting Watchlisted movies")
                    spanSizeOverride { totalSpanCount, _, _ -> totalSpanCount }
                }
            }
            is Resource.Loading -> {
                loading {
                    id("load-favourite-movies")
                    description("Loading Watchlisted movies")
                    spanSizeOverride { totalSpanCount, _, _ -> totalSpanCount }
                }
            }
        }.safe
    }
}