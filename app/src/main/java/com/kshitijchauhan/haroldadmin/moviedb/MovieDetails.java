package com.kshitijchauhan.haroldadmin.moviedb;

import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;


import com.bumptech.glide.Glide;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MovieDetails extends AppCompatActivity {

    private static final String LOG_TAG = MovieDetails.class.getName();
    public static final String BASE_URL = "https://api.themoviedb.org/3/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        Intent intent = getIntent();
        String ID = intent.getStringExtra("ID");
        Toolbar movieDetailsToolbar = findViewById(R.id.details_activity_toolbar);
        setSupportActionBar(movieDetailsToolbar);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        /* Build OkHTTP client and Retrofit client for making network requests */
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.client(httpClient.build()).build();
        TMDbInterface client = retrofit.create(TMDbInterface.class);

        Call<Movie> getMovieDetails = client.getMovieDetails(ID, BuildConfig.TMDb_API_KEY);
        getMovieDetails.enqueue(new Callback<Movie>() {
            @Override
            public void onResponse(Call<Movie> call, Response<Movie> response) {

                if(response.isSuccessful()) {
                    Movie movie = response.body();
                    final ConstraintLayout constraintLayout = findViewById(R.id.constraint_layout);
                    ImageView posterView = findViewById(R.id.poster_imageview);
                    TextView titleView = findViewById(R.id.title_textview);
                    TextView yearView = findViewById(R.id.year_textview);
                    TextView descriptionView = findViewById(R.id.description_textview);
                    TextView RuntimeView = findViewById(R.id.runtime_textview);
                    RatingBar ratingBar = findViewById(R.id.ratingBar);
                    ratingBar.setVisibility(View.INVISIBLE);
                    TextView genreView = findViewById(R.id.genre_view);
                    TextView taglineView = findViewById(R.id.tagline_view);

                    Glide
                            .with(MovieDetails.this)
                            .load("https://image.tmdb.org/t/p/w154" + movie.getPosterPath())
                            .into(posterView);
                    titleView.setText(movie.getTitle());
                    yearView.setText(movie.getReleaseDate().substring(0, 4));
                    descriptionView.setText(movie.getOverview());
                    String runtimeText = movie.getRuntime() + " " + getString(R.string.runtime_units);
                    RuntimeView.setText(runtimeText);
                    double stars = movie.getRating();

                    ratingBar.setVisibility(View.VISIBLE);
                    ratingBar.setNumStars((int) (stars/2));
                    genreView.setText(movie.getGenre());

                    taglineView.setText(movie.getTagline());
                    android.support.v7.app.ActionBar actionBar = getSupportActionBar();
                    actionBar.setTitle(movie.getTitle());
                }
                else if(response.body() == null){
                    Log.e(LOG_TAG, "Error getting movie response");
                }
                else {
                    Log.e(LOG_TAG, "Response unsuccessful");
                }

            }

            @Override
            public void onFailure(Call<Movie> call, Throwable t) {
                Toast.makeText(MovieDetails.this, "Error loading movie details", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
