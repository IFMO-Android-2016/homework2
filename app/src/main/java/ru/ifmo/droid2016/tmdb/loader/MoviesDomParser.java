package ru.ifmo.droid2016.tmdb.loader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import ru.ifmo.droid2016.tmdb.model.Movie;
import ru.ifmo.droid2016.tmdb.utils.IOUtils;

/**
 * Created by Антон on 21.11.2016.
 */

public final class MoviesDomParser {
    static List<Movie> parseMovies(InputStream in) throws
            IOException,
            JSONException,
            BadResponseException {

        final String content = IOUtils.readToString(in, "UTF-8");
        final JSONObject json = new JSONObject(content);
        return parseMovies(json);
    }

    private static List<Movie> parseMovies(JSONObject json) throws
            IOException,
            JSONException,
            BadResponseException {

        final List<Movie> movies = new ArrayList<>();
        final JSONArray moviesJsonArray = json.getJSONArray("results");

        for (int i = 0; i < moviesJsonArray.length(); i++) {
            final JSONObject movieJson = moviesJsonArray.optJSONObject(i);

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

    private MoviesDomParser() {};
}
