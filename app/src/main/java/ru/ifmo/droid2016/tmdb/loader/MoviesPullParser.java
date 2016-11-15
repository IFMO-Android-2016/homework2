package ru.ifmo.droid2016.tmdb.loader;

import android.support.annotation.Nullable;
import android.util.JsonReader;
import android.util.JsonToken;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import ru.ifmo.droid2016.tmdb.model.Movie;


/**
 * Created by Roman on 12/11/2016.
 */

public class MoviesPullParser {
    private MoviesPullParser() {}
    @Nullable
    public static List<Movie> parseMovies(InputStream in) throws IOException {
        JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));
        List<Movie> movies = null;
        try {
            reader.beginObject();
            String curName;
            while (reader.hasNext()) {
                curName = reader.nextName();
                if (curName.equals("results")) {
                    movies = parseMoviesArray(reader);
                } else {
                    reader.skipValue();
                }
            }
            reader.endObject();
        } finally {
            reader.close();
        }
        return movies;
    }

    private static Movie parseMovie(JsonReader reader) throws IOException {
        String posterPath = null, originalTitle = null, overviewText = null, title = null;
        reader.beginObject();
        while (reader.hasNext()) {
            String key = reader.nextName();
            if (key.equals("poster_path") && reader.peek() != JsonToken.NULL)
                posterPath = reader.nextString();
            else if (key.equals("original_title") && reader.peek() != JsonToken.NULL)
                originalTitle = reader.nextString();
            else if (key.equals("overview"))
                overviewText = reader.nextString();
            else if (key.equals("title"))
                title = reader.nextString();
            else
                reader.skipValue();
        }
        reader.endObject();
        return new Movie(posterPath, originalTitle, overviewText, title);
    }

    private static List<Movie> parseMoviesArray(JsonReader reader) throws IOException {
        List<Movie> movies = new ArrayList<>();
        reader.beginArray();
        while (reader.hasNext()) {
            movies.add(parseMovie(reader));
        }
        reader.endArray();
        return movies;
    }
}
