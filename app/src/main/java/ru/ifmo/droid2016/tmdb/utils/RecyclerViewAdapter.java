package ru.ifmo.droid2016.tmdb.utils;

import android.app.LoaderManager;
import android.content.Loader;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;

import ru.ifmo.droid2016.tmdb.R;
import ru.ifmo.droid2016.tmdb.apiLoaders.apiLoaderGetPopularMovies;
import ru.ifmo.droid2016.tmdb.loader.LoadResult;
import ru.ifmo.droid2016.tmdb.loader.ResultType;
import ru.ifmo.droid2016.tmdb.model.Movie;



public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.PersonViewHolder> {

    final List<Movie> movies;

    public RecyclerViewAdapter(List<Movie> movies) {
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
        holder.image.setImageURI(movies.get(position).posterPath);
        holder.title.setText(movies.get(position).localizedTitle);
        holder.discribe.setText(movies.get(position).overviewText);
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }


    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    class PersonViewHolder extends RecyclerView.ViewHolder {
        SimpleDraweeView image;
        TextView title;
        TextView discribe;

        public PersonViewHolder(View itemView) {
            super(itemView);
            image = (SimpleDraweeView) itemView.findViewById(R.id.poster);
            title = (TextView) itemView.findViewById(R.id.title);
            discribe = (TextView) itemView.findViewById(R.id.discribe);
        }
    }
}
