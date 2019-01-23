package com.kshitijchauhan.haroldadmin.moviedb.ui.in_theatres


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.kshitijchauhan.haroldadmin.moviedb.R
import com.kshitijchauhan.haroldadmin.moviedb.ui.BaseFragment
import com.kshitijchauhan.haroldadmin.moviedb.ui.UIState
import com.kshitijchauhan.haroldadmin.moviedb.ui.common.model.LoadingTask
import com.kshitijchauhan.haroldadmin.moviedb.ui.main.MainViewModel
import com.mikepenz.itemanimators.AlphaInAnimator
import kotlinx.android.synthetic.main.fragment_in_theatres.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class InTheatresFragment : BaseFragment() {

    private val TASK_LOAD_IN_THEATRES_MOVIES = "load-in-theatres-movies"

    private val mainViewModel: MainViewModel by sharedViewModel()
    private val inTheatresViewModel: InTheatresViewModel by viewModel()
    private var moviesAdapter: MoviesAdapter? = null

    override val associatedUIState: UIState = UIState.InTheatresScreenState

    override fun notifyBottomNavManager() {
        mainViewModel.updateBottomNavManagerState(this.associatedUIState)
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
                moviesAdapter?.updateList(it)
                mainViewModel.completeLoadingTask(TASK_LOAD_IN_THEATRES_MOVIES, viewLifecycleOwner)
            })
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val linearLayoutManager = LinearLayoutManager(context)
        moviesAdapter = MoviesAdapter(Glide.with(this), mutableListOf()) { id ->
            mainViewModel.updateStateTo(UIState.DetailsScreenState(id))
        }
        rvMovies.apply {
            layoutManager = linearLayoutManager
            adapter = moviesAdapter
            itemAnimator = AlphaInAnimator()
        }
    }

    override fun onDestroyView() {
        moviesAdapter = null
        super.onDestroyView()
    }
}
