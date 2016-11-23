package ru.ifmo.droid2016.tmdb.loader;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;

import ru.ifmo.droid2016.tmdb.model.Movie;

/**
 * Created by Vlad on 22.11.2016.
 */

public class FilmsParser {

    public static List<Movie> parseFilms(JSONObject json) throws JSONException, BadResponseException {
        final String status = json.optString("status_message", "OK");
        if (!"OK".equals(status)) {
            throw new BadResponseException("Unexpected response status from API: " + status);
        }

        final List<Movie> list = new LinkedList<>();
        final JSONArray array = json.getJSONArray("results");
        for (int i = 0; i < array.length(); ++i) {
            final JSONObject element = array.optJSONObject(i);
            if (element != null) {
                final String posterPath = element.optString("poster_path", null);
                final String overview = element.optString("overview", null);
                final String localizedTitle = element.optString("title", null);
                final String originalTitle = element.optString("original_title", null);
                if (!TextUtils.isEmpty(posterPath) && !TextUtils.isEmpty(overview) && !TextUtils.isEmpty(localizedTitle) && !TextUtils.isEmpty(originalTitle))
                    list.add(new Movie(posterPath, originalTitle, overview, localizedTitle));
            }
        }
        return list;
    }
}
