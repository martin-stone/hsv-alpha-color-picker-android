package com.rarepebble.colorpickerdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;

import com.rarepebble.colorpicker.ColorPreference;

public class MainActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.replace(android.R.id.content, new DemoPreferenceFragment())
					.commit();
			NonDeveloperMessage.maybeShow(this);
		}
	}

	static public class DemoPreferenceFragment extends PreferenceFragmentCompat {
		@Override
		public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
			addPreferencesFromResource(R.xml.preferences);
		}

		@Override
		public void onDisplayPreferenceDialog(Preference preference) {
			if (preference instanceof ColorPreference) {
				((ColorPreference) preference).showDialog(this, 0);
			} else super.onDisplayPreferenceDialog(preference);
		}
	}
}
