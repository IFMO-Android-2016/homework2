package ru.ifmo.droid2016.tmdb;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import ru.ifmo.droid2016.tmdb.loader.LoadResult;
import ru.ifmo.droid2016.tmdb.loader.MovieLoader;
import ru.ifmo.droid2016.tmdb.loader.ResultType;
import ru.ifmo.droid2016.tmdb.model.Movie;
import ru.ifmo.droid2016.tmdb.utils.RecylcerDividersDecorator;

/**
 * Экран, отображающий список популярных фильмов из The Movie DB.
 */
public class PopularMoviesActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<LoadResult<List<Movie>>> {

    private static final String TAG = PopularMoviesActivity.class.getSimpleName();
    private RecyclerView movies;
    private List<Movie> movieList = new ArrayList<>();
    private MovieAdapter adapter;
    @Nullable
    private MovieLoader loader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initContentView();
        loader = (MovieLoader)
                getSupportLoaderManager().initLoader(0, null, PopularMoviesActivity.this);
    }

    private void initContentView() {
        setContentView(R.layout.activity_popular_movies);
        movies = (RecyclerView) findViewById(R.id.movies);
        movies.setAdapter(adapter = new MovieAdapter());
        movies.setLayoutManager(new LinearLayoutManager(this));
        //noinspection deprecation
        movies.addItemDecoration(
                new RecylcerDividersDecorator(getResources().getColor(android.R.color.darker_gray)));
        movies.setItemAnimator(null);
    }

    @Override
    public Loader<LoadResult<List<Movie>>> onCreateLoader(int id, Bundle args) {
//        Log.i("lul", "onCreateLoader");
        return new MovieLoader(this);
    }

    @Override
    public void onLoadFinished(Loader<LoadResult<List<Movie>>> loader, LoadResult<List<Movie>> result) {
//        Log.i("kek", "loaded");
        switch (result.resultType) {
            case ERROR:
            case NO_INTERNET:
                adapter.error = result.resultType;
                //no break here
            case OK:
                movieList = result.data;
                adapter.onLoad(true);
                break;

            case LAST_PAGE:
                movieList = result.data;
                adapter.onLoad(false);
                break;
        }
    }

    @Override
    public void onLoaderReset(Loader<LoadResult<List<Movie>>> loader) {
//        Log.i("kek", "reset");
    }

    public void onClickTryAgain(View view) {
        adapter.error = null;
        adapter.notifyItemChanged(adapter.count);
        if (loader != null) {
            loader.forceLoad();
        }
    }

    class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {
        private final int MOVIE = 1;
        private final int PROGRESS_BAR = 2;
        private final int ERROR = 3;

        private int count = 0;
        private ResultType error = null;
        private boolean showProgressBar = true;

        @Override
        public int getItemViewType(int position) {
            if (position < count) {
                return MOVIE;
            } else {
                if (error == null) {
                    return PROGRESS_BAR;
                } else {
                    return ERROR;
                }
            }
        }

        @Override
        public MovieAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            switch (viewType) {
                case MOVIE:
                    return new ViewHolder((ViewGroup) getLayoutInflater()
                            .inflate(R.layout.movie_layout, parent, false), viewType);
                case PROGRESS_BAR:
                    return new ViewHolder((ProgressBar) getLayoutInflater()
                            .inflate(R.layout.progress_bar_layout, parent, false), viewType);
                case ERROR:
                    return new ViewHolder((ViewGroup) getLayoutInflater()
                            .inflate(R.layout.error_layout, parent, false), viewType);
                default:
                    Log.wtf(TAG, "onCreateViewHolder/" + viewType);
                    return null;
            }
        }

        @Override
        public void onBindViewHolder(MovieAdapter.ViewHolder holder, int position) {
            holder.lul(position, error);
        }

        @Override
        public int getItemCount() {
            return count + (showProgressBar ? 1 : 0);   // +1 progress bar
        }

        void onLoad(boolean b) {
            showProgressBar = b;
            count = movieList.size();
            notifyDataSetChanged();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            private final int viewType;
            private SimpleDraweeView poster;
            private TextView title;
            private TextView overview;
            private TextView textError;

            ViewHolder(ViewGroup itemView, int viewType) {
                super(itemView);
                this.viewType = viewType;

                switch (viewType) {
                    case MOVIE:
                        poster = (SimpleDraweeView) this.itemView.findViewById(R.id.poster);
                        title = (TextView) this.itemView.findViewById(R.id.title);
                        overview = (TextView) this.itemView.findViewById(R.id.overview);

                        poster.setAspectRatio(2.0f / 3);
                        break;

                    case ERROR:
                        textError = (TextView) this.itemView.findViewById(R.id.textError);
                }
            }

            ViewHolder(ProgressBar progress, int viewType) {
                super(progress);

                this.viewType = viewType;
            }

            void showContent(Movie movie) {
                poster.setImageURI(movie.posterPath);
                title.setText(movie.localizedTitle);
                overview.setText(movie.overviewText);

//                Log.i(TAG, movie.posterPath);
            }

            void showError(ResultType resultType) {
                switch (resultType) {
                    case NO_INTERNET:
                        textError.setText(R.string.no_internet_connection_found);
                        break;

                    case ERROR:
                        textError.setText(R.string.unknown_error_occurred);
                        break;
                }
            }

            void lul(int position, ResultType error) {
                switch (viewType) {
                    case MOVIE:
                        showContent(movieList.get(position));
                        return;

                    case PROGRESS_BAR:
                        if (loader != null) {
                            loader.forceLoad(position);
                        }
                        return;

                    case ERROR:
                        showError(error);
                        //noinspection UnnecessaryReturnStatement
                        return;
                }
            }
        }
    }

    @Override
    protected void onStart() {
        if (loader != null) {
            loader.setLocale(Locale.getDefault().getLanguage());
        }
        Log.d(TAG, "onStart");
        super.onStart();
    }
}
