package com.rarepebble.colorpicker;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class ValueView extends View {

	private ValueListener listener;
	private final Paint borderPaint;
	private int w;
	private int h;
	private Path borderPath;
	private Bitmap bitmap;
	private final Path pointerPath;
	private final Paint pointerPaint;

	private float hue;
	private float sat;
	private float currentValue;

	public interface ValueListener {
		void onValueChanged(float value);
	}

	public ValueView(Context context) {
		this(context, null);
	}

	public ValueView(Context context, AttributeSet attrs) {
		super(context, attrs);

		borderPaint = new Paint();
		borderPaint.setColor(0xff808080);
		borderPaint.setStrokeWidth(2); //XXX dip to pixels
		borderPaint.setStyle(Paint.Style.STROKE);
		borderPaint.setAntiAlias(true);

		pointerPaint = new Paint();
		pointerPaint.setColor(0xff808080);
		pointerPaint.setStrokeWidth(2); //XXX dip to pixels
		pointerPaint.setStyle(Paint.Style.STROKE);
		pointerPaint.setAntiAlias(true);

		pointerPath = new Path();
		pointerPath.addCircle(0, 0, 10, Path.Direction.CW); //XXX dip to pixels
	}

	public void setFromColor(int color) {
		float[] hsv = new float[]{0f, 0f, 0f};
		Color.colorToHSV(color, hsv);
		hue = hsv[0];
		sat = hsv[1];
		currentValue = hsv[2];
		optimisePointerColour();
		invalidate();
	}

	public void updateHueSat(float hue, float sat) {
		this.hue = hue;
		this.sat = sat;
		bitmap = makeBitmap(hue, sat, w, h);
		invalidate();
	}

	private void optimisePointerColour() {
		pointerPaint.setColor(currentValue > 0.5f ? 0xff000000 : 0xffffffff);
	}


	public void setChangeListener(ValueListener listener) {
		this.listener = listener;
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		this.w = w;
		this.h = h;
		borderPath = new Path();
		borderPath.addRect(new RectF(0, 0, w, h), Path.Direction.CW);

		bitmap = makeBitmap(hue, sat, w, h);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		int action = event.getActionMasked();
		switch (action) {
			case MotionEvent.ACTION_DOWN:
			case MotionEvent.ACTION_MOVE:
				currentValue = valueForPos(event.getX(), event.getY());
				optimisePointerColour();
				if (listener != null) {
					listener.onValueChanged(currentValue);
				}
				invalidate();
				return true;
		}
		return super.onTouchEvent(event);
	}

//	private void clamp(PointF pointer, float x, float y) {
//		x = Math.min(x, w);
//		y = Math.min(y, h);
//		final float dx = w - x;
//		final float dy = h - y;
//		final float r = (float)Math.sqrt(dx * dx + dy * dy);
//		if (r > w) {
//			x = w - dx * w / r;
//			y = w - dy * w / r;
//		}
//		pointer.set(x, y);
//	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		canvas.drawBitmap(bitmap, null, new Rect(0, 0, w, h), null);
		canvas.drawPath(borderPath, borderPaint);

		canvas.save(Canvas.MATRIX_SAVE_FLAG);
		if (isWide()) {
			canvas.translate(w * currentValue, h / 2);
		}
		else {
			canvas.translate(w / 2, h * (1 - currentValue));
		}
		canvas.drawPath(pointerPath, pointerPaint);
		canvas.restore();
	}

	private boolean isWide() {
		return w > h;
	}

	private static Bitmap makeBitmap(float hue, float sat, int w, int h) {
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

	private float valueForPos(float x, float y) {
		final float val = isWide() ? (float)x / w : 1 - (float)y / h;
		return Math.max(0, Math.min(1, val));
	}

}
