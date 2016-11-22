package ru.ifmo.droid2016.tmdb.api;

import android.net.Uri;
import android.util.Log;

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
    private static final String API_KEY = "d0c1849f24d8225541440f2517c5a260";

    private static final Uri BASE_URI = Uri.parse("https://api.themoviedb.org/3");
    //private static final Uri IMAGE_URI = Uri.parse("http://image.tmdb.org/t/p/w500");

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
        Log.d("getPopuparMovies", lang);
        String result = BASE_URI.buildUpon().appendPath("movie")
                .appendPath("popular").appendQueryParameter("page", "1")
                .appendQueryParameter("api_key", API_KEY)
                .appendQueryParameter("language", lang).toString();
        return (HttpURLConnection) new URL(result).openConnection();
    }
}
