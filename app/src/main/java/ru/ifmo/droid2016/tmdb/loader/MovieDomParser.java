package ru.ifmo.droid2016.tmdb.loader;

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
 * Created by Andrey on 20.11.2016.
 */

public final class MovieDomParser {


    public static List<Movie> parseMovies(InputStream in) throws
            IOException,
            JSONException,
            BadResponseException {
        final String content = IOUtils.readToString(in, "UTF-8");
        final JSONObject json = new JSONObject(content);
        return parseMovies(json);

    }


    private static List<Movie> parseMovies(JSONObject json) throws
            IOException,
            JSONException,
            BadResponseException {

/*
        final String status = json.getString("status");
        if(!"OK".equals(status)){
            throw new BadResponseException("Bad response: " + status);
        }
*/


        final JSONArray moviesJson = json.getJSONArray("results");
        final List<Movie> movies = new ArrayList<>();
        for (int i = 0; i < moviesJson.length(); i++) {
            final JSONObject movieJson = moviesJson.optJSONObject(i);
            if (movieJson != null) {
                final String originalTitle = movieJson.optString("original_title");
                final String localizedTitle = movieJson.optString("title");
                final String overviewText = movieJson.optString("overview");
                String posterPath = movieJson.optString("poster_path");

                if (!TextUtils.isEmpty(originalTitle) && !TextUtils.isEmpty(localizedTitle)
                        && !TextUtils.isEmpty(overviewText) && !TextUtils.isEmpty(posterPath)) {
                    final Movie movie = new Movie(posterPath, originalTitle, overviewText,
                            localizedTitle);
                    movies.add(movie);
                }

            }
        }


        return movies;
    }

}
