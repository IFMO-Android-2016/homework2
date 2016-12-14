package ru.ifmo.droid2016.tmdb.loader;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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


public class TmbParser {
    @NonNull
    public static List<Movie> parseTmb(InputStream in) throws
            IOException,
            JSONException,
            BadResponseException {

        final String content = IOUtils.readToString(in, "UTF-8");
        final JSONObject json = new JSONObject(content);
        return parseMovies(json);
    }

    @NonNull
    private static List<Movie> parseMovies(JSONObject json) throws
            IOException,
            JSONException,
            BadResponseException {
//
//        final String status = json.getString("status");
//        if (!"OK".equals(status)) {
//            throw new BadResponseException("Unexpected response status from API: " + status);
//        }
        ArrayList<Movie> movies = new ArrayList<>();


        final JSONArray movieJson = json.getJSONArray("results");

        for (int i = 0; i < movieJson.length(); i++) {
            JSONObject movieJsoneObj = movieJson.optJSONObject(i);
            if (movieJsoneObj != null) {

                final String posterPath = movieJsoneObj.optString("poster_path", null);
                final String originalTitle = movieJsoneObj.optString("original_title", null);
                final String overviewText = movieJsoneObj.optString("overview", null);
                final String localizedTitle = movieJsoneObj.optString("title", null);

                if (!TextUtils.isEmpty(posterPath) && !TextUtils.isEmpty(originalTitle)) {
                    movies.add(new Movie(posterPath, originalTitle, overviewText, localizedTitle));
                }
            }

        }
        return movies;
    }

    private TmbParser() {}
}
