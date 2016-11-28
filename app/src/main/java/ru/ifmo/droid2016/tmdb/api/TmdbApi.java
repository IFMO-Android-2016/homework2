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
    //private static final String API_KEY = "ee1c42c80c58d28bc54efc844b63d114";
    private static final String API_KEY = "6920a828bf4889a34e7e13399f6f6129";

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
        Uri uri = Uri.parse("https://api.themoviedb.org/3").buildUpon()
                .appendPath("movie")
                .appendPath("popular")
                .appendQueryParameter("language", lang)
                .appendQueryParameter("api_key", API_KEY)
                .build();
        return (HttpURLConnection) new URL(uri.toString()).openConnection();
    }
}
