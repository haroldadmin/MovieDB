package com.kshitijchauhan.haroldadmin.moviedb;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Rating;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

public class MovieDetails extends AppCompatActivity {

    private static final String LOG_TAG = MovieDetails.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        Intent intent = getIntent();
        String IMDbID = intent.getStringExtra("IMDb ID");
        Toolbar movieDetailsToolbar = findViewById(R.id.details_activity_toolbar);
        setSupportActionBar(movieDetailsToolbar);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        MovieDetailsLoadTask movieDetailsLoadTask = new MovieDetailsLoadTask();
        URL detailsURL = QueryUtils.createDetailsURL(IMDbID);
        Log.v(LOG_TAG, detailsURL.toString());
        movieDetailsLoadTask.execute(detailsURL);
    }

    private class MovieDetailsLoadTask extends AsyncTask<URL, Void, Movie> {
        @Override
        protected Movie doInBackground(URL... urls) {
            String JSONResponse;
            Movie movie = new Movie();
            try {
                JSONResponse = QueryUtils.makeHTTPRequest(urls[0]);
                JSONObject root = new JSONObject(JSONResponse);
//                JSONArray ratings_array = root.getJSONArray("Ratings");
//                HashMap<String, String> ratings = new HashMap<>();
//                for (int i = 0; i < ratings_array.length(); i++)
//                {
//                    JSONObject rating = ratings_array.getJSONObject(i);
//                    ratings.put(rating.getString("Source"), rating.getString("Value"));
//                }
//                movie.setRatings(ratings);
                movie.setName(root.getString("Title"));
                Log.v(LOG_TAG, root.getString("Title"));
                movie.setYear(root.getString("Year"));
                Log.v(LOG_TAG, root.getString("Year"));
                movie.setParentalRating(root.getString("Rated"));
                Log.v(LOG_TAG, root.getString("Rated"));
                movie.setReleaseDate(root.getString("Released"));
                Log.v(LOG_TAG, root.getString("Released"));
                movie.setRuntime(root.getString("Runtime"));
                Log.v(LOG_TAG, root.getString("Runtime"));
                movie.setGenre(root.getString("Genre"));
                Log.v(LOG_TAG, root.getString("Genre"));
                movie.setDirector(root.getString("Director"));
                Log.v(LOG_TAG, root.getString("Director"));
                movie.setWriter(root.getString("Writer"));
                Log.v(LOG_TAG, root.getString("Writer"));
//                movie.setCast(root.getString("Cast").split(", "));
//                Log.v(LOG_TAG, root.getString("Cast"));
                movie.setDescription(root.getString("Plot"));
                Log.v(LOG_TAG, root.getString("Plot"));
                movie.setWebsite(root.getString("Website"));
                Log.v(LOG_TAG, root.getString("Website"));
                movie.setRatings(root.getString("imdbRating"));

                Bitmap poster = BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.no_image);
                try {
                    URL posterURL = new URL(root.getString("Poster"));
                    HttpURLConnection connection = (HttpURLConnection) posterURL.openConnection();
                    connection.setDoInput(true);
                    connection.connect();
                    InputStream input = connection.getInputStream();
                    poster = BitmapFactory.decodeStream(input);
                    Log.v(LOG_TAG, "retrieved poster");
                    movie.setPoster(poster);
                }
                catch (MalformedURLException e) {
                    Log.e(LOG_TAG, "Couldn't retrieve poster due to bad search URL");
                }
                catch (IOException e) {
                    Log.e(LOG_TAG, "Couldn't retrieve poster due to connection problem");
                }
            }
            catch (IOException e) {
                Log.e(LOG_TAG, "Connection while retrieving movie details");
            }
            catch (JSONException e) {
                Log.e(LOG_TAG, "Error parsing JSON file.");
            }

            return movie;
        }

        @Override
        protected void onPostExecute(Movie movie) {
            ImageView posterView = findViewById(R.id.poster_imageview);
            TextView titleView = findViewById(R.id.title_textview);
            TextView yearView = findViewById(R.id.year_textview);
            TextView descriptionView = findViewById(R.id.description_textview);
            TextView directorView = findViewById(R.id.director_textview);
            RatingBar ratingBar = findViewById(R.id.ratingBar);
            TextView genreView = findViewById(R.id.genre_view);
            TextView parentalRatingView = findViewById(R.id.parental_rating);
            posterView.setImageBitmap(movie.getPoster());
            titleView.setText(movie.getName());
            yearView.setText(movie.getYear());
            descriptionView.setText(movie.getDescription());
            directorView.setText(movie.getDirector());
            double stars = Double.parseDouble(movie.getIMDbRating())/2;
            ratingBar.setNumStars((int) stars);
            genreView.setText(movie.getGenre());
            parentalRatingView.setText(movie.getParentalRating());
            android.support.v7.app.ActionBar actionBar = getSupportActionBar();
            actionBar.setTitle(movie.getName());
        }
    }
}
