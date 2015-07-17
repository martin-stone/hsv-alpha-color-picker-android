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
	private final Paint borderPaint;
	private final Path pointerPath;
	private int w;
	private int h;
	private Path borderPath;
	private Bitmap bitmap;
	private PointF pointer = new PointF();
	private Paint pointerPaint;

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

		pointerPaint = new Paint();
		pointerPaint.setColor(0xff000000);
		pointerPaint.setStrokeWidth(2); //XXX dip to pixels
		pointerPaint.setStyle(Paint.Style.STROKE);
		pointerPaint.setAntiAlias(true);

		pointerPath = new Path();
		pointerPath.addCircle(0, 0, 10, Path.Direction.CW); //XXX dip to pixels
	}

	public void setFromColor(int color) {
		float[] hsv = new float[]{0f, 0f, 1f};
		Color.colorToHSV(color, hsv);
		//pointer.set();
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
		borderPath = new Path();
		borderPath.moveTo(w, 0);
		borderPath.lineTo(w, h);
		borderPath.lineTo(0, h);
		borderPath.addArc(new RectF(0, 0, 2 * w, 2 * h), 180, 270);
		borderPath.close();

		final int scale = 2;
		final int radius = Math.min(w, h) / scale;
		bitmap = makeBitmap(radius);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		int action = event.getActionMasked();
		switch (action) {
			case MotionEvent.ACTION_DOWN:
			case MotionEvent.ACTION_MOVE:
				clamp(pointer, event.getX(), event.getY());
//				if ( listener != null ) {
//					listener.colorSelected( getColorForPoint( (int)event.getX(), (int)event.getY(), colorHsv ) );
//				}
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
				double r = radiusPx - 1; // gives values 0...1 inclusive
				double dx = (r - x) / r;
				double dy = (r - y) / r;
				final double angle = Math.atan2(dy, dx);
				final double distance = Math.sqrt(dx * dx + dy * dy);
				double hue = 360 * angle / (Math.PI / 2);
				double sat = Math.pow(distance, 2); // exaggerate desaturated
				int alpha = (int)(Math.max(0, Math.min(1, (1 - sat) * r) * 255)); // antialias edge
				hsv[0] = (float)hue;
				hsv[1] = (float)sat;
				colors[i] = Color.HSVToColor(alpha, hsv);
			}
		}
		return Bitmap.createBitmap(colors, radiusPx, radiusPx, Bitmap.Config.ARGB_8888);
	}


}
