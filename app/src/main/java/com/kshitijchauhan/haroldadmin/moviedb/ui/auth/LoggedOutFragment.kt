package com.kshitijchauhan.haroldadmin.moviedb.ui.auth

import android.graphics.Bitmap
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.transition.Fade
import androidx.transition.Slide
import androidx.transition.TransitionManager
import androidx.transition.TransitionSet
import com.kshitijchauhan.haroldadmin.moviedb.R
import com.kshitijchauhan.haroldadmin.moviedb.remote.service.auth.CreateSessionRequest
import com.kshitijchauhan.haroldadmin.moviedb.ui.BaseFragment
import com.kshitijchauhan.haroldadmin.moviedb.ui.main.MainViewModel
import com.kshitijchauhan.haroldadmin.moviedb.utils.extensions.gone
import com.kshitijchauhan.haroldadmin.moviedb.utils.extensions.log
import com.kshitijchauhan.haroldadmin.moviedb.utils.extensions.visible
import kotlinx.android.synthetic.main.fragment_logged_out.*

class LoggedOutFragment : BaseFragment() {

    private lateinit var mainViewModel: MainViewModel
    private lateinit var authenticationViewModel: AuthenticationViewModel

    companion object {
        fun newInstance() = LoggedOutFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_logged_out, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        mainViewModel = ViewModelProviders.of(activity!!).get(MainViewModel::class.java)
        authenticationViewModel = ViewModelProviders.of(this).get(AuthenticationViewModel::class.java)

        mainViewModel.updateToolbarTitle("Login")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        authWebView.apply {
            webViewClient = object : WebViewClient() {

                override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                    url?.let {
                        log("Loading this url in webview: $it")
                    }
                    mainViewModel.setProgressBarVisible(true)
                    super.onPageStarted(view, url, favicon)
                }

                override fun onPageFinished(view: WebView?, url: String?) {
                    super.onPageFinished(view, url)
                    mainViewModel.setProgressBarVisible(false)
                    if (url?.contains("allow") == true) {
                        handleAuthorizationSuccessful(
                            authenticationViewModel.requestToken.value
                                ?: throw IllegalStateException("Can not create session ID because request token is empty")
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
                        .addTarget(tvInfo)
                        .addTarget(btLogin)
                        .setDuration(200)
                )
                addTransition(
                    Slide(Gravity.BOTTOM)
                        .addTarget(authWebView)
                        .setDuration(200)
                )
            }

            TransitionManager.beginDelayedTransition(container, transition)
            infoGroup.gone()
            webGroup.visible()

            authenticationViewModel.requestToken.observe(viewLifecycleOwner, Observer { token ->
                if (token.isNotBlank())
                    authorizeToken(token)
            })
        }
    }

    private fun authorizeToken(token: String) {
        authWebView.loadUrl("https://www.themoviedb.org/authenticate/$token")
    }

    private fun handleAuthorizationSuccessful(token: String) {
        authenticationViewModel.createSession(CreateSessionRequest(token))

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

        authenticationViewModel.authSuccess.observe(viewLifecycleOwner, Observer {
            mainViewModel.apply {
                // We need a new instance of sessionId Interceptor
                rebuildAppComponent()

                showSnackbar("Login Successful!")

                signalClearBackstack()
            }
        })
    }
}
