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

    private static final String API_KEY = "api_key=944d85daa5ec28af8aee2b1971e143ec";
    private static final String LANG = "language=";
    private static final String PAGE = "page=1";

    private static final String BASE = "https://api.themoviedb.org/3/movie/popular?";


    private TmdbApi() {}

    /**
     * Возвращает {@link HttpURLConnection} для выполнения запроса популярных фильмов
     *
     * https://developers.themoviedb.org/3/movies/get-popular-movies
     *
     * @param lang язык пользователя
     */
    public static HttpURLConnection getPopularMoviesRequest(String lang) throws IOException {

        return (HttpURLConnection) new URL(Uri.parse(BASE + API_KEY + "&" + LANG + lang + "&" + PAGE)
                                              .toString())
                                              .openConnection();
    }

}
