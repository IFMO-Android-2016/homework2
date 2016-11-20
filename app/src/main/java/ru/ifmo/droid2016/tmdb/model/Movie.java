package ru.ifmo.droid2016.tmdb.model;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Информация о фильме, полученная из The Movie DB API
 */

public class Movie {

    /**
     * Path изображения постера фильма.
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
}
