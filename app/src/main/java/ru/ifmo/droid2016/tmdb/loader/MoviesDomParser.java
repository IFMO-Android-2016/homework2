package ru.ifmo.droid2016.tmdb.loader;

import android.support.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import ru.ifmo.droid2016.tmdb.model.Movie;
import ru.ifmo.droid2016.tmdb.utils.IOUtils;

import static ru.ifmo.droid2016.tmdb.utils.Constants.IMAGE_BASE_URL;

/**
 * 12 of November
 */

class MoviesDomParser {
    @NonNull
    static ArrayList<Movie> parseMovies(InputStream in) throws JSONException, IOException {

        ArrayList<Movie> ans = new ArrayList<>();
        final String content = IOUtils.readToString(in, "UTF-8");
        final JSONObject json = new JSONObject(content);

        final JSONArray results = json.getJSONArray("results");

        for (int i = 0; i < results.length(); i++) {
            JSONObject result = results.getJSONObject(i);
            Movie m = new Movie(
                    IMAGE_BASE_URL + result.getString("poster_path"),
                    result.getString("overview"),
                    result.getString("title"));
            ans.add(m);
        }

        return ans;

    }
}
