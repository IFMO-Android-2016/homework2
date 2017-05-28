package ru.ifmo.droid2016.tmdb.loader;

import android.support.annotation.NonNull;
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
 * Created by danilskarupin on 22.11.16.
 */

public class MovieParser {
    @NonNull
    public static List<Movie> parse(InputStream in) throws
            IOException,
            JSONException,
            BadResponseException {

        final String content = IOUtils.readToString(in, "UTF-8");
        final JSONObject json = new JSONObject(content);
        return parse(json);
    }

    @NonNull
    private static List<Movie> parse(JSONObject json) throws
            IOException,
            JSONException,
            BadResponseException {

//        final String status = json.getString("status");
//        if (!"OK".equals(status)) {
//            throw new BadResponseException("Unexpected response status from API: " + status);
//        }

        final JSONArray moviesJson = json.getJSONArray("results");
        final ArrayList<Movie> movies = new ArrayList<>();

        for (int i = 0; i < moviesJson.length(); i++) {
            final JSONObject movieJson = moviesJson.optJSONObject(i);

            if (moviesJson != null) {
                final String posterPath = movieJson.optString("poster_path", null);
                final String originalTitle = movieJson.optString("original_title", null);
                final String overviewText = movieJson.optString("overview", null);
                final String localizedTitle = movieJson.optString("title", null);
                final String releaseDate = movieJson.optString("release_date", null);

                if (!TextUtils.isEmpty(posterPath) && !TextUtils.isEmpty(originalTitle)) {
                    final Movie movie = new Movie(posterPath, originalTitle, overviewText, localizedTitle, releaseDate);
                    movies.add(movie);
                }
            }
        }
        return movies;
    }
}
