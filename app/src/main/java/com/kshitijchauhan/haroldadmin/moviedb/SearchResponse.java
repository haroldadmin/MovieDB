package com.kshitijchauhan.haroldadmin.moviedb;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class SearchResponse {

    @SerializedName("page")
    private int page;
    @SerializedName("total_results")
    private int totalResults;
    @SerializedName("total_pages")
    private int total_pages;
    @SerializedName("results")
    ArrayList<Movie> searchResults = new ArrayList<Movie>();

    public int getPage() {
        return page;
    }

    public int getTotalResults() {
        return totalResults;
    }

    public int getTotal_pages() {
        return total_pages;
    }

    public ArrayList<Movie> getSearchResults() {
        return searchResults;
    }
}
