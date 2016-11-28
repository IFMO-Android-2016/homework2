package ru.ifmo.droid2016.tmdb;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ru.ifmo.droid2016.tmdb.loader.LoadResult;
import ru.ifmo.droid2016.tmdb.loader.MovieLoader;
import ru.ifmo.droid2016.tmdb.loader.ResultType;
import ru.ifmo.droid2016.tmdb.model.Movie;


/**
 * Экран, отображающий список популярных фильмов из The Movie DB.
 */
public class PopularMoviesActivity extends FragmentActivity implements LoaderManager.LoaderCallbacks<LoadResult<List<Movie>>> {
    private RecyclerView myView;
    private SimpleAdapter adapter;
    private TextView errorField;
    private BroadcastReceiver receiver;
    private Loader<LoadResult<List<Movie>>> loader;
    private ProgressBar progressBar;
    private Parcelable recyclerState;
    private static int LOADER_ID = 0;
    private boolean was = false;
    private static final String NO_INTERNET_ERROR = "Проверьте подключение к сети";
    private static final String ANOTHER_ERROR = "Данным не суждено загрузиться";
    private static final String LIST_STATE_KEY = "list position";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popular_movies);

        myView = (RecyclerView) findViewById(R.id.movies_recycle_view);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        errorField = (TextView) findViewById(R.id.error_field);
        if (savedInstanceState != null)
            recyclerState = savedInstanceState.getParcelable(LIST_STATE_KEY);

        myView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new SimpleAdapter(this, new ArrayList<Movie>());
        myView.setAdapter(adapter);
        progressBar.setVisibility(ProgressBar.VISIBLE);
        loader = getSupportLoaderManager().initLoader(LOADER_ID, null, this);

        if (receiver == null) {
            receiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    updateLang();
                }
            };
            IntentFilter intentFilter = new IntentFilter(Intent.ACTION_LOCALE_CHANGED);
            registerReceiver(receiver, intentFilter);
        }
    }

    void updateLang() {
        recyclerState = myView.getLayoutManager().onSaveInstanceState();
        loader = getSupportLoaderManager().restartLoader(LOADER_ID, null, this);
        //loader.forceLoad();

    }

    @Override
    public Loader<LoadResult<List<Movie>>> onCreateLoader(int id, Bundle args) {
        Loader<LoadResult<List<Movie>>> loader = new MovieLoader(this);
        return loader;
    }

    @Override
    public void onLoadFinished(Loader<LoadResult<List<Movie>>> loader, LoadResult<List<Movie>> data) {
        progressBar.setVisibility(ProgressBar.INVISIBLE);
        if (data.resultType == ResultType.OK) {
            adapter = new SimpleAdapter(this, data.data);
            myView.setAdapter(adapter);
            if (recyclerState != null)
                myView.getLayoutManager().onRestoreInstanceState(recyclerState);
            myView.setVisibility(RecyclerView.VISIBLE);
        } else if (data.resultType == ResultType.NO_INTERNET) {
            errorField.setText(NO_INTERNET_ERROR);
            errorField.setVisibility(TextView.VISIBLE);
        } else {
            errorField.setText(ANOTHER_ERROR);
            errorField.setVisibility(TextView.VISIBLE);
        }
    }

    @Override
    public void onLoaderReset(Loader<LoadResult<List<Movie>>> loader) {
    }


    public class SimpleAdapter extends RecyclerView.Adapter<SimpleAdapter.MyViewHolder> {
        private final List<Movie> movies;
        private final LayoutInflater li;


        private SimpleAdapter(Context context, List<Movie> tmp) {
            movies = tmp;
            li = LayoutInflater.from(context);
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new MyViewHolder(li.inflate(R.layout.item_list, parent, false));
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            Movie tmp = movies.get(position);
            holder.overview.setText(tmp.overviewText);
            holder.title.setText(tmp.localizedTitle);
            holder.image.setImageBitmap(tmp.image);
        }

        @Override
        public int getItemCount() {
            return movies.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            final TextView title;
            final ImageView image;
            final TextView overview;
            MyViewHolder(View itemView) {
                super(itemView);

                title = (TextView) itemView.findViewById(R.id.title);
                image = (ImageView) itemView.findViewById(R.id.movie_image);
                overview = (TextView) itemView.findViewById(R.id.overview);
            }
        }

        ArrayList<String> get_overview() {
            ArrayList<String> list = new ArrayList<>();
            for (Movie m: movies) {
                list.add(m.overviewText);
            }
            return list;
        }

        ArrayList<String> get_title() {
            ArrayList<String> list = new ArrayList<>();
            for (Movie m: movies) {
                list.add(m.localizedTitle);
            }
            return list;
        }

        ArrayList<String> get_original_title() {
            ArrayList<String> list = new ArrayList<>();
            for (Movie m: movies) {
                list.add(m.originalTitle);
            }
            return list;
        }

        ArrayList<String> get_poster_path() {
            ArrayList<String> list = new ArrayList<>();
            for (Movie m: movies) {
                list.add(m.posterPath);
            }
            return list;
        }

        Parcelable[] get_image() {
            Parcelable[] list = new Parcelable[movies.size()];
            for (int i = 0; i < movies.size(); i++) {
                list[i] = movies.get(i).image;
            }
            return list;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (receiver != null) {
            unregisterReceiver(receiver);
            receiver = null;
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelable(LIST_STATE_KEY, myView.getLayoutManager().onSaveInstanceState());
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }
}
