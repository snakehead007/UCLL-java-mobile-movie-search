package be.ucll.java.mobile.ucllmoviesearch.recyclerview;

import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import be.ucll.java.mobile.ucllmoviesearch.R;
import be.ucll.java.mobile.ucllmoviesearch.model.internal.Movie;

public class MovieHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private static final String TAG = "MovieHolder";

    private ImageView imgMoviePoster;
    private TextView txtTitle;
    private TextView lblYear;
    private ImageButton btnFavorite;

    private Movie movie;

    private final MovieClick clickHandler;

    // Constructor
    public MovieHolder(View itemView, MovieClick ch) {
        super(itemView);

        this.clickHandler = ch;

        imgMoviePoster = itemView.findViewById(R.id.imgPoster);
        txtTitle = itemView.findViewById(R.id.lblTitle);
        lblYear = itemView.findViewById(R.id.lblYearValue);
        btnFavorite = itemView.findViewById(R.id.btnFavorite);
        btnFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickHandler.onFavoriteClick(movie);
            }
        });

        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Log.d(TAG, "Element " + getAdapterPosition() + " clicked.");
        clickHandler.onMovieClick(movie);
    }

    public void setMovie(Movie m) {
        this.movie = m;
        if (movie != null) {
            // Use the third party library Picasso to retrieve the image in the Internet
            // and display it in the ImageView.
            Picasso.get().load(movie.getPosterURL()).into(imgMoviePoster);

            // Set the movie title
            txtTitle.setText(movie.getTitle());

            // Set the movie year of release
            lblYear.setText(movie.getYear());

            // Display the right 'Favorite' star
            btnFavorite.setImageResource(movie.isFavorite() ? android.R.drawable.btn_star_big_on : android.R.drawable.btn_star_big_off);
        }
    }

}