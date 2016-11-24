package ru.ifmo.droid2016.tmdb.loader;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import ru.ifmo.droid2016.tmdb.loader.BadResponseException;
import ru.ifmo.droid2016.tmdb.model.Movie;
import ru.ifmo.droid2016.tmdb.utils.IOUtils;

/**
 * Created by Nemzs on 22.11.2016.
 */
public class Parser {
    public List<Movie> parseMovies(InputStream in) throws
    IOException,
            JSONException,
            BadResponseException {
        List<Movie> data = null;
        final String content = IOUtils.readToString(in, "UTF-8");
        final JSONObject json = new JSONObject(content);
        return parseMovies(json);
    }

    public List<Movie> parseMovies(JSONObject mainObject) throws JSONException {
        final ArrayList<Movie> movies = new ArrayList<>();
        JSONArray pItem = mainObject.getJSONArray("results");
        for (int i=0;i<pItem.length();i++) {
            JSONObject movieJS = pItem.optJSONObject(i);
            if(movieJS!=null) {
                String poster = movieJS.getString("poster_path");
                String originalName = movieJS.getString("original_title");
                String preview = movieJS.getString("overview");
                String localName = movieJS.getString("title");
                Movie movie = new Movie(poster,originalName,preview,localName);
                movies.add(movie);
            }
        }
        return(movies);
    }
}
