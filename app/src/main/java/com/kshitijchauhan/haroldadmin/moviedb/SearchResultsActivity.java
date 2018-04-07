package com.kshitijchauhan.haroldadmin.moviedb;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.AsyncTaskLoader;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class SearchResultsActivity extends AppCompatActivity {

    String query = "";
    private static final String LOG_TAG = SearchResultsActivity.class.getName();
    ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);

        // Get the intent, verify the action and get the query
        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            query = intent.getStringExtra(SearchManager.QUERY);
            Log.v(LOG_TAG, "Query successfully received: " + query);
        }
        progress = new ProgressDialog(this);
        progress.setTitle("Loading");
        progress.setMessage("Getting search results...");
        progress.setCancelable(false); // disable dismiss by tapping outside of the dialog
        progress.show();
        new getSearchResultsTask().execute(query);
    }

    private class getSearchResultsTask extends AsyncTask<String, Void, ArrayList<Movie>> {
        @Override
        protected ArrayList<Movie> doInBackground(String... strings) {
            ArrayList<Movie> searchResults = new ArrayList<>();
            String JSONResponse;
            try {
                JSONResponse = QueryUtils.makeHTTPRequest(QueryUtils.createSearchURL(strings[0]));
            }
            catch (IOException e) {
                Log.v(LOG_TAG, "Problem retrieving response from server");
                return searchResults;
            }
            try {
                JSONObject root = new JSONObject(JSONResponse);
                JSONArray results = root.getJSONArray("Search");
                int length = Integer.parseInt(root.getString("totalResults"));
                for (int i = 0; i < length; i++) {
                    JSONObject movie_details = results.getJSONObject(i);
                    Bitmap poster = BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.no_image);
                    try {
                        URL posterURL = new URL(movie_details.getString("Poster"));
                        HttpURLConnection connection = (HttpURLConnection) posterURL.openConnection();
                        connection.setDoInput(true);
                        connection.connect();
                        InputStream input = connection.getInputStream();
                        poster = BitmapFactory.decodeStream(input);
                        Log.v(LOG_TAG, "retrieved poster");
                    }
                    catch (MalformedURLException e) {
                        Log.e(LOG_TAG, "Couldn't retrieve poster due to bad search URL");
                    }
                    catch (IOException e) {
                        Log.e(LOG_TAG, "Couldn't retrieve poster due to connection problem");
                    }
                    String name = movie_details.getString("Title");
                    String year = movie_details.getString("Year");
                    Log.v(LOG_TAG, name + " " + year);
                    Movie movie = new Movie(name,
                                            year,
                                            poster);
                    searchResults.add(movie);
                    Log.v(LOG_TAG, "Successfully added movie: " + movie.getName());
                }
            }
            catch (JSONException e) {
                Log.e(LOG_TAG, "Problem parsing JSON response");
                return searchResults;
            }
            return searchResults;
        }

        @Override
        protected void onPostExecute(ArrayList<Movie> searchResults) {
            ListView listView = findViewById(R.id.searchResultsList);
            listView.setAdapter(new SearchResultsAdapter(SearchResultsActivity.this, searchResults));
            Log.v(LOG_TAG, "Successfully set adapter to listview");
            progress.dismiss();
        }
    }
}