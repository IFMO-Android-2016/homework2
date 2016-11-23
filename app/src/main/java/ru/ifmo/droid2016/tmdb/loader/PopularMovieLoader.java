package ru.ifmo.droid2016.tmdb.loader;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import com.facebook.stetho.Stetho;
import com.facebook.stetho.urlconnection.StethoURLConnectionManager;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.SocketException;
import java.util.List;

import ru.ifmo.droid2016.tmdb.api.TmdbApi;
import ru.ifmo.droid2016.tmdb.model.Movie;
import ru.ifmo.droid2016.tmdb.utils.IOUtils;

public class PopularMovieLoader extends AsyncTaskLoader<LoadResult<List<Movie>>> {

    public static String LOCALE = "locale";

    private LoadResult<List<Movie>> result = null;
    private Bundle bundle;
    private String lang;

    public PopularMovieLoader(Context context, Bundle bundle) {
        super(context);
        this.lang = getLanguage();
    }

    private String getLanguage() {
        String lang;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            lang = getContext().getResources().getConfiguration().getLocales().get(0).getLanguage();
        } else {
            lang = getContext().getResources().getConfiguration().locale.getLanguage();
        }
        return lang;
    }

    @Override
    public void onContentChanged() {
        super.onContentChanged();
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        Log.d("Loader", "Start load");
        String newLang = getLanguage();
        if (result == null || !this.lang.equals(newLang)) {
            this.lang = newLang;
            forceLoad();
        } else
            deliverResult(result);
    }

    @Override
    protected void onForceLoad() {
        super.onForceLoad();
        Log.d("Loader", "Force load");
    }

    @Override
    public void deliverResult(LoadResult<List<Movie>> data) {
        this.result = data;
        super.deliverResult(data);
    }

    @Override
    public LoadResult<List<Movie>> loadInBackground() {
        result = new LoadResult<>(ResultType.ERROR, null);
        HttpURLConnection urlConnection = null;
        try {
            urlConnection = TmdbApi.getPopularMoviesRequest(this.lang);
            urlConnection.connect();
            if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK)
                result = new LoadResult<>(ResultType.OK, PopularMovieParser.parseMovieList(urlConnection.getInputStream()));
        } catch (IOException e) {
            if (!IOUtils.isConnectionAvailable(getContext(), false)) {
                return new LoadResult<>(ResultType.NO_INTERNET, null);
            }
            if (urlConnection != null)
                urlConnection.disconnect();
            e.printStackTrace();
            return new LoadResult<>(ResultType.ERROR, null);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
        return result;
    }
}
