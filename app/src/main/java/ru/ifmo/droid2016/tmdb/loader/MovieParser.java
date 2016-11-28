package ru.ifmo.droid2016.tmdb.loader;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ru.ifmo.droid2016.tmdb.model.Movie;

/**
 * Created by Anya on 21.11.2016.
 */

public class MovieParser {

    @NonNull
    public static List<Movie> parseMovies(JSONObject json) throws
            IOException,
            JSONException,
            BadResponseException {

        final JSONArray resultJsons = json.getJSONArray("results");
        ArrayList<Movie> movies = new ArrayList<>();

        for (int i = 0; i < resultJsons.length(); i++) {
            JSONObject movieJson = resultJsons.optJSONObject(i);

            if (movieJson != null) {
                final String posterPath = movieJson.optString("poster_path", null);
                final String originalTitle = movieJson.optString("original_title", null);
                final String overview = movieJson.optString("overview", null);
                final String title = movieJson.optString("title", null);

                if (!TextUtils.isEmpty(posterPath) && !TextUtils.isEmpty(originalTitle)) {
                    movies.add(new Movie(posterPath, originalTitle, overview, title));
                }
            }

        }
        return movies;
    }

    private MovieParser() {}
}