package com.kshitijchauhan.haroldadmin.moviedb;

public class Genre {

    private String id;
    private String name;

    public Genre(String name) {
        this.name = name;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
