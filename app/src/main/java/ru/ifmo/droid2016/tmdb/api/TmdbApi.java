package ru.ifmo.droid2016.tmdb.api;

import android.net.Uri;

import java.io.IOException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import static ru.ifmo.droid2016.tmdb.utils.Constants.API_KEY;
import static ru.ifmo.droid2016.tmdb.utils.Constants.BASE_URL;

public final class TmdbApi {

    public static HttpsURLConnection getPopularMoviesRequest(String lang, int page) throws IOException {
        Uri uri = Uri.parse(BASE_URL).buildUpon()
                .appendPath("movie")
                .appendEncodedPath("popular")
                .appendQueryParameter("api_key", API_KEY)
                .appendQueryParameter("language", lang)
                .appendQueryParameter("page", String.valueOf(page))
                .build();
        return (HttpsURLConnection) new URL(uri.toString()).openConnection();
    }
}
