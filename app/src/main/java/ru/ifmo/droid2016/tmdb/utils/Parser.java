package ru.ifmo.droid2016.tmdb.utils;

import android.util.JsonReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import ru.ifmo.droid2016.tmdb.model.Movie;

public class Parser {

    public static List<Movie> parse(InputStream in) {
        JsonReader reader = new JsonReader(new InputStreamReader(in));
        ArrayList<Movie> result = new ArrayList<>();
        try {
            reader.beginObject();
            reader.nextName();
            reader.nextInt();
            reader.nextName();
            reader.beginArray();
            while (reader.hasNext()) {
                String posterPath, originalTitle, overviewText, localizedTitle, vote;
                posterPath = originalTitle = overviewText = localizedTitle = vote = "";
                reader.beginObject();
                while (reader.hasNext()) {
                    final String name = reader.nextName();
                    if (name == null) {
                        reader.skipValue();
                        continue;
                    }
                    switch (name) {
                        case "poster_path": posterPath = reader.nextString(); break;
                        case "original_title": originalTitle = reader.nextString(); break;
                        case "overview": overviewText = reader.nextString(); break;
                        case "localized_title": localizedTitle = reader.nextString(); break;
                        case "vote_average": vote = "" + reader.nextDouble(); break;
                        default: reader.skipValue();
                    }
                }
                reader.endObject();
                if (localizedTitle.isEmpty()) {
                    localizedTitle = originalTitle;
                }
                result.add(new Movie(posterPath, originalTitle, overviewText,
                        localizedTitle, vote));
            }
            reader.endArray();
            reader.nextName();
            reader.nextInt();
            reader.nextName();
            reader.nextInt();
            reader.endObject();
            IOUtils.closeSilently(reader);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }
}
