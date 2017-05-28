package ru.ifmo.droid2016.tmdb.utils;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ru.ifmo.droid2016.tmdb.model.Movie;

/**
 * Created by Ivan-PC on 07.12.2016.
 */

public class JsonParser {

    private static final String LOG_TAG = JsonParser.class.getSimpleName();

    public static List<Movie> parse(String strJson) throws JSONException {
        ArrayList<Movie> answer = new ArrayList<>();
        JSONObject data = new JSONObject(strJson);
        JSONArray result = data.getJSONArray("results");


        for (int i = 0; i < result.length(); ++i) {
            JSONObject cur = result.getJSONObject(i);
            answer.add(new Movie(cur.getString("poster_path"), cur.getString("original_title"),
                    cur.getString("overview"), cur.getString("title")));
        }
        return answer;
    }
}
