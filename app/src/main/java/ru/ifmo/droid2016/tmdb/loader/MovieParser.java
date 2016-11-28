package ru.ifmo.droid2016.tmdb.loader;

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
 * Created by roman on 13.11.16.
 */

public class MovieParser {
    public static List<Movie> parseMovies(InputStream in) throws IOException, JSONException, BadResponseException {
        final String content = IOUtils.readToString(in, "UTF-8");
        final JSONObject json = new JSONObject(content);
        return parseMovies(json);
    }

    private static List<Movie> parseMovies(JSONObject json) throws IOException, JSONException, BadResponseException {
        final JSONArray moviesJSON = json.getJSONArray("results");
        final ArrayList<Movie> movies = new ArrayList<>();

        for (int i = 0; i < moviesJSON.length(); i++) {
            final JSONObject movieJSON = moviesJSON.getJSONObject(i);

            if (movieJSON != null) {
                final String posterPath = movieJSON.optString("poster_path");
                final String originalTitleText = movieJSON.optString("original_title");
                final String titleText = movieJSON.optString("title");
                final String overviewText = movieJSON.optString("overview");

                if (!TextUtils.isEmpty(overviewText) && !TextUtils.isEmpty(titleText) && !TextUtils.isEmpty(posterPath) && !TextUtils.isEmpty(originalTitleText)) {
                    final Movie movie = new Movie(posterPath, originalTitleText, overviewText, titleText);
                    movies.add(movie);
                }
            }
        }
        return movies;
    }
    private MovieParser () {}
}
