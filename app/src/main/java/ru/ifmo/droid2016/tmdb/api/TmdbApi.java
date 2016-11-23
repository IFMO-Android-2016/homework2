package ru.ifmo.droid2016.tmdb.api;

import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Методы для работы с The Movie DB API
 *
 * https://www.themoviedb.org/documentation/api
 */
public final class TmdbApi {

    // TODO: Зарегистрироваться на https://www.themoviedb.org и получить свой собственный ключ
    private static final String API_KEY = "3f2af85ecd781cece1350be3204ca359";

    private static final Uri BASE_URI = Uri.parse("https://api.themoviedb.org/3/movie/popular");
    private static final String BASE_SIZE = "w500";
    private static final Uri BASE_IMAGE_URI = Uri.parse("http://image.tmdb.org/t/p/");


    private TmdbApi() {}

    /**
     * Возвращает {@link HttpURLConnection} для выполнения запроса популярных фильмов
     *
     * https://developers.themoviedb.org/3/movies/get-popular-movies
     *
     * @param lang язык пользователяw1280
     */
    public static HttpURLConnection getPopularMoviesRequest(String lang) throws IOException {
        // TODO
        return (HttpURLConnection) new URL(BASE_URI.toString() + "?api_key=" + API_KEY + "&language=" + lang).openConnection();
    }

    public static HttpURLConnection getImage(String key) throws IOException {
        return (HttpURLConnection) new URL(BASE_IMAGE_URI.toString() + BASE_SIZE + key).openConnection();
    }
}
