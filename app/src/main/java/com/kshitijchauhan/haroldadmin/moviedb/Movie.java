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
    private String releaseDate;
    private String parentalRating;
    private String runtime;
    private String genre;
    private String writer;
    private String[] cast;
    private String description;
    private String IMDbrating;
    private String posterURL;
    private String website;
    private Bitmap poster;
    private String imdbID;

    private static final String LOG_TAG = Movie.class.getName();

    /*Creating an empty constructor because there's no way to know what movie details might be missing for any given movie
    This constructor allows the creatipn of a movie with no details given. Setter methods can be used to add as much movie
    information as is possible.
     */
    public Movie() {

    }

    /*This constructor is mostly useless.
    Probably won't use this.
     */
    public Movie(String name, String director, String year, String releaseDate,
                 String parentalRating, String runtime, String genre, String writer,
                 String[] cast, String description, String IMDbrating,
                 String posterURL, Bitmap poster, String website, String IMDbID) {
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
        this.IMDbrating = IMDbrating;
        this.posterURL = posterURL;
        this.poster = poster;
        this.website = website;
        this.imdbID = IMDbID;
    }

    /*This constructor is useful while creating movie search results.
     */
    public Movie (String name, String year, Bitmap poster, String imdbID) {
        this.name = name;
        this.year = year;
        this.poster = poster;
        this.imdbID = imdbID;
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

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getParentalRating() {
        return parentalRating;
    }

    public void setParentalRating(String parentalRating) {
        this.parentalRating = parentalRating;
    }

    public String getRuntime() {
        return runtime;
    }

    public void setRuntime(String runtime) {
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

    public String getIMDbRating() {
        return IMDbrating;
    }

    public void setRatings(String IMDbrating) {
        this.IMDbrating = IMDbrating;
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

    public void setImdbID(String ID) {
        this.imdbID = ID;
    }

    public String getImdbID() {
        return imdbID;
    }

}
