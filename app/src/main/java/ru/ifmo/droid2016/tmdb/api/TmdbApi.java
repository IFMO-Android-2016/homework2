package ru.ifmo.droid2016.tmdb.api;

import android.net.Uri;

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

    private static final String API_KEY = "65ec3131ffb031ba80a03ea69ba82c1a";

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
        // TODO
        Uri request = BASE_URI.buildUpon()
                .appendPath("movie").appendPath("popular")
                .appendQueryParameter("api_key", API_KEY).appendQueryParameter("language", lang).appendQueryParameter("page", "1")
                .build();
        return (HttpURLConnection) new URL(request.toString()).openConnection();
    }
}
