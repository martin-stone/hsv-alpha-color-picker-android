package com.rarepebble.colorpickerdemo;

import android.app.Activity;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.widget.Toast;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.replace(android.R.id.content, new DemoPreferenceFragment())
					.commit();
		}
	}

	static public class DemoPreferenceFragment extends PreferenceFragment {
		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			addPreferencesFromResource(R.xml.preferences);

			demonstrateChangeListener();
		}

		private void demonstrateChangeListener() {
			findPreference("optionalColor").setOnPreferenceChangeListener(
					new Preference.OnPreferenceChangeListener() {
						@Override
						public boolean onPreferenceChange(Preference preference, Object newValue) {
							Toast.makeText(getActivity(), String.format("%x", newValue), Toast.LENGTH_SHORT).show();
							return true;
						}
					});
		}
	}
}
