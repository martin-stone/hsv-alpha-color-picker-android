package com.rarepebble.colorpicker;

import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import androidx.preference.PreferenceDialogFragmentCompat;

public class ColorPreferenceFragment extends PreferenceDialogFragmentCompat {

	public static ColorPreferenceFragment newInstance(String prefKey) {
		ColorPreferenceFragment fragment = new ColorPreferenceFragment();
		Bundle bundle = new Bundle(1);
		bundle.putString(PreferenceDialogFragmentCompat.ARG_KEY, prefKey);
		fragment.setArguments(bundle);
		return fragment;
	}

	@Override
	protected void onPrepareDialogBuilder(AlertDialog.Builder builder) {
		super.onPrepareDialogBuilder(builder);
		((ColorPreference)getPreference()).prepareDialogBuilder(builder);
	}

	@Override
	public void onDialogClosed(boolean b) {
	}
}
