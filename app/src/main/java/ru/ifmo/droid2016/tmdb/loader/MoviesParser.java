package ru.ifmo.droid2016.tmdb.loader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import ru.ifmo.droid2016.tmdb.model.Movie;
import ru.ifmo.droid2016.tmdb.utils.IOUtils;

class MoviesParser {
    static List<Movie> parseMovies(InputStream in) throws JSONException, IOException {
        final String content = IOUtils.readToString(in, "UTF-8");
        final JSONObject json = new JSONObject(content);
        return parseMovies(json);
    }

    private static List<Movie> parseMovies(JSONObject json) throws JSONException {
        final JSONArray moviesJson = json.getJSONArray("results");
        final ArrayList<Movie> movies = new ArrayList<>();

        for (int i = 0; i < moviesJson.length(); i++) {
            final JSONObject movieJson = moviesJson.optJSONObject(i);

            if (movieJson != null) {

                movies.add(new Movie(
                        "https://image.tmdb.org/t/p/w780" + movieJson.optString("poster_path", null),
                        movieJson.optString("original_title", null),
                        movieJson.optString("overview", null),
                        movieJson.optString("title", null)
                ));
            }
        }

        return movies;
    }
}
