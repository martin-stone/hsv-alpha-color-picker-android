package com.rarepebble.colorpicker;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.AttributeSet;

public class ValueView extends SliderViewBase implements ObservableColor.Observer {

	private ObservableColor observableColor = new ObservableColor(0);

	public ValueView(Context context) {
		this(context, null);
	}

	public ValueView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public void observeColor(ObservableColor observableColor) {
		this.observableColor = observableColor;
		observableColor.addObserver(this);
	}

	@Override
	public void updateColor(ObservableColor observableColor) {
		setPos(this.observableColor.getValue());
		updateBitmap();
		invalidate();
	}

	@Override
	protected void notifyListener(float currentPos) {
		observableColor.updateValue(currentPos, this);
	}

	@Override
	protected int getPointerColor(float currentPos) {
		final int color = observableColor.getColor();
		float brightColorLightness = (Color.red(color) * 0.2126f + Color.green(color) * 0.7152f + Color.blue(color) * 0.0722f)/0xff;
		float posLightness = currentPos * brightColorLightness;
		return posLightness > 0.5f ? 0xff000000 : 0xffffffff;
	}

	protected Bitmap makeBitmap(int w, int h) {
		final boolean isWide = w > h;
		final int n = Math.max(w, h);
		int[] colors = new int[n];

		float[] hsv = new float[]{0, 0, 0};
		Color.colorToHSV(observableColor.getColor(), hsv);

		for (int i = 0; i < n; ++i) {
			hsv[2] = isWide ? (float)i / n : 1 - (float)i / n;
			colors[i] = Color.HSVToColor(hsv);
		}
		final int bmpWidth = isWide ? w : 1;
		final int bmpHeight = isWide ? 1 : h;
		return Bitmap.createBitmap(colors, bmpWidth, bmpHeight, Bitmap.Config.ARGB_8888);
	}

}
