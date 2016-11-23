package ru.ifmo.droid2016.tmdb;

import android.util.JsonReader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import ru.ifmo.droid2016.tmdb.model.Movie;
import ru.ifmo.droid2016.tmdb.utils.IOUtils;

public class Parser {
    public static List<Movie> parse(InputStream in) throws IOException, JSONException {
        String parsed = IOUtils.readToString(in, "UTF-8");
        JSONObject json = new JSONObject(parsed);
        JSONArray results = json.getJSONArray("results");
        ArrayList<Movie> movies = new ArrayList<>();
        for (int i = 0; i < results.length(); i++) {
            JSONObject movie = results.getJSONObject(i);
            movies.add(new Movie(
                    movie.getString("poster_path"),
                    movie.getString("original_title"),
                    movie.optString("overview"),
                    movie.optString("title"),
                    movie.getDouble("vote_average")
            ));
        }
        return movies;
    }
}
