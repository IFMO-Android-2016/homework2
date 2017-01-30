package ru.ifmo.droid2016.tmdb.api;

import android.net.Uri;
import android.util.Log;

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
    private static final String API_KEY = "f8bbf92401f5af3ed3ad4b117eaacee3";
    private static final String BASE = "https://api.themoviedb.org/3";
    private static final Uri BASE_URI = Uri.parse(BASE);
    private static final String TAG = TmdbApi.class.getName();


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
        StringJoiner request = new StringJoiner(BASE + "/movie/popular?", "&", "");
        request.append("api_key=", API_KEY);
        request.append("language=", lang);
        request.append("page=", "1"); // TODO: 29.01.2017 load more pages
        Log.d(TAG, "getPopularMoviesRequest: request " + request);
        return (HttpURLConnection) new URL(request.toString()).openConnection();
    }
}

class StringJoiner {
    private final String delimiter;
    private final String postfix;

    private StringBuilder builder;
    private boolean first = true;

    StringJoiner(String prefix, String delimiter, String postfix) {
        this.delimiter = delimiter;
        this.postfix = postfix;

        builder = new StringBuilder(prefix);
    }

    void append(String... strings) {
        addDelimiter();
        for(String str : strings) {
            builder.append(str);
        }
    }

    @Override
    public String toString() {
        builder.append(postfix);
        return builder.toString();
    }

    private void addDelimiter() {
        if (!first) {
            builder.append(delimiter);
        }
        first = false;
    }

}

