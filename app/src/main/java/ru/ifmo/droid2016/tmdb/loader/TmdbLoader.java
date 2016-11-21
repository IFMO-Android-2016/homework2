package ru.ifmo.droid2016.tmdb.loader;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.facebook.stetho.urlconnection.StethoURLConnectionManager;

import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import ru.ifmo.droid2016.tmdb.api.TmdbApi;
import ru.ifmo.droid2016.tmdb.model.Movie;
import ru.ifmo.droid2016.tmdb.utils.IOUtils;

import static ru.ifmo.droid2016.tmdb.loader.ResultType.ERROR;
import static ru.ifmo.droid2016.tmdb.loader.ResultType.OK;

/**
 * 12 of November
 */

public class TmdbLoader extends AsyncTaskLoader<LoadResult<List<Movie>>> {

    private static final String TAG = "BackgroundLoader";
    private final String lang;
    private final int page;
    private boolean loaded = false;
    private LoadResult<List<Movie>> listLoadResult;


    public TmdbLoader(Context context, String lang, int page) {
        super(context);
        this.lang = lang;
        this.page = page;

    }

    @Override
    protected void onStartLoading() {
        Log.i(TAG, "onStartLoading: [loaded=" + loaded + "|id=" + getId() + "|page=" + page + "]");
        if (!loaded)
            forceLoad();
        else
            deliverResult(listLoadResult);
    }


    @Override
    public LoadResult<List<Movie>> loadInBackground() {
        StethoURLConnectionManager stethoManager = new StethoURLConnectionManager(TAG);
        ResultType ans = ERROR;
        List<Movie> data = new ArrayList<>();

        HttpsURLConnection connection = null;
        InputStream in = null;

        try {
            connection = TmdbApi.getPopularMoviesRequest(lang, page);
            Log.i(TAG, "Performing request: " + connection.getURL());

            stethoManager.preConnect(connection, null);
            connection.connect();
            stethoManager.postConnect();

            if (connection.getResponseCode() == HttpsURLConnection.HTTP_OK) {

                in = stethoManager.interpretResponseStream(connection.getInputStream());

                data = MoviesDomParser.parseMovies(in);

                ans = ResultType.OK;

            } else {
                throw new BadResponseException("HTTP: " + connection.getResponseCode()
                        + ", " + connection.getResponseMessage());
            }
        } catch (IOException e) {
            Log.e(TAG, "loadInBackground: IOE", e);
            stethoManager.httpExchangeFailed(e);
            if (IOUtils.isConnectionAvailable(getContext(), false)) {
                ans = ResultType.ERROR;
            } else {
                ans = ResultType.NO_INTERNET;
            }
        } catch (BadResponseException e) {
            Log.e(TAG, "Failed to get Tmdb: ", e);
        } catch (JSONException e) {
            Log.e(TAG, "Parsing filed: ", e);
        } finally {
            IOUtils.closeSilently(in);
            if (connection != null) {
                connection.disconnect();
            }
        }

        if (ans == OK) loaded = true;

        listLoadResult = new LoadResult<>(ans, data, page);
        return listLoadResult;
    }
}
