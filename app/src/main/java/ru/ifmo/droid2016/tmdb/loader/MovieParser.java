package ru.ifmo.droid2016.tmdb.loader;

/**
 * Created by xRoms on 20.11.2016.
 */
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ru.ifmo.droid2016.tmdb.model.Movie;
import ru.ifmo.droid2016.tmdb.utils.IOUtils;

/**
 * Методы для парсинга ответов от Webcams API при помощи JSONObject (DOM parser)
 */
public final class MovieParser {

    @NonNull
    public static List<Movie> parseMovies(InputStream in) throws
            IOException,
            JSONException,
            BadResponseException {

        final String content = IOUtils.readToString(in, "UTF-8");
        final JSONObject json = new JSONObject(content);
        return parseMovies(json);
    }

    @NonNull
    private static List<Movie> parseMovies(JSONObject json) throws
            IOException,
            JSONException,
            BadResponseException {

        final JSONArray moviesJson = json.getJSONArray("results");
        final ArrayList<Movie> movies = new ArrayList<>();

        for (int i = 0; i < moviesJson.length(); i++) {
            final JSONObject movieJson = moviesJson.optJSONObject(i);
            if (movieJson != null) {
                final String localtitle = movieJson.optString("title", null);
                final String originaltitle = movieJson.optString("original_title", null);
                final String description = movieJson.optString("overview", null);
                final String imageUrl = "https://image.tmdb.org/t/p/w780/" + movieJson.optString("poster_path", null);
                final Movie movie = new Movie(imageUrl, originaltitle, description, localtitle);
                movies.add(movie);
            }
        }
        return movies;
    }

    private MovieParser() {}
}

