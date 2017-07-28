/*
 * Copyright (C) 2015 Martin Stone
 * Copyright (C) 2017 Laszlo Kustra
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.rarepebble.colorpicker;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.preference.PreferenceDialogFragmentCompat;
import android.view.Window;
import android.view.WindowManager;

public class ColorPreferenceDialogFragmentCompat extends PreferenceDialogFragmentCompat {

	public static ColorPreferenceDialogFragmentCompat newInstance(String key) {
		final ColorPreferenceDialogFragmentCompat fragment =
				new ColorPreferenceDialogFragmentCompat();
		final Bundle b = new Bundle(1);
		b.putString(ARG_KEY, key);
		fragment.setArguments(b);
		return fragment;
	}

	private ColorPreferenceCompat getColorPreference() {
		return (ColorPreferenceCompat) getPreference();
	}

	@Override
	protected void onPrepareDialogBuilder(AlertDialog.Builder builder) {
		super.onPrepareDialogBuilder(builder);
		final ColorPreferenceCompat preference = getColorPreference();
		Integer persistedColor = preference.getColor();

		final ColorPickerView picker = new ColorPickerView(getContext());
		picker.setColor(
				persistedColor != null
						? persistedColor
						: (preference.defaultColor != null ? preference.defaultColor : Color.GRAY));
		picker.showAlpha(preference.showAlpha);
		picker.showHex(preference.showHex);
		picker.showPreview(preference.showPreview);
		builder
				.setTitle(null)
				.setView(picker)
				.setPositiveButton(preference.getPositiveButtonText(), new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						final int color = picker.getColor();
						if (preference.callChangeListener(color)) {
							preference.setColor(color);
						}
					}
				});
		if (preference.selectNoneButtonText != null) {
			builder.setNeutralButton(preference.selectNoneButtonText, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					if (preference.callChangeListener(null)) {
						preference.setColor(null);
					}
				}
			});
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		hideKeyboard();
	}

	private void hideKeyboard() {
		Window window = getDialog().getWindow();
		if (window != null) {
			window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		}
	}

	@Override
	public void onDialogClosed(boolean positiveResult) {
		// do nothing
	}
}
