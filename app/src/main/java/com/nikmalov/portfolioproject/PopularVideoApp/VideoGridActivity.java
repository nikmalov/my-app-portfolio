package com.nikmalov.portfolioproject.PopularVideoApp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.nikmalov.portfolioproject.R;

public class VideoGridActivity extends AppCompatActivity {


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.video_grid_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().
                    add(R.id.video_grid_activity, new MoviePostersFragment()).commit();
        }
    }
}
