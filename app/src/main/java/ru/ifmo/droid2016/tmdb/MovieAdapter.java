package ru.ifmo.droid2016.tmdb;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Roman on 16/11/2016.
 */

public class MovieAdapter
        extends android.support.v7.widget.RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {
    @Override
    public int getItemCount() {
        return 0;
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {

    }

    static class MovieViewHolder extends RecyclerView.ViewHolder {
        public MovieViewHolder(View itemView) {
            super(itemView);
        }
    }
}
