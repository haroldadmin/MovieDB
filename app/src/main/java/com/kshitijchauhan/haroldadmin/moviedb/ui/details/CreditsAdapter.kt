package com.kshitijchauhan.haroldadmin.moviedb.ui.details

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.kshitijchauhan.haroldadmin.moviedb.R
import com.kshitijchauhan.haroldadmin.moviedb.remote.service.movie.CastMember
import com.kshitijchauhan.haroldadmin.moviedb.utils.extensions.getProfilePictureUrl
import kotlinx.android.synthetic.main.item_credit_actor.view.*

class CreditsAdapter(private val glide: RequestManager) :
    ListAdapter<CastMember, CreditsAdapter.ViewHolder>(CastMemberDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_credit_actor, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(castMember: CastMember) {
            itemView.tvCreditActorName.text = castMember.name
            glide.load(castMember.profilePath.getProfilePictureUrl())
                .apply(
                    RequestOptions()
                        .placeholder(R.drawable.ic_round_account_circle_24px)
                        .error(R.drawable.ic_round_account_circle_24px)
                        .fallback(R.drawable.ic_round_account_circle_24px)
                )
                .into(itemView.ivCreditActorPhoto)
        }
    }
}