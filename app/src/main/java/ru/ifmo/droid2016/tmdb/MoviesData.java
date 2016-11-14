package ru.ifmo.droid2016.tmdb;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;

import java.util.ArrayList;

import ru.ifmo.droid2016.tmdb.model.Movie;

/**
 * Created by Октай on 14.11.2016.
 */

public class MoviesData extends Fragment {

    public static final String TAG = MoviesData.class.getCanonicalName();
    private ArrayList<Movie> currentMovies = new ArrayList<>();

    public MoviesData() {
        this.setRetainInstance(true);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public ArrayList<Movie> getCurrentMovies() {
        return currentMovies;
    }
}
