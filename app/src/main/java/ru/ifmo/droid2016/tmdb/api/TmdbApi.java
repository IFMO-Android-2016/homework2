package ru.ifmo.droid2016.tmdb.api;

import android.net.Uri;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.InterfaceAddress;
import java.net.URL;

/**
 * Методы для работы с The Movie DB API
 *
 * https://www.themoviedb.org/documentation/api
 */
public final class TmdbApi {

    // TODO: Зарегистрироваться на https://www.themoviedb.org и получить свой собственный ключ
    private static final String API_KEY = "ccadf933b396d4dbc803d1d17c32448b";

    private static final Uri BASE_URI = Uri.parse("https://api.themoviedb.org/3");


    private TmdbApi() {}

    /**
     * Возвращает {@link HttpURLConnection} для выполнения запроса популярных фильмов
     *
     * https://developers.themoviedb.org/3/movies/get-popular-movies
     *
     * @param lang язык пользователя
     */
    public static HttpURLConnection getPopularMoviesRequest(String lang, int page) throws IOException {
        // TODO
        Uri uri = BASE_URI
                .buildUpon()
                .appendPath("movie")
                .appendPath("popular")
                .appendQueryParameter("api_key", API_KEY)
                .appendQueryParameter("language", lang)
                .appendQueryParameter("page", Integer.toString(page))
                .build();
        return (HttpURLConnection) new URL(uri.toString()).openConnection();
    }
}
