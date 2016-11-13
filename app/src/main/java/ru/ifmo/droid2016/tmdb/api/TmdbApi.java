package ru.ifmo.droid2016.tmdb.api;

import android.net.Uri;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/**
 * Методы для работы с The Movie DB API
 * <p>
 * https://www.themoviedb.org/documentation/api
 */
public final class TmdbApi {

    private static final String API_KEY = "f13114b3b7333c8b9469542f2152b829";

    private static final String BASE_URL = "https://api.themoviedb.org/3";


    /**
     * Возвращает {@link HttpURLConnection} для выполнения запроса популярных фильмов
     * <p>
     * https://developers.themoviedb.org/3/movies/get-popular-movies
     *
     *
     */


    public static HttpsURLConnection getPopularMoviesRequest(String lang, int page) throws IOException {
        Uri uri = Uri.parse(BASE_URL).buildUpon()
                .appendPath("movie")
                .appendEncodedPath("popular")
                .appendQueryParameter("api_key", API_KEY)
                .appendQueryParameter("language", lang)
                .appendQueryParameter("page", String.valueOf(page))
                .build();
        return (HttpsURLConnection) new URL(uri.toString()).openConnection();
    }
}
