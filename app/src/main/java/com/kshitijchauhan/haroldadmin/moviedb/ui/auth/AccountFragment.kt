package com.kshitijchauhan.haroldadmin.moviedb.ui.auth


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.kshitijchauhan.haroldadmin.moviedb.R
import com.kshitijchauhan.haroldadmin.moviedb.repository.data.Resource
import com.kshitijchauhan.haroldadmin.moviedb.repository.data.remote.service.account.AccountDetailsResponse
import com.kshitijchauhan.haroldadmin.moviedb.ui.BaseFragment
import com.kshitijchauhan.haroldadmin.moviedb.ui.UIState
import com.kshitijchauhan.haroldadmin.moviedb.ui.main.MainViewModel
import com.kshitijchauhan.haroldadmin.moviedb.utils.extensions.safe
import kotlinx.android.synthetic.main.fragment_account.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class AccountFragment : BaseFragment() {

    private val mainViewModel: MainViewModel by sharedViewModel()
    private val authenticationViewModel: AuthenticationViewModel by viewModel()
    private val glideRequestManager: RequestManager by inject("fragment-glide-request-manager") {
        parametersOf(this)
    }
    override val associatedUIState: UIState by lazy {
        UIState.AccountScreenState.AuthenticatedScreenState
    }

    override fun updateToolbarTitle() {
        mainViewModel.updateToolbarTitle(getString(R.string.title_account_screen))
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_account, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        updateToolbarTitle()

        if (authenticationViewModel.accountDetails.value == null) {
            authenticationViewModel.getAccountDetails()
        }

        authenticationViewModel.accountDetails.observe(viewLifecycleOwner, Observer { accountInfo ->
            updateView(accountInfo)
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        updateToolbarTitle()

        btLogout.setOnClickListener {
            handleLogout()
            findNavController().popBackStack()
        }
    }

    private fun handleLogout() {
        authenticationViewModel.setNewSessionIdToInterceptor("")
        mainViewModel.apply {
            setAuthenticationStatus(false)
            setSessionId("")
            showSnackbar(getString(R.string.message_logout_success))
        }
    }

    private fun updateView(accountInfo: Resource<AccountDetailsResponse>) {
        when (accountInfo) {
            is Resource.Success -> {
                with(accountInfo.data) {
                    glideRequestManager
                        .load("https://www.gravatar.com/avatar/${avatar.gravatar.hash}")
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .apply(
                            RequestOptions()
                                .centerCrop()
                                .placeholder(R.drawable.ic_round_account_circle_24px)
                                .fallback(R.drawable.ic_round_account_circle_24px)
                                .error(R.drawable.ic_round_account_circle_24px)
                                .circleCrop()
                        )
                        .into(ivAvatar)
                    tvName.text = name
                    tvUsername.text = username
                }
            }
            is Resource.Error -> {
                // TODO handle this
            }
            is Resource.Loading -> {
                // TODO handle this
            }
        }.safe
    }
}
