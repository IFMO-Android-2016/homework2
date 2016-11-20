package ru.ifmo.droid2016.tmdb.loader;

import android.support.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import ru.ifmo.droid2016.tmdb.model.Movie;
import ru.ifmo.droid2016.tmdb.utils.IOUtils;

final class MoviesDomParser {
    @NonNull
    static List<Movie> parseMovies(InputStream in) throws
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
        final List<Movie> movies = new ArrayList<>();

        for (int i = 0; i < moviesJson.length(); i++) {
            final JSONObject movieJson = moviesJson.optJSONObject(i);

            if (movieJson != null) {
                final int id = movieJson.optInt("id", 0);
                final String posterPath = movieJson.optString("poster_path", "");
                final String originalTitle = movieJson.optString("original_title", "");
                final String overviewText = movieJson.optString("overview", "");
                final String localizedTitle = movieJson.optString("title", "");
                final double rating = movieJson.optDouble("vote_average", 0.0);

                final Movie movie = new Movie(id, posterPath, originalTitle, overviewText, localizedTitle, rating);
                movies.add(movie);
            }
        }
        return movies;
    }

    private MoviesDomParser() {}
}
