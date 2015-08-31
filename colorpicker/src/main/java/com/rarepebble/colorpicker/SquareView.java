package com.rarepebble.colorpicker;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

class SquareView extends View {
	private static final int MIN_SIZE_DIP = 200;

	private final int minSizePx;

	public SquareView(Context context, AttributeSet attrs) {
		super(context, attrs);
		minSizePx = (int)Resources.dipToPixels(context, MIN_SIZE_DIP);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// Constrain to square
		final int w = MeasureSpec.getSize(widthMeasureSpec);
		final int h = MeasureSpec.getSize(heightMeasureSpec);
		final int modeW = MeasureSpec.getMode(widthMeasureSpec);
		final int modeH = MeasureSpec.getMode(heightMeasureSpec);
		int size = minSizePx;
		if (modeW == MeasureSpec.EXACTLY) {
			size = w;
		}
		else if (modeH == MeasureSpec.EXACTLY) {
			size = h;
		}
		else if (modeW == MeasureSpec.AT_MOST && modeH == MeasureSpec.AT_MOST) {
			size = Math.min(w, h);
		}
		setMeasuredDimension(size, size);
	}
}
