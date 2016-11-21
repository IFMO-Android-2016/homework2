package ru.ifmo.droid2016.tmdb.model;

import android.os.Parcel;
import android.support.annotation.NonNull;

import ru.ifmo.droid2016.tmdb.R;

public class LoadingMovie extends Movie {

    private boolean isLoading = true;
    private int message = R.string.error;

    public LoadingMovie(int id, @NonNull String posterPath, @NonNull String originalTitle, @NonNull String overviewText, @NonNull String localizedTitle, double rating) {
        super(id, posterPath, originalTitle, overviewText, localizedTitle, rating);
    }

    protected LoadingMovie(Parcel in) {
        super(in);
    }

    public LoadingMovie() {
        super(0, "", "", "", "", 0);
    }

    public boolean isLoading() {
        return isLoading;
    }

    public void displayLoader() {
        isLoading = true;
    }

    public void displayError(int message) {
        this.message = message;
        isLoading = false;
    }

    public int getMessage() {
        return message;
    }
}
