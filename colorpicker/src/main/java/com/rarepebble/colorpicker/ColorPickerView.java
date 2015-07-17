package com.rarepebble.colorpicker;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

public class ColorPickerView extends FrameLayout implements HueSatView.HueSatListener {
	private final View swatchView;

	public ColorPickerView(Context context) {
		this(context, null);
	}

	public ColorPickerView(Context context, AttributeSet attrs) {
		super(context, attrs);
		LayoutInflater.from(context).inflate(R.layout.picker, this);

		final HueSatView hueSatView = (HueSatView)findViewById(R.id.hueSatView);
		hueSatView.setChangeListener(this);

		swatchView = findViewById(R.id.swatch);
	}


	@Override
	public void onHueSatChanged(float hue, float sat) {
//		Log.e("hue", Float.toString(hue));
//		Log.e("sat", Float.toString(sat));
		swatchView.setBackgroundColor(Color.HSVToColor(new float[]{hue, sat, 1}));
	}
}
