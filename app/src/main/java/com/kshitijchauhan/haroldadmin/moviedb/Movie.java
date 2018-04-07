package com.kshitijchauhan.haroldadmin.moviedb;

import android.graphics.Bitmap;
import android.util.Log;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;

public class Movie {

    /*This is a class to define a movie object along with its various attributes
     */

    private String name;
    private String director;
    private String year;
    private Date releaseDate;
    private String parentalRating;
    private int runtime;
    private String genre;
    private String writer;
    private String[] cast;
    private String description;
    private HashMap<String, Integer> ratings;
    private String posterURL;
    private String website;
    private Bitmap poster;

    private static final String LOG_TAG = Movie.class.getName();


    public Movie(String name, String director, String year, Date releaseDate,
                 String parentalRating, int runtime, String genre, String writer,
                 String[] cast, String description, HashMap<String, Integer> ratings,
                 String posterURL, Bitmap poster, String website) {
        this.name = name;
        this.director = director;
        this.year = year;
        this.releaseDate = releaseDate;
        this.parentalRating = parentalRating;
        this.runtime = runtime;
        this.genre = genre;
        this.writer = writer;
        this.cast = cast;
        this.description = description;
        this.ratings = ratings;
        this.posterURL = posterURL;
        this.poster = poster;
        this.website = website;
    }

    public Movie (String name, String year, Bitmap poster) {
        this.name = name;
        this.year = year;
        this.poster = poster;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getParentalRating() {
        return parentalRating;
    }

    public void setParentalRating(String parentalRating) {
        this.parentalRating = parentalRating;
    }

    public int getRuntime() {
        return runtime;
    }

    public void setRuntime(int runtime) {
        this.runtime = runtime;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getWriter() {
        return writer;
    }

    public void setWriter(String writer) {
        this.writer = writer;
    }

    public String[] getCast() {
        return cast;
    }

    public void setCast(String[] cast) {
        this.cast = cast;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public HashMap<String, Integer> getRatings() {
        return ratings;
    }

    public void setRatings(HashMap<String, Integer> ratings) {
        this.ratings = ratings;
    }

    public URL getPosterURL() {
        URL url = null;
        try {
            url = new URL(posterURL);
        }
        catch (MalformedURLException e) {
            Log.e(LOG_TAG,"Malformed URL error");
        }
        return url;
    }

    public void setPosterURL(String posterURL) {
        this.posterURL = posterURL;
    }

    public URL getWebsite() {
        URL url = null;
        try {
            url = new URL(posterURL);
        }
        catch (MalformedURLException e) {
            Log.e(LOG_TAG,"Malformed URL error");
        }
        return url;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public Bitmap getPoster() {
        return poster;
    }

    public void setPoster(Bitmap moviePoster) {
        poster = moviePoster;
    }

}
