package com.rarepebble.colorpicker;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.AttributeSet;

public class AlphaView extends SliderViewBase {

	private AlphaListener listener;

	private int color;

	public interface AlphaListener {
		void onAlphaChanged(int alpha);
	}

	public AlphaView(Context context) {
		this(context, null);
	}

	public AlphaView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public void setFromColor(int color) {
		this.color = color | 0xff000000;
		setPos((float)Color.alpha(color)/0xff);
		invalidate();
	}

	public void updateColor(int color) {
		this.color = color;
		updateBitmap();
		invalidate();
	}

	public void setChangeListener(AlphaListener listener) {
		this.listener = listener;
	}

	@Override
	protected void notifyListener(float currentPos) {
		if (listener != null) {
			listener.onAlphaChanged((int)(currentPos * 0xff));
		}
	}

	@Override
	protected int getPointerColor(float currentPos) {
		float solidColorLightness = (Color.red(color) * 0.2126f + Color.green(color) * 0.7152f + Color.blue(color) * 0.0722f)/0xff;
		float posLightness = 1 + currentPos * (solidColorLightness - 1);
		return posLightness > 0.5f ? 0xff000000 : 0xffffffff;
	}

	@Override
	protected Bitmap makeBitmap(int w, int h) {
		final boolean isWide = w > h;
		final int n = Math.max(w, h);
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
