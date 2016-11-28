package ru.ifmo.droid2016.tmdb.model;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.JsonReader;

import java.io.IOException;

/**
 * Информация о фильме, полученная из The Movie DB API
 */

public class Movie {

    /**
     * Path изображения постера фильма. Как из Path получить URL, описано здесь:
     *
     * https://developers.themoviedb.org/3/getting-started/images
     *
     * В рамках ДЗ можно не выполнять отдельный запрос /configuration, а использовать
     * базовый URL для картинок: http://image.tmdb.org/t/p/ и
     */
    public final @NonNull String posterPath;

    /**
     * Название фильма на языке оригинала.
     */
    public final @NonNull String originalTitle;

    /**
     * Описание фильма на языке пользователя.
     */
    public final @Nullable String overviewText;

    /**
     * Название фильма на языке пользователя.
     */
    public final @Nullable String localizedTitle;

    public Movie(String posterPath,
                 String originalTitle,
                 String overviewText,
                 String localizedTitle) {
        this.posterPath = posterPath;
        this.originalTitle = originalTitle;
        this.overviewText = overviewText;
        this.localizedTitle = localizedTitle;
    }

    public static Movie parseMovie(JsonReader jr) throws IOException {
        String posterPath = "";
        String originalTitle = "";
        String overviewText = "";
        String localizedTitle = "";

        jr.beginObject();

        while (jr.hasNext()) {
            final String id = jr.nextName();
            if (id == null) {
                jr.skipValue();
                continue;
            }

            switch (id) {
                case "poster_path" :
                    posterPath = "https://image.tmdb.org/t/p/w500" + jr.nextString();
                    break;
                case "original_title" :
                    originalTitle = jr.nextString();
                    break;
                case "overview" :
                    overviewText = jr.nextString();
                    break;
                case "title" :
                    localizedTitle = jr.nextString();
                    break;
                default:
                    jr.skipValue();
                    break;
            }
        }
        jr.endObject();
        return new Movie(posterPath, originalTitle, overviewText, localizedTitle);
    }
}
