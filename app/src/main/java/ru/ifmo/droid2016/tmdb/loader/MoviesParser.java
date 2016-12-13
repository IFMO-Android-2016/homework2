package ru.ifmo.droid2016.tmdb.loader;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import ru.ifmo.droid2016.tmdb.model.Movie;
import ru.ifmo.droid2016.tmdb.utils.IOUtils;

class MoviesParser {
    static List<Movie> parseMovies(InputStream in) throws Exception {
        return parseMovies(new JSONObject(IOUtils.readToString(in, "UTF-8")));
    }

    private static List<Movie> parseMovies(JSONObject json) throws Exception {
        JSONArray arr = json.getJSONArray("results");
        ArrayList<Movie> list = new ArrayList<>();
        for (int i = 0; i < arr.length(); i++) {
            JSONObject obj = arr.optJSONObject(i);
            if (obj != null) {
                list.add(new Movie(
                        "https://image.tmdb.org/t/p/w780" + obj.optString("poster_path", null),
                        obj.optString("original_title", null),
                        obj.optString("overview", null),
                        obj.optString("title", null)
                ));
            }
        }
        return list;
    }
}
