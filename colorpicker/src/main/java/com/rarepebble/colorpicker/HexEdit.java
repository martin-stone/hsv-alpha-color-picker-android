/*
 * Copyright (C) 2015 Martin Stone
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

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

public class HexEdit {

	public static void setUpListeners(final EditText hexEdit, final ObservableColor observableColor) {

		class MultiObserver implements ObservableColor.Observer, TextWatcher {

			@Override
			public void updateColor(ObservableColor observableColor) {
				// Prevent onTextChanged getting called when we update text programmatically
				hexEdit.removeTextChangedListener(this);

				final String colorString = String.format("%08x", observableColor.getColor());
				hexEdit.setText(colorString);

				hexEdit.addTextChangedListener(this);
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				try {
					int color = (int)(Long.parseLong(s.toString(), 16) & 0xffffffff);
					observableColor.updateColor(color, this);
				}
				catch (NumberFormatException e) {
					observableColor.updateColor(0, this);
				}
			}

			@Override
			public void afterTextChanged(Editable s) {
			}
		}

		final MultiObserver multiObserver = new MultiObserver();
		hexEdit.addTextChangedListener(multiObserver);
		observableColor.addObserver(multiObserver);
	}
}
