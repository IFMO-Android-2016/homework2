package ru.ifmo.droid2016.tmdb.api;

import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Locale;

/**
 * Методы для работы с The Movie DB API
 *
 * https://www.themoviedb.org/documentation/api
 */
public final class TmdbApi {

    private static final String API_KEY = "142821898b593e3bf34f2fc3aed34880";

    private static final Uri BASE_URI = Uri.parse("https://api.themoviedb.org/3");

    private TmdbApi() {}

    /**
     * Возвращает {@link HttpURLConnection} для выполнения запроса популярных фильмов
     *
     * https://developers.themoviedb.org/3/movies/get-popular-movies
     *
     * @param lang язык пользователя
     */
    public static HttpURLConnection getPopularMoviesRequest(String lang) throws IOException {

        Uri uri = Uri.parse(BASE_URI.toString()).buildUpon()
                .appendPath("movie")
                .appendPath("popular")
                .appendQueryParameter("api_key", API_KEY)
                .appendQueryParameter("language", lang).build();

        Log.d("api", uri.toString());

        HttpURLConnection connection = (HttpURLConnection) new URL(uri.toString()).openConnection();
        return connection;
    }
}
