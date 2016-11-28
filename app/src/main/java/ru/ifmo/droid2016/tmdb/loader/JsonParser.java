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

/**
 * Created by Дарья on 19.11.2016.
 */

public final class JsonParser {

    public static List<Movie> parse(InputStream inputStream) throws IOException, JSONException, BadResponseException {


        final String content = IOUtils.readToString(inputStream, "UTF-8");
        final JSONObject json = new JSONObject(content);
        final String STATUS_MESSAGE = "status_message";
        final String status = json.has(STATUS_MESSAGE) ? json.getString(STATUS_MESSAGE) : "OK";
        if (!"OK".equals(status)) {
            throw new BadResponseException("Unexpected response status from API: " + status);
        }

        final JSONArray moviesJson = json.getJSONArray("results");
        final ArrayList<Movie> movies = new ArrayList<>();

        for (int i = 0; i < moviesJson.length(); i++){
            JSONObject movieJson = moviesJson.optJSONObject(i);
            if (movieJson == null)
                continue;
            final Movie movie = new Movie(movieJson.optString("poster_path"),
                    movieJson.optString("original_title"),
                    movieJson.optString("overview"),
                    movieJson.optString("title"));
            movies.add(movie);
        }

        return movies;
    }




}
