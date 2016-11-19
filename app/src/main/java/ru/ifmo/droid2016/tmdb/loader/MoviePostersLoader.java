package ru.ifmo.droid2016.tmdb.loader;

import android.content.Context;
import android.content.res.Resources;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.facebook.stetho.urlconnection.StethoURLConnectionManager;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import ru.ifmo.droid2016.tmdb.api.TmdbApi;
import ru.ifmo.droid2016.tmdb.model.Movie;
import ru.ifmo.droid2016.tmdb.utils.IOUtils;

/**
 * Created by Lenovo on 09.11.2016.
 */
public class MoviePostersLoader extends AsyncTaskLoader<LoadResult<List<Movie>>> {


    List<Movie> data;
    private String TAG = "Movie";
    private String curLanguage;
    private int[] pages;
    private int curPage;

    public MoviePostersLoader(Context context, int[] pages, String curLanguage) {
        super(context);
        this.pages = pages;
        curPage = 0;
        data = new ArrayList<>();
        this.curLanguage = null;
//        Log.d("Language######", curLanguage);
    }


    @Override
    public LoadResult<List<Movie>> loadInBackground() {
        Log.d("######Loader", "Load in Background");
        final StethoURLConnectionManager stethoManager = new StethoURLConnectionManager("API");
        ResultType resultType = ResultType.ERROR;
        List<Movie> currentData = new ArrayList<>();
        int[] pagesToDownload = new int[0];
        if (!Resources.getSystem().getConfiguration().locale.getLanguage().equals(curLanguage)) {
            pagesToDownload = pages;
            curLanguage = Resources.getSystem().getConfiguration().locale.getLanguage();
            data = new ArrayList<>();
            Log.d("Did this", "YEEEEEEEEEEEEEES");
        }
        HttpURLConnection connection = null;
        InputStream in = null;

        try {
            for (int curPage : pagesToDownload) {
                connection = TmdbApi.getPopularMoviesRequest(curLanguage);

                Log.d("Movies##########", "Performing request: " + connection.getURL());

                stethoManager.preConnect(connection, null);
                connection.connect();
                stethoManager.postConnect();

                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    in = connection.getInputStream();
                    in = stethoManager.interpretResponseStream(in);
                    Log.d("Response", "Ok");
                    currentData.addAll(MoviePosterParser.parseMovies(in));
                } else {
                    throw new BadResponseException("HTTP: " + connection.getResponseCode()
                            + ", " + connection.getResponseMessage());
                }
            }
            resultType = ResultType.OK;
        } catch (MalformedURLException e) {
            Log.e(TAG, "Failed to get movies", e);
        } catch (IOException e) {
            stethoManager.httpExchangeFailed(e);
            if (IOUtils.isConnectionAvailable(getContext(), false)) {
                resultType = ResultType.ERROR;
            } else {
                resultType = ResultType.NO_INTERNET;
            }
        } catch (Exception e) {
            Log.e(TAG, "Failed to get movies: ", e);
        } finally {
            IOUtils.closeSilently(in);
            if (connection != null) {
                connection.disconnect();
            }
        }

        data.addAll(currentData);
        return new LoadResult<>(resultType, data);
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }
}
