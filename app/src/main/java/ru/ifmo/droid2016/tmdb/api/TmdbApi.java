package ru.ifmo.droid2016.tmdb.api;

import android.net.Uri;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public final class TmdbApi {
    private TmdbApi() {}

    private static final String API_KEY = "0abbed08db8b698d848e265f69101a45";
    private static final Uri BASE_URI = Uri.parse("https://api.themoviedb.org/3");

    public static HttpURLConnection getPopularMoviesRequest(String lang, int page) throws IOException {
        Uri uri = BASE_URI.buildUpon()
                .appendPath("movie")
                .appendPath("popular")
                .appendQueryParameter("api_key", API_KEY)
                .appendQueryParameter("language", lang)
                .appendQueryParameter("page", String.valueOf(page))
                .build();
        return (HttpURLConnection) new URL(uri.toString()).openConnection();
    }
}