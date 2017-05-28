package ru.ifmo.droid2016.tmdb.loader;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import ru.ifmo.droid2016.tmdb.model.Movie;
import ru.ifmo.droid2016.tmdb.utils.IOUtils;

class MovieParser {
    static List<Movie> parse(InputStream inStream) throws JSONException, IOException, BadResponseException {
        final JSONObject jMovies = new JSONObject(IOUtils.readToString(inStream, "UTF-8"));
        return parseJson(jMovies);
    }

    private static List<Movie> parseJson(JSONObject jObject) throws JSONException {
        final JSONArray resultJson = jObject.getJSONArray("results");
        final ArrayList<Movie> movies = new ArrayList<>();

        for (int i = 0; i < resultJson.length(); i++) {
            final JSONObject movieJsom = resultJson.optJSONObject(i);
            if (movieJsom == null) continue;

            movies.add(new Movie(
                            "https://image.tmdb.org/t/p/w780" + movieJsom.optString("poster_path", null),
                            movieJsom.optString("original_title", null), movieJsom.optString("overview", null),
                            movieJsom.optString("title", null)
                    )
            );

        }
        return movies;
    }
}

