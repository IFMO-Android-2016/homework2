package ru.ifmo.droid2016.tmdb.api;

import android.net.Uri;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Методы для работы с The Movie DB API
 * <p>
 * https://www.themoviedb.org/documentation/api
 */
public final class TmdbApi {

    // TODO: Зарегистрироваться на https://www.themoviedb.org и получить свой собственный ключ
    private static final String API_KEY = "501e6fcc454ce0eea1a1ca7f18158712";
    private static final String KEY_HEADER_NAME = "api_key";
    private static final Uri BASE_URI = Uri.parse("https://api.themoviedb.org/3");


    private TmdbApi() {
    }

    /**
     * Возвращает {@link HttpURLConnection} для выполнения запроса популярных фильмов
     * <p>
     * https://developers.themoviedb.org/3/movies/get-popular-movies
     *
     * @param lang язык пользователя
     */

    public static HttpURLConnection getPopularMoviesRequest(String lang, String page) throws
            IOException {
        // TODO

        Uri uri = BASE_URI.buildUpon()
                .appendPath("movie")
                .appendEncodedPath("popular")
                .appendQueryParameter(KEY_HEADER_NAME, API_KEY)
                .appendQueryParameter("language", lang)
                .appendQueryParameter("page", page)
                .build();

        return (HttpURLConnection) new URL(uri.toString()).openConnection();
    }
}
