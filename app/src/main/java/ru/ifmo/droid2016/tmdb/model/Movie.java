package ru.ifmo.droid2016.tmdb.model;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Информация о фильме, полученная из The Movie DB API
 */

public class Movie {

    /**
     * Path изображения постера фильма. Как из Path получить URL, описано здесь:
     *
     * https://developers.themoviedb.org/3/getting-started/languages
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

    public final @Nullable  String vote;

    public Movie(@NonNull String posterPath,
                 @NonNull String originalTitle,
                 @Nullable String overviewText,
                 @Nullable String localizedTitle,
                 @Nullable String vote) {
        this.posterPath = posterPath;
        this.originalTitle = originalTitle;
        this.overviewText = overviewText;
        this.localizedTitle = localizedTitle;
        this.vote = "Average vote: " + vote;
    }
}
