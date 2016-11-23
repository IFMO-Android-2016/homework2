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
 * Created by 1 on 22/11/16.
 */

public class JSONParser {
    public static List<Movie> parseMovies(InputStream in) throws IOException, JSONException, BadResponseException {
        final String content = IOUtils.readToString(in, "UTF-8");
        final JSONObject json = new JSONObject(content);
        return parseMovies(json);
    }

    private static List<Movie> parseMovies(JSONObject json) throws IOException, JSONException, BadResponseException {
        final JSONArray moviesJSON = json.getJSONArray("results");
        final ArrayList<Movie> movies = new ArrayList<Movie>();

        for (int i = 0; i < moviesJSON.length(); i++) {
            final JSONObject currentMovie = moviesJSON.optJSONObject(i);
            if (currentMovie != null) {
                final String currentPosterPath = currentMovie.optString("poster_path", null);
                final String currentOriginalTitle = currentMovie.optString("original_title", null);
                final String currentOverview = currentMovie.optString("overview", null);
                final String currentTitle = currentMovie.optString("title", null);

                if (!(currentPosterPath == null) && !(currentOriginalTitle == null) && !(currentOverview == null) &&
                        !(currentTitle == null)){
                    final Movie movie = new Movie(currentPosterPath, currentOriginalTitle,
                            currentOverview, currentTitle);
                    movies.add(movie);
                }
            }
        }
        return movies;
    }
}
