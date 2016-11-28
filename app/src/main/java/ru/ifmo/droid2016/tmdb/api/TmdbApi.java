package ru.ifmo.droid2016.tmdb.api;

import android.net.Uri;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Методы для работы с The Movie DB API
 * <p/>
 * https://www.themoviedb.org/documentation/api
 */
public final class TmdbApi {

    private static final String API_KEY = "e58d2b7c6c07252f6aa52391f397b065";

    private static final String BASE_URI = "https://api.themoviedb.org/3";

    private TmdbApi() {
    }

    /**
     * Возвращает {@link HttpURLConnection} для выполнения запроса популярных фильмов
     * <p/>
     * https://developers.themoviedb.org/3/movies/get-popular-movies
     *
     * @param lang язык пользователя
     */
    public static HttpURLConnection getPopularMoviesRequest(String lang) throws IOException {
        Uri uri = Uri.parse(BASE_URI).buildUpon().appendPath("movie")
                .appendPath("popular")
                .appendQueryParameter("api_key", API_KEY)
                .appendQueryParameter("language", lang)
//                .appendQueryParameter("page", Integer.toString(pageNum));
                .appendQueryParameter("page", "1") //firstly loading only first page
                .build();

        return (HttpURLConnection) new URL(uri.toString()).openConnection();
    }
}
