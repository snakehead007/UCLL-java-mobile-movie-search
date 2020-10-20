package be.ucll.java.mobile.ucllmoviesearch.recyclerview;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import be.ucll.java.mobile.ucllmoviesearch.R;
import be.ucll.java.mobile.ucllmoviesearch.model.internal.Movie;

public class MoviesAdapter extends RecyclerView.Adapter<MovieHolder> {
    private static final String TAG = "MoviesAdapter";

    private ClickHandler clickHandler;
    private List<Movie> list;

    public MoviesAdapter(ClickHandler clickHandler, List<Movie> list) {
        this.clickHandler = clickHandler;
        this.list = list;
    }

    @Override
    public MovieHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.movie_item, viewGroup, false);

        return new MovieHolder(v, clickHandler);
    }

    // Voor elk item in de List van de adapter wordt deze method aangeroepen
    // om de 'Data' te visualiseren als 1 lijn of kaartje in de recyclerview
    @Override
    public void onBindViewHolder(@NonNull MovieHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: " + list.get(position));
        Movie m = list.get(position);
        holder.setMovie(m);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

}
