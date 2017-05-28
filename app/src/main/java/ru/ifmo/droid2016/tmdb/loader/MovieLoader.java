package ru.ifmo.droid2016.tmdb.loader;

import android.support.v4.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import com.facebook.stetho.urlconnection.StethoURLConnectionManager;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.io.IOException;

import ru.ifmo.droid2016.tmdb.api.TmdbApi;
import ru.ifmo.droid2016.tmdb.model.Movie;
import ru.ifmo.droid2016.tmdb.utils.IOUtils;


public class MovieLoader extends AsyncTaskLoader<LoadResult<List<Movie>>> {//resultType && List of Movies
    private int neededPage;

    public MovieLoader(Context context, int page) {
        super(context);
        neededPage = page;
    }
    protected  void onStartLoading(){forceLoad();}

    public LoadResult<List<Movie>> loadInBackground() {
        HttpURLConnection connection = null;
        InputStream inStream = null;
        List<Movie> data_ = new ArrayList<>();

        ResultType resultType = ResultType.ERROR;
        //Log.e("load", "Entering page load: " + neededPage);
        for (int iPage = 0; iPage < neededPage; iPage++) {
            final StethoURLConnectionManager stethoManager = new StethoURLConnectionManager("API");
            try {
                connection = TmdbApi.getPopularMoviesRequest(Locale.getDefault().getLanguage(), iPage + 1);
                //Log.e("language", "" + Locale.getDefault().getLanguage());
                stethoManager.preConnect(connection, null);
                connection.connect();
                stethoManager.postConnect();

                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    inStream = connection.getInputStream();
                    inStream = stethoManager.interpretResponseStream(inStream);
                    data_.addAll(MovieParser.parse(inStream));
                    resultType = ResultType.OK;
                } else {
                    throw new BadResponseException("HTTP: " + connection.getResponseCode()
                            + ", " + connection.getResponseMessage());
                }

            } catch (BadResponseException e) {
                Log.e("load", "Failed to get Information: ", e);
                resultType = ResultType.ERROR;
            } catch (IOException e) {
                stethoManager.httpExchangeFailed(e);
                if (IOUtils.isConnectionAvailable(getContext(), false))
                    resultType = ResultType.ERROR;
                else
                    resultType = ResultType.NO_INTERNET;
                Log.e("load", "Failed to get Information: " + "connection: " + IOUtils.isConnectionAvailable(getContext(), false), e);
            } catch (Exception e) {
                Log.e("load", "Failed to get Information: ", e);
            }
        }
        return new LoadResult<>(resultType, data_);
    }
}
