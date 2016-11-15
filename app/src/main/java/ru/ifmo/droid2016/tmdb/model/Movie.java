package ru.ifmo.droid2016.tmdb.model;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import ru.ifmo.droid2016.tmdb.loader.BadResponseException;
import ru.ifmo.droid2016.tmdb.utils.IOUtils;

/**
 * Информация о фильме, полученная из The Movie DB API
 */

public class Movie {

    /**
     * Path изображения постера фильма. Как из Path получить URL, описано здесь:
     * <p>
     * https://developers.themoviedb.org/3/getting-started/languages
     * <p>
     * В рамках ДЗ можно не выполнять отдельный запрос /configuration, а использовать
     * базовый URL для картинок: http://image.tmdb.org/t/p/ и
     */
    public final
    @NonNull
    String posterPath;

    /**
     * Название фильма на языке оригинала.
     */
    public final
    @NonNull
    String originalTitle;

    /**
     * Описание фильма на языке пользователя.
     */
    public final
    @Nullable
    String overviewText;

    /**
     * Название фильма на языке пользователя.
     */
    public final
    @Nullable
    String localizedTitle;

    public Movie(String posterPath,
                 String originalTitle,
                 String overviewText,
                 String localizedTitle) {
        this.posterPath = posterPath;
        this.originalTitle = originalTitle;
        this.overviewText = overviewText;
        this.localizedTitle = localizedTitle;
    }


    @NonNull
    public static List<Movie> parseMovie(InputStream in) throws
            IOException,
            JSONException,
            BadResponseException {

        final String content = IOUtils.readToString(in, "UTF-8");
        final JSONObject json = new JSONObject(content);
        return parseMovies(json);
    }

    @NonNull
    private static List<Movie> parseMovies(JSONObject json) throws
            IOException,
            JSONException,
            BadResponseException {

        final JSONArray resultJson = json.getJSONArray("results");
        final ArrayList<Movie> movies = new ArrayList<>();

        for (int i = 0; i < resultJson.length(); i++) {
            final JSONObject movieJson = resultJson.optJSONObject(i);

            if (movieJson != null) {
                final String posterPath = "https://image.tmdb.org/t/p/w500" + movieJson.optString("poster_path", null);
                final String title = movieJson.optString("original_title", null);
                final String overview = movieJson.optString("overview", null);
                final String locTitle = movieJson.optString("title", null);

                movies.add(new Movie(posterPath, title, overview, locTitle));
            }
        }
        return movies;
    }
}
