package com.nikmalov.portfolioproject;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.nikmalov.portfolioproject.PopularVideoApp.MoviePostersActivity;

import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class CoreMenu extends Activity {

	private Toast mAppToast;

	private final static String BUTTON_NOT_FOUND = "Button wasn't found.";
	private static Map<CharSequence, Class> projectButtonToActivityMap = new HashMap<>(6);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_core_menu);
        ButterKnife.bind(this);
		Resources resources = getResources();
		projectButtonToActivityMap.
			put(resources.getString(R.string.popular_movies), MoviePostersActivity.class);
		//new mappings are to be introduced
	}

    //for learning purposes first 3 buttons use xml-based approach
    @OnClick({R.id.app4LaunchButton, R.id.app5LaunchButton, R.id.app6LaunchButton})
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
