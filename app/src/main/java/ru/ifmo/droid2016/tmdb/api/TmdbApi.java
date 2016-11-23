package ru.ifmo.droid2016.tmdb.api;

import android.net.Uri;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Locale;

public final class TmdbApi {

    private static final String API_KEY = "62009868f1b751608eed22340d59da62";

    private static final Uri BASE_URI = Uri.parse("https://api.themoviedb.org/3");


    private TmdbApi() {}

    public static HttpURLConnection getPopularMoviesRequest(String lang, int page) throws IOException {
        Uri uri = Uri.parse(String.valueOf(BASE_URI)).buildUpon()
                .appendPath("movie")
                .appendPath("popular")
                .appendQueryParameter("api_key", API_KEY)
                .appendQueryParameter("language", lang)
                .appendQueryParameter("page", String.valueOf(page))
                .build();
        return (HttpURLConnection) new URL(uri.toString()).openConnection();
    }
}
