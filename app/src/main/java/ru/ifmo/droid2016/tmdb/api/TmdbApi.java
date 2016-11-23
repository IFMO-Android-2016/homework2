package ru.ifmo.droid2016.tmdb.api;

import android.net.Uri;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.Locale;

/**
 * Методы для работы с The Movie DB API
 *
 * https://www.themoviedb.org/documentation/api
 */
public final class TmdbApi {

    private static final String API_KEY = "dd2ae0cae737045f34389f6f45af8a34";

    private static final Uri BASE_URI = Uri.parse("https://api.themoviedb.org/3/movie/popular");

    private TmdbApi() {}

    /**
     * Возвращает {@link HttpURLConnection} для выполнения запроса популярных фильмов
     *
     * https://developers.themoviedb.org/3/movies/get-popular-movies
     *
     * @param lang язык пользователя
     */

    public static HttpURLConnection getPopularMoviesRequest(String lang) throws IOException {
        Uri uri = Uri.parse(String.valueOf(BASE_URI)).buildUpon()
                .appendQueryParameter("api_key", API_KEY)
                .appendQueryParameter("language", lang)
                .build();
        return (HttpURLConnection) new URL(uri.toString()).openConnection();
    }
}
