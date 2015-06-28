package com.nikmalov.portfolioproject.popularVideoApp;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.GridView;

import com.nikmalov.portfolioproject.R;

public class VideoGridActivity extends Activity {

	private GridView mGridView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_video_grid);
		mGridView = (GridView)findViewById(R.id.gridView);
		mGridView.setAdapter(new MoviePosterAdapter(this));
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_video_grid, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();

		if (id == R.id.sorting_switcher) {
			showSortingSwitcher();
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	private void showSortingSwitcher() {
		//TODO: add switching order logic
	}
}
