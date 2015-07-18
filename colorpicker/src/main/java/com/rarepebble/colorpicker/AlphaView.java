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

	@Override
	protected void notifyListener(float currentPos) {
		if (listener != null) {
			listener.onAlphaChanged((int)(currentPos * 0xff));
		}
	}

	public void setFromColor(int color) {
		this.color = color | 0xff000000;
		setPos((float)Color.alpha(color)/0xff);
		invalidate();
	}

	public void updateColor(int color) {
		this.color = color;
		optimisePointerColour();
		updateBitmap();
		invalidate();
	}

//	protected void optimisePointerColour() {
//		pointerPaint.setColor(currentAlpha > 0.5f ? 0xff000000 : 0xffffffff);
//	}


	public void setChangeListener(AlphaListener listener) {
		this.listener = listener;
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
