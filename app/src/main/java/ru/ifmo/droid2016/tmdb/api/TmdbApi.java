package ru.ifmo.droid2016.tmdb.api;

import android.net.Uri;
import android.util.JsonReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import ru.ifmo.droid2016.tmdb.loader.LoadResult;
import ru.ifmo.droid2016.tmdb.loader.ResultType;
import ru.ifmo.droid2016.tmdb.model.Movie;

import ru.ifmo.droid2016.tmdb.utils.IOUtils;

/**
 * Методы для работы с The Movie DB API
 *
 * https://www.themoviedb.org/documentation/api
 */
public final class TmdbApi {


    private static final String API_KEY = "64b39a13026000129e40f6be905b8a48";

    private static final Uri BASE_URI = Uri.parse("https://api.themoviedb.org/3");


    private TmdbApi() {}

    /**
     * Возвращает {@link HttpURLConnection} для выполнения запроса популярных фильмов
     *
     * https://developers.themoviedb.org/3/movies/get-popular-movies
     *
     * @param lang язык пользователя
     */
    public static HttpURLConnection getPopularMoviesRequest(String lang) throws IOException {
        String ulrRequest = BASE_URI.buildUpon().appendPath("3").appendPath("movies").encodedPath("get-popular-movies")
                .appendQueryParameter("api_key", API_KEY).appendQueryParameter("language", lang).toString();
        return (HttpURLConnection) new URL(ulrRequest).openConnection();
    }


}
