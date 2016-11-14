package ru.ifmo.droid2016.tmdb;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import ru.ifmo.droid2016.tmdb.loader.LoadResult;
import ru.ifmo.droid2016.tmdb.model.Movie;

import static ru.ifmo.droid2016.tmdb.utils.Constants.ITEMS_ON_PAGE;

class RecyclerAdapter
        extends RecyclerView.Adapter<RecyclerAdapter.MovieViewHolder> {

    private final LayoutInflater layoutInflater;

    @NonNull
    private SparseArray<List<Movie>> movies;


    RecyclerAdapter(Context context) {
        this.movies = new SparseArray<>();
        this.layoutInflater = LayoutInflater.from(context);
    }

    void addMovies(@NonNull LoadResult<List<Movie>> m) {
        /*if (movies.get(m.page - 1) != null)
            adding existing List<Movies>, loaders going wrong".
            That will not break anything but on wai~ why?
        */
        movies.put(m.page - 1, m.data);
        notifyDataSetChanged();
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return MovieViewHolder.newInstance(layoutInflater, parent);
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {
        final Movie movie = movies.get(position / ITEMS_ON_PAGE).get(position % ITEMS_ON_PAGE);
        holder.titleView.setText(movie.getLocalizedTitle());
        holder.imageView.setImageURI(movie.getPosterPath());
        holder.descriptionView.setText(movie.getOverviewText());
    }

    @Override
    public int getItemCount() {
        return movies.size() * ITEMS_ON_PAGE;
    }

    void setMovies(@NonNull LoadResult<List<Movie>> tempMovies) {
        this.movies = new SparseArray<>();
        addMovies(tempMovies);
    }

    static class MovieViewHolder extends RecyclerView.ViewHolder {

        final SimpleDraweeView imageView;
        final TextView titleView;
        final TextView descriptionView;

        private MovieViewHolder(View itemView) {
            super(itemView);
            imageView = (SimpleDraweeView) itemView.findViewById(R.id.movie_image);
            titleView = (TextView) itemView.findViewById(R.id.movie_title);
            descriptionView = (TextView) itemView.findViewById(R.id.movie_description);
        }

        static MovieViewHolder newInstance(LayoutInflater layoutInflater, ViewGroup parent) {
            final View view = layoutInflater.inflate(R.layout.movie_item, parent, false);
            return new MovieViewHolder(view);
        }
    }


}
