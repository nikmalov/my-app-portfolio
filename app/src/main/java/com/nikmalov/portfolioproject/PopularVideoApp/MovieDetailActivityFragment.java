package com.nikmalov.portfolioproject.PopularVideoApp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.nikmalov.portfolioproject.R;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * A placeholder fragment containing a simple view.
 */
public class MovieDetailActivityFragment extends Fragment {

    public static final String imgQuality = "w780/";

    ImageView poster;
    TextView titleTextView;
    TextView overviewTextView;
    RatingBar userRating;
    TextView releaseDateView;

    public MovieDetailActivityFragment() {
    }

    @SuppressLint("SimpleDateFormat")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.movie_detail_fragment, container, false);
        Intent intent = getActivity().getIntent();
        poster = (ImageView)rootView.findViewById(R.id.posterImage);
        titleTextView = (TextView)rootView.findViewById(R.id.movie_title);
        overviewTextView = (TextView)rootView.findViewById(R.id.movie_overview);
        userRating = (RatingBar)rootView.findViewById(R.id.ratingBar);
        releaseDateView = (TextView)rootView.findViewById(R.id.release_date);

        poster.setImageBitmap((Bitmap) intent.getParcelableExtra(Movie.POSTER));
        titleTextView.setText(intent.getStringExtra(Movie.TITLE));
        overviewTextView.setText(intent.getStringExtra(Movie.OVERVIEW));
        userRating.setRating((float)intent.getDoubleExtra(Movie.USER_RATING, 0)/2);
        releaseDateView.setText(new SimpleDateFormat("yyyy").
                format(new Date(intent.getLongExtra(Movie.RELEASE_DATE, 0))));
        return rootView;
    }

    class MovieDetailsAsyncTask extends AsyncTask<Integer, Void, Void> {

        @Override
        protected Void doInBackground(Integer... params) {
            return null;//TODO: download trailers' uris and reviews
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);//TODO populate corresponding views
        }
    }

}
