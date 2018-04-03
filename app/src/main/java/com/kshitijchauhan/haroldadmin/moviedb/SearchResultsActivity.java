package com.kshitijchauhan.haroldadmin.moviedb;

import android.app.SearchManager;
import android.content.AsyncTaskLoader;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class SearchResultsActivity extends AppCompatActivity {

    String query;
    private static final String LOG_TAG = SearchResultsActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);

        // Get the intent, verify the action and get the query
        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            query = intent.getStringExtra(SearchManager.QUERY);
            ArrayList<Movie> searchResults = QueryUtils.getSearchResults("Dunkirk");
        }

        new getSearchResultsTask().execute();
    }

    private class getSearchResultsTask extends AsyncTask<String, Void, ArrayList<Movie>> {
        @Override
        protected ArrayList<Movie> doInBackground(String... strings) {
            String JSONResponse = null;
            URL searchURL = QueryUtils.createSearchURL(query);
            try {
                JSONResponse = QueryUtils.makeHTTPRequest(searchURL);
            }
            catch (IOException e) {
                Log.e(LOG_TAG, "Connection error");
            }
            ArrayList<Movie> searchResults = QueryUtils.getSearchResults(JSONResponse);
            Log.v(LOG_TAG, "SUCCESSFULLY RETRIEVED SEARCH RESULTS");
            return searchResults;
        }

        @Override
        protected void onPostExecute(ArrayList<Movie> movies) {
            super.onPostExecute(movies);
            ListView listView = findViewById(R.id.searchResultsList);
            listView.setAdapter(new SearchResultsAdapter(SearchResultsActivity.this, movies));
        }
    }
}