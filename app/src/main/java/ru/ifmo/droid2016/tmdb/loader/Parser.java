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

    public static List<Movie> parseMovie(InputStream in) throws
            IOException,
            JSONException,
            BadResponseException {

        final String content = IOUtils.readToString(in, "UTF-8");
        final JSONObject json = new JSONObject(content);
        return parseMovie(json);
    }

    @NonNull
    private static List<Movie> parseMovie(JSONObject json) throws
            IOException,
            JSONException,
            BadResponseException {
        List<Movie> ans = new ArrayList<>();
        JSONArray arr = json.getJSONArray("results");
        for (int i = 0; i < arr.length(); i++) {
            JSONObject mov = arr.optJSONObject(i);
            if (mov == null)
                continue;
            ans.add(new Movie(mov.optString("poster_path"),
                    mov.optString("original_title"),
                    mov.optString("overview"),
                    mov.optString("title")));
        }
        return ans;
    }
}
