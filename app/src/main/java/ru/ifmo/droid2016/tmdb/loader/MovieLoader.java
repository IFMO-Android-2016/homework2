package ru.ifmo.droid2016.tmdb.loader;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.facebook.stetho.urlconnection.StethoURLConnectionManager;

import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.List;
import java.util.Locale;

import ru.ifmo.droid2016.tmdb.PopularMoviesActivity;
import ru.ifmo.droid2016.tmdb.api.TmdbApi;
import ru.ifmo.droid2016.tmdb.model.Movie;
import ru.ifmo.droid2016.tmdb.utils.IOUtils;

/**
 * Created by Игорь on 19.11.2016.
 */

public class MovieLoader extends AsyncTaskLoader<LoadResult<List<Movie>>> {
    public static final String LOG_TAG = "MovieLoader";

    private String lang;
    private int page;

    public MovieLoader(Context context, Bundle args) {
        super(context);
        lang = args.getString(PopularMoviesActivity.LANG_TAG, "en");
        page = args.getInt(PopularMoviesActivity.LAST_PAGE_TAG, 1);
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public LoadResult<List<Movie>> loadInBackground() {
        //TODO: what is it?
        final StethoURLConnectionManager stethoManager = new StethoURLConnectionManager("API");

        Log.d(LOG_TAG, "start loading in background");

        ResultType resultType = ResultType.ERROR;
        List<Movie> resultList = null;
        HttpURLConnection connection = null;
        InputStream in = null;

        try {
            connection = TmdbApi.getPopularMoviesRequest(lang, page);

            stethoManager.preConnect(connection, null);
            connection.connect();
            stethoManager.postConnect();

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                in = connection.getInputStream();
                in = stethoManager.interpretResponseStream(in);

                resultList = MovieDOMParser.parseMovies(in);

                resultType = ResultType.OK;
            } else {
                throw new BadResponseException("HTTP: " + connection.getResponseCode() + ", "
                        + connection.getResponseMessage());
            }
        } catch (IOException e) {
            stethoManager.httpExchangeFailed(e);
            if (IOUtils.isConnectionAvailable(getContext(), false)) {
                resultType = ResultType.ERROR;
            } else {
                resultType = ResultType.NO_INTERNET;
            }
        } catch (BadResponseException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeSilently(in);
            if (connection != null) {
                connection.disconnect();
            }
        }

        return new LoadResult<>(resultType, resultList);
    }
}
