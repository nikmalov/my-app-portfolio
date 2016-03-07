package com.nikmalov.portfolioproject.PopularVideoApp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.nikmalov.portfolioproject.R;

public class MoviePostersActivity extends AppCompatActivity
        implements MoviePostersFragment.MovieSelectedCallback {

    private static final String MOVIE_DETAILS_FRAGMENT_TAG = "MOVIE_DETAILS_TAG";
    private boolean isTwoPane;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.video_grid_activity);
        if (findViewById(R.id.movie_details_container) != null) {
            isTwoPane = true;
            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction().
                        replace(R.id.movie_details_container, new MovieDetailActivityFragment(),
                                MOVIE_DETAILS_FRAGMENT_TAG).commit();
            }
        } else {
            isTwoPane = false;
        }
    }

    @Override
    public void onMovieSelected(Movie movie) {
        if (!isTwoPane) {
            Intent movieDetailsIntent = new Intent(this, MovieDetailActivity.class);
            Utilities.fillIntentWithMovieData(movieDetailsIntent, movie);
            startActivity(movieDetailsIntent);
        } else {
            MovieDetailActivityFragment daf = new MovieDetailActivityFragment();
            Bundle data = new Bundle();
            data.putParcelable(Movie.MOVIE, movie);
            daf.setArguments(data);
            getSupportFragmentManager().beginTransaction().replace(
                    R.id.movie_details_container, daf, MOVIE_DETAILS_FRAGMENT_TAG).commit();
        }
    }
}
