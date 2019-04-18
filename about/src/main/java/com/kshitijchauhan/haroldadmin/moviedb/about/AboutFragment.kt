package com.kshitijchauhan.haroldadmin.moviedb.about

import android.content.Context
import android.net.Uri
import android.widget.Toast
import com.danielstone.materialaboutlibrary.ConvenienceBuilder
import com.danielstone.materialaboutlibrary.MaterialAboutFragment
import com.danielstone.materialaboutlibrary.items.MaterialAboutActionItem
import com.danielstone.materialaboutlibrary.items.MaterialAboutItem
import com.danielstone.materialaboutlibrary.items.MaterialAboutTitleItem
import com.danielstone.materialaboutlibrary.model.MaterialAboutCard
import com.danielstone.materialaboutlibrary.model.MaterialAboutList
import com.kshitijchauhan.haroldadmin.moviedb.BuildConfig

class AboutFragment : MaterialAboutFragment() {

    override fun getMaterialAboutList(ctx: Context): MaterialAboutList = buildAboutCards(ctx)

    override fun getTheme(): Int {
        return R.style.Theme_Mal_Dark_AboutMovieDB
    }

    private fun buildAboutCards(ctx: Context): MaterialAboutList {
        val appCardBuilder = MaterialAboutCard.Builder().apply {
            this += MaterialAboutTitleItem.Builder()
                .text(com.kshitijchauhan.haroldadmin.moviedb.R.string.app_name)
                .desc(getString(R.string.app_description))
                .icon(com.kshitijchauhan.haroldadmin.moviedb.R.mipmap.ic_launcher)
                .setOnLongClickAction {
                    Toast.makeText(requireContext(), getString(R.string.easter_egg_message), Toast.LENGTH_SHORT)
                        .show()
                }
                .build()

            this += ConvenienceBuilder.createVersionActionItem(
                ctx,
                resources.getDrawable(R.drawable.ic_round_update_24px, ctx.theme),
                getString(R.string.version),
                false
            )

            this += ConvenienceBuilder.createRateActionItem(
                ctx,
                resources.getDrawable(R.drawable.ic_round_star_rate_18px, ctx.theme),
                getString(R.string.rate_the_app),
                getString(R.string.rate_the_app_subtext)
            )
        }

        val authorCardBuilder = MaterialAboutCard.Builder().apply {
            title(getString(R.string.author))

            this += MaterialAboutActionItem.Builder()
                .text(getString(R.string.author_name))
                .subText(getString(R.string.github_username))
                .icon(R.drawable.ic_github_circle)
                .setOnClickAction {
                    ConvenienceBuilder.createWebsiteOnClickAction(
                        ctx,
                        Uri.parse("https://www.github.com/haroldadmin")
                    ).onClick()
                }
                .build()

            this += MaterialAboutActionItem.Builder()
                .text(getString(R.string.github_repository))
                .subText(getString(R.string.app_contributions))
                .icon(R.drawable.ic_github_circle)
                .setOnClickAction {
                    ConvenienceBuilder.createWebsiteOnClickAction(
                        ctx,
                        Uri.parse("https://www.github.com/haroldadmin/MovieDB")
                    ).onClick()
                }
                .build()
        }

        val tmdbCardBuilder = MaterialAboutCard.Builder().apply {
            title("TMDb")

            this += MaterialAboutTitleItem.Builder()
                .desc(getString(com.kshitijchauhan.haroldadmin.moviedb.about.R.string.tmdb_credits_desc))
                .icon(R.drawable.ic_tmdb)
                .setOnClickAction {
                    ConvenienceBuilder.createWebsiteOnClickAction(
                        ctx,
                        Uri.parse("http://themoviedb.org/")
                    ).onClick()
                }
                .setOnLongClickAction {
                    Toast.makeText(requireContext(), getString(R.string.easter_egg), Toast.LENGTH_SHORT)
                        .show()
                }
                .build()
        }

        val builder = MaterialAboutList.Builder()
            .addCard(appCardBuilder.build())
            .addCard(authorCardBuilder.build())
            .addCard(tmdbCardBuilder.build())

        if (BuildConfig.DEBUG) {
            val debugCard = MaterialAboutCard.Builder().apply {
                this += MaterialAboutTitleItem.Builder()
                    .desc("You are using a debug build of the app.")
                    .build()
            }
            builder.addCard(debugCard.build())
        }

        return builder.build()
    }

    private operator fun <T : MaterialAboutItem> MaterialAboutCard.Builder.plusAssign(item: T) {
        this.addItem(item)
    }
}