package ru.ifmo.droid2016.tmdb;

import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import ru.ifmo.droid2016.tmdb.loader.LoadResult;
import ru.ifmo.droid2016.tmdb.loader.TmdbLoader;
import ru.ifmo.droid2016.tmdb.loader.TmdbReloader;
import ru.ifmo.droid2016.tmdb.model.Movie;
import ru.ifmo.droid2016.tmdb.utils.RecylcerDividersDecorator;
import ru.ifmo.droid2016.tmdb.utils.TmdbRecyclerAdapter;

/**
 * Экран, отображающий список популярных фильмов из The Movie DB.
 */
public class PopularMoviesActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<LoadResult<List<Movie>>> {

    private TextView errorView;
    private ProgressBar progressBar;
    private RecyclerView recyclerView;

    private TmdbRecyclerAdapter adapter;

    private String language;
    private int page;
    private String error;
    private boolean reloading = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popular_movies);

        errorView = (TextView) findViewById(R.id.error_view);
        errorView.setVisibility(View.GONE);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setVisibility(View.GONE);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new RecylcerDividersDecorator(R.color.colorPrimaryDark));
        recyclerView.setAdapter(adapter = new TmdbRecyclerAdapter(this));

        recyclerView.setRecyclerListener(new RecyclerView.RecyclerListener() {

            @Override
            public void onViewRecycled(RecyclerView.ViewHolder holder) {
                LinearLayoutManager llm = (LinearLayoutManager) recyclerView.getLayoutManager();
                if(llm.findLastVisibleItemPosition() + 10 >= adapter.getItemCount())
                    getSupportLoaderManager().initLoader(++page, null, PopularMoviesActivity.this);
            }
        });

        if(savedInstanceState != null) {
            language = savedInstanceState.getString("language");
            page = savedInstanceState.getInt("page");
            error = savedInstanceState.getString("error");
            String newLanguage = Locale.getDefault().getLanguage();
            if(!language.equals(newLanguage)) {
                language = newLanguage;
                reloading = true;
                getSupportLoaderManager().restartLoader(page, null, this);
            }else {
                if(savedInstanceState.containsKey("movies")) {
                    adapter.updateMovies(fromBytes(savedInstanceState.getByteArray("movies")));
                    if(!error.isEmpty()) {
                        error(error);
                        getSupportLoaderManager().restartLoader(page, null, this);
                    }else
                        loaded();
                }else {
                    getSupportLoaderManager().restartLoader(page, null, this);
                }
            }
        }else {
            language = Locale.getDefault().getLanguage();
            page = 1;
            error = "";
            getSupportLoaderManager().initLoader(page, null, this);
        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("language", language);
        outState.putInt("page", page);
        outState.putString("error", error);
        if(adapter == null || adapter.getListed().isEmpty())
            return;
        outState.putByteArray("movies", toBytes(adapter.getListed()));
    }

    private byte[] toBytes(List<Movie> movies) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            DataOutputStream dos = new DataOutputStream(baos);
            dos.writeInt(movies.size());
            for(Movie movie : movies) {
                dos.writeUTF(movie.posterPath);
                dos.writeUTF(movie.originalTitle);
                dos.writeUTF(movie.overviewText);
                dos.writeUTF(movie.localizedTitle);
            }
            dos.close();
            return baos.toByteArray();
        }catch(Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    private List<Movie> fromBytes(byte[] bytes) {
        List<Movie> movies = new ArrayList<>();
        try {
            ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
            DataInputStream dis = new DataInputStream(bais);
            int size = dis.readInt();
            for(int i = 0; i < size; ++i)
                movies.add(new Movie(dis.readUTF(), dis.readUTF(), dis.readUTF(), dis.readUTF()));
            dis.close();
        }catch(Exception ex) {
            ex.printStackTrace();
        }
        return movies;
    }

    @Override
    public Loader<LoadResult<List<Movie>>> onCreateLoader(int id, Bundle args) {
        return reloading ? new TmdbReloader(this, language, page) : new TmdbLoader(this, language, page);
    }

    @Override
    public void onLoadFinished(Loader<LoadResult<List<Movie>>> loader, LoadResult<List<Movie>> data) {
        switch(data.resultType) {
            case OK:
                reloading = false;
                adapter.updateMovies(data.data);
                loaded();
                break;
            case NO_INTERNET:
                error("Something is wrong with your internet connection.\nTurn your phone after you resolve this issue.");
                break;
            default:
                error("Unexpected error occured. You can do nothing with it );");
                break;
        }
    }

    @Override
    public void onLoaderReset(Loader<LoadResult<List<Movie>>> loader) {
        reset();
    }

    private void loaded() {
        error = "";
        recyclerView.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
        errorView.setVisibility(View.GONE);
    }

    private void reset() {
        error("No movies can found");
    }

    private void error(String message) {
        error = message;
        recyclerView.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);
        errorView.setVisibility(View.VISIBLE);
        errorView.setText(message);
    }

}
