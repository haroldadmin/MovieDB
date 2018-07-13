package com.kshitijchauhan.haroldadmin.moviedb;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.bumptech.glide.TransitionOptions;
import com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieDetails extends AppCompatActivity {

    private static final String LOG_TAG = MovieDetails.class.getName();
    private static String imdbID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        Intent intent = getIntent();
        String ID = intent.getStringExtra("ID");
        Toolbar movieDetailsToolbar = findViewById(R.id.details_activity_toolbar);
        setSupportActionBar(movieDetailsToolbar);
        final android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        final ScrollView scrollView = findViewById(R.id.scrollView);
        final ImageView posterView = findViewById(R.id.poster_imageview);
        final TextView titleView = findViewById(R.id.title_textview);
        final TextView yearView = findViewById(R.id.year_textview);
        final TextView descriptionView = findViewById(R.id.description_textview);
        final RatingBar ratingBar = findViewById(R.id.rating_bar);
        final TextView genreView = findViewById(R.id.genre_textview);
        final TextView taglineView = findViewById(R.id.tagline_textview);
        final CardView descriptionCard = findViewById(R.id.description_card);
        final CardView miscCard = findViewById(R.id.misc_card);

        TMDbClient client = ServiceGenerator.createService(TMDbClient.class);

        Call<Movie> getMovieDetails = client.getMovieDetails(ID, BuildConfig.TMDb_API_KEY);

        getMovieDetails.enqueue(new Callback<Movie>() {
            @Override
            public void onResponse(Call<Movie> call, Response<Movie> response) {

                if(response.isSuccessful()) {
                    final Movie movie = response.body();

                    Glide
                            .with(MovieDetails.this)
                            .asBitmap()
                            .load(QueryUtils.getMovieDetailsPosterURL(movie.getPosterPath()))
                            .apply(new RequestOptions()
                                    .placeholder(R.drawable.no_image)
                                    .error(R.drawable.no_image)
                                    .centerCrop())
                            .into(new SimpleTarget<Bitmap>() {
                                @Override
                                public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                    posterView.setImageBitmap(resource);
                                    Palette.Builder paletteBuilder = Palette.from(resource);
                                    paletteBuilder.generate(new Palette.PaletteAsyncListener() {
                                        @Override
                                        public void onGenerated(@NonNull Palette palette) {
                                            scrollView.setBackgroundColor(palette.getDarkMutedColor(ContextCompat.getColor(MovieDetails.this ,R.color.scrollViewBackground)));
                                            descriptionCard.setCardBackgroundColor(palette.getDarkVibrantColor(ContextCompat.getColor(MovieDetails.this, R.color.scrollViewBackground)));
                                            miscCard.setCardBackgroundColor(palette.getDarkVibrantColor(ContextCompat.getColor(MovieDetails.this, R.color.scrollViewBackground)));
                                            actionBar.setBackgroundDrawable(new ColorDrawable());
                                            Window window = getWindow();
                                            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                                            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                                            window.setStatusBarColor(palette.getDarkMutedColor(ContextCompat.getColor(MovieDetails.this, R.color.scrollViewBackground)));
                                        }
                                    });
                                }
                            });
                    titleView.setText(movie.getTitle());

                    if (movie.getReleaseDate() != null) {
                        yearView.setText(movie.getReleaseDate().substring(0, 4));
                    }
                    else {
                        yearView.setText("N/A");
                    }
                    descriptionView.setText(movie.getOverview());
                    actionBar.setTitle(movie.getTitle());
                    ratingBar.setNumStars((int) movie.getRating());
                    genreView.setText(movie.getGenre());
                    taglineView.setText(movie.getTagline());
                    imdbID = movie.getImdbID();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.movie_details_app_bar_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.imdbButton) {
            String url = QueryUtils.imdbUriGenerator(imdbID);
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(url));
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}
