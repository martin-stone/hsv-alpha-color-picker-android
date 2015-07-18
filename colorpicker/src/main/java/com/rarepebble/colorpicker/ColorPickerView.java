package com.rarepebble.colorpicker;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

public class ColorPickerView extends FrameLayout implements HueSatView.HueSatListener {
	private final HueSatView hueSatView;
	private int color;
	private final View swatchView;

	public ColorPickerView(Context context) {
		this(context, null);
	}

	public ColorPickerView(Context context, AttributeSet attrs) {
		super(context, attrs);
		LayoutInflater.from(context).inflate(R.layout.picker, this);

		hueSatView = (HueSatView)findViewById(R.id.hueSatView);
		hueSatView.setChangeListener(this);

		swatchView = findViewById(R.id.swatch);
	}

	public int getColor() {
		return color;
	}

	public void setColor(int color) {
		hueSatView.setFromColor(color);
	}

	@Override
	public void onHueSatChanged(float hue, float sat) {
//		Log.e("hue", Float.toString(hue));
//		Log.e("sat", Float.toString(sat));
		color = Color.HSVToColor(new float[]{hue, sat, 1});
		swatchView.setBackgroundColor(color);
	}


}
