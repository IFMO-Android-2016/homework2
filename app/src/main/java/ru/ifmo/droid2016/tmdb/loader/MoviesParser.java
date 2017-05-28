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
 * Created by Михайлов Никита on 19.11.2016.
 */

public class MoviesParser {
    public static List<Movie> parseMovies(InputStream in) throws IOException, JSONException{
        JSONArray moviesJSON = new JSONObject(IOUtils.readToString(in, "UTF-8")).getJSONArray("results");
        List<Movie> movies = new ArrayList<>();
        JSONObject curMovie;

        for(int i = 0; i < moviesJSON.length(); i++){
            curMovie = moviesJSON.getJSONObject(i);
            movies.add(new Movie("https://image.tmdb.org/t/p/w500" + curMovie.optString("poster_path", null),
                    curMovie.optString("original_title", "Empty title"),
                    curMovie.optString("overview", "No overview provided."),
                    curMovie.optString("title", "No title"),
                    curMovie.optInt("id", 0)));
        }

        return movies;
    }
}
