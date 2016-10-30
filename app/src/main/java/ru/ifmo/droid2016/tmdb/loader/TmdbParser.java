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
 * Created by -- on 30.10.2016.
 */

public class TmdbParser {
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
                final String posterPath = movieJson.optString("poster_path", null);
                final String originalTitle = movieJson.optString("original_title", null);
                final String overviewText = movieJson.optString("overview", null);
                final String localizedTitle = movieJson.optString("title", null);

                if (!TextUtils.isEmpty(posterPath) && !TextUtils.isEmpty(originalTitle)
                        && !TextUtils.isEmpty(overviewText) && !TextUtils.isEmpty(localizedTitle)) {
                    final Movie movie = new Movie(posterPath, originalTitle, overviewText, localizedTitle);
                    movies.add(movie);
                }
            }
        }
        return movies;
    }

    private TmdbParser() {}
}
