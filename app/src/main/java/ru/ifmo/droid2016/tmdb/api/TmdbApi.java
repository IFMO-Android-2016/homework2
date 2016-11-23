package ru.ifmo.droid2016.tmdb.api;

import android.net.Uri;
import android.util.Log;

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

    // TODO: Зарегистрироваться на https://www.themoviedb.org и получить свой собственный ключ
    private static final String API_KEY = "0dd081bb0beeca553d888d084acb4f87";

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
    public static HttpURLConnection getPopularMoviesRequest(String lang, int page) throws IOException {
        Log.d("Films", lang);
        Uri uri = BASE_URI.buildUpon()
                .appendPath("movie")
                .appendPath("popular")
                .appendQueryParameter("api_key", API_KEY)
                .appendQueryParameter("language", lang)
                .appendQueryParameter("page", Integer.toString(page))
                .build();
        Log.d("MyLog", uri.toString());
        return (HttpURLConnection) new URL(uri.toString()).openConnection();
    }
}
