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
import android.view.View;

public class HueSatView extends View {
	private final Paint borderPaint;
	private int w;
	private int h;
	private Path border;
	private Bitmap bitmap;

	public HueSatView(Context context) {
		this(context, null);
	}

	public HueSatView(Context context, AttributeSet attrs) {
		super(context, attrs);
		borderPaint = new Paint();
		borderPaint.setColor(0xff808080);
		borderPaint.setStrokeWidth(2); //XXX dip to pixels
		borderPaint.setStyle(Paint.Style.STROKE);
		borderPaint.setAntiAlias(true);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int w = MeasureSpec.getSize(widthMeasureSpec);
		int h = MeasureSpec.getSize(heightMeasureSpec);
		w = h = Math.min(w, h);
		setMeasuredDimension(w, h);
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		//super.onSizeChanged(w, h, oldw, oldh);
		this.w = w;
		this.h = h;
		border = new Path();
		border.moveTo(w, 0);
		border.lineTo(w, h);
		border.lineTo(0, h);
		border.addArc(new RectF(0, 0, 2 * w, 2 * h), 180, 270);

		int scale = 2;
		int radius = Math.min(w, h) / scale;
		bitmap = makeBitmap(radius);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		canvas.drawBitmap(bitmap, null, new Rect(0, 0, w, h), null);
		canvas.drawPath(border, borderPaint);
	}

	private static Bitmap makeBitmap(int radiusPx) {
		int[] colors = new int[radiusPx * radiusPx];
		float[] hsv = new float[]{0f, 0f, 1f};
		for (int y = 0; y < radiusPx; ++y) {
			for (int x = 0; x < radiusPx; ++x) {
				int i = x + y * radiusPx;
				double r = radiusPx-1; // gives values 0...1 inclusive
				double dx = (r-x)/r;
				double dy = (r-y)/r;
				final double angle = Math.atan2(dy, dx);
				final double distance = Math.sqrt(dx*dx + dy*dy);
				double hue = 360 * angle / (Math.PI/2);
				double sat = Math.pow(distance, 2); // exaggerate desaturated
				int alpha = (int)(Math.max(0, Math.min(1, (1-sat)*r)*255)); // antialias edge
				hsv[0] = (float)hue;
				hsv[1] = (float)sat;
				colors[i] = Color.HSVToColor(alpha, hsv);
			}
		}
		return Bitmap.createBitmap(colors, radiusPx, radiusPx, Bitmap.Config.ARGB_8888);
	}
}
