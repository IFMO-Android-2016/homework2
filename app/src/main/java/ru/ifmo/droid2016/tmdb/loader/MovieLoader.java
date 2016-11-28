package ru.ifmo.droid2016.tmdb.loader;

import android.app.LoaderManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;
import android.widget.ProgressBar;
import org.json.JSONException;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.List;
import java.util.Locale;

import ru.ifmo.droid2016.tmdb.api.TmdbApi;
import ru.ifmo.droid2016.tmdb.model.Movie;

/**
 * Created by nikita on 20.11.16.
 */

public class MovieLoader extends AsyncTaskLoader<LoadResult<List<Movie>>> {

	private final static String TAG = "Movies";
	private LoadResult<List<Movie>> data;

	public MovieLoader(Context context) {
		super(context);
	}

	@Override
	public LoadResult<List<Movie>> loadInBackground() {
		HttpURLConnection connection = null;
		//data = null;
		try {
			connection = TmdbApi.getPopularMoviesRequest(Locale.getDefault().getLanguage());
			Log.d(TAG, "Request: " + connection.getURL());
			List<Movie> movies = MoviesParser.parseMovies(new BufferedInputStream(connection.getInputStream()));
			data = new LoadResult<>(ResultType.OK, movies);
		} catch (IOException e) {
			e.printStackTrace();
			data = new LoadResult<>(ResultType.NO_INTERNET, null);
		} catch (JSONException e) {
			data = new LoadResult<>(ResultType.ERROR, null);
		} finally {
			if (connection != null)
				connection.disconnect();
		}
		return data;
	}

	static Bitmap getImages(String url) throws IOException {
		HttpURLConnection connection = null;
		Bitmap bitmap = null;
		try {
			connection = TmdbApi.getImage(url);
			bitmap = BitmapFactory.decodeStream(connection.getInputStream());
		} finally {
			if (connection != null)
				connection.disconnect();
		}
		return bitmap;
	}

	@Override
	public void deliverResult(LoadResult<List<Movie>> data) {
		if (isReset())
			return;
		this.data = data;
		super.deliverResult(data);
	}

	@Override
	protected void onStartLoading() {
		super.onStartLoading();
		if (data != null) {
			deliverResult(data);
		} else {
			forceLoad();
		}
	}

	@Override
	protected void onStopLoading() {
		super.onStopLoading();
		cancelLoad();
	}

	protected void onReset() {
		super.onReset();

		onStopLoading();
	}
}
