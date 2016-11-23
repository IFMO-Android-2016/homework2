package ru.ifmo.droid2016.tmdb.loader;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;



import ru.ifmo.droid2016.tmdb.model.Movie;
import ru.ifmo.droid2016.tmdb.utils.IOUtils;

/**
 * Created by ivan on 21.11.16.
 */

public class DOMParser {
    public static ArrayList<Movie> parseList(InputStream in) throws JSONException, IOException{

        ArrayList<Movie> list = new ArrayList<>();
        String content = IOUtils.readToString(in, "UTF-8");
        JSONObject json = new JSONObject(content);

        final String BASE_IMAGE = "https://image.tmdb.org/t/p/w500";

        JSONArray jsonArray = json.getJSONArray("results");

        for(int i = 0; i < jsonArray.length(); i++) {
            JSONObject result = jsonArray.getJSONObject(i);
            Movie movie = new Movie(BASE_IMAGE + result.getString("poster_path"),
                    result.getString("original_title"),
                    result.getString("overview"),
                    result.getString("title"));

            list.add(movie);
        }

        return list;
    }
}
