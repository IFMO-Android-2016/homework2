package ru.ifmo.droid2016.tmdb.api;

import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Методы для работы с The Movie DB API
 *
 * https://www.themoviedb.org/documentation/api
 */
public final class TmdbApi {

    private static final String API_KEY = "b78da094eb1abf8d9f81f079cd1d229a";

    private static final Uri BASE_URI = Uri.parse("https://api.themoviedb.org/3");
    private static final String TAG = TmdbApi.class.getSimpleName();


    private TmdbApi() {}

    /**
     * Возвращает {@link HttpURLConnection} для выполнения запроса популярных фильмов
     *
     * https://developers.themoviedb.org/3/movies/get-popular-movies
     *
     * @param lang язык пользователя
     */
    public static HttpURLConnection getPopularMoviesRequest(String lang, long page) throws IOException {
        Uri.Builder uri = BASE_URI.buildUpon()
                .appendEncodedPath("movie/popular")
                .appendQueryParameter("api_key", API_KEY)
                .appendQueryParameter("language", lang)
                .appendQueryParameter("page", String.valueOf(page));
        Log.i(TAG, uri.toString());
        return (HttpURLConnection) new URL(uri.toString()).openConnection();
    }
}
