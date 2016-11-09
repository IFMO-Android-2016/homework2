package ru.ifmo.droid2016.tmdb.loader;

import android.util.JsonReader;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import ru.ifmo.droid2016.tmdb.model.Movie;

/**
 * Created by ghost3432 on 09.11.16.
 */

public class Parser {
    private static final String TAG = Parser.class.getSimpleName();

    static List<Movie> readJson(InputStream in) throws IOException {
        List<Movie> result = null;
        JsonReader reader = new JsonReader(new InputStreamReader(in));
        try {
            reader.beginObject();
            while (reader.hasNext()) {
                switch (reader.nextName()) {
                    case "page":
                        reader.nextLong();//TODO
                        break;

                    case "results":
                        result = readResults(reader);
                        break;

                    case "total_results":
                        reader.nextInt();
                        break;

                    case "total_pages":
                        reader.nextInt();//TODO
                        break;

                    default:
                        Log.wtf(TAG, "something wrong at readJson()");
                        reader.skipValue();
                }
            }
            reader.endObject();
        } finally {
            reader.close();
        }

        return result;
    }

    private static List<Movie> readResults(JsonReader reader) throws IOException {
        List<Movie> result = new ArrayList<>(20);

        reader.beginArray();
        while (reader.hasNext()) {
            result.add(readMovie(reader));
        }
        reader.endArray();

        return result;
    }

    private static Movie readMovie(JsonReader reader) throws IOException {
        String posterPath = null;
        String originalTitle = null;
        String overviewText = null;
        String localizedTitle = null;
        reader.beginObject();
        while (reader.hasNext()) {
            switch (reader.nextName()) {
                case "poster_path":
                    posterPath = reader.nextString();
                    break;

                case "original_title":
                    originalTitle = reader.nextString();
                    break;

                case "overview":
                    overviewText = reader.nextString();
                    break;

                case "title":
                    localizedTitle = reader.nextString();
                    break;

                default:
                    reader.skipValue();
            }
        }
        reader.endObject();

        return new Movie(posterPath, originalTitle, overviewText, localizedTitle);
    }
}
