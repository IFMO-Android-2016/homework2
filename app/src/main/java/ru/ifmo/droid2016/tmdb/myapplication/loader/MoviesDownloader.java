package ru.ifmo.droid2016.tmdb.myapplication.loader;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.v4.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;
import org.json.JSONException;
import org.json.JSONObject;
import ru.ifmo.droid2016.tmdb.myapplication.api.TmdbApi;
import ru.ifmo.droid2016.tmdb.myapplication.model.Movie;
import ru.ifmo.droid2016.tmdb.myapplication.model.ResponseParser;
import ru.ifmo.droid2016.tmdb.myapplication.utils.IOUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Locale;

public class MoviesDownloader extends AsyncTaskLoader<LoadResult<List<Movie>>> {

    private final int page;
    public static File directory = new File
            (Environment.getExternalStorageDirectory().getAbsolutePath() + "/tmdb");

    private static String uri_begin = "http://image.tmdb.org/t/p/w300";

    public MoviesDownloader(Context context, int page) {
        super(context);
        this.page = page;
    }


    @Override
    public LoadResult<List<Movie>> loadInBackground() {
        try {
            String language = Locale.getDefault().getLanguage();
            HttpURLConnection connection = TmdbApi.getPopularMoviesRequest(language, page);
            InputStream inputStream = connection.getInputStream();
            String content = IOUtils.readToString(inputStream, "UTF-8");
            IOUtils.closeSilently(inputStream);
            LoadResult<List<Movie>> result = new LoadResult<>(ResultType.OK, ResponseParser.parser(new JSONObject(content)));
            assert result.data != null;
            for (Movie movie : result.data) {
                saveImageFromURL(movie.posterPath);
            }
            return result;
        } catch (IOException e) {
            e.printStackTrace();
            return new LoadResult<>(ResultType.ERROR, null);
        } catch (JSONException e) {
            e.printStackTrace();
            return new LoadResult<>(ResultType.NO_INTERNET, null);
        }
    }

    private void saveImageFromURL(String posterName) {
        //check is image downloaded
        File file = new File(directory, posterName);
        if (file.exists() && !file.isDirectory()) {
            Log.d("isDir", "" + file.isDirectory());
            Log.d("already saved", posterName);
            return;
        }
        if (file.isDirectory()) {
            Log.d("deleted", "" + file.delete());
        }
        try {
            //download
            String src = uri_begin + posterName;
            Log.d("save", src);
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url
                    .openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap bmp = BitmapFactory.decodeStream(input);

            //write
            //FileOutputStream out = getContext().openFileOutput(posterName, Context.MODE_PRIVATE);
            file = new File(directory, posterName);
            Log.d("trying save", "" + file.createNewFile());
            Log.d("isDir", "" + file.isDirectory());
            FileOutputStream out = new FileOutputStream(file);

            assert bmp != null;
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.close();
            Log.d("saved", directory + posterName);
        } catch (Exception e) {
            Log.d("NOT SAVED", directory + posterName);
            e.printStackTrace();
        }
    }


}
