package com.rarepebble.colorpicker;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.AttributeSet;

public class ValueView extends SliderViewBase {

	private ValueListener listener;

	private float hue;
	private float sat;

	public interface ValueListener {
		void onValueChanged(float value);
	}

	public ValueView(Context context) {
		this(context, null);
	}

	public ValueView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public void setFromColor(int color) {
		float[] hsv = new float[]{0f, 0f, 0f};
		Color.colorToHSV(color, hsv);
		hue = hsv[0];
		sat = hsv[1];
		setPos(hsv[2]);
		invalidate();
	}

	public void updateHueSat(float hue, float sat) {
		this.hue = hue;
		this.sat = sat;
		updateBitmap();
		invalidate();
	}

	public void setChangeListener(ValueListener listener) {
		this.listener = listener;
	}

	@Override
	protected void notifyListener(float currentPos) {
		if (listener != null) {
			listener.onValueChanged(currentPos);
		}
	}

	@Override
	protected int getPointerColor(float currentPos) {
		final int color = Color.HSVToColor(new float[]{hue, sat, currentPos});
		float brightColorLightness = (Color.red(color) * 0.2126f + Color.green(color) * 0.7152f + Color.blue(color) * 0.0722f)/0xff;
		float posLightness = currentPos * brightColorLightness;
		return posLightness > 0.5f ? 0xff000000 : 0xffffffff;
	}

	protected Bitmap makeBitmap(int w, int h) {
		final boolean isWide = w > h;
		final int n = Math.max(w, h);
		int[] colors = new int[n];
		float[] hsv = new float[]{hue, sat, 0f};
		for (int i = 0; i < n; ++i) {
			hsv[2] = isWide ? (float)i / n : 1 - (float)i / n;
			colors[i] = Color.HSVToColor(hsv);
		}
		final int bmpWidth = isWide ? w : 1;
		final int bmpHeight = isWide ? 1 : h;
		return Bitmap.createBitmap(colors, bmpWidth, bmpHeight, Bitmap.Config.ARGB_8888);
	}

}
