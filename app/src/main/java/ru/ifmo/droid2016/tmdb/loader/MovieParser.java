package ru.ifmo.droid2016.tmdb.loader;

import android.util.JsonReader;
import android.util.JsonToken;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import ru.ifmo.droid2016.tmdb.model.Movie;

/**
 * Created by ghost3432 on 09.11.16.
 */

class MovieParser {
    private static final String TAG = MovieParser.class.getSimpleName();
    private static int totalPages = Integer.MAX_VALUE, totalResults = Integer.MAX_VALUE;

    static List<Movie> readJson(InputStream in) throws IOException, BadResponseException {
        List<Movie> result = null;
        JsonReader reader = new JsonReader(new InputStreamReader(in));
        //noinspection TryFinallyCanBeTryWithResources
        try {
            reader.beginObject();
            while (reader.hasNext()) {
                switch (reader.nextName()) {
                    case "page":
                        reader.nextLong();
                        break;

                    case "results":
                        result = readResults(reader);
                        break;

                    case "total_results":
                        totalResults = reader.nextInt();
                        break;

                    case "total_pages":
                        totalPages = reader.nextInt();
                        break;

                    default:
                        reader.skipValue();
                }
            }
            reader.endObject();
        } catch (IllegalStateException x) {
            throw new BadResponseException(x);
        } finally {
            //noinspection ThrowFromFinallyBlock
            reader.close();
        }

        if (result == null) {
            throw new BadResponseException("can't find key \"results\"");
        }

        return result;
    }

    private static List<Movie> readResults(JsonReader reader) throws IOException, BadResponseException {
        List<Movie> result = new ArrayList<>(20);

        reader.beginArray();
        while (reader.hasNext()) {
            result.add(readMovie(reader));
        }
        reader.endArray();

        return result;
    }

    private static Movie readMovie(JsonReader reader) throws IOException, BadResponseException {
        String posterPath = null;
        String originalTitle = null;
        String overviewText = null;
        String localizedTitle = null;
        reader.beginObject();
        while (reader.hasNext()) {
            switch (reader.nextName()) {
                case "poster_path":
                    if (reader.peek() == JsonToken.STRING) {
                        posterPath = reader.nextString();
                    } else {
                        reader.nextNull();
                    }
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

        if (originalTitle == null || overviewText == null || localizedTitle == null) {
            throw new BadResponseException("one of the fields is null: " + originalTitle + "/" +
                    overviewText + "/" + localizedTitle);
        }

        return new Movie(posterPath, originalTitle, overviewText, localizedTitle);
    }

    static int getTotalPages() {
        return totalPages;
    }

    static int getTotalResults() {
        return totalResults;
    }
}
