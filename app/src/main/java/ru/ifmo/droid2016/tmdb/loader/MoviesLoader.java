package ru.ifmo.droid2016.tmdb.loader;

import android.support.v4.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.List;
import java.util.Locale;

import ru.ifmo.droid2016.tmdb.api.TmdbApi;
import ru.ifmo.droid2016.tmdb.model.Movie;
import ru.ifmo.droid2016.tmdb.parser.JSONParser;

/**
 * Created by Koroleva Yana.
 */

public class MoviesLoader extends AsyncTaskLoader<LoadResult<List<Movie>>> {
    public MoviesLoader(Context context) {
        super(context);
    }

    @Override
    public LoadResult<List<Movie>> loadInBackground() {
        ResultType resultType = ResultType.ERROR;
        List<Movie> result = null;

        try {
            HttpURLConnection connection =
                    TmdbApi.getPopularMoviesRequest(Locale.getDefault().getLanguage());

            connection.connect();

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                resultType = ResultType.OK;
                Log.e("Meow", "Result OK.");
                result = JSONParser.parse(connection.getInputStream());
            } else {
                resultType = ResultType.NO_INTERNET;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return new LoadResult<>(resultType, result);
    }
}
