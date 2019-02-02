package com.kshitijchauhan.haroldadmin.moviedb.ui.movie_details

import android.view.View
import android.widget.TextView
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.kshitijchauhan.haroldadmin.moviedb.R
import com.kshitijchauhan.haroldadmin.moviedb.repository.movies.AccountState
import com.kshitijchauhan.haroldadmin.moviedb.ui.common.CustomMaterialButton
import com.kshitijchauhan.haroldadmin.moviedb.ui.common.KotlinEpoxyHolder
import com.pierfrancescosoffritti.androidyoutubeplayer.player.YouTubePlayerView
import com.pierfrancescosoffritti.androidyoutubeplayer.player.listeners.AbstractYouTubePlayerListener

@EpoxyModelClass(layout = R.layout.view_main_text)
abstract class MainTextModel : EpoxyModelWithHolder<MainTextModel.MainTextViewHolder>() {

    @EpoxyAttribute
    lateinit var text: String

    override fun bind(holder: MainTextViewHolder) {
        super.bind(holder)
        holder.mainText.text = text
    }

    inner class MainTextViewHolder : KotlinEpoxyHolder() {
        val mainText by bind<TextView>(R.id.tvMainText)
    }
}

@EpoxyModelClass(layout = R.layout.view_youtube_player)
abstract class TrailerModel : EpoxyModelWithHolder<TrailerModel.TrailerHolder>() {

    @EpoxyAttribute
    lateinit var trailerKey: String

    override fun bind(holder: TrailerHolder) {
        super.bind(holder)
        holder.youtubePlayer.initialize({ initializedPlayer ->
            initializedPlayer.addListener(object : AbstractYouTubePlayerListener() {
                override fun onReady() {
                    super.onReady()
                    initializedPlayer.cueVideo(trailerKey, 0f)
                }
            })
        }, true)
    }

    override fun unbind(holder: TrailerHolder) {
        super.unbind(holder)
        holder.youtubePlayer.release()
    }

    inner class TrailerHolder : KotlinEpoxyHolder() {
        val youtubePlayer by bind<YouTubePlayerView>(R.id.movieTrailer)
    }
}

@EpoxyModelClass(layout = R.layout.view_movie_info_bar)
abstract class InfoBarModel : EpoxyModelWithHolder<InfoBarModel.InfoBarHolder>() {

    @EpoxyAttribute
    lateinit var accountStates: AccountState

    @EpoxyAttribute(EpoxyAttribute.Option.DoNotHash)
    lateinit var watchListClickListener: View.OnClickListener

    @EpoxyAttribute(EpoxyAttribute.Option.DoNotHash)
    lateinit var favouritesClickListener: View.OnClickListener

    override fun bind(holder: InfoBarHolder) {
        super.bind(holder)

        accountStates.isWatchlisted?.let { watchlisted ->
            holder.watchlistButton.apply {
                setRemoveFromListState(watchlisted)
                text = if (watchlisted) "Un-Watchlist" else "Add to Watchlist"
                setOnClickListener(watchListClickListener)
            }
        } ?: holder.watchlistButton.apply {
            setUnauthenticatedState(true)
            setOnClickListener(watchListClickListener)
        }

        accountStates.isFavourited?.let { favourite ->
            holder.favouriteButton.apply {
                setRemoveFromListState(favourite)
                text = if (favourite) "Un-Favourite" else "Add to Favourites"
                setOnClickListener(favouritesClickListener)
            }
        } ?: holder.favouriteButton.apply {
            setUnauthenticatedState(true)
            setOnClickListener(favouritesClickListener)
        }
    }

    override fun unbind(holder: InfoBarHolder) {
        super.unbind(holder)
        holder.favouriteButton.apply {
            setRemoveFromListState(false)
            setUnauthenticatedState(true)
        }
        holder.watchlistButton.apply {
            setRemoveFromListState(false)
            setUnauthenticatedState(true)
        }
    }

    inner class InfoBarHolder : KotlinEpoxyHolder() {
        val favouriteButton by bind<CustomMaterialButton>(R.id.btToggleFavourite)
        val watchlistButton by bind<CustomMaterialButton>(R.id.btToggleWatchlist)
    }
}