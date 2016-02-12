package com.nikmalov.portfolioproject.PopularVideoApp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.nikmalov.portfolioproject.R;
import com.squareup.picasso.Picasso;
import static com.nikmalov.portfolioproject.PopularVideoApp.Utilities.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class MovieDetailActivityFragment extends Fragment {

    private List<String[]> trailers = new ArrayList<>();
    private List<String[]> reviews = new ArrayList<>();
    DetailedViewListAdapter mAdapter;
    ListView mListView;

    public MovieDetailActivityFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int movieId = getActivity().getIntent().getIntExtra(Movie.MOVIE_ID, 0);
        if (movieId != 0) {
            new MovieDetailsAsyncTask().execute(movieId);
            new MovieTrailersAsyncTask().execute(movieId);
            new MovieReviewsAsyncTask().execute(movieId);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.movie_detail_fragment, container, false);
        Intent intent = getActivity().getIntent();
        String movieTitle = intent.getStringExtra(Movie.TITLE);
        ((TextView)rootView.findViewById(R.id.detailed_movie_header)).setText(movieTitle);
        mListView = (ListView)rootView.findViewById(R.id.detailed_movie_list_view);
        Movie movie = new Movie(intent.getIntExtra(Movie.MOVIE_ID, 0),
                movieTitle, intent.getDoubleExtra(Movie.USER_RATING, 0),
                intent.getStringExtra(Movie.OVERVIEW), intent.getStringExtra(Movie.POSTER_PATH),
                new Date(intent.getLongExtra(Movie.RELEASE_DATE, 0)));
        mAdapter = new DetailedViewListAdapter(getActivity(), movie, trailers, reviews);
        return rootView;
    }

    class MovieDetailsAsyncTask extends AsyncTask<Integer, Void, Movie> {

        @Override
        protected Movie doInBackground(Integer... params) {
            if (params.length == 0)
                return null;
            int movieId = params[0];

            HttpURLConnection urlConnection;
            BufferedReader reader;
            String jsonResult;
            Movie movie = null;

            try {
                URL url = new URL(URL_REQUEST_ANCHOR + "/" + movieId + API_KEY);
                urlConnection = (HttpURLConnection)url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream stream = urlConnection.getInputStream();
                if (stream == null)
                    return null;

                StringBuilder builder = new StringBuilder();
                reader = new BufferedReader(new InputStreamReader(stream));

                String line;
                while((line = reader.readLine()) != null)
                    builder.append(line).append("\n");

                if (builder.length() == 0)
                    return null;

                jsonResult = builder.toString();

                try {
                    movie = parseJsonMovieDetails(jsonResult);
                } catch (JSONException e) {
                    Log.e(e.toString(), "Failed to parse Json input.");
                    e.printStackTrace();
                }


            } catch (IOException e) {
                e.printStackTrace();
            }
            return movie;
        }

        @Override
        protected void onPostExecute(Movie movie) {
            mAdapter.setMovie(movie);
            mListView.setAdapter(mAdapter);
            Log.i(getClass().getSimpleName(), "Finished loading movie details.");
        }

        private Movie parseJsonMovieDetails(String jsonInput) throws JSONException {
            JSONObject movieObject = new JSONObject(jsonInput);
            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

            String posterPath = movieObject.getString(Movie.POSTER_PATH);
            Date releaseDate = dateFormat.
                    parse(movieObject.getString(Movie.RELEASE_DATE), new ParsePosition(0));
            final Movie movie = new Movie(movieObject.getInt(Movie.MOVIE_ID),
                    movieObject.getString(Movie.TITLE),
                    movieObject.getDouble(Movie.USER_RATING),
                    movieObject.getString(Movie.OVERVIEW),
                    posterPath,
                    releaseDate);
            movie.setDuration(movieObject.getInt(Movie.DURATION));
            //set poster image
            try {
                movie.setPoster(Picasso.with(getActivity()).
                        load(IMAGE_URL_ANCHOR + DETAILED_IMG_QUALITY + posterPath).get());
            } catch (IOException e) {
                e.printStackTrace();
            }
            return movie;
        }
    }

    class MovieTrailersAsyncTask extends AsyncTask<Integer, Void, List<String[]>> {

        @Override
        protected List<String[]> doInBackground(Integer... params) {
            if (params.length == 0)
                return null;
            int movieId = params[0];

            HttpURLConnection urlConnection;
            BufferedReader reader;
            String jsonResult;
            List<String[]> trailers = null;

            try {
                URL url = new URL(URL_REQUEST_ANCHOR + "/" + movieId + VIDEOS_URI_PART + API_KEY);
                urlConnection = (HttpURLConnection)url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream stream = urlConnection.getInputStream();
                if (stream == null)
                    return null;

                StringBuilder builder = new StringBuilder();
                reader = new BufferedReader(new InputStreamReader(stream));

                String line;
                while((line = reader.readLine()) != null)
                    builder.append(line).append("\n");

                if (builder.length() == 0)
                    return null;

                jsonResult = builder.toString();

                try {
                    trailers = parseJsonMovieTrailers(jsonResult);
                } catch (JSONException e) {
                    Log.e(e.toString(), "Failed to parse Json input.");
                    e.printStackTrace();
                }


            } catch (IOException e) {
                e.printStackTrace();
            }
            return trailers;
        }

        @Override
        protected void onPostExecute(List<String[]> trailers) {
            mAdapter.setTrailersList(trailers);
            mListView.setAdapter(mAdapter);
            Log.i(getClass().getSimpleName(), "Finished loading movie trailers.");
        }

        private List<String[]> parseJsonMovieTrailers(String jsonInput) throws JSONException {

            JSONObject rawData = new JSONObject(jsonInput);
            JSONArray trailerObjects = rawData.getJSONArray("results");

            JSONObject trailer;
            List<String[]> result = new ArrayList<>();
            for (int i = 0; i < trailerObjects.length(); i++) {
                trailer = trailerObjects.getJSONObject(i);
                result.add(new String[]{trailer.getString("name"), trailer.getString("key")});
            }
            return result;
        }
    }

    class MovieReviewsAsyncTask extends AsyncTask<Integer, Void, List<String[]>> {

        @Override
        protected List<String[]> doInBackground(Integer... params) {
            if (params.length == 0)
                return null;
            int movieId = params[0];

            HttpURLConnection urlConnection;
            BufferedReader reader;
            String jsonResult;
            List<String[]> reviews = null;

            try {
                URL url = new URL(URL_REQUEST_ANCHOR + "/" + movieId + REVIEWS_URI_PART + API_KEY);
                urlConnection = (HttpURLConnection)url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream stream = urlConnection.getInputStream();
                if (stream == null)
                    return null;

                StringBuilder builder = new StringBuilder();
                reader = new BufferedReader(new InputStreamReader(stream));

                String line;
                while((line = reader.readLine()) != null)
                    builder.append(line).append("\n");

                if (builder.length() == 0)
                    return null;

                jsonResult = builder.toString();

                try {
                    reviews = parseJsonMovieReviews(jsonResult);
                } catch (JSONException e) {
                    Log.e(e.toString(), "Failed to parse Json input.");
                    e.printStackTrace();
                }


            } catch (IOException e) {
                e.printStackTrace();
            }
            return reviews;
        }

        @Override
        protected void onPostExecute(List<String[]> reviews) {
            mAdapter.setReviews(reviews);
            mListView.setAdapter(mAdapter);//TODO: instead of resetting adapter in every postExecute implement cyclicBarrier
            Log.i(getClass().getSimpleName(), "Finished loading movie reviews.");
        }

        private List<String[]> parseJsonMovieReviews(String jsonInput) throws JSONException {

            JSONObject rawData = new JSONObject(jsonInput);
            JSONArray reviewObjects = rawData.getJSONArray("results");

            JSONObject review;
            List<String[]> result = new ArrayList<>();
            for (int i = 0; i < reviewObjects.length(); i++) {
                review = reviewObjects.getJSONObject(i);
                result.add(new String[]{review.getString("author"), review.getString("content"),
                        review.getString("url")});
            }
            return result;
        }
    }

}
