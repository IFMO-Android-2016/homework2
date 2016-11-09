package ru.ifmo.droid2016.tmdb.api;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.net.Uri;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;


import javax.net.ssl.HttpsURLConnection;

import ru.ifmo.droid2016.tmdb.utils.IOUtils;

import static java.util.Locale.US;

/**
 * Методы для работы с The Movie DB API
 *
 * https://www.themoviedb.org/documentation/api
 */
public final class TmdbApi {

    // DONE: Зарегистрироваться на https://www.themoviedb.org и получить свой собственный ключ
    private static final String API_KEY = "31bf565a67cbfa5e1daeec55085b720f";


    private static final Uri BASE_URI = Uri.parse("https://api.themoviedb.org/3");


    private TmdbApi() {}

    /**
     * Возвращает {@link HttpURLConnection} для выполнения запроса популярных фильмов
     *
     * https://developers.themoviedb.org/3/movies/get-popular-movies
     *
     * @param lang язык пользователя
     */
    public static HttpsURLConnection getPopularMoviesRequest(int pageId, String lang) throws IOException {
        // DONE
        String uri = "@@@";

        try {
            uri = BASE_URI + "/movie/popular" + "?api_key=" + API_KEY + "&language=" + lang + "&page=" + String.valueOf(pageId);

        } catch (Exception ex) {
            Log.d("???", ex.toString());
        }

        Log.d("final_uri", uri);

        return (HttpsURLConnection) new URL(uri).openConnection();
    }

}
