package com.kshitijchauhan.haroldadmin.moviedb.about

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.kshitijchauhan.haroldadmin.moviedb.about.databinding.FragmentAboutBinding
import org.koin.android.ext.android.inject
import org.koin.core.qualifier.named

class AboutFragment : Fragment() {

    private lateinit var binding: FragmentAboutBinding
    private val epoxyHandler by inject<Handler>(named("epoxy-handler"))
    private val state = AboutState()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentAboutBinding.inflate(inflater, container, false)

        binding.rvAbout.apply {
            setController(epoxyController)
            epoxyController.setData(state)
        }
        return binding.root
    }

    private val epoxyController by lazy { AboutEpoxyController(epoxyHandler, callbacks) }

    private val callbacks by lazy {
        object : AboutEpoxyController.AboutEpoxyControllerCallbacks {
            override val onRateClick: View.OnClickListener = View.OnClickListener {
                val uri = Uri.parse("market://details?id=${requireContext().packageName}")
                createIntent(uri).let { intent -> startActivity(intent) }
            }
            override val onAuthorClick: View.OnClickListener = View.OnClickListener {
                val uri = Uri.parse(state.authorUrl)
                createIntent(uri).let { intent -> startActivity(intent) }
            }

            override val onRepoClick: View.OnClickListener = View.OnClickListener {
                val uri = Uri.parse(state.repositoryUrl)
                createIntent(uri).let { intent -> startActivity(intent) }
            }

            override val onTmdbClick: View.OnClickListener = View.OnClickListener {
                val uri = Uri.parse(state.tmdbUrl)
                createIntent(uri).let { intent -> startActivity(intent)}
            }
        }
    }

    private fun createIntent(uri: Uri): Intent {
        return Intent(Intent.ACTION_VIEW, uri)
            .apply {
                flags = Intent.FLAG_ACTIVITY_NO_HISTORY or
                        Intent.FLAG_ACTIVITY_NEW_DOCUMENT or
                        Intent.FLAG_ACTIVITY_MULTIPLE_TASK
            }
    }
}
