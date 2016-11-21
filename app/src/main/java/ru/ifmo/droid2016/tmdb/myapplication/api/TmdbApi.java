package ru.ifmo.droid2016.tmdb.myapplication.api;

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

    private static final String API_KEY = "3cef8287205e6c4eae20a34de9c43dd9";

    private static final Uri BASE_URI = Uri.parse("https://api.themoviedb.org/3/movie/popular");


    private TmdbApi() {}

    /**
     * Возвращает {@link HttpURLConnection} для выполнения запроса популярных фильмов
     *
     * https://developers.themoviedb.org/3/movies/get-popular-movies
     *
     * @param lang язык пользователя
     */
    public static HttpURLConnection getPopularMoviesRequest(String lang, int page) throws IOException {
        URL requestURL = new URL(BASE_URI.toString() + "?api_key=" + API_KEY + "&language=" + lang + "&page=" + page);
        HttpURLConnection request = (HttpURLConnection) requestURL.openConnection();
        request.setRequestMethod("GET");
        return request;
    }
}
