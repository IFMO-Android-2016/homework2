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

public class Parser {
    static List<Movie> parse(InputStream in) throws IOException, JSONException, BadResponseException{
        final JSONObject json = new JSONObject(IOUtils.readToString(in, "UTF-8"));
        return parse(json);
    }

    private static List<Movie> parse(JSONObject json) throws IOException, JSONException, BadResponseException {
        final List<Movie> movies = new ArrayList<>();
        final JSONArray jsonMovies = json.getJSONArray("results");
        for (int i = 0; i < jsonMovies.length(); i++) {
            JSONObject movie = jsonMovies.optJSONObject(i);
            if (movie != null) {
                movies.add(new Movie(
                        "https://image.tmdb.org/t/p/w780" + movie.optString("poster_path", null),
                        movie.optString("original_title", null),
                        movie.optString("overview", null),
                        movie.optString("title", null)
                ));
            }
        }
        return movies;
    }
}
