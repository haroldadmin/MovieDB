package com.kshitijchauhan.haroldadmin.moviedb.ui.library

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.ViewCompat
import com.airbnb.epoxy.*
import com.bumptech.glide.RequestManager
import com.kshitijchauhan.haroldadmin.moviedb.R
import com.kshitijchauhan.haroldadmin.moviedb.ui.common.KotlinEpoxyHolder
import org.koin.core.parameter.parametersOf
import org.koin.standalone.KoinComponent
import org.koin.standalone.get

@EpoxyModelClass(layout = R.layout.item_section_header)
abstract class HeaderModel: EpoxyModelWithHolder<HeaderModel.HeaderViewHolder>() {

    @EpoxyAttribute
    lateinit var title: String

    @EpoxyAttribute
    lateinit var transitionName: String

    override fun bind(holder: HeaderViewHolder) {
        super.bind(holder)
        holder.title.text = title
    }

    class HeaderViewHolder: KotlinEpoxyHolder() {
        val title by bind<TextView>(R.id.tvSectionHeader)
    }
}

@EpoxyModelClass(layout = R.layout.item_moviegrid)
abstract class MovieModel: EpoxyModelWithHolder<MovieModel.MovieViewHolder>(), KoinComponent {

    @EpoxyAttribute
    lateinit var posterUrl: String

    @EpoxyAttribute(EpoxyAttribute.Option.DoNotHash)
    lateinit var clickListener: View.OnClickListener

    @EpoxyAttribute
    lateinit var transitionName: String

    @EpoxyAttribute
    var movieId: Int? = null

    override fun bind(holder: MovieViewHolder) {
        super.bind(holder)
        get<RequestManager>("view-glide-request-manager") { parametersOf(holder.poster as View) }
            .load(posterUrl)
            .into(holder.poster)
        ViewCompat.setTransitionName(holder.poster, transitionName)
        holder.poster.setOnClickListener(clickListener)
    }

    inner class MovieViewHolder: KotlinEpoxyHolder() {
        val poster by bind<ImageView>(R.id.ivPoster)
    }
}

@EpoxyModelClass(layout = R.layout.view_empty_list)
abstract class EmptyListModel: EpoxyModelWithHolder<EmptyListModel.EmptyListViewHolder>() {

    @EpoxyAttribute
    lateinit var text: String

    override fun bind(holder: EmptyListViewHolder) {
        super.bind(holder)
        holder.textView.text = text
    }
    inner class EmptyListViewHolder: KotlinEpoxyHolder() {
        val textView by bind<TextView>(R.id.tvEmptyList)
    }
}

@EpoxyModelClass(layout = R.layout.view_need_to_login)
abstract class NeedToLoginModel: EpoxyModelWithHolder<NeedToLoginModel.NeedToLoginViewHolder>() {

    inner class NeedToLoginViewHolder: KotlinEpoxyHolder() {
        val textview by bind<TextView>(R.id.tvNeedToLogin)
    }
}