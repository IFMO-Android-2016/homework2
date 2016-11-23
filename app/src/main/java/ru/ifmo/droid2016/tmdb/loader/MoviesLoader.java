package ru.ifmo.droid2016.tmdb.loader;

/**
 * Created by Дарья on 22.11.2016.
 */
import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.facebook.stetho.urlconnection.StethoURLConnectionManager;

import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.util.List;
import java.util.Locale;

import ru.ifmo.droid2016.tmdb.api.*;
import ru.ifmo.droid2016.tmdb.utils.*;
import ru.ifmo.droid2016.tmdb.model.*;


public class MoviesLoader extends AsyncTaskLoader<LoadResult<List<Movie>>> {

    public MoviesLoader(Context context){
        super(context);
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public LoadResult<List<Movie>> loadInBackground() {
        final StethoURLConnectionManager stethoManager = new StethoURLConnectionManager("API");

        ResultType resultType = ResultType.ERROR;
        List<Movie> data = null;

        HttpURLConnection connection = null;
        InputStream inputStream = null;

        try {
            String lang = Locale.getDefault().getLanguage();
            connection = TmdbApi.getPopularMoviesRequest(lang);

            stethoManager.preConnect(connection, null);
            connection.connect();
            stethoManager.postConnect();

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {

                inputStream = connection.getInputStream();
                inputStream = stethoManager.interpretResponseStream(inputStream);

                data = JsonParser.parse(inputStream);

                resultType = ResultType.OK;



            } else {
                // consider all other codes as errors
                throw new BadResponseException("HTTP: " + connection.getResponseCode()
                        + ", " + connection.getResponseMessage());
            }

        } catch (IOException e) {
            stethoManager.httpExchangeFailed(e);
            try {
                if (!IOUtils.isConnectionAvailable(getContext(), false)) {
                    resultType = ResultType.NO_INTERNET;
                }
            } catch (Exception er){
                er.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeSilently(inputStream);
            if (connection != null) {
                connection.disconnect();
            }
        }

        return new LoadResult<>(resultType, data);
    }

}
