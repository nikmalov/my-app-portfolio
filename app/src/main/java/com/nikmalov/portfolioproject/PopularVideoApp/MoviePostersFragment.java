package com.nikmalov.portfolioproject.PopularVideoApp;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MoviePostersFragment extends Fragment {

    private static final String LOG_TAG = MoviePostersFragment.class.getSimpleName();

    private MovieListType defaultType = MovieListType.POPULAR;
    private MoviePosterAdapter postersAdapter;
    private final String URL_ANCHOR = "http://api.themoviedb.org/3/movie";
    private final String API_KEY = "?api_key=41320596822de654b82ddb5d040d4127";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new MovieGetterAsyncTask().execute(defaultType);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.video_grid_fragment, container, false);

        GridView postersGridView = (GridView)rootView.findViewById(R.id.postersGridView);
        postersAdapter = new MoviePosterAdapter(getActivity(), Collections.EMPTY_LIST);
        postersGridView.setAdapter(postersAdapter);

        return rootView;
    }

    private class MovieGetterAsyncTask extends AsyncTask<MovieListType, Void, List<Movie>> {

        private final String imageUrlAnchor = "http://image.tmdb.org/t/p/" + "w185/";

        @Override
        protected List<Movie> doInBackground(MovieListType... params) {
            if (params[0] == null)
                return null;
            HttpURLConnection urlConnection;
            BufferedReader reader;
            String jsonResult;
            List<Movie> movieList = new ArrayList<>();

            try {
                URL url = new URL(URL_ANCHOR + params[0].urlPostfix + API_KEY);
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
        }


        private List<Movie> parseJsonMovieInfo(String jsonInput) throws JSONException {
            JSONObject rawData = new JSONObject(jsonInput);
            JSONArray movies = rawData.getJSONArray("results");
            List<Movie> result = new ArrayList<>();
            JSONObject movieObject;

            for (int i = 0; i < movies.length(); i++) {
                movieObject = movies.getJSONObject(i);
                JSONArray genreIdsJsonArray = movieObject.getJSONArray(Movie.GENRE_IDS);
                List<Integer> genreIds = new ArrayList<>();
                for (int j = 0; j < genreIdsJsonArray.length(); j++) {
                    genreIds.add(genreIdsJsonArray.getInt(j));
                }
                final Movie movie = new Movie(movieObject.getInt(Movie.MOVIE_ID),
                        movieObject.getString(Movie.TITLE),
                        genreIds,
                        movieObject.getBoolean(Movie.IS_ADULT),
                        movieObject.getString(Movie.OVERVIEW));
                //set poster image
                try {
                    movie.setPoster(Picasso.with(getActivity()).
                            load(imageUrlAnchor + movieObject.getString(Movie.POSTER_PATH)).get());
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
