package be.ucll.java.mobile.ucllmoviesearch;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import be.ucll.java.mobile.ucllmoviesearch.model.internal.Movie;
import be.ucll.java.mobile.ucllmoviesearch.model.webservice.MovieSearch;
import be.ucll.java.mobile.ucllmoviesearch.model.webservice.Search;
import be.ucll.java.mobile.ucllmoviesearch.recyclerview.ClickHandler;
import be.ucll.java.mobile.ucllmoviesearch.recyclerview.MoviesAdapter;

public class MainActivity extends AppCompatActivity implements Response.Listener, Response.ErrorListener, ClickHandler {
    private static final String TAG = "MainActivity";
    private static final String OMDB_API_URL = "https://www.omdbapi.com/?apikey=ba57c0a5&type=movie&s=";
    private static final String IMDB_WEBSITE_URL = "https://m.imdb.com/title/";

    private TextView txtSearch;
    private RecyclerView rvMovies;
    private MoviesAdapter adapter;
    private List<Movie> movies;
    private RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtSearch = findViewById(R.id.txtSearch);
        rvMovies = findViewById(R.id.rvMovies);
        rvMovies.setLayoutManager(new LinearLayoutManager(this));
    }

    public void onBtnSearchClick(View view) {
        // Instantiate the RequestQueue for asynchronous operations
        queue = Volley.newRequestQueue(this);

        // Encode as UTF8 characters f.i. replace a space by a + sign
        String searchterm = txtSearch.getText().toString();
        try {
            searchterm = URLEncoder.encode(searchterm, "UTF-8");
        } catch (UnsupportedEncodingException ignore) {
        }

        String url = OMDB_API_URL + searchterm;
        Log.d(TAG, "URL: " + url);

        // Prepare the request to be send out towards the REST service OMDB_API_URL..
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, url, null, this, this);

        // Add the request to the RequestQueue for asynchronous retrieval on separate thread.
        queue.add(req);
    }

    @Override
    public void onResponse(Object response) {
        // Cast into Gson JSONObject
        JSONObject jsono = (JSONObject) response;

        // Log the output as debug information
        Log.d(TAG, jsono.toString());

        // Convert REST String to Pojo's using GSON libraries
        MovieSearch omdbrespo = new Gson().fromJson(jsono.toString(), MovieSearch.class);
        if (omdbrespo != null && omdbrespo.getSearch() != null && omdbrespo.getSearch().size() > 0) {
            movies = new ArrayList<>(omdbrespo.getSearch().size());
            try {
                // Movie is a smaller custom Pojo storing but the items that interest us
                // so we create Movie Pojo's from the info returned from the REST service Pojo's
                Movie movie;
                for (Search s : omdbrespo.getSearch()) {
                    // We only display films WITH a poster
                    if (s.getPoster().equalsIgnoreCase("N/A")) continue;

                    movie = new Movie();
                    movie.setTitle(s.getTitle());
                    movie.setYear(s.getYear());
                    movie.setPosterURL(s.getPoster());
                    movie.setImdbID(s.getImdbID());
                    movies.add(movie);
                }

                // Sort all films using a comparator
                // In this case
                //   FIRST the favorite movies
                //   THEN the year (of release) of the movie, most recent ones first
                // (See Movie pojo)
                Collections.sort(movies);
            } catch (Exception e) {
                Log.e(TAG, "General issue", e);
            }

            // Future improvement: Retrieve page 2, 3, 4 ... if available.

            adapter = new MoviesAdapter(this, movies);
            Toast.makeText(this, getString(R.string.msgMoviesfoundPrefix) + movies.size() + getString(R.string.msgMoviesfoundSuffix) + omdbrespo.getTotalResults(), Toast.LENGTH_LONG).show();
        } else {
            // No movies found for search term
            adapter = new MoviesAdapter(this, new ArrayList<Movie>());
            Toast.makeText(this, getString(R.string.msgNoMoviesfound) + txtSearch.getText(), Toast.LENGTH_LONG).show();
        }

        rvMovies.setAdapter(adapter);
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        // This is when the call upon the web service remains unanswered or in error
        Toast.makeText(this, error.getMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onMovieClick(Movie movie) {
        // Open the browser and go to imdb.com for more detail about the movie
        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(IMDB_WEBSITE_URL + movie.getImdbID()));
        startActivity(i);
    }

    @Override
    public void onFavoriteClick(Movie movie) {
        // Check/uncheck as favorite
        movie.toggleFavorite();

        // Sort to have the favorites on top
        Collections.sort(movies);

        // Create a new adapter and set it on the Recyclerview
        adapter = new MoviesAdapter(this, movies);
        rvMovies.setAdapter(adapter);
    }
}
