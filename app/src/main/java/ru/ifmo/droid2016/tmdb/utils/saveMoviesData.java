package ru.ifmo.droid2016.tmdb.utils;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.content.Loader;
import android.util.JsonReader;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

import ru.ifmo.droid2016.tmdb.api.TmdbApi;
import ru.ifmo.droid2016.tmdb.loader.LoadResult;
import ru.ifmo.droid2016.tmdb.loader.ResultType;
import ru.ifmo.droid2016.tmdb.model.Movie;

/**
 * Created by Alex on 30.10.2016.
 */

public class SaveMoviesData extends AsyncTaskLoader<LoadResult<List<Movie>>> {

    private final List<Movie> movies;
    public SaveMoviesData(Context context, List<Movie> movies) {
        super(context);
        this.movies = movies;
    }

    @Override
    public LoadResult<List<Movie>> loadInBackground() {
        return new LoadResult<>(ResultType.OK, movies);
    }

}

