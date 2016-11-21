package ru.ifmo.droid2016.tmdb.myapplication.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;

public class ResponseParser {
    public static List<Movie> parser(JSONObject response) throws JSONException {
        JSONArray results = response.getJSONArray("results");
        List<Movie> parsed = new LinkedList<>();
        for (int i = 0; i < results.length(); i++){
            JSONObject movieJSON = results.getJSONObject(i);
            Movie movie = new Movie(movieJSON.getString("poster_path"),
                    movieJSON.getString("original_title"),
                    movieJSON.getString("overview"),
                    movieJSON.getString("title"));
            parsed.add(movie);
        }
        return parsed;
    }
}
