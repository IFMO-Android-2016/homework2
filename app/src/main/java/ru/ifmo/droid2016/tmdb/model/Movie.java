package ru.ifmo.droid2016.tmdb.model;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class Movie {

    private final @NonNull String posterPath;

    private final @Nullable String overviewText;

    /*
    еее, бойлерплэйт, еее
     */
    @Nullable
    public String getLocalizedTitle() {
        return localizedTitle;
    }

    @NonNull
    public String getPosterPath() {
        return posterPath;
    }

    @Nullable
    public String getOverviewText() {
        return overviewText;
    }

    /**
     * Название фильма на языке пользователя.
     */
    private final @Nullable String localizedTitle;

    public Movie(@NonNull String posterPath,
                 @Nullable String overviewText,
                 @Nullable String localizedTitle) {
        this.posterPath = posterPath;
        this.overviewText = overviewText;
        this.localizedTitle = localizedTitle;
    }
}
