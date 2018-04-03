package com.kshitijchauhan.haroldadmin.moviedb;

import android.app.SearchManager;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Intent intent = new Intent(this, SearchResultsActivity.class);
        intent.putExtra(SearchManager.QUERY, "Dunkirk");
        startActivity(intent);
    }
}
