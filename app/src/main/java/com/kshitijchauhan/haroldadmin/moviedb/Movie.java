package com.kshitijchauhan.haroldadmin.moviedb;

import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Date;

public class Movie {

    @SerializedName("adult")
    private String adult;
    @SerializedName("budget")
    private String budget;
    @SerializedName("genres")
    private List<Genre> genres;
    @SerializedName("homepage")
    private String homepage;
    @SerializedName("id")
    private String id;
    @SerializedName("imdb_id")
    private String imdbID;
    @SerializedName("original_language")
    private String originalLanguage;
    @SerializedName("original_title")
    private String originalTitle;
    @SerializedName("overview")
    private String overview;
    @SerializedName("poster_path")
    private String posterPath;
    @SerializedName("release_date")
    private String releaseDate;
    @SerializedName("revenue")
    private String revenue;
    @SerializedName("runtime")
    private String runtime;
    @SerializedName("status")
    private String status;
    @SerializedName("title")
    private String title;
    @SerializedName("vote_average")
    private double rating;
    @SerializedName("tagline")
    private String tagline;

    private String genre;

    public String getTagline() {
        return tagline;
    }

    public String getAdult() {
        return adult;
    }

    public String getBudget() {
        return budget;
    }

    public List<Genre> getGenres() {
        return genres;
    }

    public String getHomepage() {
        return homepage;
    }

    public String getId() {
        return id;
    }

    public String getImdbID() {
        return imdbID;
    }

    public String getOriginalLanguage() {
        return originalLanguage;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public String getOverview() {
        return overview;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public String getRevenue() {
        return revenue;
    }

    public String getRuntime() {
        return runtime;
    }

    public String getStatus() {
        return status;
    }

    public String getTitle() {
        return title;
    }

    public String getGenre() {
        return genre;
    }

    public double getRating() {
        return rating;
    }

    public Movie(String id,
                 String title,
                 String originalLanguage,
                 int genreID,
                 String overview,
                 String releaseDate) {
        this.id = id;
        this.title = title;
        this.originalLanguage = originalLanguage;
        this.genre = QueryUtils.getGenreFromId(genreID);
        this.overview = overview;
        this.releaseDate = releaseDate;


    }
}
