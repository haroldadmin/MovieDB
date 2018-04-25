package com.kshitijchauhan.haroldadmin.moviedb;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServiceGenerator {

    private static final String BASE_URL = "https://api.themoviedb.org/3/";

    private static GsonBuilder gsonBuilder = new GsonBuilder()
            .setDateFormat("YYYY");
    private static Gson gson = gsonBuilder.create();

    private static Retrofit.Builder builder = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson));

    private static HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);

    private static OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();

    private static Retrofit retrofit = builder.build();

    public static <S> S createService(Class<S> serviceClass) {

        if (!httpClientBuilder.interceptors().contains(loggingInterceptor)) {
            httpClientBuilder.addInterceptor(loggingInterceptor);
            builder = builder.client(httpClientBuilder.build());
            retrofit = builder.build();
        }
        return retrofit.create(serviceClass);
    }
}
