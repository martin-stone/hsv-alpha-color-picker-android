package com.rarepebble.colorpicker;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

public class ColorPickerView extends FrameLayout implements HueSatView.HueSatListener, ValueView.ValueListener {
	private final HueSatView hueSatView;
	private final ValueView valueView;
	private int color;
	private final View swatchView;
	private float hue;
	private float sat;
	private float value;

	public ColorPickerView(Context context) {
		this(context, null);
	}

	public ColorPickerView(Context context, AttributeSet attrs) {
		super(context, attrs);
		LayoutInflater.from(context).inflate(R.layout.picker, this);

		hueSatView = (HueSatView)findViewById(R.id.hueSatView);
		hueSatView.setChangeListener(this);

		valueView = (ValueView)findViewById(R.id.valueView);
		valueView.setChangeListener(this);

		swatchView = findViewById(R.id.swatch);
	}

	public int getColor() {
		return color;
	}

	public void setColor(int color) {
		float[] hsv = {0,0,0};
		Color.colorToHSV(color, hsv);
		hue = hsv[0];
		sat = hsv[1];
		value = hsv[2];
		swatchView.setBackgroundColor(color);
		hueSatView.setFromColor(color);
		valueView.setFromColor(color);
	}

	@Override
	public void onHueSatChanged(float hue, float sat) {
//		Log.e("hue", Float.toString(hue));
//		Log.e("sat", Float.toString(sat));
		this.hue = hue;
		this.sat = sat;
		valueView.updateHueSat(hue, sat);
		updateColor();
	}

	@Override
	public void onValueChanged(float value) {
		this.value = value;
		updateColor();
	}

	private void updateColor() {
		color = Color.HSVToColor(new float[]{hue, sat, value});
		swatchView.setBackgroundColor(color);
	}

}
