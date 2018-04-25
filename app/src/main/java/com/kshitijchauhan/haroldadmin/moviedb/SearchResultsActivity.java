package com.kshitijchauhan.haroldadmin.moviedb;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.support.v7.widget.Toolbar;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Toast;

import java.util.ArrayList;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SearchResultsActivity extends AppCompatActivity {

    String query = "";
    private static final String LOG_TAG = SearchResultsActivity.class.getName();
    public static final String BASE_URL = "https://api.themoviedb.org/3/";
    ProgressDialog progress;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;


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

        mRecyclerView = findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);


        Toolbar searchResultsActivityToolbar = findViewById(R.id.search_results_activity_toolbar);
        setSupportActionBar(searchResultsActivityToolbar);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Search Results");


        progress = new ProgressDialog(this);
        progress.setTitle("Loading");
        progress.setMessage("Getting search results...");
        progress.setCancelable(false); // disable dismiss by tapping outside of the dialog
        progress.show();

        TMDbClient client = ServiceGenerator.createService(TMDbClient.class);

        Call<SearchResponse> searchResponseCall = client.getSearchResults(BuildConfig.TMDb_API_KEY, query);

        searchResponseCall.enqueue(new Callback<SearchResponse>() {
            @Override
            public void onResponse(Call<SearchResponse> call, Response<SearchResponse> response) {
                if (response.isSuccessful() && response.body().getSearchResults() != null) {
                    ArrayList<Movie> searchResults = response.body().getSearchResults();
                    if (searchResults.size() != 0) {
                        SearchResultsAdapter searchResultsAdapter = new SearchResultsAdapter(SearchResultsActivity.this, searchResults);
                        mRecyclerView.setAdapter(searchResultsAdapter);
                        int resId = R.anim.layout_animation_fall_down;
                        LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(SearchResultsActivity.this, resId);
                        mRecyclerView.setLayoutAnimation(animation);
                        progress.dismiss();
                    }
                    else {
                        progress.dismiss();
                        Toast.makeText(SearchResultsActivity.this, "No search results found", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<SearchResponse> call, Throwable t) {
                progress.dismiss();
                Toast.makeText(SearchResultsActivity.this, "Error retrieving search results", Toast.LENGTH_SHORT).show();
                Log.e(LOG_TAG, "Could not retrieve search results using retrofit");
            }
        });
    }
}