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

public class Parser {

    @NonNull
    public static List<Movie> parse(InputStream is) throws JSONException, IOException {
        final String content = IOUtils.readToString(is, "UTF-8");
        final JSONObject json = new JSONObject(content);
        return parseMovie(json);
    }

    @NonNull
    private static List<Movie> parseMovie(JSONObject json) throws JSONException {
        List<Movie> ret = new ArrayList<>();
        JSONArray res = json.getJSONArray("results");
        for (int i = 0; i < res.length(); i++) {
            JSONObject obj = res.optJSONObject(i);
            if (obj != null) {
                ret.add(new Movie(obj.optString("poster_path"),
                        obj.optString("original_title"),
                        obj.optString("overview"),
                        obj.optString("title")));
            }
        }
        return ret;
    }

}


