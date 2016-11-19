package ru.ifmo.droid2016.tmdb.api;

import android.net.Uri;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import ru.ifmo.droid2016.tmdb.model.Movie;

/**
 * Методы для работы с The Movie DB API
 *
 * https://www.themoviedb.org/documentation/api
 */
public final class TmdbApi {

    private static final String API_KEY = "f6492abe899e95aaf29851bf8a8121d7";

    private static final Uri BASE_URI = Uri.parse("https://api.themoviedb.org/3");
    private static final Uri IMAGE_URI = Uri.parse("http://image.tmdb.org/t/p/w500");

    private TmdbApi() {}

    /**
     * Возвращает {@link HttpURLConnection} для выполнения запроса популярных фильмов
     *
     * https://developers.themoviedb.org/3/movies/get-popular-movies
     *
     * @param lang язык пользователя
     */
    public static HttpURLConnection getPopularMoviesRequest(String lang, int page) throws IOException {
//        System.out.println("getPopularMoviesRequest");
        String request = BASE_URI.buildUpon()
                .appendPath("movie").appendPath("popular")
                .appendQueryParameter("api_key", API_KEY)
                .appendQueryParameter("language", lang)
                .appendQueryParameter("page", "" + (page + 1))
                .toString();

        return (HttpURLConnection) new URL(request).openConnection();
    }

    public static Uri getPosterUri(Movie movie) {
        return IMAGE_URI.buildUpon()
                .appendPath(movie.posterPath.substring(1)).build();
    }
}
