package ru.ifmo.droid2016.tmdb.loader;

import android.graphics.Bitmap;
import android.widget.ProgressBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import ru.ifmo.droid2016.tmdb.model.Movie;

/**
 * Created by nikita on 19.11.16.
 */

public final class MoviesParser {

	public static List<Movie> parseMovies(InputStream in) throws IOException, JSONException {
		ByteArrayOutputStream result = new ByteArrayOutputStream();
		byte[] buffer = new byte[8096];
		int length;
		while ((length = in.read(buffer)) != -1) {
			result.write(buffer, 0, length);
		}
		String stringFromStream = result.toString("UTF-8");

		ArrayList <Movie> movies = new ArrayList<>();

		JSONObject content = new JSONObject(stringFromStream);
		JSONArray films = content.getJSONArray("results");

		for (int i = 0; i < films.length(); i++) {
			JSONObject tmp = films.getJSONObject(i);
			movies.add(new Movie(tmp.getString("poster_path"), tmp.getString("original_title"), tmp.getString("overview"), tmp.getString("title"), MovieLoader.getImages(tmp.getString("poster_path"))));
		}

		return movies;
	}
}
