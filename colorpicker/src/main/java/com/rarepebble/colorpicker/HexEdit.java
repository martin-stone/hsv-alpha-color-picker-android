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
