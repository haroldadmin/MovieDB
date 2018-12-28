package com.kshitijchauhan.haroldadmin.moviedb.ui.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.kshitijchauhan.haroldadmin.moviedb.R
import com.kshitijchauhan.haroldadmin.moviedb.remote.service.movie.Movie
import com.kshitijchauhan.haroldadmin.moviedb.ui.BaseFragment
import com.kshitijchauhan.haroldadmin.moviedb.utils.Constants
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_main_alternate.*
import kotlinx.android.synthetic.main.fragment_movie_details.*


class MovieDetailsFragment : BaseFragment() {

    private val compositeDisposable = CompositeDisposable()

    companion object {
        fun newInstance(movieId: Int): MovieDetailsFragment {
            val newInstance = MovieDetailsFragment()
            newInstance.arguments = Bundle()
                .apply {
                    putInt(Constants.KEY_MOVIE_ID, movieId)
                }
            return newInstance
        }
    }

    private lateinit var viewModel: MovieDetailsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_movie_details, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(MovieDetailsViewModel::class.java)
        arguments?.getInt(Constants.KEY_MOVIE_ID)?.let { id ->
            viewModel.getMovieDetails(id)
        }

        viewModel.movieDetails.observe(viewLifecycleOwner, Observer { movie ->
            updateView(movie)
        })
    }

    private fun updateView(movie: Movie) {

        (activity as AppCompatActivity)
            .mainCollapsingToolbarLayout
            .title = movie.title

        Glide.with(this)
            .load(movie.posterPath)
            .transition(DrawableTransitionOptions.withCrossFade())
            .apply(
                RequestOptions()
                    .centerCrop()
            )
            .into(ivPoster)

        Glide.with(this)
            .asBitmap()
            .transition(BitmapTransitionOptions.withCrossFade())
            .apply(
                RequestOptions()
                    .centerCrop()
            )
            .load(movie.backdropPath)
            .into(ivBackdrop)

        tvTitle.text = movie.title
        chipMovieYear.text = movie.releaseDate
        chipMovieGenre.text = movie.genres[0].name
        chipMovieRating.text = String.format("%.2f", movie.voteAverage)
        tvDescription.text = movie.overview
    }

    override fun onStop() {
        super.onStop()
        compositeDisposable.dispose()
    }

}
