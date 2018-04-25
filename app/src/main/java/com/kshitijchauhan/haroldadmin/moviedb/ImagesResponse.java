package com.kshitijchauhan.haroldadmin.moviedb;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ImagesResponse {

    @SerializedName("id")
    private String id;
    @SerializedName("backdrops")
    private List<Backdrop> backdrops;

    public String getId() {
        return id;
    }

    public List<Backdrop> getBackdrops() {
        return backdrops;
    }
}
