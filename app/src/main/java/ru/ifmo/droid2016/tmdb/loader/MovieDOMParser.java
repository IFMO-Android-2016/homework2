package ru.ifmo.droid2016.tmdb.loader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOError;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import ru.ifmo.droid2016.tmdb.model.Movie;
import ru.ifmo.droid2016.tmdb.utils.IOUtils;

/**
 * Created by Игорь on 19.11.2016.
 */

public final class MovieDOMParser {
    public static List<Movie> parseMovies(InputStream in) throws
            IOException,
            JSONException,
            BadResponseException {
        final String content = IOUtils.readToString(in, "UTF-8");
        final JSONObject json = new JSONObject(content);
        return parseMovies(json);
    }

    private static List<Movie> parseMovies(JSONObject json) throws
            JSONException {
        final JSONArray resultsJson = json.getJSONArray("results");
        final ArrayList<Movie> resultArray = new ArrayList<Movie>(20);
        for (int i = 0; i < resultsJson.length(); ++i) {
            JSONObject movieJson = resultsJson.getJSONObject(i);
            final String posterPath = movieJson.getString("poster_path");
            final String originalTitle = movieJson.getString("original_title");
            final String overviewText = movieJson.getString("overview");
            final String localizedTitle = movieJson.getString("title");
            resultArray.add(new Movie(posterPath, originalTitle, overviewText, localizedTitle));
        }

        return resultArray;
    }
}
