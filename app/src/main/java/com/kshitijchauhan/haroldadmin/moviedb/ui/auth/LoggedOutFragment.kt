package com.kshitijchauhan.haroldadmin.moviedb.ui.auth

import android.graphics.Bitmap
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import androidx.lifecycle.Observer
import androidx.transition.Fade
import androidx.transition.Slide
import androidx.transition.TransitionManager
import androidx.transition.TransitionSet
import com.kshitijchauhan.haroldadmin.moviedb.R
import com.kshitijchauhan.haroldadmin.moviedb.repository.data.Resource
import com.kshitijchauhan.haroldadmin.moviedb.repository.data.remote.service.auth.CreateSessionRequest
import com.kshitijchauhan.haroldadmin.moviedb.ui.BaseFragment
import com.kshitijchauhan.haroldadmin.moviedb.ui.UIState
import com.kshitijchauhan.haroldadmin.moviedb.ui.main.MainViewModel
import com.kshitijchauhan.haroldadmin.moviedb.utils.extensions.gone
import com.kshitijchauhan.haroldadmin.moviedb.utils.extensions.safe
import com.kshitijchauhan.haroldadmin.moviedb.utils.extensions.visible
import kotlinx.android.synthetic.main.fragment_logged_out.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoggedOutFragment : BaseFragment() {

    private val mainViewModel: MainViewModel by sharedViewModel()
    private val authenticationViewModel: AuthenticationViewModel by viewModel()

    override val associatedUIState: UIState = UIState.AccountScreenState.UnauthenticatedScreenState

    override fun notifyBottomNavManager() {
        mainViewModel.updateBottomNavManagerState(this.associatedUIState)
    }

    override fun updateToolbarTitle() {
        mainViewModel.updateToolbarTitle(getString(R.string.title_account_screen))
    }

    companion object {
        fun newInstance() = LoggedOutFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_logged_out, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        updateToolbarTitle()

        authWebView.apply {
            webViewClient = object : WebViewClient() {

                override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                    super.onPageStarted(view, url, favicon)
                }

                override fun onPageFinished(view: WebView?, url: String?) {
                    super.onPageFinished(view, url)
                    if (url?.contains("allow") == true) {
                        handleAuthorizationSuccessful(
                            authenticationViewModel.requestToken.value!!
                        )
                    }
                }
            }
            settings.javaScriptEnabled = true
        }

        btLogin.setOnClickListener {
            authenticationViewModel.getRequestToken()

            val transition = TransitionSet()
            transition.apply {
                ordering = TransitionSet.ORDERING_SEQUENTIAL
                addTransition(
                    Slide(Gravity.TOP)
                        .addTarget(ivKey)
                        .addTarget(tvNeedToLogin)
                        .addTarget(btLogin)
                        .setDuration(300)
                )
                addTransition(
                    Slide(Gravity.BOTTOM)
                        .addTarget(authWebView)
                        .setDuration(300)
                )
                interpolator = FastOutSlowInInterpolator()
            }

            TransitionManager.beginDelayedTransition(container, transition)
            infoGroup.gone()
            webGroup.visible()

            authenticationViewModel.requestToken.observe(viewLifecycleOwner, Observer { tokenResource ->
                when (tokenResource) {
                    is Resource.Success -> {
                        authorizeToken(tokenResource.data)
                    }
                    is Resource.Error -> {
                        // TODO handle this
                    }
                    is Resource.Loading -> {
                        // TODO handle this
                    }
                }.safe
            })
        }
    }

    private fun authorizeToken(token: String) {
        authWebView.loadUrl("https://www.themoviedb.org/authenticate/$token")
    }

    private fun handleAuthorizationSuccessful(token: Resource<String>) {
        when (token) {
            is Resource.Success -> {
                authenticationViewModel.createSession(CreateSessionRequest(token.data))

                val transition = TransitionSet()
                transition.apply {
                    ordering = TransitionSet.ORDERING_SEQUENTIAL
                    addTransition(
                        Fade()
                            .addTarget(authWebView)
                            .setDuration(200)
                    )
                    addTransition(
                        Fade()
                            .addTarget(pbLoading)
                            .addTarget(tvPleaseWait)
                            .setDuration(200)
                    )
                }

                TransitionManager.beginDelayedTransition(container, transition)
                webGroup.gone()
                loadingGroup.visible()
            }
            else -> {
                // TODO Handle this
            }
        }.safe

        authenticationViewModel.accountDetails.observe(viewLifecycleOwner, Observer { accountDetailsResource ->
            when (accountDetailsResource) {
                is Resource.Success -> {
                    mainViewModel.apply {
                        showSnackbar(getString(R.string.message_login_success))
                        signalClearBackstack()
                    }
                    Unit
                }
                is Resource.Error -> {
                    // TODO Handle this
                    Unit
                }
                is Resource.Loading -> {
                    // TODO Handle this
                    Unit
                }
            }.safe
        })
    }

    // TODO Destroy the webview to prevent memory leaks and crashes in onPageFinished
}
