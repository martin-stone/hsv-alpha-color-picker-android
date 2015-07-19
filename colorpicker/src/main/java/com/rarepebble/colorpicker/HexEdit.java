package com.rarepebble.colorpicker;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

public class HexEdit {

	public static void setUpListeners(final EditText hexEdit, final ObservableColor observableColor) {

		final ObservableColor.Observer observer = new ObservableColor.Observer() {
			@Override
			public void updateColor(ObservableColor observableColor) {
				final String colorString = String.format("%08x", observableColor.getColor());
				hexEdit.setText(colorString);
			}
		};

		observableColor.addObserver(observer);

		hexEdit.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				try {
					int color = (int)(Long.parseLong(s.toString(), 16) & 0xffffffff);
					observableColor.notifyOtherObservers(color, observer);
				}
				catch (NumberFormatException e) {
					observableColor.notifyOtherObservers(0, observer);
				}
			}

			@Override
			public void afterTextChanged(Editable s) {
			}
		});
	}

}
