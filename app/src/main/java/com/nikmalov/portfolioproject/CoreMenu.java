package com.nikmalov.portfolioproject;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.nikmalov.portfolioproject.PopularVideoApp.VideoGridActivity;

import java.util.HashMap;
import java.util.Map;

public class CoreMenu extends Activity {

	private Toast mAppToast;

	public Button popularMoviesButton;
	public Button scoresButton;
	public Button libraryButton;
	public Button buildItButton;
	public Button xyzReaderButton;
	public Button capstoneButton;

	private final static String BUTTON_NOT_FOUND = "Button wasn't found.";
	private static Map<CharSequence, Class> projectButtonToActivityMap = new HashMap<>(6);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_core_menu);
		setButtons();
		Resources resources = getResources();
		projectButtonToActivityMap.
			put(resources.getString(R.string.popular_movies), VideoGridActivity.class);
		//new mappings are to be introduced
	}

	public void setButtons() {
		//for learning purposes first 3 buttons implement xml-based on-click behaviour
		popularMoviesButton = (Button)findViewById(R.id.app1LaunchButton);
		scoresButton = (Button)findViewById(R.id.app2LaunchButton);
		libraryButton = (Button)findViewById(R.id.app3LaunchButton);
		buildItButton = (Button)findViewById(R.id.app4LaunchButton);
		buildItButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				showMessage(R.string.build_it_bigger);
			}
		});
		xyzReaderButton = (Button)findViewById(R.id.app5LaunchButton);
		xyzReaderButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				showMessage(R.string.xyz_reader);
			}
		});
		capstoneButton = (Button)findViewById(R.id.app6LaunchButton);
		capstoneButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				showMessage(R.string.capstone_project);
			}
		});
	}

	public void showMessage(int appNameId) {
		String appName = getResources().getString(appNameId);
		displayMessage("This button will launch my " + appName + "!");
	}

	public void showMessage(View buttonView) {
		if (!(buttonView instanceof Button))
			return;
		Button button = (Button)buttonView;
		displayMessage("This button will launch my " + button.getText() + "!");
	}

	public void launchApp(View buttonView) {
		if (!(buttonView instanceof Button))
			return;
		Button button = (Button)buttonView;
		Class activityClass = projectButtonToActivityMap.get(button.getText());
		if (activityClass == null) {
			displayMessage(BUTTON_NOT_FOUND);
		}
		Intent intent = new Intent(getApplicationContext(), activityClass);
		startActivity(intent);

	}

	private void displayMessage(String message) {
		if (mAppToast != null)
			mAppToast.cancel();
		mAppToast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT);
		mAppToast.show();
	}
}
