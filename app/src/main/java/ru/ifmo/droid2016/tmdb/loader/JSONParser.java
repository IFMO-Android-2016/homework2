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

public class JSONParser {
    public static List<Movie> parse(InputStream in) throws
            IOException,
            JSONException,
            BadResponseException {

        final String content = IOUtils.readToString(in, "UTF-8");
        final JSONObject json = new JSONObject(content);
        return parse(json);
    }

    private static List<Movie> parse(JSONObject json) throws
            IOException,
            JSONException,
            BadResponseException {

        final JSONArray resultJson = json.getJSONArray("results");
        final List<Movie> movies = new ArrayList<>();

        for (int i = 0; i < resultJson.length(); i++) {
            final JSONObject movieJson = resultJson.optJSONObject(i);

            if (movieJson != null) {
                movies.add(new Movie(
                        "http://image.tmdb.org/t/p/w396" + movieJson.optString("poster_path"),
                        movieJson.optString("original_title"),
                        movieJson.optString("overview"),
                        movieJson.optString("title")
                ));
            }
        }
        return movies;
    }
}