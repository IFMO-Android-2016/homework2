package io.github.d1v1nation.tmdb.api;

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
    private static final String API_KEY = "e68c1489c2540e3edc348bb9a932039c";

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

        Uri uri = (BASE_URI).buildUpon()
                .appendPath("movie")
                .appendPath("popular")
                .appendQueryParameter("api_key", API_KEY)
                .appendQueryParameter("language", lang)
                .build();
        return (HttpURLConnection) new URL(uri.toString()).openConnection();
    }

    public static HttpURLConnection getPopularMoviesRequest(String lang, int page) throws IOException {
        Uri uri = (BASE_URI).buildUpon()
                .appendPath("movie")
                .appendPath("popular")
                .appendQueryParameter("api_key", API_KEY)
                .appendQueryParameter("language", lang)
                .appendQueryParameter("page", Integer.toString(page))
                .build();
        return (HttpURLConnection) new URL(uri.toString()).openConnection();
    }
}
