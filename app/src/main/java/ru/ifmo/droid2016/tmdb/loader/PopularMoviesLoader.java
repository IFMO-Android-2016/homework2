package ru.ifmo.droid2016.tmdb.loader;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.facebook.stetho.urlconnection.StethoURLConnectionManager;

import org.json.JSONException;

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
 * Created by Елена on 23.11.2016.
 */
public class PopularMoviesLoader extends AsyncTaskLoader<LoadResult<List<Movie>>> {
    private final int page;
    private final static String TAG = "MoviesLoader";

    public PopularMoviesLoader(Context context, int page) {
        super(context);
        this.page = page;
    }

    @Override
    protected void onStartLoading(){
        forceLoad();
    }

    @Override
    public LoadResult<List<Movie>> loadInBackground() {
        StethoURLConnectionManager stethoURLConnectionManager = new StethoURLConnectionManager("API");
        Log.d(TAG, "Load pages " + page);
        ResultType resultType = ResultType.ERROR; //default
        final List<Movie> data = new ArrayList<>();
        HttpURLConnection connection;
        InputStream in = null;

        for (int curPage = 1; curPage <= page; curPage++){
            try {
                connection = TmdbApi.getPopularMoviesRequest(Locale.getDefault().getLanguage(), curPage);
                stethoURLConnectionManager.preConnect(connection, null);
                connection.connect();
                stethoURLConnectionManager.postConnect();

                if (connection.getResponseCode() == 200) {
                    in = connection.getInputStream();
                    in = stethoURLConnectionManager.interpretResponseStream(in);
                    data.addAll(Parser.parse(in));
                    resultType = ResultType.OK;
                } else {
                    stethoURLConnectionManager = new StethoURLConnectionManager("API");
                    throw
                            new BadResponseException("HTTP: " + connection.getResponseCode());
                }

                stethoURLConnectionManager = new StethoURLConnectionManager("API");
            } catch (JSONException | BadResponseException e) {
                Log.e(TAG, "Sorry, we couldn't download movies :C");
            } catch (IOException e) {
                stethoURLConnectionManager.httpExchangeFailed(e);
                if (IOUtils.isConnectionAvailable(getContext() , false)) {
                    resultType = ResultType.ERROR;
                } else {
                    resultType = ResultType.NO_INTERNET;
                }
            }
        }
        return new LoadResult<>(resultType, data);
    }
}
