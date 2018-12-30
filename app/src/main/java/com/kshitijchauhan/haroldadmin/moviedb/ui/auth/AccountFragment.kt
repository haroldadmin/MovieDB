package com.kshitijchauhan.haroldadmin.moviedb.ui.auth


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.kshitijchauhan.haroldadmin.moviedb.MovieDBApplication
import com.kshitijchauhan.haroldadmin.moviedb.R
import com.kshitijchauhan.haroldadmin.moviedb.remote.service.account.AccountDetailsResponse
import com.kshitijchauhan.haroldadmin.moviedb.ui.BaseFragment
import com.kshitijchauhan.haroldadmin.moviedb.ui.main.MainViewModel
import com.kshitijchauhan.haroldadmin.moviedb.utils.extensions.getMainHandler
import com.kshitijchauhan.haroldadmin.moviedb.utils.extensions.log
import com.kshitijchauhan.haroldadmin.moviedb.utils.extensions.postDelayed
import kotlinx.android.synthetic.main.activity_main_alternate.*
import kotlinx.android.synthetic.main.fragment_account.*

class AccountFragment : BaseFragment() {

    private lateinit var mainViewModel: MainViewModel
    private lateinit var authenticationViewModel: AuthenticationViewModel

    companion object {
        fun newInstance() = AccountFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        log("onCreateView")
        return inflater.inflate(R.layout.fragment_account, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        mainViewModel = ViewModelProviders.of(activity!!).get(MainViewModel::class.java)
        authenticationViewModel = ViewModelProviders.of(this).get(AuthenticationViewModel::class.java)

        (activity as AppCompatActivity)
            .mainCollapsingToolbarLayout
            .title = "Your Account"

        authenticationViewModel.getAccountDetails()

        authenticationViewModel.accountDetails.observe(viewLifecycleOwner, Observer { accountInfo ->
            updateView(accountInfo)
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btLogout.setOnClickListener {
            mainViewModel.apply {
                setAuthenticationStatus(false)
                setSessionId("")
                // We need a new instance of sessionId Interceptor
                (activity?.application as MovieDBApplication).rebuildAppComponent()
                showSnackbar("Logged out successfully!")
                fragmentManager?.popBackStack()
            }
        }
    }

    private fun updateView(accountInfo: AccountDetailsResponse) {
        with(accountInfo) {
            Glide.with(this@AccountFragment)
                .load("https://www.gravatar.com/avatar/${avatar.gravatar.hash}")
                .transition(DrawableTransitionOptions.withCrossFade())
                .apply(
                    RequestOptions()
                        .centerCrop()
                        .placeholder(R.drawable.ic_round_account_circle_24px)
                )
                .into(ivAvatar)
            tvName.text = name
            tvUsername.text = username
        }
    }
}
