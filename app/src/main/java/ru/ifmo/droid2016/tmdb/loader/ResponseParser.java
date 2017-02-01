package ru.ifmo.droid2016.tmdb.loader;

import android.text.style.TtsSpan;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import ru.ifmo.droid2016.tmdb.model.Movie;

/**
 * Created by Anton on 29.01.2017.
 */
public class ResponseParser {

    public static ArrayList<Movie> parse(String str) throws JSONException {
        ArrayList<Movie> result = new ArrayList<>();

        JSONObject jsonObject = new JSONObject(str);
        JSONArray jsonMovies = jsonObject.getJSONArray("results");
        for (int i = 0; i < jsonMovies.length(); ++i) {
            JSONObject jsonMovie = jsonMovies.getJSONObject(i);
            result.add(new Movie(jsonMovie));
        }

        return result;
    }
}
