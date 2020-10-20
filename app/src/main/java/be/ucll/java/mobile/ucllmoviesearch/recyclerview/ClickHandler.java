package be.ucll.java.mobile.ucllmoviesearch.recyclerview;

import be.ucll.java.mobile.ucllmoviesearch.model.internal.Movie;

public interface ClickHandler {
    void onMovieClick(Movie movie);

    void onFavoriteClick(Movie movie);
}
