package com.rarepebble.colorpicker;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

public class ColorPickerView extends FrameLayout implements HueSatView.HueSatListener, ValueView.ValueListener, AlphaView.AlphaListener {
	private final HueSatView hueSatView;
	private final ValueView valueView;
	private final AlphaView alphaView;
	private int color;
	private final View swatchView;
	private float hue;
	private float sat;
	private float value;
	private int alpha;

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

		alphaView = (AlphaView)findViewById(R.id.alphaView);
		alphaView.setChangeListener(this);

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
		alpha = Color.alpha(color);
		swatchView.setBackgroundColor(color);
		hueSatView.setFromColor(color);
		valueView.setFromColor(color);
		alphaView.setFromColor(color);
	}

	@Override
	public void onHueSatChanged(float hue, float sat) {
		this.hue = hue;
		this.sat = sat;
		valueView.updateHueSat(hue, sat);
		updateColor();
		alphaView.updateColor(color);
	}

	@Override
	public void onValueChanged(float value) {
		this.value = value;
		updateColor();
		alphaView.updateColor(color);
	}

	@Override
	public void onAlphaChanged(int alpha) {
		this.alpha = alpha;
		updateColor();
	}

	private void updateColor() {
		color = Color.HSVToColor(alpha, new float[]{hue, sat, value});
		swatchView.setBackgroundColor(color);
	}

}
