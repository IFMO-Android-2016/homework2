package ru.ifmo.droid2016.tmdb.loader;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.List;
import java.util.Locale;

import ru.ifmo.droid2016.tmdb.api.TmdbApi;
import ru.ifmo.droid2016.tmdb.model.Movie;
import ru.ifmo.droid2016.tmdb.utils.IOUtils;

import com.facebook.stetho.urlconnection.StethoURLConnectionManager;

import org.json.JSONException;

/**
 * Created by Михайлов Никита on 19.11.2016.
 */

public class MoviesLoader extends AsyncTaskLoader<LoadResult<List<Movie>>> {
    int thisPage;


    public MoviesLoader(Context context, int thisPage) {
        super(context);
        this.thisPage = thisPage;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public LoadResult<List<Movie>> loadInBackground(){
        Log.d("MovieLoader", "Loading initiated");
        ResultType resultType = ResultType.ERROR;
        List<Movie> movies = null;
        InputStream load = null;
        HttpURLConnection connection = null;
        final StethoURLConnectionManager stethoManager = new StethoURLConnectionManager("API");

        try{
            connection = TmdbApi.getPopularMoviesRequest(thisPage, Locale.getDefault().getLanguage());
            stethoManager.preConnect(connection, null);
            connection.connect();
            stethoManager.postConnect();

            if(connection.getResponseCode() == HttpURLConnection.HTTP_OK){
                Log.d(TAG, "200 response code");
                load = stethoManager.interpretResponseStream(connection.getInputStream());
                movies = MoviesParser.parseMovies(load);
                resultType = ResultType.OK;
            }else{
                throw new BadResponseException(connection.getResponseMessage());
            }
        }catch(IOException e) {
            if (!IOUtils.isConnectionAvailable(getContext(), false)) {
                resultType = ResultType.NO_INTERNET;
                Log.d(TAG, "No internet connection");
            }
        }catch(BadResponseException | JSONException e){
            Log.d(TAG, e.getCause() + " " + e.getMessage());
        }finally{
            IOUtils.closeSilently(load);
            if(connection != null)
                connection.disconnect();
        }

        return new LoadResult<>(resultType, movies);
    }

    private static final String TAG = "MoviesLoader";

}
