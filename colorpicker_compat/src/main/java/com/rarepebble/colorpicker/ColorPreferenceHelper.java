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

import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;

public class ColorPreferenceHelper {
	private static final String DIALOG_FRAGMENT_TAG =
			"android.support.v7.preference.PreferenceFragment.DIALOG";

	private PreferenceFragmentCompat preferenceFragment;

	public ColorPreferenceHelper(PreferenceFragmentCompat preferenceFragment) {
		this.preferenceFragment = preferenceFragment;
	}

	public boolean onDisplayPreferenceDialog(Preference preference) {
		if (preference instanceof ColorPreferenceCompat) {

			if (preferenceFragment.getFragmentManager().findFragmentByTag(DIALOG_FRAGMENT_TAG) == null) {
				ColorPreferenceDialogFragmentCompat fragment =
						ColorPreferenceDialogFragmentCompat.newInstance(preference.getKey());
				fragment.setTargetFragment(preferenceFragment, 0);
				fragment.show(preferenceFragment.getFragmentManager(), DIALOG_FRAGMENT_TAG);
			}

			return true;
		} else {
			return false;
		}
	}
}
