package ru.ifmo.droid2016.tmdb.model;

import android.support.annotation.NonNull;

/**
 * Primary Movie Info from The Movie DB API
 */

public class Movie {

    /**
     * Poster Path (2/3 aspect ratio image-poster)
     */
    public final @NonNull String posterPath;

    /**
     * Backdrop Path (16/9 aspect ratio image-backdrop)
     */
    public final @NonNull String backdropPath;

    /**
     * Localized movie's title
     */
    public final @NonNull String title;

    /**
     * Original movie's title
     */
    public final @NonNull String originalTitle;

    /**
     * Localized movie's description
     */
    public final @NonNull String overview;

    /**
     * Average movie's vote rating
     */
    public final @NonNull String voteAverage;

    public Movie(@NonNull String title,
                 @NonNull String originalTitle,
                 @NonNull String overview,
                 @NonNull String voteAverage,
                 @NonNull String posterPath,
                 @NonNull String backdropPath) {
        this.title = title;
        this.originalTitle = originalTitle;
        this.overview = overview;
        this.voteAverage = voteAverage;
        this.posterPath = "https://image.tmdb.org/t/p/w500" + posterPath;
        this.backdropPath = "https://image.tmdb.org/t/p/w500" + backdropPath;
    }
}
