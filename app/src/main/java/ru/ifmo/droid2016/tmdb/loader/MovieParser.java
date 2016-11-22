package ru.ifmo.droid2016.tmdb.loader;

import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

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
 * Created by YA on 20.11.2016.
 */
public class MovieParser {

    @NonNull
    public static List<Movie> parseMovies (InputStream in) throws
            JSONException, IOException {
        final JSONObject jsonObj = new JSONObject(IOUtils.readToString(in, "UTF-8"));
        final JSONArray jsonArrayOfMovies = jsonObj.getJSONArray("results");
        final ArrayList<Movie> arrayOfMovies = new ArrayList<>();
        for (int ind = 0; ind < jsonArrayOfMovies.length(); ind++) {
            final JSONObject currentMovie = jsonArrayOfMovies.optJSONObject(ind);
            if (currentMovie != null) {
                final String originalTitle = currentMovie.optString("original_title", null);
                final String localizedTitle = currentMovie.optString("title", null);
                final String overviewText = currentMovie.optString("overview", null);
                final String posterPath = currentMovie.optString("poster_path", null);
                if (!TextUtils.isEmpty(originalTitle) &&
                        !TextUtils.isEmpty(localizedTitle) &&
                        !TextUtils.isEmpty(overviewText) &&
                        !TextUtils.isEmpty(posterPath)) {
                    arrayOfMovies.add(new Movie(posterPath, originalTitle, overviewText, localizedTitle));
                }
            }
        }
        return arrayOfMovies;
    }
}
