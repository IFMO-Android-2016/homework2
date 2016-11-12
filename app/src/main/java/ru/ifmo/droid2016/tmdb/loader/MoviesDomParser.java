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
import java.util.Collections;
import java.util.List;

import ru.ifmo.droid2016.tmdb.model.Movie;
import ru.ifmo.droid2016.tmdb.utils.IOUtils;


/**
 * Created by Mr.Fiskerton on 012 12.11.16.
 */

public class MoviesDomParser {
    @NonNull
    public static List<Movie> parseMovies(InputStream in) throws
            IOException,
            JSONException,
            BadResponseException {

        final String content = IOUtils.readToString(in, "UTF-8");
        final JSONObject json = new JSONObject(content);
        return parseWebcams(json);
    }

    @NonNull
    private static List<Movie> parseWebcams(JSONObject json) throws
            IOException,
            JSONException,
            BadResponseException {

        final String status = json.getString("status");
        if (!"OK" .equals(status)) {
            throw new BadResponseException("Unexpected response status from API: " + status);
        }

        final JSONObject resultJson = json.getJSONObject("result");
        final JSONArray moviesJson = resultJson.getJSONArray("webcams");
        final ArrayList<Movie> movies = new ArrayList<>();

        for (int i = 0; i < moviesJson.length(); i++) {
            final JSONObject movieJson = moviesJson.optJSONObject(i);

            if (movieJson != null) {
                final String posterPath = movieJson.optString("poster_path", null);
                final String originalTitel = movieJson.optString("original_title", null);
                final String overwiewText = movieJson.optString("overview", null);
                final String localizedTitle = movieJson.optString("title", null);

                if (!TextUtils.isEmpty(posterPath) && !TextUtils.isEmpty(originalTitel)
                        && !TextUtils.isEmpty(overwiewText) && !TextUtils.isEmpty(localizedTitle)) {
                    final Movie movie = new Movie(posterPath, originalTitel, overwiewText, localizedTitle);
                    movies.add(movie);
                }
            }
        }
        return movies;
    }

    @Nullable
    private static String getImageUrlFromJsonByType(JSONObject imageJson, String imageType)
            throws IOException, JSONException {
        JSONObject images = imageJson.optJSONObject(imageType);
        if (images != null) {
            return images.optString(PREFERRED_IMAGE_SIZE, null);
        }
        return null;
    }

    private static final String PREFERRED_IMAGE_SIZE = "preview";

    private MoviesDomParser() {}
}
