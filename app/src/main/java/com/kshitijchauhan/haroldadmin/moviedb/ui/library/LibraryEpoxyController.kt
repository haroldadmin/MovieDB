package com.kshitijchauhan.haroldadmin.moviedb.ui.library

import com.airbnb.epoxy.AutoModel
import com.airbnb.epoxy.Typed3EpoxyController
import com.bumptech.glide.RequestManager
import com.kshitijchauhan.haroldadmin.moviedb.repository.data.Resource
import com.kshitijchauhan.haroldadmin.moviedb.repository.movies.Movie
import com.kshitijchauhan.haroldadmin.moviedb.ui.common.*
import com.kshitijchauhan.haroldadmin.moviedb.utils.extensions.safe

class LibraryEpoxyController(
    private val callbacks: EpoxyCallbacks,
    private val glide: RequestManager
) : Typed3EpoxyController<Resource<List<Movie>>, Resource<List<Movie>>, Boolean>() {

    @AutoModel
    lateinit var emptyListModelFavourited: InfoTextModel_
    @AutoModel
    lateinit var emptyListModelWatchlist: InfoTextModel_
    @AutoModel
    lateinit var needToLoginModel: NeedToLoginModel_

    override fun buildModels(
        favouriteMovies: Resource<List<Movie>>?,
        watchlistedMovies: Resource<List<Movie>>?,
        isAuthenticated: Boolean?
    ) {
        if (isAuthenticated == false) {
            needToLoginModel
                .spanSizeOverride { totalSpanCount, _, _ -> totalSpanCount }
                .also { add(it) }
        } else {
            header {
                id("favourites")
                title("Favourites")
                spanSizeOverride { totalSpanCount, _, _ -> totalSpanCount }
            }

            when (favouriteMovies) {
                is Resource.Success -> {
                    when {
                        favouriteMovies.data.isEmpty() -> {
                            emptyListModelFavourited
                                .text("You have not favourited any movies yet.")
                                .spanSizeOverride { totalSpanCount, _, _ -> totalSpanCount }
                                .also { this.add(it) }
                            Unit
                        }

                        else -> favouriteMovies.data.forEach { favouriteMovie ->
                            movie {
                                id(favouriteMovie.id)
                                movieId(favouriteMovie.id)
                                glide(glide)
                                posterUrl(favouriteMovie.posterPath)
                                transitionName("poster-${favouriteMovie.id}")
                                clickListener { model, _, clickedView, _ ->
                                    callbacks.onMovieItemClicked(model.movieId!!, model.transitionName(), clickedView)
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
                    infoText {
                        id("loading-favourite-movies")
                        text("Loading Favourite movies")
                        spanSizeOverride { totalSpanCount, _, _ -> totalSpanCount }
                    }
                }
                null -> Unit
            }.safe

            header {
                id("watchlist")
                title("Watchlist")
                spanSizeOverride { totalSpanCount, _, _ -> totalSpanCount }
            }

            when (watchlistedMovies) {
                is Resource.Success -> {
                    when {
                        watchlistedMovies.data.isEmpty() -> {
                            emptyListModelWatchlist
                                .text("You have not watchlisted any movies yet")
                                .spanSizeOverride { totalSpanCount, _, _ -> totalSpanCount }
                                .also { this.add(it) }
                            Unit
                        }
                        else -> watchlistedMovies.data.forEach { watchlistedMovie ->
                            movie {
                                id(watchlistedMovie.id)
                                movieId(watchlistedMovie.id)
                                glide(glide)
                                posterUrl(watchlistedMovie.posterPath)
                                transitionName("poster-${watchlistedMovie.id}")
                                clickListener { model, _, clickedView, _ ->
                                    callbacks.onMovieItemClicked(model.movieId!!, model.transitionName(), clickedView)
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
                    infoText {
                        id("loading-watchlist-movies")
                        text("Loading Watchlisted movies")
                        spanSizeOverride { totalSpanCount, _, _ -> totalSpanCount }
                    }
                }
                null -> Unit
            }.safe
        }
    }

}