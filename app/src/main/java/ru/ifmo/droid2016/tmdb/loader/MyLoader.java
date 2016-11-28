package ru.ifmo.droid2016.tmdb.loader;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.facebook.stetho.urlconnection.StethoURLConnectionManager;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.util.List;
import java.util.Locale;

import ru.ifmo.droid2016.tmdb.api.TmdbApi;
import ru.ifmo.droid2016.tmdb.model.Movie;
import ru.ifmo.droid2016.tmdb.utils.IOUtils;

/**
 * Created by ivantrofimov on 23.11.16.
 */

public class MyLoader extends AsyncTaskLoader<LoadResult<List<Movie>>> {
    private LoadResult<List<Movie>> movieList;

    public boolean isLoad = false;

    public MyLoader(Context context) {
        super(context);
    }

    protected void onStartLoading() {
        if (isLoad) {
            deliverResult(movieList);
        } else {
            forceLoad();
        }
    }

    public void deliverResult(LoadResult<List<Movie>> data) {
        super.deliverResult(data);
    }

    protected void onForceLoad() {
        super.onForceLoad();
    }

    public LoadResult<List<Movie>> loadInBackground() {
        final StethoURLConnectionManager manager = new StethoURLConnectionManager("API");
        ResultType resultType = ResultType.ERROR;
        List<Movie> data = null;
        HttpURLConnection connection = null;
        InputStream is = null;
        try {
            connection = TmdbApi.getPopularMoviesRequest(Locale.getDefault().getLanguage());
            manager.preConnect(connection, null);
            connection.connect();
            manager.postConnect();
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                is = manager.interpretResponseStream(connection.getInputStream());
                data = Parser.parseTmdb(manager.interpretResponseStream(connection.getInputStream()));
                resultType = ResultType.OK;
            } else {
                resultType = ResultType.ERROR;
            }
        } catch (MalformedURLException e) {
            resultType = ResultType.ERROR;
        } catch (IOException e) {
            manager.httpExchangeFailed(e);
            try {
                if (IOUtils.isConnectionAvailable(getContext(), false)) {
                    resultType = ResultType.NO_INTERNET;
                } else {
                    resultType = ResultType.ERROR;
                }
            } catch (Exception a) {
                resultType = ResultType.ERROR;
            }
        } catch (Exception e) {
            resultType = ResultType.ERROR;
        } finally {
            IOUtils.closeSilently(is);
            if (connection != null) connection.disconnect();
        }
        isLoad = resultType == ResultType.OK;
        return movieList = new LoadResult<>(resultType, data);
    }

}
