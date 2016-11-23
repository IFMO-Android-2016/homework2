package ru.ifmo.droid2016.tmdb.api;

import android.net.Uri;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public final class TmdbApi {
    private static final String API_KEY = "d6b3db55bfb61cb0b244928457d05fa0";
    private static final Uri BASE_URI = Uri.parse("https://api.themoviedb.org/3");

    private TmdbApi() {}

    public static HttpURLConnection getPopularMoviesRequest(String lang, String page) throws IOException {
        Uri uri = Uri.parse(String.valueOf(BASE_URI)).buildUpon()
                .appendPath("movie")
                .appendPath("popular")
                .appendQueryParameter("api_key", API_KEY)
                .appendQueryParameter("language", lang)
                .appendQueryParameter("page", page)
                .build();
        return (HttpURLConnection) new URL(uri.toString()).openConnection();
    }
}
