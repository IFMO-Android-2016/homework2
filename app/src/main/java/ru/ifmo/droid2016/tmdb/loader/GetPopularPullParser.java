package ru.ifmo.droid2016.tmdb.loader;

import android.util.JsonReader;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import ru.ifmo.droid2016.tmdb.model.Movie;

public  class GetPopularPullParser {
    private static String LOG_TAG = "my_tag";

    public static LoadResult<List<Movie>> parse(InputStream in) {
        LoadResult<List<Movie>> rez;

        try {
            JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));
            try {
                reader.beginObject();
                String next = reader.nextName();

                if (! next.equals("page")) {
                    throw new Exception();
                }
                reader.skipValue();

                next = reader.nextName();
                if (!next.equals("results")) {
                    throw new Exception();
                }

                rez = readArray(reader);
                reader.endObject();
            } finally {
                reader.close();
            }
        } catch (Exception ex) {
            rez = new LoadResult<List<Movie>>(ResultType.ERROR, null);
        };
        return rez;
    }

    private static LoadResult<List<Movie>> readArray(JsonReader reader) throws IOException {
        List<Movie> films = new ArrayList<Movie>();
        reader.beginArray();
        while (reader.hasNext()) {
            films.add(readFilm(reader));
        }
        reader.endArray();
        return new LoadResult(ResultType.OK, films);
    }

    private static Movie readFilm(JsonReader reader) throws  IOException{
        String posterPath = null;
        String originalTitle = null;
        String overviewText = null;
        String localizedTitle = null;

        reader.beginObject();
        while (reader.hasNext()) {
            switch (reader.nextName()) {
                case "poster_path": posterPath = reader.nextString(); break;
                case "original_title": originalTitle = reader.nextString(); break;
                case "overview": overviewText = reader.nextString(); break;
                case "title": localizedTitle = reader.nextString(); break;
                default: reader.skipValue();
            }
        }
        reader.endObject();

        Log.d(LOG_TAG, posterPath);
        Log.d(LOG_TAG, originalTitle);
        Log.d(LOG_TAG, overviewText);
        Log.d(LOG_TAG, localizedTitle);

        Log.d(LOG_TAG, "-------------");

        return new Movie(posterPath, originalTitle, overviewText, localizedTitle);
    }
}
