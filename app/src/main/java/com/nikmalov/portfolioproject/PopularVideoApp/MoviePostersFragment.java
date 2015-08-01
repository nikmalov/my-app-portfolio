package com.nikmalov.portfolioproject.PopularVideoApp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ListAdapter;

import com.nikmalov.portfolioproject.R;

import java.util.List;

public class MoviePostersFragment extends Fragment {

    private MovieListType defaultType = MovieListType.POPULAR;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.video_grid_fragment, container, false);

        List<Movie> currentMovieList = getMovieList(defaultType);//TODO

        GridView postersGridView = (GridView)rootView.findViewById(R.id.postersGridView);
        ListAdapter postersAdapter = new MoviePosterAdapter(getActivity(), currentMovieList);
        postersGridView.setAdapter(postersAdapter);

        return rootView;
    }

    private List<Movie> getMovieList(MovieListType type) {
        return null;//TODO
    }
}
