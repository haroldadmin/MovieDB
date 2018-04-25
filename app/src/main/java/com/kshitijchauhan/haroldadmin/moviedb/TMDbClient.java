package com.kshitijchauhan.haroldadmin.moviedb;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface TMDbClient {

    @GET("search/movie")
    Call<SearchResponse> getSearchResults(@Query("api_key") String api_key,
                                       @Query("query") String query);

    @GET("movie/{id}")
    Call<Movie> getMovieDetails(@Path("id") String movieID,
                                @Query("api_key") String api_key);

    @GET("movie/{id}/images")
    Call<ImagesResponse> getMovieImages(@Path("id") String movieID,
                                        @Query("api_key") String api_key);

}
