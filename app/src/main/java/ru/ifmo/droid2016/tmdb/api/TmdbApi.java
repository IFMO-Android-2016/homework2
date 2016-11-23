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

    // TODO: Зарегистрироваться на https://www.themoviedb.org и получить свой собственный ключ
    private static final String API_KEY = "04abd37d8c32ea90b13eea3071a488e3";

    private static final Uri BASE_URI = Uri.parse("https://api.themoviedb.org/3");

    /**
     * Возвращает {@link HttpURLConnection} для выполнения запроса популярных фильмов
     *
     * https://developers.themoviedb.org/3/movies/get-popular-movies
     *
     * @param lang язык пользователя
     */
    public static HttpURLConnection getPopularMoviesRequest(int page, String lang) throws IOException {
        // TODO
        Uri uri = BASE_URI.buildUpon().appendPath("movie").appendPath("popular")
                .appendQueryParameter("api_key", API_KEY)
                .appendQueryParameter("language", lang)
                .appendQueryParameter("page", String.valueOf(page))
                .build();
        return (HttpURLConnection) new URL(uri.toString()).openConnection();

    }

    private TmdbApi() {}
}
