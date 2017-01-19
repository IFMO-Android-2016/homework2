package ru.ifmo.droid2016.tmdb.parser;

import android.util.JsonReader;
import android.util.JsonToken;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import ru.ifmo.droid2016.tmdb.model.Movie;

/**
 * Created by Koroleva Yana.
 */

public class JSONParser {
    public static List<Movie> parse(InputStream inputStream) throws IOException{
        List<Movie> result = new ArrayList<>();
        try (JsonReader reader = new JsonReader(new InputStreamReader(inputStream, "UTF-8"))) {
            reader.beginObject();
            while(reader.hasNext()) {
                String name = reader.nextName();
                if (name.equals("result")) {
                    reader.beginArray();
                    while (reader.hasNext()) {
                        if (reader.peek() == JsonToken.NULL) {
                            reader.skipValue();
                        }
                        reader.beginObject();
                        String curPosterPath = "";
                        String curOriginalTitle = "";
                        String curOverview = "";
                        String curTitle = "";
                        String curName = reader.nextName();
                        switch (curName) {
                            case "poster_path":
                                curPosterPath = reader.nextString();
                                break;
                            case "original_title":
                                curOriginalTitle = reader.nextString();
                                break;
                            case "overview":
                                curOverview = reader.nextString();
                                break;
                            case "title":
                                curTitle = reader.nextString();
                                break;
                            default:
                                reader.skipValue();
                                break;
                        }

                        result.add(new Movie(curPosterPath, curOriginalTitle, curOverview, curTitle));
                        reader.endObject();
                    }
                    reader.endArray();
                }
            }
            reader.endObject();
        }
        return result;
    }
}

