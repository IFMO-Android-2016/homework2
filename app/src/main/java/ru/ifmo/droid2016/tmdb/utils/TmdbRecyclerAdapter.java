package ru.ifmo.droid2016.tmdb.utils;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;

import ru.ifmo.droid2016.tmdb.R;
import ru.ifmo.droid2016.tmdb.model.Movie;

/**
 * Created by RinesThaix on 22.11.16.
 */

public class TmdbRecyclerAdapter extends RecyclerView.Adapter<TmdbRecyclerAdapter.Item> {

    private final Context context;
    private final LayoutInflater layoutInflater;
    private final List<Movie> listed = new ArrayList<>();

    public TmdbRecyclerAdapter(Context context) {
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
    }

    public TmdbRecyclerAdapter updateMovies(List<Movie> movies) {
        int previously = listed.size();
        listed.addAll(movies);
        notifyItemRangeInserted(previously + 1, movies.size());
        return this;
    }

    public List<Movie> getListed() {
        return listed;
    }

    @Override
    public Item onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Item(parent, layoutInflater);
    }

    @Override
    public void onBindViewHolder(Item item, int pos) {
        Movie movie = listed.get(pos);
        item.title.setText(movie.localizedTitle + " (" + movie.originalTitle + ")");
        item.description.setText(movie.overviewText);
        item.poster.setImageURI(movie.getFullPosterPath());
        item.poster.setAspectRatio(2/3f);
    }

    @Override
    public int getItemCount() {
        return listed.size();
    }

    public static class Item extends RecyclerView.ViewHolder {

        private final TextView title, description;
        private final SimpleDraweeView poster;

        public Item(ViewGroup parentGroup, LayoutInflater layoutInflater) {
            this(layoutInflater.inflate(R.layout.item, parentGroup, false));
        }

        public Item(View view) {
            super(view);
            this.title = (TextView) view.findViewById(R.id.title);
            this.description = (TextView) view.findViewById(R.id.description);
            this.poster = (SimpleDraweeView) view.findViewById(R.id.poster);
        }

    }

}