//package ru.ifmo.droid2016.tmdb.api;
//
//import android.net.Uri;
//
//import java.io.IOException;
//import java.net.HttpURLConnection;
//import java.net.URL;
//
///**
// * Методы для работы с The Movie DB API
// * <p>
// * https://www.themoviedb.org/documentation/api
// */
//public final class TmdbApi {
//
//    // TODO: Зарегистрироваться на https://www.themoviedb.org и получить свой собственный ключ
//    //private static final String API_KEY = "96448cea301eab7ac49816d3a97df152";
//    private static final String API_KEY = "1c6badbb9b491a4b9ec35b180d7a7a25";
//
//    //private static final Uri BASE_URI = Uri.parse("https://api.themoviedbT.org/3");
//
//    private static final Uri BASE_URI = Uri.parse("https://api.themoviedb.org/3");
//    private TmdbApi() {
//    }
//
//    /**
//     * Возвращает {@link HttpURLConnection} для выполнения запроса популярных фильмов
//     * <p>
//     * https://developers.themoviedb.org/3/movies/get-popular-movies
//     *
//     * @param lang язык пользователя
//     */
//    public static HttpURLConnection getPopularMoviesRequest(String lang) throws IOException {
//
//
//        Uri uri = Uri.parse("https://api.themoviedb.org/3").buildUpon()
//                .appendPath("movie")
//                .appendPath("popular")
//                .appendQueryParameter("language", lang)
//                .appendQueryParameter("api_key", API_KEY)
//                .build();
//
//        return (HttpURLConnection) new URL(BASE_URI.toString()).openConnection();
//    }
//}
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

    private static final String API_KEY = "ee1c42c80c58d28bc54efc844b63d114";
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
        // TODO
        Uri uri = Uri.parse("https://api.themoviedb.org/3").buildUpon()
                .appendPath("movie")
                .appendPath("popular")
                .appendQueryParameter("language", lang)
                .appendQueryParameter("api_key", API_KEY)
                .appendQueryParameter("page", "1")
                .build();
        return (HttpURLConnection) new URL(uri.toString()).openConnection();
    }
}
