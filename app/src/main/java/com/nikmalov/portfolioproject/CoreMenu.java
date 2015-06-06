package com.nikmalov.portfolioproject;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


public class CoreMenu extends Activity {

	private Toast mAppToast;

	public Button spotifyButton;
	public Button scoresButton;
	public Button libraryButton;
	public Button buildItButton;
	public Button xyzReaderButton;
	public Button capstoneButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_core_menu);
		setButtons();
	}

	public void setButtons() {
		//for learning purposes first 3 buttons implement xml-based on-click behaviour
		spotifyButton = (Button)findViewById(R.id.app1LaunchButton);
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
		if (mAppToast != null)
			mAppToast.cancel();

		mAppToast = Toast.makeText(getApplicationContext(),
			"This button will launch my " + appName + "!", Toast.LENGTH_SHORT);
		mAppToast.show();
	}

	public void showMessage(View buttonView) {
		if (!(buttonView instanceof Button))
			return;
		Button button = (Button)buttonView;
		if (mAppToast != null)
			mAppToast.cancel();
		mAppToast = Toast.makeText(getApplicationContext(),
			"This button will launch my " + button.getText() + "!", Toast.LENGTH_SHORT);
		mAppToast.show();
	}
}
