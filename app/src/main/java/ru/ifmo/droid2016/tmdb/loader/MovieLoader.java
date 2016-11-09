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
    private final String TAG = MovieLoader.class.getSimpleName();

    private long page;
    private String locale = Locale.getDefault().getLanguage();
    private List<Movie> movieList = new ArrayList<>(100);
    private int lastLoadedPage = 0;

    public MovieLoader(Context context) {
        super(context);
        page = 1;
    }

    public MovieLoader(Context context, long page) {
        super(context);
        this.page = page;
    }

    @Override
    public LoadResult<List<Movie>> loadInBackground() {
        List<Movie> data = null;
        ResultType resultType = ResultType.ERROR;
        HttpURLConnection connection = null;
        try {
            connection = TmdbApi.getPopularMoviesRequest(locale, page);

            connection.connect();

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream in = connection.getInputStream();
                data = Parser.readJson(in);
                resultType = ResultType.OK;
            } else {
                resultType = ResultType.ERROR;
            }

        } catch (IOException e) {
            if (IOUtils.isConnectionAvailable(getContext(), false)) {
                resultType = ResultType.ERROR;
            } else {
                resultType = ResultType.NO_INTERNET;
            }
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
        Log.i(TAG, "OK");
        return new LoadResult<>(resultType, movieList);
    }

    public void forceLoad(int entry) {
        page = 1 + entry / 20;
        if (lastLoadedPage == page) {
            page ++;
        }
        lastLoadedPage = (int) page;
        super.forceLoad();
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }
}
