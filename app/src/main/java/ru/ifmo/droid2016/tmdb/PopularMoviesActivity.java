package ru.ifmo.droid2016.tmdb;

import android.app.LoaderManager;
import android.content.Loader;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ru.ifmo.droid2016.tmdb.apiLoaders.apiLoaderGetPopularMovies;
import ru.ifmo.droid2016.tmdb.loader.LoadResult;
import ru.ifmo.droid2016.tmdb.loader.ResultType;
import ru.ifmo.droid2016.tmdb.model.Movie;

/**
 * Экран, отображающий список популярных фильмов из The Movie DB.
 */
public class PopularMoviesActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<LoadResult<List<Movie>>> {

    RecyclerView rv;
    LinearLayoutManager llm;
    RVAdapter radapter;
    List<Movie> currentMovies;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popular_movies);
        rv = (RecyclerView) findViewById(R.id.rv);
        rv.setHasFixedSize(true);
        llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);
        currentMovies = new ArrayList<>();
        radapter = new RVAdapter(currentMovies);
        rv.setAdapter(radapter);
        getLoaderManager().initLoader(0, null, this);
        getLoaderManager().getLoader(0).forceLoad();
    }


    @Override
    public Loader<LoadResult<List<Movie>>> onCreateLoader(int id, Bundle args) {
        Loader<LoadResult<List<Movie>>> loader = new apiLoaderGetPopularMovies(this, 1);
        return loader;
    }

    @Override
    public void onLoadFinished(Loader<LoadResult<List<Movie>>> loader, LoadResult<List<Movie>> data) {
        if (data.resultType == ResultType.OK) {
            currentMovies.addAll(0, data.data);
        }

    }

    @Override
    public void onLoaderReset(Loader<LoadResult<List<Movie>>> loader) {

    }
}



class RVAdapter extends RecyclerView.Adapter<RVAdapter.PersonViewHolder> {

    final List<Movie> movies;

    public RVAdapter(List<Movie> movies) {
        this.movies = movies;
    }

    @Override
    public PersonViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.film_card, parent, false);
        PersonViewHolder ps = new PersonViewHolder(v);
        return ps;
    }

    @Override
    public void onBindViewHolder(PersonViewHolder holder, int position) {
        holder.image.setImageResource(R.drawable.picture);
        holder.title.setText(movies.get(position).originalTitle);
        holder.discribe.setText(movies.get(position).overviewText);
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }


   /* @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }*/

    class PersonViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView title;
        TextView discribe;

        public PersonViewHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.poster);
            title = (TextView) itemView.findViewById(R.id.title);
            discribe = (TextView) itemView.findViewById(R.id.discribe);
        }
    }
}
