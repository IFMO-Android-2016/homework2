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

/**
 * Created by Kirill Antonov on 22.11.2016.
 */

public class AnswerParser {
    @NonNull
    public static List<Movie> parseAnswer(InputStream in) throws
            IOException,
            JSONException,
            BadResponseException {

        final String content = IOUtils.readToString(in, "UTF-8");
        final JSONObject json = new JSONObject(content);
        return parseAnswer(json);
    }

    @NonNull
    private static List<Movie> parseAnswer(JSONObject json) throws
            IOException,
            JSONException,
            BadResponseException {

        final JSONArray moviesJson = json.getJSONArray("results");
        final ArrayList<Movie> movies = new ArrayList<>();

        for (int i = 0; i < moviesJson.length(); i++) {
            final JSONObject movieJson = moviesJson.optJSONObject(i);

            if (movieJson != null) {
                movies.add(
                        new Movie("https://image.tmdb.org/t/p/w500".concat(movieJson.optString("poster_path")),
                        movieJson.optString("original_title"),
                        movieJson.optString("overview"),
                        movieJson.optString("title")));
            }
        }
        return movies;
    }
}
