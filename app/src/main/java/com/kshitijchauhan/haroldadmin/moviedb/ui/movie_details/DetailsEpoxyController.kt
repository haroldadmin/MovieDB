package com.kshitijchauhan.haroldadmin.moviedb.ui.movie_details

import android.view.View
import com.airbnb.epoxy.Typed4EpoxyController
import com.bumptech.glide.RequestManager
import com.kshitijchauhan.haroldadmin.moviedb.repository.actors.Actor
import com.kshitijchauhan.haroldadmin.moviedb.repository.data.Resource
import com.kshitijchauhan.haroldadmin.moviedb.repository.movies.AccountState
import com.kshitijchauhan.haroldadmin.moviedb.repository.movies.Movie
import com.kshitijchauhan.haroldadmin.moviedb.repository.movies.MovieTrailer
import com.kshitijchauhan.haroldadmin.moviedb.ui.common.actor
import com.kshitijchauhan.haroldadmin.moviedb.ui.common.header
import com.kshitijchauhan.haroldadmin.moviedb.ui.common.infoText
import com.kshitijchauhan.haroldadmin.moviedb.utils.extensions.safe

class DetailsEpoxyController(
    private val callbacks: MovieDetailsCallbacks,
    private val glide: RequestManager
) : Typed4EpoxyController<Resource<Movie>?, Resource<AccountState>?, Resource<MovieTrailer>?, List<Resource<Actor>>>() {

    interface MovieDetailsCallbacks {
        fun toggleMovieFavouriteStatus()
        fun toggleMovieWatchlistStatus()
        fun onActorItemClicked(id: Int, transitionName: String, sharedView: View?)
    }

    override fun buildModels(
        movieResource: Resource<Movie>?,
        accountStateResource: Resource<AccountState>?,
        trailerResource: Resource<MovieTrailer>?,
        actors: List<Resource<Actor>>?
    ) {

        when (accountStateResource) {
            is Resource.Success -> {
                val accountState = accountStateResource.data
                infoBar {
                    id("info")
                    accountStates(accountState)
                    watchListClickListener { _, _, _, _ ->
                        callbacks.toggleMovieWatchlistStatus()
                    }
                    favouritesClickListener { _, _, _, _ ->
                        callbacks.toggleMovieFavouriteStatus()
                    }
                    spanSizeOverride { totalSpanCount, _, _ -> totalSpanCount }
                }
            }
            is Resource.Error -> Unit
            is Resource.Loading -> Unit
            else -> Unit
        }.safe

        when (movieResource) {
            is Resource.Success -> {
                val movie = movieResource.data
                header {
                    id("description")
                    title("Description")
                    spanSizeOverride { totalSpanCount, _, _ -> totalSpanCount }
                }

                mainText {
                    id("overview")
                    text(movie.overview)
                    spanSizeOverride { totalSpanCount, _, _ -> totalSpanCount }
                }
            }
            is Resource.Error -> {
                header {
                    id("description")
                    title("Description")
                    spanSizeOverride { totalSpanCount, _, _ -> totalSpanCount }
                }

                mainText {
                    id("overview")
                    text("We can't find a description for this movie")
                    spanSizeOverride { totalSpanCount, _, _ -> totalSpanCount }
                }
            }
            is Resource.Loading -> {
                header {
                    id("description")
                    title("Description")
                    spanSizeOverride { totalSpanCount, _, _ -> totalSpanCount }
                }

                infoText {
                    id("overview")
                    text("Loading description...")
                    spanSizeOverride { totalSpanCount, _, _ -> totalSpanCount }
                }
            }
            else -> Unit
        }.safe

        when (trailerResource) {
            is Resource.Success -> {
                header {
                    id("trailer")
                    title("Trailer")
                    spanSizeOverride { totalSpanCount, _, _ -> totalSpanCount }
                }

                trailer {
                    id(trailerResource.data.movieId)
                    trailerKey(trailerResource.data.youtubeVideoKey)
                    spanSizeOverride { totalSpanCount, _, _ -> totalSpanCount }
                }
            }
            is Resource.Error -> {
                header {
                    id("trailer")
                    title("Trailer")
                    spanSizeOverride { totalSpanCount, _, _ -> totalSpanCount }
                }

                infoText {
                    id("trailer-empty")
                    text("We can't find a trailer for this movie")
                    spanSizeOverride { totalSpanCount, _, _ -> totalSpanCount }
                }
            }
            is Resource.Loading -> {
                header {
                    id("trailer")
                    title("Trailer")
                    spanSizeOverride { totalSpanCount, _, _ -> totalSpanCount }
                }

                infoText {
                    id("trailer-loading")
                    text("Loading trailer...")
                    spanSizeOverride { totalSpanCount, _, _ -> totalSpanCount }
                }
            }
            else -> Unit
        }.safe


        actors.let {
            header {
                id("cast")
                title("Cast")
                spanSizeOverride { totalSpanCount, _, _ -> totalSpanCount }
            }

            it.takeIf { list -> !list.isNullOrEmpty() }?.forEach { actorResource ->
                when (actorResource) {
                    is Resource.Success -> {
                        actor {
                            id(actorResource.data.id)
                            actorId(actorResource.data.id)
                            name(actorResource.data.name)
                            glide(glide)
                            pictureUrl(actorResource.data.profilePictureUrl)
                            transitionName("actor-${actorResource.data.id}")
                            clickListener { model, _, clickedView, _ ->
                                callbacks.onActorItemClicked(model.actorId!!, model.transitionName(), clickedView)
                            }
                        }
                    }
                    is Resource.Error -> Unit
                    is Resource.Loading -> Unit
                }.safe

            } ?: infoText {
                id("cast-empty")
                text("This list seems to be empty")
                spanSizeOverride { totalSpanCount, _, _ -> totalSpanCount }
            }
        }
    }
}