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
 * Created by Елена on 23.11.2016.
 */
public class Parser {
    private static String err = null;
    static List<Movie> parse(InputStream inputStream) throws JSONException, IOException {
        final String content = IOUtils.readToString(inputStream, "UTF-8");
        final JSONObject jsonObject = new JSONObject(content);
        return parseMoviesList(jsonObject);
    }

    private static List<Movie> parseMoviesList(JSONObject json) throws JSONException {
        final JSONArray moviesToParse = json.getJSONArray("results");
        final ArrayList<Movie> parsed = new ArrayList<>();
        for (int i = 0; i < moviesToParse.length(); ++i){
            final JSONObject curFilm = moviesToParse.optJSONObject(i);
            if (curFilm != null){
                parsed.add(new Movie(
                        "https://image.tmdb.org/t/p/w780" + curFilm.optString("poster_path"),
                        curFilm.optString("original_title"),
                        curFilm.optString("overview"),
                        curFilm.optString("title")
                ));
            }
        }
        return parsed;
    }
}
