package ru.ifmo.droid2016.tmdb;

        import android.content.Context;
        import android.support.annotation.NonNull;
        import android.support.v7.widget.RecyclerView;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.TextView;

        import com.facebook.drawee.view.SimpleDraweeView;

        import java.util.Collections;
        import java.util.List;

        import ru.ifmo.droid2016.tmdb.model.Movie;

public class MovieRecyclerAdapter
        extends RecyclerView.Adapter<MovieRecyclerAdapter.MovieViewHolder> {

    private final Context context;
    private final LayoutInflater layoutInflater;

    @NonNull
    private List<Movie> movies = Collections.emptyList();

    public MovieRecyclerAdapter(Context context) {
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
    }

    public void setMovies(@NonNull List<Movie> movies) {
        this.movies = movies;
        notifyDataSetChanged();
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return MovieViewHolder.newInstance(layoutInflater, parent);
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {
        final Movie movie = movies.get(position);
        holder.originalTitleView.setText(movie.originalTitle);
        holder.localTitleView.setText(movie.localizedTitle);
        holder.descriptionView.setText(movie.overviewText);
        holder.imageView.setImageURI(movie.posterPath);
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    static class MovieViewHolder extends RecyclerView.ViewHolder {

        final SimpleDraweeView imageView;
        final TextView originalTitleView;
        final TextView localTitleView;
        final TextView descriptionView;

        private MovieViewHolder(View itemView) {
            super(itemView);
            imageView = (SimpleDraweeView) itemView.findViewById(R.id.movie_poster);
            originalTitleView = (TextView) itemView.findViewById(R.id.movie_original_title);
            localTitleView = (TextView) itemView.findViewById(R.id.movie_local_title);
            descriptionView = (TextView) itemView.findViewById(R.id.movie_description);
        }

        static MovieViewHolder newInstance(LayoutInflater layoutInflater, ViewGroup parent) {
            final View view = layoutInflater.inflate(R.layout.item_movie, parent, false);
            return new MovieViewHolder(view);
        }
    }
}
