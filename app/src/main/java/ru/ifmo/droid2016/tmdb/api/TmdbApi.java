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

    private static final String API_KEY = "d7b7d04c84b56703d7b81af742fde29a";

    private static final Uri BASE_URI = Uri.parse("https://api.themoviedb.org/3/movie/popular"
            + "?api_key=" + API_KEY + "&language=");
    private static final String LOG_TAG = TmdbApi.class.getSimpleName();


    private TmdbApi() {}

    /**
     * Возвращает {@link HttpURLConnection} для выполнения запроса популярных фильмов
     *
     * https://developers.themoviedb.org/3/movies/get-popular-movies
     *
     * @param lang язык пользователя
     */
    public static HttpURLConnection getPopularMoviesRequest(String lang, int page) throws IOException {
        Log.d(LOG_TAG, BASE_URI.toString() + lang + "&page="
                + Integer.toString(page));
        return (HttpURLConnection) new URL(BASE_URI.toString() + lang + "&page="
                + Integer.toString(page)).openConnection();
    }
}
