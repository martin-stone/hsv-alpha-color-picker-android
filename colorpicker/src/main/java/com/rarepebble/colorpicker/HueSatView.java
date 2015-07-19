package com.rarepebble.colorpicker;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class HueSatView extends View {

	public interface HueSatListener {
		void onHueSatChanged(float hue, float sat);
	}

	private HueSatListener listener;
	private final Paint borderPaint;
	private final Paint pointerPaint;
	private final Path pointerPath;
	private final Path borderPath;
	private int w;
	private int h;
	private Bitmap bitmap;

	private PointF pointer = new PointF();
	private float currentHue;
	private float currentSat;

	public HueSatView(Context context) {
		this(context, null);
	}

	public HueSatView(Context context, AttributeSet attrs) {
		super(context, attrs);

		borderPaint = Resources.makeLinePaint(context);
		pointerPaint = Resources.makeLinePaint(context);
		pointerPaint.setColor(0xff000000);
		pointerPath = Resources.makePointerPath(context);
		borderPath = new Path();
	}

	public void setFromColor(int color) {
		float[] hsv = new float[]{0f, 0f, 1f};
		Color.colorToHSV(color, hsv);
		currentHue = hsv[0];
		currentSat = hsv[1];
		setPointer(pointer, currentHue, currentSat, w);
	}

	public void setChangeListener(HueSatListener listener) {
		this.listener = listener;
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// Constrain to square
		int w = MeasureSpec.getSize(widthMeasureSpec);
		int h = MeasureSpec.getSize(heightMeasureSpec);
		w = h = Math.min(w, h);
		setMeasuredDimension(w, h);
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		this.w = w;
		this.h = h;
		float inset = borderPaint.getStrokeWidth() / 2;
		makeBorderPath(borderPath, w, h, inset);

		final int scale = 2;
		final int bitmapRadius = Math.min(w, h) / scale;
		bitmap = makeBitmap(bitmapRadius);

		setPointer(pointer, currentHue, currentSat, w);
	}

	private static void makeBorderPath(Path borderPath, int w, int h, float inset) {
		w -= inset;
		h -= inset;
		borderPath.reset();
		borderPath.moveTo(w, inset);
		borderPath.lineTo(w, h);
		borderPath.lineTo(inset, h);
		borderPath.addArc(new RectF(inset, inset, 2 * w, 2 * h), 180, 270);
		borderPath.close();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		int action = event.getActionMasked();
		switch (action) {
			case MotionEvent.ACTION_DOWN:
			case MotionEvent.ACTION_MOVE:
				clamp(pointer, event.getX(), event.getY());
				currentHue = hueForPos(pointer.x, pointer.y, w);
				currentSat = satForPos(pointer.x, pointer.y, w);
				if (listener != null) {
					listener.onHueSatChanged(currentHue, currentSat);
				}
				invalidate();
				return true;
		}
		return super.onTouchEvent(event);
	}

	private void clamp(PointF pointer, float x, float y) {
		x = Math.min(x, w);
		y = Math.min(y, h);
		final float dx = w - x;
		final float dy = h - y;
		final float r = (float)Math.sqrt(dx * dx + dy * dy);
		if (r > w) {
			x = w - dx * w / r;
			y = w - dy * w / r;
		}
		pointer.set(x, y);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		canvas.drawBitmap(bitmap, null, new Rect(0, 0, w, h), null);
		canvas.drawPath(borderPath, borderPaint);

		canvas.save(Canvas.MATRIX_SAVE_FLAG);
		canvas.translate(pointer.x, pointer.y);
		canvas.drawPath(pointerPath, pointerPaint);
		canvas.restore();
	}

	private static Bitmap makeBitmap(int radiusPx) {
		int[] colors = new int[radiusPx * radiusPx];
		float[] hsv = new float[]{0f, 0f, 1f};
		for (int y = 0; y < radiusPx; ++y) {
			for (int x = 0; x < radiusPx; ++x) {
				int i = x + y * radiusPx;
				float sat = satForPos(x, y, radiusPx);
				int alpha = (int)(Math.max(0, Math.min(1, (1 - sat) * radiusPx)) * 255); // antialias edge
				hsv[0] = hueForPos(x, y, radiusPx);
				hsv[1] = sat;
				colors[i] = Color.HSVToColor(alpha, hsv);
			}
		}
		return Bitmap.createBitmap(colors, radiusPx, radiusPx, Bitmap.Config.ARGB_8888);
	}

	private static float hueForPos(float x, float y, float radiusPx) {
		final double r = radiusPx - 1; // gives values 0...1 inclusive
		final double dx = (r - x) / r;
		final double dy = (r - y) / r;
		final double angle = Math.atan2(dy, dx);
		final double hue = 360 * angle / (Math.PI / 2);
		return (float)hue;
	}

	private static float satForPos(float x, float y, float radiusPx) {
		final double r = radiusPx - 1; // gives values 0...1 inclusive
		final double dx = (r - x) / r;
		final double dy = (r - y) / r;
		final double sat = dx * dx + dy * dy; // leave it squared -- exaggerates pale colours
		return (float)sat;
	}

	private static void setPointer(PointF pointer, float hue, float sat, float radiusPx) {
		final float r = radiusPx - 1; // for values 0...1 inclusive
		final double distance = r * Math.sqrt(sat);
		final double angle = hue / 360 * Math.PI / 2;
		final double dx = distance * Math.cos(angle);
		final double dy = distance * Math.sin(angle);
		pointer.set(r - (float)dx, r - (float)dy);
	}


}
