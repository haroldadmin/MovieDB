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

    String query = "";
    private static final String LOG_TAG = SearchResultsActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);

        // Get the intent, verify the action and get the query
        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            query = intent.getStringExtra(SearchManager.QUERY);
//            ArrayList<Movie> searchResults = QueryUtils.getSearchResults("Dunkirk");
        }
        query = "Dunkirk"; // Hardcoded as of now for debugging purposes
        // TODO remove the hardcoding here.

        new getSearchResultsTask().execute();
    }

    private class getSearchResultsTask extends AsyncTask<String, Void, ArrayList<Movie>> {
        @Override
        protected ArrayList<Movie> doInBackground(String... strings) {
            ArrayList<Movie> searchResults = QueryUtils.getSearchResults(query);
            return searchResults;
        }

        @Override
        protected void onPostExecute(ArrayList<Movie> movies) {
            ListView listView = findViewById(R.id.searchResultsList);
            listView.setAdapter(new SearchResultsAdapter(SearchResultsActivity.this, movies));
        }
    }
}