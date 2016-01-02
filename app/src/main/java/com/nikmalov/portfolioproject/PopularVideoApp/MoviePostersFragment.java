package com.nikmalov.portfolioproject.PopularVideoApp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.nikmalov.portfolioproject.R;
import com.squareup.picasso.Picasso;

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
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class MoviePostersFragment extends Fragment {

    private static final String LOG_TAG = MoviePostersFragment.class.getSimpleName();
    private static final String DATA_STORED = "dataStored";
    public static final String imageUrlAnchor = "http://image.tmdb.org/t/p/";
    public static final String imgQuality = "w500/";

    private MovieListType currentType;
    private ArrayList<Movie> storedMovieList;
    private MovieListType lastLoadedType;
    private SharedPreferences preference;
    private MoviePosterAdapter postersAdapter;
    private final String URL_REQUEST_ANCHOR = "http://api.themoviedb.org/3/movie";
    private final String API_KEY = "?api_key=" + "41320596822de654b82ddb5d040d4127";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        preference = PreferenceManager.getDefaultSharedPreferences(getActivity());
        currentType = getCurrentSortingType();
        if (savedInstanceState == null || savedInstanceState.isEmpty()) {
            postersAdapter = new MoviePosterAdapter(getActivity(), Collections.EMPTY_LIST);
            new MovieGetterAsyncTask().execute(currentType);
        } else {
            storedMovieList = savedInstanceState.getParcelableArrayList(DATA_STORED);
            postersAdapter = new MoviePosterAdapter(getActivity(), storedMovieList);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList(DATA_STORED, storedMovieList);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onStart() {
        super.onStart();
        currentType = getCurrentSortingType();
        if (currentType != lastLoadedType)
            new MovieGetterAsyncTask().execute(currentType);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_movie_posters_fragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            startActivity(new Intent(getActivity(), SettingsActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.video_grid_fragment, container, false);

        GridView postersGridView = (GridView)rootView.findViewById(R.id.postersGridView);
        postersGridView.setAdapter(postersAdapter);

        return rootView;
    }

    private MovieListType getCurrentSortingType() {
        String sortingTypeString = preference.getString(getString(R.string.sorting_type_key),
                getString((R.string.sorting_type_popularity)));
        return getString((R.string.sorting_type_top_rated)).equalsIgnoreCase(sortingTypeString) ?
                MovieListType.TOP_RATED : MovieListType.POPULAR;
    }

    private class MovieGetterAsyncTask extends AsyncTask<MovieListType, Void, List<Movie>> {

        @Override
        protected List<Movie> doInBackground(MovieListType... params) {
            if (params[0] == null)
                return null;
            HttpURLConnection urlConnection;
            BufferedReader reader;
            String jsonResult;
            List<Movie> movieList = new ArrayList<>();

            try {
                URL url = new URL(URL_REQUEST_ANCHOR + params[0].urlPostfix + API_KEY);
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
                    movieList = parseJsonMovieInfo(jsonResult);
                } catch (JSONException e) {
                    Log.e(LOG_TAG, "Failed to parse Json input.");
                    e.printStackTrace();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
            return movieList;
        }

        @Override
        protected void onPostExecute(List<Movie> movieList) {
            postersAdapter.setMovieList(movieList);
            storedMovieList = new ArrayList<>(movieList);
            lastLoadedType = currentType;
        }

        private List<Movie> parseJsonMovieInfo(String jsonInput) throws JSONException {
            JSONObject rawData = new JSONObject(jsonInput);
            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            JSONArray movies = rawData.getJSONArray("results");
            List<Movie> result = new ArrayList<>();
            JSONObject movieObject;

            for (int i = 0; i < movies.length(); i++) {
                movieObject = movies.getJSONObject(i);
                Date releaseDate = dateFormat.
                        parse(movieObject.getString(Movie.RELEASE_DATE), new ParsePosition(0));
                final Movie movie = new Movie(movieObject.getInt(Movie.MOVIE_ID),
                        movieObject.getString(Movie.TITLE),
                        movieObject.getDouble(Movie.USER_RATING),
                        movieObject.getString(Movie.OVERVIEW),
                        releaseDate);
                //set poster image
                try {
                    movie.setPoster(Picasso.with(getActivity()).
                            load(imageUrlAnchor + imgQuality +
                                    movieObject.getString(Movie.POSTER_PATH)).get());
                } catch (IOException e) {
                    Log.e(LOG_TAG, "Was not able to download and set poster.");
                    e.printStackTrace();
                }
                result.add(movie);
            }
            return result;
        }
    }
}
