package ru.ifmo.droid2016.tmdb.api;

import android.net.Uri;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Методы для работы с The Movie DB API
 * <p>
 * https://www.themoviedb.org/documentation/api
 */
public final class TmdbApi {

    private static final String KEY_STRING = "api_key";
    private static final String LANG_STRING = "language";
    private static final String PAGE_STRING = "page";
    private static final String API_KEY = "337d9080b2406af38c0789cdb93dc9da";

    private static final Uri BASE_URI = Uri.parse("https://api.themoviedb.org/3");


    private TmdbApi() {
    }

    /**
     * Возвращает {@link HttpURLConnection} для выполнения запроса популярных фильмов
     * <p>
     * https://developers.themoviedb.org/3/movies/get-popular-movies
     *
     * @param lang язык пользователя
     */
    public static HttpURLConnection getPopularMoviesRequest(String lang) throws IOException {
        Uri.Builder builder = BASE_URI.buildUpon();
        builder.appendEncodedPath("movie/popular").appendQueryParameter(KEY_STRING, API_KEY)
                .appendQueryParameter(LANG_STRING, lang)
                .appendQueryParameter(PAGE_STRING, "1");
        URL url = new URL(builder.build().toString());
        return (HttpURLConnection) url.openConnection();
    }
}
