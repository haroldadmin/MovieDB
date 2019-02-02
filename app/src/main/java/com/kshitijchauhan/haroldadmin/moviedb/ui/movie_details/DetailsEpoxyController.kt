package com.kshitijchauhan.haroldadmin.moviedb.ui.movie_details

import android.view.View
import androidx.lifecycle.Lifecycle
import com.airbnb.epoxy.Typed4EpoxyController
import com.bumptech.glide.RequestManager
import com.kshitijchauhan.haroldadmin.moviedb.repository.actors.Actor
import com.kshitijchauhan.haroldadmin.moviedb.repository.movies.AccountState
import com.kshitijchauhan.haroldadmin.moviedb.repository.movies.Movie
import com.kshitijchauhan.haroldadmin.moviedb.ui.common.actor
import com.kshitijchauhan.haroldadmin.moviedb.ui.common.header
import com.kshitijchauhan.haroldadmin.moviedb.ui.common.infoText

class DetailsEpoxyController(
    private val callbacks: MovieDetailsCallbacks,
    private val glide: RequestManager
) : Typed4EpoxyController<Movie?, AccountState?, String?, List<Actor>?>() {

    interface MovieDetailsCallbacks {
        fun toggleMovieFavouriteStatus()
        fun toggleMovieWatchlistStatus()
        fun onActorItemClicked(id: Int, transitionName: String, sharedView: View?)
    }

    override fun buildModels(movie: Movie?, accountState: AccountState?, trailerKey: String?, actors: List<Actor>?) {

        accountState?.let {
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

        header {
            id("description")
            title("Description")
            spanSizeOverride { totalSpanCount, _, _ -> totalSpanCount }
        }

        mainText {
            id("overview")
            text(movie?.overview ?: "")
            spanSizeOverride { totalSpanCount, _, _ -> totalSpanCount }
        }

        header {
            id("trailer")
            title("Trailer")
            spanSizeOverride { totalSpanCount, _, _ -> totalSpanCount }
        }

        trailerKey.takeIf { !it.isNullOrBlank() }?.let {
            trailer {
                id(it)
                trailerKey(it)
                spanSizeOverride { totalSpanCount, _, _ -> totalSpanCount }
            }
        } ?: infoText {
            id("trailer-empty")
            text("We can't find a trailer for this movie")
            spanSizeOverride { totalSpanCount, _, _ -> totalSpanCount }
        }

        actors?.let {
            header {
                id("cast")
                title("Cast")
                spanSizeOverride { totalSpanCount, _, _ -> totalSpanCount }
            }

            it.takeIf { list -> list.isNotEmpty() }?.forEach {
                actor {
                    id(it.id)
                    actorId(it.id)
                    name(it.name)
                    glide(glide)
                    pictureUrl(it.profilePictureUrl)
                    transitionName("actor-$it.id")
                    clickListener { model, _, clickedView, _ ->
                        callbacks.onActorItemClicked(model.actorId!!, model.transitionName(), clickedView)
                    }
                }
            } ?: infoText {
                id("cast-empty")
                text("This list seems to be empty")
                spanSizeOverride { totalSpanCount, _, _ -> totalSpanCount }
            }
        }
    }
}