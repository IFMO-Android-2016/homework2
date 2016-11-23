package io.github.d1v1nation.tmdb.loader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import io.github.d1v1nation.tmdb.model.Movie;
import io.github.d1v1nation.tmdb.utils.IOUtils;

/**
 * @author d1v1nation (catanaut@gmail.com)
 *         <p>
 *         23.11.16 of movies | io.github.d1v1nation.tmdb.loader
 */
public class MovieParser {
    public static List<Movie> parseMovies(InputStream in) throws
            IOException,
            JSONException,
            BadResponseException {

        final String content = IOUtils.readToString(in, "UTF-8");
        final JSONObject json = new JSONObject(content);
        return parseMovies(json);
    }

    public static List<Movie> parseMovies(JSONObject json) throws
            IOException,
            JSONException,
            BadResponseException {

        final JSONArray results = json.getJSONArray("results");
        final ArrayList<Movie> data = new ArrayList<>();

        for (int i = 0; i < results.length(); i++) {
            final JSONObject mo = results.optJSONObject(i);
            if (mo != null) {
                final String orig_title = mo.getString("original_title");
                final String overview = mo.optString("overview");
                final String title = mo.getString("title");
                String image = mo.optString("poster_path");
                if (image != null) {
                    image = "http://image.tmdb.org/t/p/w780" + image; // TODO : god damn poster sizes!
                }

                data.add(new Movie(image, orig_title, overview, title));
            }
        }

        return data;
    }

    public static Movie parseMovie(JSONObject mo) throws JSONException {
        return new Movie(mo.getString("poster_path"),
                mo.getString("original_title"),
                mo.getString("overview"),
                mo.getString("title"));
    }
}
