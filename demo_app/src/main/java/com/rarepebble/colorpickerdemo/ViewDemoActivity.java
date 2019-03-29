package com.rarepebble.colorpickerdemo;

import android.app.Activity;
import android.os.Bundle;

import com.rarepebble.colorpicker.ColorPickerView;

public class ViewDemoActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view_demo);

		ColorPickerView picker = (ColorPickerView)findViewById(R.id.colorPicker);
		picker.setColor(0xffff0000);
	}
}
