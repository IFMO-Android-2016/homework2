package ru.ifmo.droid2016.tmdb.loader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import ru.ifmo.droid2016.tmdb.PopularMoviesActivity;
import ru.ifmo.droid2016.tmdb.model.Movie;
import ru.ifmo.droid2016.tmdb.utils.IOUtils;

/**
 * Created by vadim on 21.11.16.
 */

public final class MovieJSONParser {

    static List<Movie> parseMovies(InputStream in) throws
            IOException,
            JSONException,
            BadResponseException {

        String string = IOUtils.readToString(in, "UTF-8");
        IOUtils.readAndCloseSilently(in);

        JSONObject json = new JSONObject(string);
        return parseMovies(json);
    }

    static List<Movie> parseMovies(JSONObject json) throws
            IOException,
            JSONException,
            BadResponseException {
        List<Movie> data = new ArrayList<>();
        JSONArray movies = json.getJSONArray("results");
        for (int i = 0; i < movies.length(); ++i) {
            JSONObject current = movies.getJSONObject(i);
            Movie newMovie = new Movie(current.getString("poster_path"),
                    current.getString("original_title"),
                    current.getString("overview"),
                    current.getString("title"));
            data.add(newMovie);
        }
        return data;
    }

}
