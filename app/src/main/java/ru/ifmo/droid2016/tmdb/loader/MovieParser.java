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
 * Created by Mug on 23.11.2016.
 */

public class MovieParser {
    @NonNull
    public static List<Movie> parseMovies(InputStream in) throws
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

        final JSONArray movieJson = json.getJSONArray("results");
        final ArrayList<Movie> movies = new ArrayList<>();

        for (int i = 0; i < movieJson.length(); i++) {
            final JSONObject movieJsonObj = movieJson.optJSONObject(i);

            if (movieJsonObj != null) {
                final String posterPath = movieJsonObj.optString("poster_path", null);
                final String originalTitle = movieJsonObj.optString("original_title", null);
                final String overviewText = movieJsonObj.optString("overview", null);
                final String localizedTitle = movieJsonObj.optString("title", null);

                if (!TextUtils.isEmpty(posterPath) && !TextUtils.isEmpty(originalTitle)) {
                    final Movie movie = new Movie(posterPath, originalTitle, overviewText, localizedTitle);
                    movies.add(movie);
                }
            }

        }
        return movies;
    }

    private MovieParser() {}
}