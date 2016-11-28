package ru.ifmo.droid2016.tmdb.api;

import android.net.Uri;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Locale;

/**
 * Методы для работы с The Movie DB API
 * <p>
 * https://www.themoviedb.org/documentation/api
 */
public final class TmdbApi {
    private static final String API_KEY = "7ceb601dab82aeb68b85458eeb2cca93";

    private static final Uri BASE_URI = Uri.parse("https://api.themoviedb.org/3");

    public static final String IMAGE_URI = "http://image.tmdb.org/t/p/w500";

    private TmdbApi() {
    }

    /**
     * Возвращает {@link HttpURLConnection} для выполнения запроса популярных фильмов
     * <p>
     * https://developers.themoviedb.org/3/movies/get-popular-movies
     *
     * @param lang язык пользователя
     */
    public static HttpURLConnection getPopularMoviesRequest(String lang, int page) throws IOException {
        // TODO
        Uri uri = BASE_URI.buildUpon()
                .appendEncodedPath("movie/popular")
                .appendQueryParameter("api_key", API_KEY)
                .appendQueryParameter("language", lang)
                .appendQueryParameter("page", String.valueOf(page)).build();
        return (HttpURLConnection) new URL(uri.toString()).openConnection();
    }
}
