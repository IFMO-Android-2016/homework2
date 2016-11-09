package ru.ifmo.droid2016.tmdb;

import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;

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
        movies.addItemDecoration(
                new RecylcerDividersDecorator(getResources().getColor(android.R.color.darker_gray)));
    }

    @Override
    public Loader<LoadResult<List<Movie>>> onCreateLoader(int id, Bundle args) {
        Log.i("lol", "onCreateLoader");
        return new MovieLoader(this);
    }

    @Override
    public void onLoadFinished(Loader<LoadResult<List<Movie>>> loader, LoadResult<List<Movie>> result) {
        Log.i("kek", "loaded");
        switch (result.resultType) {
            case OK:
                movieList = result.data;
                adapter.onLoad(true);
                break;

            case ERROR:
            case NO_INTERNET:
                adapter.error = result.resultType;
                adapter.notifyDataSetChanged();
                break;

            case LAST_PAGE:
                movieList = result.data;
                adapter.onLoad(false);
                break;
        }
    }

    @Override
    public void onLoaderReset(Loader<LoadResult<List<Movie>>> loader) {
        Log.i("kek", "reset");
    }

    public void onClickTryAgain(View view) {
        adapter.error = null;
        adapter.notifyItemChanged(adapter.count);
        loader.forceLoad();
    }

    class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {
        private int count = 0;
        private ResultType error = null;
        private boolean showProgressBar = true;

        @Override
        public MovieAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(getLayoutInflater().inflate(R.layout.movie_layout, parent, false));
        }

        @Override
        public void onBindViewHolder(MovieAdapter.ViewHolder holder, int position) {
            if (position < count) {
                holder.showContent(movieList.get(position));
            } else {
                if (error == null) {
                    holder.showProgressBar();
                    loader.forceLoad(position);
                } else {
                    holder.showError(error);
                }
            }
        }

        @Override
        public int getItemCount() {
            return count + (showProgressBar ? 1 : 0);   // +1 progress bar
        }

        public void onLoad(boolean b) {
            showProgressBar = b;
            count = movieList.size();
            notifyDataSetChanged();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            private final ViewGroup itemView;

            private final LinearLayout content, error;
            private final ProgressBar progress;
            private final SimpleDraweeView poster;
            private final TextView title, overview, internet, unknown;

            public ViewHolder(View itemView) {
                super(itemView);
                this.itemView = (ViewGroup) itemView;

                content = (LinearLayout) this.itemView.findViewById(R.id.content);
                poster = (SimpleDraweeView) this.itemView.findViewById(R.id.poster);
                title = (TextView) this.itemView.findViewById(R.id.title);
                overview = (TextView) this.itemView.findViewById(R.id.overview);
                progress = (ProgressBar) this.itemView.findViewById(R.id.progress);

                error = (LinearLayout) this.itemView.findViewById(R.id.error);
                internet = (TextView) this.itemView.findViewById(R.id.internet);
                unknown = (TextView) this.itemView.findViewById(R.id.unknown);

                poster.setAspectRatio(2.0f / 3);
            }

            void showProgressBar() {
                content.setVisibility(View.GONE);
                error.setVisibility(View.GONE);
                progress.setVisibility(View.VISIBLE);
            }

            void showContent(Movie movie) {
                content.setVisibility(View.VISIBLE);
                error.setVisibility(View.GONE);
                progress.setVisibility(View.GONE);

                poster.setImageURI(movie.posterPath);
                title.setText(movie.localizedTitle);
                overview.setText(movie.overviewText);

                Log.i(TAG, movie.posterPath);
            }

            void showError(ResultType resultType) {
                content.setVisibility(View.GONE);
                error.setVisibility(View.VISIBLE);
                progress.setVisibility(View.GONE);

                switch (resultType) {
                    case NO_INTERNET:
                        unknown.setVisibility(View.GONE);
                        internet.setVisibility(View.VISIBLE);
                        break;

                    case ERROR:
                        internet.setVisibility(View.GONE);
                        unknown.setVisibility(View.VISIBLE);
                        break;
                }
            }
        }
    }

    @Override
    protected void onResume() {
        Log.d(TAG, "onResume");
        super.onResume();
    }
}
