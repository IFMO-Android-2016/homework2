package ru.ifmo.droid2016.tmdb;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.DraweeView;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import ru.ifmo.droid2016.tmdb.loader.MovieLoader;
import ru.ifmo.droid2016.tmdb.model.Movie;

/**
 * Created by Nemzs on 23.11.2016.
 */
public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    private List<Movie> movies;
    private final Context context;
    private final LayoutInflater layoutInflater;

    // Provide a reference to the views for each data item
// Complex data items may need more than one view per item, and
// you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public View mTextView;
        public ViewHolder(View v) {
            super(v);
            mTextView = v;
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public MyAdapter(Context context) {
                movies = new ArrayList<>();
                this.context = context;
                this.layoutInflater = LayoutInflater.from(context);
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
// create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.film, parent, false);
// set the view's size, margins, paddings and layout parameters
        Log.d("My","some");
        //movies
        //filmName.setText(mDataset[i++]);
        ViewHolder vh = new ViewHolder( v);
        return vh;
    }
    public void setMovies(List<Movie> movies){
        this.movies = movies;
        notifyDataSetChanged();
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
// - get element from your dataset at this position
// - replace the contents of the view with that element
        Movie movie = movies.get(position);
        TextView originalNameView = (TextView)holder.mTextView.findViewById(R.id.originalName);
        TextView previewView = (TextView)holder.mTextView.findViewById(R.id.preview);
        TextView localNameView = (TextView)holder.mTextView.findViewById(R.id.localName);
        DraweeView poster = (DraweeView)holder.mTextView.findViewById(R.id.poster);
        originalNameView.setText(movies.get(position).originalTitle);
        previewView.setText(movies.get(position).overviewText);
        localNameView.setText(movies.get(position).localizedTitle);

        poster.setImageURI(Uri.parse(movie.posterPath));
        poster.setAspectRatio(2f / 3);
        //Log.d("My",Integer.toString(position)+' '+movie.posterPath);
        //holder.mTextView.setText(mDataset[position]);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return movies.size();
    }
}