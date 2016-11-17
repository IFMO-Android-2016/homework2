package ru.ifmo.droid2016.tmdb.loader;

import android.support.annotation.NonNull;
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
 * Created by Lenovo on 09.11.2016.
 */

public class MoviePosterParser {

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
            final JSONObject currentMovie = moviesJson.optJSONObject(i);

            if (currentMovie != null) {
                final String originalTitle = currentMovie.optString("original_title", null);
                final String localizedTitle = currentMovie.optString("title", null);
                final String overviewText = currentMovie.optString("overview", null);
                final String posterPath = currentMovie.optString("poster_path", null);

                if (!TextUtils.isEmpty(originalTitle) &&
                        !TextUtils.isEmpty(localizedTitle) &&
                        !TextUtils.isEmpty(overviewText) &&
                        !TextUtils.isEmpty(posterPath)) {
                    movies.add(new Movie(posterPath, originalTitle, overviewText, localizedTitle));
                }
            }

        }

        return movies;
    }

}
