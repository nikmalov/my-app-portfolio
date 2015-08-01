package com.nikmalov.portfolioproject.PopularVideoApp;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.nikmalov.portfolioproject.R;

public class VideoGridActivity extends FragmentActivity {


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.video_grid_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().
                    add(R.id.video_grid_activity, new MoviePostersFragment()).commit();
        }
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
