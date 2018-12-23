package com.kshitijchauhan.haroldadmin.moviedb.ui.auth


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import com.kshitijchauhan.haroldadmin.moviedb.R
import com.kshitijchauhan.haroldadmin.moviedb.ui.BaseFragment
import com.kshitijchauhan.haroldadmin.moviedb.ui.UIState
import com.kshitijchauhan.haroldadmin.moviedb.ui.main.MainViewModel
import com.kshitijchauhan.haroldadmin.moviedb.utils.log

class LoginFragment : BaseFragment() {

    override val associatedState: UIState = UIState.AuthScreenState

    private lateinit var mainViewModel: MainViewModel

    companion object {
        fun newInstance() = LoginFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        log("onCreateView")
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mainViewModel = ViewModelProviders.of(activity!!).get(MainViewModel::class.java)
    }

    override fun onDestroyView() {
        val lastState: UIState? = mainViewModel.peekState()?.first
        if (isRemoving && lastState != associatedState) {
            lastState?.let {
                mainViewModel.updateState(UIState.AuthScreenState to lastState)
            }
        }
        super.onDestroyView()
    }
}
