package ru.ifmo.droid2016.tmdb.loader;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.facebook.stetho.urlconnection.StethoURLConnectionManager;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.List;

import ru.ifmo.droid2016.tmdb.api.TmdbApi;
import ru.ifmo.droid2016.tmdb.model.Movie;
import ru.ifmo.droid2016.tmdb.utils.IOUtils;

public class PopularMoviesLoader extends AsyncTaskLoader<LoadResult<List<Movie>>> {

    private final String lang;
    private final int page;

    public PopularMoviesLoader(Context context, String lang, int page) {
        super(context);
        this.lang = lang;
        this.page = page;
    }

    private ResultType resultType = ResultType.ERROR;
    private List<Movie> data = null;

    @Override
    protected void onStartLoading() {
        if (data != null) {
            deliverResult(new LoadResult<>(resultType, data));
        } else {
            forceLoad();
        }
    }

    @Override
    public LoadResult<List<Movie>> loadInBackground() {
        StethoURLConnectionManager manager = new StethoURLConnectionManager("API");

        HttpURLConnection connection = null;
        InputStream is = null;

        try {
            connection = TmdbApi.getPopularMoviesRequest(lang, page);
            manager.preConnect(connection, null);

            connection.connect();
            manager.postConnect();

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                is = connection.getInputStream();
                is = manager.interpretResponseStream(is);
                data = Parser.parse(is);
                resultType = ResultType.OK;
            }
        } catch (IOException e) {
            manager.httpExchangeFailed(e);
            try {
                if (IOUtils.isConnectionAvailable(getContext(), false)) {
                    resultType = ResultType.ERROR;
                } else {
                    resultType = ResultType.NO_INTERNET;
                }
            } catch (Exception innerE) {
                innerE.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeSilently(is);
            if (connection != null) {
                connection.disconnect();
            }
        }
        return new LoadResult<>(resultType, data);
    }
}
