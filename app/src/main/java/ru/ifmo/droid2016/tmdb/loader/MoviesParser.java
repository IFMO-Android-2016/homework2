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

/**
 * Created by Nik on 08.03.2017.
 */

class MoviesParser {
    private static final String IMAGES_URL_PREFIX = "https://image.tmdb.org/t/p/w1000";
    static List<Movie> parseMovies(InputStream in) throws IOException, JSONException {
        final String content = IOUtils.readToString(in, "UTF-8");
        return parseMovies(new JSONObject(content));
    }
    private static List<Movie> parseMovies(JSONObject json) throws JSONException {
        JSONArray moviesJSON = json.getJSONArray("results");
        ArrayList<Movie> movies = new ArrayList<>();
        for (int i = 0; i < moviesJSON.length(); ++i) {
            JSONObject movieJSON = moviesJSON.getJSONObject(i);

            if (movieJSON != null) {
                String posterPath = IMAGES_URL_PREFIX + movieJSON.optString("poster_path", null);
                String originalTitle = movieJSON.optString("original_title", null);
                String title = movieJSON.optString("title", null);
                String overview = movieJSON.optString("overview", null);
                if (posterPath != null && originalTitle != null) {
                    movies.add(new Movie(posterPath, originalTitle, overview, title));
                }
            }
        }
        return movies;
    }

    private MoviesParser() {}
}
