package com.nikmalov.portfolioproject.popularVideoApp;

import android.content.Context;
import android.util.JsonReader;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public class MoviePosterAdapter extends BaseAdapter {

	Context mContext;
	private int defaultNumberOfPosters = 15;
	private String urlStringAnchor = "http://image.tmdb.org/t/p/" + "w185";
	private List popularMovies = new ArrayList();
	private List topRatedMovies = new ArrayList();

	private String testUrl = "//nBNZadXqJSdt05SHLqgT0HuC5Gm.jpg";
	private enum RequestType {

		POPULAR_REQUEST("http://api.themoviedb.org/3/movie/popular"),
		RATED_REQUEST("http://api.themoviedb.org/3/movie/top_rated");

		String requestString;

		RequestType(String requestString) {
			this.requestString = requestString;
		}
	}

	public MoviePosterAdapter(Context context) {
		mContext = context;
	}

	@Override
	public int getCount() {
		return defaultNumberOfPosters;
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ImageView imageView = new ImageView(mContext);
		Picasso.with(mContext).load(getUri(RequestType.POPULAR_REQUEST, position)).into(imageView);
		return imageView;
	}

/*	private void getLinks(RequestType type) {
		try {
			URL requestUrl = new URL(type.requestString);
			URLConnection connection = requestUrl.openConnection();
			InputStream inputStream = connection.getInputStream();
			JsonReader reader = new JsonReader(new InputStreamReader(inputStream));
			reader.

		} catch (Exception e) {

		} finally {

		}
	}
*/
	private String getUri(RequestType type, int number) {
		return urlStringAnchor + testUrl;
		//	(type.equals(RequestType.POPULAR_REQUEST) ?
		//	popularMovies.get(number) : topRatedMovies.get(number));
	}
}
