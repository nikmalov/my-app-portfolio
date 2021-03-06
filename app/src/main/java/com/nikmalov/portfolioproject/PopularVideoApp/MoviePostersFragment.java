package com.nikmalov.portfolioproject.PopularVideoApp;

import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;

import com.nikmalov.portfolioproject.R;
import static com.nikmalov.portfolioproject.PopularVideoApp.Utilities.*;
import static com.nikmalov.portfolioproject.PopularVideoApp.data.FavouriteMoviesContract.FavouriteMovieEntry.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MoviePostersFragment extends Fragment {

    private static final String LOG_TAG = MoviePostersFragment.class.getSimpleName();
    private static final String DATA_STORED = "dataStored";

    private MovieListType currentType;
    private ArrayList<Movie> storedMovieList;
    private MovieListType lastLoadedType;
    private MoviePosterAdapter postersAdapter;
    @Bind(R.id.progressBar) ProgressBar progressBar;
    @Bind(R.id.postersGridView) GridView postersGridView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        currentType = getCurrentSortingType(getActivity());
        if (savedInstanceState == null || savedInstanceState.isEmpty()) {
            postersAdapter = new MoviePosterAdapter(getActivity(), Collections.EMPTY_LIST);
            new MovieGetterAsyncTask().execute(currentType);
            showProgress();
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
        currentType = getCurrentSortingType(getActivity());
        if (currentType != lastLoadedType) {
            postersAdapter.setMovieList(Collections.EMPTY_LIST);
            showProgress();
            new MovieGetterAsyncTask().execute(currentType);
        } else if (currentType == MovieListType.FAVOURITES) {
            refreshDisplayedFavourites();
        }
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
        ButterKnife.bind(this, rootView);
        postersGridView.setAdapter(postersAdapter);
        postersGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ((MoviePostersActivity)getActivity()).onMovieSelected((Movie)parent.getAdapter().getItem(position));
            }
        });

        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    /**
     * Optimisation for the case when we remove a movie from favourites and return to grid
     * of favourites, so that we don't need to query all posters once again.
     */
    private void refreshDisplayedFavourites() {
        List<Integer> currentIdList = new ArrayList<>(storedMovieList.size());
        Cursor cur = getActivity().getContentResolver().query(CONTENT_URI, null, null, null, null);
        if (cur != null && cur.moveToFirst()) {
            currentIdList.add(cur.getInt(cur.getColumnIndex(COLUMN_MOVIE_ID)));
            while (cur.moveToNext()) {
                currentIdList.add(cur.getInt(cur.getColumnIndex(COLUMN_MOVIE_ID)));
            }
        }
        if (cur != null)
            cur.close();
        storedMovieList = removeUnfavouritedMovies(currentIdList);
        postersAdapter.setMovieList(storedMovieList);
    }

    /**
     * Removes movie which are not in favourites anymore from list of currently displayed ones.
     */
    private ArrayList<Movie> removeUnfavouritedMovies(List<Integer> moviesIdList) {
        ArrayList<Movie> refreshedMovieList = new ArrayList<>(moviesIdList.size());
        for (Movie movie : storedMovieList) {
            if (moviesIdList.contains(movie.getMovieId()))
                refreshedMovieList.add(movie);
        }
        return refreshedMovieList;
    }

    private void showProgress() {
        if (progressBar != null)
            progressBar.setVisibility(View.VISIBLE);
    }

    private void hideProgress() {
        if (progressBar != null)
            progressBar.setVisibility(View.GONE);
    }

    private class MovieGetterAsyncTask extends AsyncTask<MovieListType, Void, List<Movie>> {

        @Override
        protected List<Movie> doInBackground(MovieListType... params) {
            if (params[0] == null)
                return null;
            List<Movie> movieList = new ArrayList<>();
            if (params[0] == MovieListType.FAVOURITES) {//short way, favourites stored locally
                Cursor cur = getActivity().getContentResolver().query(
                        CONTENT_URI, null, null, null, null);
                if (cur != null) {
                    if (cur.moveToFirst()) {
                        movieList.add(createMovie(cur));
                        while (cur.moveToNext()) {
                            movieList.add(createMovie(cur));
                        }
                    }
                    cur.close();
                }
                return movieList;
            }
            HttpURLConnection urlConnection;
            BufferedReader reader;
            String jsonResult;

            try {
                URL url = new URL(URL_REQUEST_ANCHOR + params[0].urlPostfix + API_KEY);
                Log.d(MovieGetterAsyncTask.class.getSimpleName(), url.toString());
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
            hideProgress();
        }

        private List<Movie> parseJsonMovieInfo(String jsonInput) throws JSONException {
            JSONObject rawData = new JSONObject(jsonInput);
            JSONArray movies = rawData.getJSONArray("results");
            List<Movie> result = new ArrayList<>();

            for (int i = 0; i < movies.length(); i++) {
                result.add(createMovieOnline(getActivity(), movies.getJSONObject(i)));
            }
            return result;
        }
    }

    public interface MovieSelectedCallback {
        void onMovieSelected(Movie movie);
    }
}
