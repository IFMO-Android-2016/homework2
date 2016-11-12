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
    private static final String API_KEY = "5e889f8c4ea4b8fb644dd96a1c5a589c";
    private static final Uri BASE_URI = Uri.parse("https://api.themoviedb.org/3");
    private static final String KEY_HEADER_NAME = "api_key";

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
        Uri uri = BASE_URI.buildUpon()
                .appendPath("movie")
                .appendPath("popular")
                .appendEncodedPath("page=" + page)
                //.appendEncodedPath("language=" + lang)//TODO: Проверить работаспособность
                .appendQueryParameter("language", lang)
                .build();
        HttpURLConnection connection = (HttpURLConnection) new URL(uri.toString()).openConnection();
        connection.setRequestProperty(KEY_HEADER_NAME, API_KEY);
        return connection;
    }

    public  static HttpURLConnection getPopularMoviesRequest (String lang) throws  IOException {
        return getPopularMoviesRequest(lang, 1);
    }
}
