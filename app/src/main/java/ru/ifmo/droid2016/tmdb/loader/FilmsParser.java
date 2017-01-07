package ru.ifmo.droid2016.tmdb.loader;

import android.text.TextUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;

import ru.ifmo.droid2016.tmdb.model.Movie;

/**
 * Created by Averin Maxim on 06.01.2017.
 */

public class FilmsParser {
    public static List<Movie> parseFilms(JSONObject json) throws JSONException, BadResponseException {
        final String status = json.optString("status_message", "OK");
        if (!status.equals("OK")) {
            throw new BadResponseException(status);
        }
        final List<Movie> films = new LinkedList<>();
        final JSONArray resultJson = json.getJSONArray("results");


        for (int i = 0; i < resultJson.length(); i++) {
            final JSONObject element = resultJson.optJSONObject(i);
            if (element != null) {
                final String posterPath = element.optString("poster_path", null);
                final String originalTitle = element.optString("original_title", null);
                final String overviewText = element.optString("overview", null);
                final String localizedTitle = element.optString("title", null);
                if (!TextUtils.isEmpty(originalTitle) && !TextUtils.isEmpty(posterPath) &&
                        !TextUtils.isEmpty(overviewText) && !TextUtils.isEmpty(localizedTitle))
                    films.add(new Movie(posterPath, originalTitle, overviewText, localizedTitle));
            }
        }
        return films;

    }
}
