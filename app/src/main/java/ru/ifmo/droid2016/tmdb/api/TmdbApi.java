package ru.ifmo.droid2016.tmdb.api;

import android.net.Uri;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public final class TmdbApi {

    private static final String API_KEY = "0189ba9326ec9db281e0ffb30cbd4e80";

    private static final Uri BASE_URI = Uri.parse("https://api.themoviedb.org/3");


    private TmdbApi() {}

    public static HttpURLConnection getPopularMoviesRequest(String lang) throws IOException {
        Uri uri = BASE_URI.buildUpon()
                .appendPath("movie")
                .appendPath("popular")
                .appendQueryParameter("api_key", API_KEY)
                .appendQueryParameter("language", lang)
                .appendQueryParameter("page", "1")
                .build();
        return (HttpURLConnection) new URL(uri.toString()).openConnection();
    }
}
