package com.nikmalov.portfolioproject;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


public class CoreMenu extends Activity {

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


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_core_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();

		//noinspection SimplifiableIfStatement
		if (id == R.id.action_settings) {
			return true;
		}

		return super.onOptionsItemSelected(item);
	}
	public void setButtons() {
		//for learning purposes first 3 buttons implement xml-based on-click behaviour
		spotifyButton = (Button)findViewById(R.id.button);
		scoresButton = (Button)findViewById(R.id.button2);
		libraryButton = (Button)findViewById(R.id.button3);

		buildItButton = (Button)findViewById(R.id.button4);
		buildItButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				showMessage(R.string.build_it_bigger);
			}
		});
		xyzReaderButton = (Button)findViewById(R.id.button5);
		xyzReaderButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				showMessage(R.string.xyz_reader);
			}
		});
		capstoneButton = (Button)findViewById(R.id.button6);
		capstoneButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				showMessage(R.string.capstone_project);
			}
		});
	}

	public void showMessage(int appNameId) {
		String appName = getResources().getString(appNameId);
		Toast.makeText(getApplicationContext(), "This button will launch my " + appName + "!",
			Toast.LENGTH_SHORT).show();
	}

	public void showMessage(View buttonView) {
		Button button;
		if (buttonView instanceof Button) {
			button = (Button)buttonView;
		} else {
			return;
		}
		Toast.makeText(getApplicationContext(),
			"This button will launch my " + button.getText() + "!", Toast.LENGTH_SHORT).show();
	}
}
