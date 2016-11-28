package ru.ifmo.droid2016.tmdb.loader;

import android.support.v4.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import com.facebook.stetho.urlconnection.StethoURLConnectionManager;

import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import ru.ifmo.droid2016.tmdb.api.TmdbApi;
import ru.ifmo.droid2016.tmdb.model.Movie;
import ru.ifmo.droid2016.tmdb.utils.IOUtils;

/**
 * Created by 1 on 22/11/16.
 */

public class JSONLoader extends AsyncTaskLoader<LoadResult<List<Movie>>>{

    private String lang;

    public JSONLoader(Context context, String lang) {
        super(context);
        this.lang = lang;
    }

    @Override
    protected void onStartLoading(){
        forceLoad();
    }

    @Override
    public LoadResult<List<Movie>> loadInBackground(){
        final StethoURLConnectionManager stethoManager = new StethoURLConnectionManager("API");
        ResultType resultType = ResultType.ERROR;
        List<Movie> data = new ArrayList<>();
        HttpURLConnection connection = null;
        InputStream in = null;
        lang = Locale.getDefault().getLanguage();

        try {
            connection = TmdbApi.getPopularMoviesRequest(lang);
            stethoManager.preConnect(connection, null);
            connection.connect();
            stethoManager.postConnect();
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK){
                in = connection.getInputStream();
                in = stethoManager.interpretResponseStream(in);
                data.addAll(JSONParser.parseMovies(in));
                resultType = ResultType.OK;
            } else {
                resultType = ResultType.NO_INTERNET;
                throw new BadResponseException("HTTP: " + connection.getResponseCode() + ", " + connection.getResponseMessage());
            }
        } catch (MalformedURLException e){
            resultType = ResultType.ERROR;
            Log.e(TAG, "MalformedURLException");
        } catch (IOException e) {
            resultType = ResultType.ERROR;
            Log.e(TAG, "IOException");
        } catch (BadResponseException e) {
            Log.e(TAG, "BadResponseException");
        } catch (JSONException e) {
            resultType = ResultType.ERROR;
            Log.e(TAG, "JSONException");
        } finally {
            IOUtils.closeSilently(in);
            if (connection != null){
                connection.disconnect();
            }
        }
        return new LoadResult<>(resultType, data);
    }

    private final String TAG = "Movies";
}
