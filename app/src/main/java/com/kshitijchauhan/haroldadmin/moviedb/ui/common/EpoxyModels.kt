package com.kshitijchauhan.haroldadmin.moviedb.ui.common

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.ViewCompat
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.kshitijchauhan.haroldadmin.moviedb.R
import org.koin.core.parameter.parametersOf
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject

@EpoxyModelClass(layout = R.layout.item_section_header)
abstract class HeaderModel : EpoxyModelWithHolder<HeaderModel.HeaderViewHolder>() {

    @EpoxyAttribute
    lateinit var title: String

    @EpoxyAttribute
    lateinit var transitionName: String

    override fun bind(holder: HeaderViewHolder) {
        super.bind(holder)
        holder.title.text = title
    }

    class HeaderViewHolder : KotlinEpoxyHolder() {
        val title by bind<TextView>(R.id.tvSectionHeader)
    }
}

@EpoxyModelClass(layout = R.layout.item_moviegrid)
abstract class MovieModel : EpoxyModelWithHolder<MovieModel.MovieViewHolder>() {

    @EpoxyAttribute
    lateinit var posterUrl: String

    @EpoxyAttribute(EpoxyAttribute.Option.DoNotHash)
    lateinit var clickListener: View.OnClickListener

    @EpoxyAttribute
    lateinit var transitionName: String

    @EpoxyAttribute
    var movieId: Int? = null

    @EpoxyAttribute
    lateinit var glide: RequestManager

    override fun bind(holder: MovieViewHolder) {
        super.bind(holder)
        glide.load(posterUrl)
            .transition(DrawableTransitionOptions.withCrossFade())
            .apply {
                RequestOptions()
                    .placeholder(R.drawable.ic_round_local_movies_24px)
                    .error(R.drawable.ic_round_local_movies_24px)
                    .fallback(R.drawable.ic_round_local_movies_24px)
            }
            .into(holder.poster)
        ViewCompat.setTransitionName(holder.poster, transitionName)
        holder.poster.setOnClickListener(clickListener)
    }

    override fun unbind(holder: MovieViewHolder) {
        super.unbind(holder)
        glide.clear(holder.poster)
    }

    inner class MovieViewHolder : KotlinEpoxyHolder(), KoinComponent {
        val poster by bind<ImageView>(R.id.ivPoster)
    }
}

@EpoxyModelClass(layout = R.layout.view_info_text)
abstract class InfoTextModel : EpoxyModelWithHolder<InfoTextModel.InfoTextViewHolder>() {

    @EpoxyAttribute
    lateinit var text: String

    override fun bind(holder: InfoTextViewHolder) {
        super.bind(holder)
        holder.textView.text = text
    }

    inner class InfoTextViewHolder : KotlinEpoxyHolder() {
        val textView by bind<TextView>(R.id.tvInfoText)
    }
}

@EpoxyModelClass(layout = R.layout.view_need_to_login)
abstract class NeedToLoginModel : EpoxyModelWithHolder<NeedToLoginModel.NeedToLoginViewHolder>() {

    inner class NeedToLoginViewHolder : KotlinEpoxyHolder() {
        val textview by bind<TextView>(R.id.tvNeedToLogin)
    }
}

@EpoxyModelClass(layout = R.layout.item_credit_actor)
abstract class ActorModel : EpoxyModelWithHolder<ActorModel.ActorHolder>() {

    @EpoxyAttribute
    var actorId: Int? = null

    @EpoxyAttribute
    lateinit var name: String

    @EpoxyAttribute
    lateinit var pictureUrl: String

    @EpoxyAttribute
    lateinit var transitionName: String

    @EpoxyAttribute(EpoxyAttribute.Option.DoNotHash)
    lateinit var clickListener: View.OnClickListener

    @EpoxyAttribute
    lateinit var glide: RequestManager

    override fun bind(holder: ActorHolder) {
        super.bind(holder)
        with(holder) {
            actorName.text = name
            glide
                .load(pictureUrl)
                .transition(DrawableTransitionOptions.withCrossFade())
                .apply(
                    RequestOptions()
                        .placeholder(R.drawable.ic_round_account_circle_24px)
                        .error(R.drawable.ic_round_account_circle_24px)
                        .fallback(R.drawable.ic_round_account_circle_24px)
                        .circleCrop()
                )
                .into(actorPicture)
            actorPicture.setOnClickListener(clickListener)
            ViewCompat.setTransitionName(actorPicture, transitionName)
        }
    }

    override fun unbind(holder: ActorHolder) {
        super.unbind(holder)
        glide.clear(holder.actorPicture)
    }

    inner class ActorHolder : KotlinEpoxyHolder(), KoinComponent {
        val actorName by bind<TextView>(R.id.tvCreditActorName)
        val actorPicture by bind<ImageView>(R.id.ivCreditActorPhoto)
    }
}

@EpoxyModelClass(layout = R.layout.item_movie_search_result)
abstract class MovieSearchResultModel : EpoxyModelWithHolder<MovieSearchResultModel.MovieSearchResultHolder>() {

    @EpoxyAttribute
    lateinit var posterUrl: String

    @EpoxyAttribute(EpoxyAttribute.Option.DoNotHash)
    lateinit var clickListener: View.OnClickListener

    @EpoxyAttribute
    lateinit var transitionName: String

    @EpoxyAttribute
    var movieId: Int? = null

    @EpoxyAttribute
    lateinit var glide: RequestManager

    @EpoxyAttribute
    lateinit var movieTitle: String

    override fun bind(holder: MovieSearchResultModel.MovieSearchResultHolder) {
        super.bind(holder)
        with(holder) {
            ViewCompat.setTransitionName(poster, transitionName)
            poster.setOnClickListener(clickListener)
            title.text = movieTitle
            glide.load(posterUrl)
                .transition(DrawableTransitionOptions.withCrossFade())
                .apply {
                    RequestOptions()
                        .placeholder(R.drawable.ic_round_local_movies_24px)
                        .error(R.drawable.ic_round_local_movies_24px)
                        .fallback(R.drawable.ic_round_local_movies_24px)
                }
                .into(holder.poster)
        }
    }

    override fun unbind(holder: MovieSearchResultHolder) {
        super.unbind(holder)
        glide.clear(holder.poster)
    }

    inner class MovieSearchResultHolder : KotlinEpoxyHolder(), KoinComponent {
        val poster by bind<ImageView>(R.id.ivPosterSearchResult)
        val title by bind<TextView>(R.id.tvTitleSearchResult)
    }
}