package ru.ifmo.droid2016.tmdb.api;

import android.net.Uri;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Методы для работы с The Movie DB API
 *
 * https://www.themoviedb.org/documentation/api
 */
public final class TmdbApi {

    private static final String API_KEY = "d1d5a619fc3860c4620842092890e8a1";

    private static final Uri BASE_URI = Uri.parse("https://api.themoviedb.org/3");


    private TmdbApi() {}

    /**
     * Возвращает {@link HttpURLConnection} для выполнения запроса популярных фильмов
     *
     * https://developers.themoviedb.org/3/movies/get-popular-movies
     *
     * @param lang язык пользователя
     */
    public static HttpURLConnection getPopularMoviesRequest(String lang, int num) throws IOException {
        // TODO
        Uri uri = BASE_URI.buildUpon()
                .appendPath("movie")
                .appendPath("popular")
                .appendQueryParameter("api_key", API_KEY)
                .appendQueryParameter("language", lang)
                .appendQueryParameter("page", num + "")
                .build();
        return (HttpURLConnection) new URL(uri.toString()).openConnection();
    }
}
