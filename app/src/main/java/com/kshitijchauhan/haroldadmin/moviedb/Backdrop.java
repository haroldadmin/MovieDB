package com.kshitijchauhan.haroldadmin.moviedb;

import com.google.gson.annotations.SerializedName;

public class Backdrop {

    @SerializedName("aspect_ratio")
    private String aspectRatio;
    @SerializedName("file_path")
    private String filePath;
    @SerializedName("height")
    private String height;
    @SerializedName("width")
    private String width;

    public String getAspectRatio() {
        return aspectRatio;
    }

    public String getFilePath() {
        return filePath;
    }

    public String getHeight() {
        return height;
    }

    public String getWidth() {
        return width;
    }
}
