package ru.ifmo.droid2016.tmdb.loader;

import android.util.JsonReader;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import ru.ifmo.droid2016.tmdb.model.Movie;

public class PopularMovieParser {
    private PopularMovieParser() {
    }

    public static List<Movie> parseMovieList(InputStream inputStream) {
        JsonReader jsonReader = new JsonReader(new InputStreamReader(inputStream));
        List<Movie> result = null;
        try {
            jsonReader.beginObject();
            while (jsonReader.hasNext()) {
                String fieldName = jsonReader.nextName();
                if (fieldName.equals("results")) {
                    result = parseResults(jsonReader);
                } else {
                    jsonReader.skipValue();
                }
            }
            jsonReader.endObject();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    private static List<Movie> parseResults(JsonReader jsonReader) throws IOException {
        List<Movie> list = new ArrayList<>();
        jsonReader.beginArray();
        while (jsonReader.hasNext()) {
            list.add(parseMovie(jsonReader));
        }
        jsonReader.endArray();
        return list;
    }

    private static Movie parseMovie(JsonReader jsonReader) throws IOException {
        String posterPath = null;
        String originalTitle = null;
        String overviewText = null;
        String localizedTitle = null;

        jsonReader.beginObject();
        while (jsonReader.hasNext()) {
            String fieldName = jsonReader.nextName();
            if (fieldName.equals("poster_path")) {
                posterPath = jsonReader.nextString();
            }
            else if (fieldName.equals("original_title")) {
                originalTitle = jsonReader.nextString();
            }
            else if (fieldName.equals("overview")) {
                overviewText = jsonReader.nextString();
            }
            else if (fieldName.equals("title")) {
                localizedTitle = jsonReader.nextString();
            } else {
                jsonReader.skipValue();
            }
        }
        jsonReader.endObject();
        return new Movie(posterPath, originalTitle, overviewText, localizedTitle);
    }
}
