package ru.ifmo.droid2016.tmdb.loader;

import android.util.JsonReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import ru.ifmo.droid2016.tmdb.model.Movie;

/**
 * Created by penguinni on 17.11.16.
 */

public class MoviesPullParser {
    private MoviesPullParser() throws IOException {}

    private static final String IMAGE_BASE_URL = "http://image.tmdb.org/t/p/w500";

    public static int totalPages = 0;

    public static List<Movie> parseMovies(InputStream in) throws IOException {
        return parseMovies(new JsonReader(new InputStreamReader(in, "UTF-8")));
    }

    private static List<Movie> parseMovies(JsonReader reader) throws IOException {
        reader.beginObject();

        while (reader.hasNext() && !reader.nextName().equals("results")) {
            reader.skipValue();
        }

        reader.beginArray();
        ArrayList<Movie> movies = new ArrayList<>();

        while (reader.hasNext()) {
            reader.beginObject();

            String token, posterPath = null,
                    originalTitle = null,
                    overviewText = null,
                    localizedTitle = null;

            while (reader.hasNext()) {
                token = reader.nextName();

                if (token == null) {
                    reader.skipValue();
                } else {
                    switch (token) {
                        case "poster_path":
                            posterPath = IMAGE_BASE_URL + reader.nextString();
                            break;
                        case "overview":
                            overviewText = reader.nextString();
                            break;
                        case "original_title":
                            originalTitle = reader.nextString();
                            break;
                        case "title":
                            localizedTitle = reader.nextString();
                            break;
                        default:
                            reader.skipValue();
                            break;
                    }
                }
            }

            movies.add(new Movie(posterPath, originalTitle, overviewText, localizedTitle));
            reader.endObject();
        }

        reader.endArray();

        while (reader.hasNext()) {
            if (reader.nextName().equals("total_pages")) {
                totalPages = reader.nextInt();
            } else {
                reader.skipValue();
            }
        }

        reader.endObject();
        return movies;
    }
}
