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
    //private static final String API_KEY = "12c1620d5fd212e4c9de63199d097880";


    private static final String API_KEY = "12c1620d5fd212e4c9de63199d097880";
    private static final Uri BASE_URI = Uri.parse("https://api.themoviedb.org/3");


    private TmdbApi() {}

    /**
     * Возвращает {@link HttpURLConnection} для выполнения запроса популярных фильмов
     *
     * https://developers.themoviedb.org/3/movies/get-popular-movies
     *
     * @param curPage текущая страница
     * @param lang язык пользователя
     */
    public static HttpURLConnection getPopularMoviesRequest(String lang, int curPage) throws IOException {
            Uri uri = Uri.parse(String.valueOf(BASE_URI)).buildUpon().appendPath("movie")
                    .appendPath("popular").appendQueryParameter("api_key", API_KEY).appendQueryParameter("language", lang)
                    .appendQueryParameter("page", String.valueOf(curPage)).build();
        return (HttpURLConnection) new URL(BASE_URI.toString()).openConnection();
    }
}
