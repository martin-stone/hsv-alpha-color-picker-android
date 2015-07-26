package com.rarepebble.colorpickerdemo;

import android.app.Activity;
import android.os.Bundle;
import android.preference.PreferenceFragment;


public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		getFragmentManager().beginTransaction()
				.replace(android.R.id.content, new PreferenceFragment() {
					@Override
					public void onCreate(Bundle savedInstanceState) {
						super.onCreate(savedInstanceState);
						addPreferencesFromResource(R.xml.preferences);
					}
				})
				.commit();
	}
}
