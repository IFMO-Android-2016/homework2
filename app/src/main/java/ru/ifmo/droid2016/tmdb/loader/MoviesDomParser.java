package ru.ifmo.droid2016.tmdb.loader;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import ru.ifmo.droid2016.tmdb.model.Movie;
import ru.ifmo.droid2016.tmdb.utils.IOUtils;

/**
 * Created by AdminPC on 20.11.2016.
 */

public final class MoviesDomParser {

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
    public static List<Movie> parseMovies(JSONObject json) throws
            IOException,
            JSONException,
            BadResponseException {
/*
        final String status = json.getString("status");
        if (!"OK".equals(status)) {
            throw new BadResponseException("Unexpected response status from API: " + status);
        }
*/
        final JSONArray moviesJson = json.getJSONArray("results");
        final ArrayList<Movie> movies = new ArrayList<>();

        for (int i = 0; i < moviesJson.length(); i++) {
            final JSONObject movieJson = moviesJson.optJSONObject(i);

            if (movieJson != null) {
                final String posterPath = movieJson.optString("poster_path", "");
                final String originalTitle = movieJson.optString("original_title", "");
                final String overviewText = movieJson.optString("overview", "");
                final String localizedTitle = movieJson.optString("title", "");

                final Movie movie = new Movie(posterPath, originalTitle, overviewText, localizedTitle);
                movies.add(movie);
            }
        }
        return movies;
    }

    private MoviesDomParser() {}
}
