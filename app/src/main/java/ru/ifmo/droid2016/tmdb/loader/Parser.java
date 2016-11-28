package ru.ifmo.droid2016.tmdb.loader;

import android.text.TextUtils;

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
 * Created by ivantrofimov on 23.11.16.
 */


public class Parser {
     static final String IMG_URL = "https://image.tmdb.org/t/p/w500";

    public static List<Movie> parseTmdb(InputStream in) throws IOException, JSONException {
        final String content = IOUtils.readToString(in, "UTF-8");
        final JSONObject json = new JSONObject(content);
        return parseMovies(json);
    }

    private static List<Movie> parseMovies(JSONObject json) throws JSONException {
        ArrayList<Movie> movies = new ArrayList<>();
        final JSONArray movieJson = json.getJSONArray("results");
        for (int i = 0; i < movieJson.length(); i++) {
            JSONObject jsonO = movieJson.optJSONObject(i);
            if (jsonO != null) {
                final String posterPath = IMG_URL + jsonO.optString("poster_path");
                final String originalTitle = jsonO.optString("original_title");
                final String overviewText = jsonO.optString("overview");
                final String localizedTitle = jsonO.optString("title");
                movies.add(new Movie(posterPath, originalTitle, overviewText, localizedTitle));
            }
        }
        return movies;
    }

    private Parser() {}
}
