package com.rarepebble.colorpickerdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;

import com.rarepebble.colorpicker.ColorPreferenceHelper;

public class CompatActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.replace(android.R.id.content, new DemoPreferenceFragment())
					.commit();
		}
	}

	static public class DemoPreferenceFragment extends PreferenceFragmentCompat {

		private ColorPreferenceHelper helper;

		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			helper = new ColorPreferenceHelper(this);
		}

		@Override
		public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
			addPreferencesFromResource(R.xml.preferences_compat);
		}

		@Override
		public void onDisplayPreferenceDialog(Preference preference) {
			if (!helper.onDisplayPreferenceDialog(preference)) {
				super.onDisplayPreferenceDialog(preference);
			}
		}
	}
}
