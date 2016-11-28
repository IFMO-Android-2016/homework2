package ru.ifmo.droid2016.tmdb;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import java.util.ArrayList;
import ru.ifmo.droid2016.tmdb.model.Movie;

/**
 * Created by Anya on 22.11.2016.
 */

public class MovieValue extends Fragment {

    public static final String TAG = MovieValue.class.getCanonicalName();
    private ArrayList<Movie> presentMovies = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public MovieValue() {
        this.setRetainInstance(true);
    }

    public ArrayList<Movie> getPresentMovies() {
        return presentMovies;
    }
}
