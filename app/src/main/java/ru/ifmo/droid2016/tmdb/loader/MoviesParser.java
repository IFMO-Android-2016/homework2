package ru.ifmo.droid2016.tmdb.loader;

import android.net.Uri;

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
 * Created by egorsafronov on 00.11.16
 */

public class MoviesParser {

    private static final String IMAGE_URI = "http://image.tmdb.org/t/p/w640";

    public static List<Movie> parse(InputStream in) throws
            IOException,
            JSONException {
        final String content = IOUtils.readToString(in, "UTF-8");
        final JSONObject json = new JSONObject(content);
        return parse(json);
    }

    private static List<Movie> parse(JSONObject json) throws JSONException {
        final JSONArray moviesJSON = json.getJSONArray("results");
        final ArrayList<Movie> movies = new ArrayList<>();

        for (int i = 0; i < moviesJSON.length(); i++) {
            final JSONObject movieJSON = moviesJSON.optJSONObject(i);
            if (movieJSON != null) {

                movies.add(new Movie(
                        IMAGE_URI + movieJSON.optString("poster_path", null),
                        movieJSON.optString("original_title", null),
                        movieJSON.optString("overview", null),
                        movieJSON.optString("title", null),
                        String.valueOf(movieJSON.optDouble("vote_average", 0.00))
                ));

            }
        }

        return movies;

    }
}
