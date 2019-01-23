package com.kshitijchauhan.haroldadmin.moviedb.ui.in_theatres


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.RequestManager
import com.kshitijchauhan.haroldadmin.moviedb.R
import com.kshitijchauhan.haroldadmin.moviedb.remote.service.common.GeneralMovieResponse
import com.kshitijchauhan.haroldadmin.moviedb.ui.BaseFragment
import com.kshitijchauhan.haroldadmin.moviedb.ui.UIState
import com.kshitijchauhan.haroldadmin.moviedb.ui.common.model.LoadingTask
import com.kshitijchauhan.haroldadmin.moviedb.ui.main.MainViewModel
import com.mikepenz.itemanimators.AlphaInAnimator
import kotlinx.android.synthetic.main.fragment_in_theatres.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class InTheatresFragment : BaseFragment() {

    private val TASK_LOAD_IN_THEATRES_MOVIES = "load-in-theatres-movies"

    private val mainViewModel: MainViewModel by sharedViewModel()
    private val inTheatresViewModel: InTheatresViewModel by viewModel()
    private val glideRequestManager: RequestManager by inject("fragment-glide-request-manager") {
        parametersOf(this)
    }
    private val moviesAdapter: MoviesAdapter by inject {
        parametersOf(glideRequestManager, mutableListOf<GeneralMovieResponse>(), { id: Int ->
            mainViewModel.updateStateTo(UIState.DetailsScreenState(id))
        })
    }

    override val associatedUIState: UIState = UIState.InTheatresScreenState

    override fun notifyBottomNavManager() {
        mainViewModel.updateBottomNavManagerState(this.associatedUIState)
    }

    override fun updateToolbarTitle() {
        mainViewModel.updateToolbarTitle("In Theatres")
    }

    companion object {
        fun newInstance() = InTheatresFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_in_theatres, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        inTheatresViewModel.apply {
            mainViewModel.addLoadingTask(LoadingTask(TASK_LOAD_IN_THEATRES_MOVIES, viewLifecycleOwner))
            getPopularMovies()

            moviesUpdate.observe(viewLifecycleOwner, Observer {
                moviesAdapter.updateList(it)
                mainViewModel.completeLoadingTask(TASK_LOAD_IN_THEATRES_MOVIES, viewLifecycleOwner)
            })
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        updateToolbarTitle()

        val linearLayoutManager = LinearLayoutManager(context)
        rvMovies.apply {
            layoutManager = linearLayoutManager
            adapter = moviesAdapter
            itemAnimator = AlphaInAnimator()
        }
    }
}
