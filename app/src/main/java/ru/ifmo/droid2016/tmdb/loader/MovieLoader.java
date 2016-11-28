package ru.ifmo.droid2016.tmdb.loader;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import ru.ifmo.droid2016.tmdb.api.TmdbApi;
import ru.ifmo.droid2016.tmdb.model.Movie;
import ru.ifmo.droid2016.tmdb.utils.IOUtils;

/**
 * Created by ghost3432 on 09.11.16.
 */

public class MovieLoader extends AsyncTaskLoader<LoadResult<List<Movie>>> {
    private static final int RESULTS_PER_PAGE = 20;
    private final String TAG = MovieLoader.class.getSimpleName();

    private volatile long page;
    private String locale = Locale.getDefault().getLanguage();
    private List<Movie> movieList = new ArrayList<>(100);
    private volatile boolean forceReload = false;

    public MovieLoader(Context context) {
        super(context);
        page = 1;
    }

    @Override
    public LoadResult<List<Movie>> loadInBackground() {
        if (forceReload) {
            movieList = new ArrayList<>(100);
            page = 1;
            forceReload = false;
        }
        if (page == 0 || page > MovieParser.getTotalPages()) {
            return new LoadResult<>(ResultType.LAST_PAGE, movieList);
        }
//        Log.i(TAG, String.valueOf(page));

        List<Movie> data = null;
        ResultType resultType = ResultType.ERROR;
        HttpURLConnection connection = null;
        try {
            connection = TmdbApi.getPopularMoviesRequest(locale, page);

            connection.connect();

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream in = connection.getInputStream();
                data = MovieParser.readJson(in);
                resultType = ResultType.OK;
            } else {
                resultType = ResultType.ERROR;
            }

        } catch (IOException e) {
            e.printStackTrace();
            if (IOUtils.isConnectionAvailable(getContext(), false)) {
                resultType = ResultType.ERROR;
            } else {
                resultType = ResultType.NO_INTERNET;
            }
        } catch (BadResponseException e) {
            e.printStackTrace();
            resultType = ResultType.ERROR;
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }

        if (data != null) {
            if (data.size() == 0) {
                resultType = ResultType.LAST_PAGE;
            } else {
                movieList.addAll(data);
            }
        }
        return new LoadResult<>(resultType, movieList);
    }

    public void forceLoad(int entry) {
//        Log.i(TAG, entry + " vs " + MovieParser.getTotalResults());
        if (entry >= MovieParser.getTotalResults()) {
            page = 0;
        } else {
            page = 1 + entry / RESULTS_PER_PAGE;
        }
        super.forceLoad();
    }

    public void setLocale(String locale) {
        if (!this.locale.equals(locale)) {
            this.locale = locale;
            this.forceReload = true;
            forceLoad();
        }
    }
}
