package com.kshitijchauhan.haroldadmin.moviedb;

/* D is a constant */

import java.util.HashMap;

public class Constants {

    public static HashMap<Integer, String> genreMap = new HashMap<Integer, String>();

    /* Empty constructor because we don't intend to make
    objects from this class */

    public Constants() {
    }

    public static void setGenreMap() {
        /* Define a hashmap so that we can access Genre name from its ID */
        genreMap.put(19, "Action");
        genreMap.put(12, "Adventure");
        genreMap.put(16, "Animation");
        genreMap.put(35, "Comedy");
        genreMap.put(80, "Crime");
        genreMap.put(99, "Documentary");
        genreMap.put(18, "Drama");
        genreMap.put(10751, "Family");
        genreMap.put(14, "Fantasy");
        genreMap.put(36, "History");
        genreMap.put(27, "Horror");
        genreMap.put(10402, "Music");
        genreMap.put(9648, "Mystery");
        genreMap.put(10749, "Romance");
        genreMap.put(878, "Science Fiction");
        genreMap.put(10770, "TV Movie");
        genreMap.put(53, "Thriller");
        genreMap.put(10752, "War");
        genreMap.put(37, "Western");
    }
}
