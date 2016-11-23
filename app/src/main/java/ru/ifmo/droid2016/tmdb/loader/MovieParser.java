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
 * Created by Disa on 23.11.2016.
 */

public class MovieParser {

    @NonNull
    public static List<Movie> parseMovies(InputStream in) throws IOException, JSONException, BadResponseException {
        final String content = IOUtils.readToString(in, "UTF-8");
        final JSONObject json = new JSONObject(content);
        final JSONArray JSONMovies = json.getJSONArray("results");
        final ArrayList <Movie> movies= new ArrayList<>();


        for (int i = 0; i < JSONMovies.length(); i++) {
            final JSONObject movieNow = JSONMovies.optJSONObject(i);
            if (movieNow != null) {
                final String posterPath = movieNow.optString("poster_path", null);
                final String originalTitle = movieNow.optString("title", null);
                final String overviewText = movieNow.optString("overview", null);
                final String localizedTitle = movieNow.optString("title", null);


                if (!TextUtils.isEmpty(posterPath) && !TextUtils.isEmpty(originalTitle) &&
                        !TextUtils.isEmpty(overviewText) && ! TextUtils.isEmpty(localizedTitle)) {
                    movies.add(new Movie(posterPath, originalTitle, overviewText, localizedTitle));
                }
            }
        }

        return movies;
    }
}
