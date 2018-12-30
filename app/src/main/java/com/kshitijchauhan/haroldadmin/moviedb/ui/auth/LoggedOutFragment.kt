package com.kshitijchauhan.haroldadmin.moviedb.ui.auth

import android.graphics.Bitmap
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.transition.*
import com.kshitijchauhan.haroldadmin.moviedb.MovieDBApplication
import com.kshitijchauhan.haroldadmin.moviedb.R
import com.kshitijchauhan.haroldadmin.moviedb.remote.service.auth.CreateSessionRequest
import com.kshitijchauhan.haroldadmin.moviedb.ui.BaseFragment
import com.kshitijchauhan.haroldadmin.moviedb.ui.UIState
import com.kshitijchauhan.haroldadmin.moviedb.ui.main.MainViewModel
import com.kshitijchauhan.haroldadmin.moviedb.utils.extensions.*
import kotlinx.android.synthetic.main.activity_main_alternate.*
import kotlinx.android.synthetic.main.fragment_logged_out.*
import java.lang.IllegalStateException

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

        (activity as AppCompatActivity)
            .mainCollapsingToolbarLayout
            .title = "Login"
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        authWebView.apply {
            webViewClient = object : WebViewClient() {

                override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                    url?.let {
                        log("Loading this url in webview: $it")
                    }
                    super.onPageStarted(view, url, favicon)
                }

                override fun onPageFinished(view: WebView?, url: String?) {
                    super.onPageFinished(view, url)
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

            TransitionManager.beginDelayedTransition(view as ViewGroup)
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

        TransitionManager.beginDelayedTransition(view as ViewGroup)
        webGroup.gone()
        loadingGroup.visible()

        authenticationViewModel.authSuccess.observe(viewLifecycleOwner, Observer {
            // We need a new instance of sessionId Interceptor
            // TODO Move this responsibility to viewmodel
            (activity?.application as MovieDBApplication).rebuildAppComponent()

            mainViewModel.apply {
                showSnackbar("Login Successful!")
                setBottomNavSelectedItemId(R.id.menuHome)
                // TODO Move this responsibility to activity
                repeat(fragmentManager?.backStackEntryCount ?: 0) {
                    fragmentManager?.popBackStack()
                }
            }
        })
    }
}
