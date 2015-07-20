package com.rarepebble.colorpicker;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.AttributeSet;

public class AlphaView extends SliderViewBase implements ObservableColor.Observer {

	private ObservableColor observableColor = new ObservableColor(0);

	public AlphaView(Context context) {
		this(context, null);
	}

	public AlphaView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public void observeColor(ObservableColor observableColor) {
		this.observableColor = observableColor;
		observableColor.addObserver(this);
	}

	@Override
	public void updateColor(ObservableColor observableColor) {
		setPos((float)observableColor.getAlpha()/0xff);
		updateBitmap();
		invalidate();
	}

	@Override
	protected void notifyListener(float currentPos) {
		observableColor.updateAlpha((int)(currentPos * 0xff), this);
	}

	@Override
	protected int getPointerColor(float currentPos) {
		int color = observableColor.getColor();
		float solidColorLightness = (Color.red(color) * 0.2126f + Color.green(color) * 0.7152f + Color.blue(color) * 0.0722f)/0xff;
		float posLightness = 1 + currentPos * (solidColorLightness - 1);
		return posLightness > 0.5f ? 0xff000000 : 0xffffffff;
	}

	@Override
	protected Bitmap makeBitmap(int w, int h) {
		final boolean isWide = w > h;
		final int n = Math.max(w, h);
		int color = observableColor.getColor();
		int[] colors = new int[n];
		for (int i = 0; i < n; ++i) {
			float alpha = isWide ? (float)i / n : 1 - (float)i / n;
			colors[i] = color & 0xffffff | (int)(alpha * 0xff) << 24;
		}
		final int bmpWidth = isWide ? w : 1;
		final int bmpHeight = isWide ? 1 : h;
		return Bitmap.createBitmap(colors, bmpWidth, bmpHeight, Bitmap.Config.ARGB_8888);
	}

}
