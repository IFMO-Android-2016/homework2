package ru.ifmo.droid2016.tmdb;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.method.MovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ru.ifmo.droid2016.tmdb.model.Movie;

/**
 * Экран, отображающий список популярных фильмов из The Movie DB.
 */
public class PopularMoviesActivity extends AppCompatActivity {

    RecyclerView rv;
    LinearLayoutManager llm;
    RVAdapter radapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popular_movies);
        rv = (RecyclerView) findViewById(R.id.rv);
        rv.setHasFixedSize(true);
        llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);
        List<Movie> mv = new ArrayList<>();
        mv.add( new Movie("dsds", "dsdsadadasda", "111", "222"));
        mv.add(new Movie("123", "456", "678", "11111111"));
        radapter = new RVAdapter(mv);
        rv.setAdapter(radapter);
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
