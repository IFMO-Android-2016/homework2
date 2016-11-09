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

final class MoviesDomParser {
    @NonNull
    static String parseBaseUrl(InputStream in) throws
            IOException,
            JSONException,
            BadResponseException {
        final String content = IOUtils.readToString(in, "UTF-8");
        final String baseUrl = new JSONObject(content).getJSONObject("images").getString("base_url");
        if (baseUrl == null) {
            throw new BadResponseException("Unexpected response page from API: " + baseUrl);
        }
        return baseUrl;
    }

    @NonNull
    static List<Movie> parseMovies(InputStream in) throws
            IOException,
            JSONException,
            BadResponseException {

        final String content = IOUtils.readToString(in, "UTF-8");
        System.out.println(content);
        final JSONObject json = new JSONObject(content);
        return parseMovies(json);
    }

    @NonNull
    private static List<Movie> parseMovies(JSONObject json) throws
            IOException,
            JSONException,
            BadResponseException {

        final int page = json.getInt("page");
        if (page != 1) {
            throw new BadResponseException("Unexpected response page from API: " + page);
        }

        final JSONArray moviesJson = json.getJSONArray("results");
        final ArrayList<Movie> movies = new ArrayList<>();

        for (int i = 0; i < moviesJson.length(); i++) {
            final JSONObject movieJson = moviesJson.optJSONObject(i);

            if (movieJson != null) {
                final int id = movieJson.optInt("id", 1);
                final String posterPath = movieJson.optString("poster_path");
                final String originalTitle = movieJson.optString("title", null);
                final String overviewText = movieJson.optString("overview", null);
                final String localizedTitle = movieJson.optString("title", null);
                final double rating = movieJson.optDouble("vote_average", 0.0);

                if (!TextUtils.isEmpty(posterPath)
                        && !TextUtils.isEmpty(originalTitle)
                        && !TextUtils.isEmpty(overviewText)
                        && !TextUtils.isEmpty(localizedTitle)) {
                    final Movie movie = new Movie(id, posterPath, originalTitle, overviewText, localizedTitle, rating);
                    System.out.println(movie);
                    movies.add(movie);
                }
            }
        }
        return movies;
    }

    private MoviesDomParser() {}
}
