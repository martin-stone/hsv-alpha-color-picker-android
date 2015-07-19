package com.rarepebble.colorpicker;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public abstract class SliderViewBase extends View {

	private final Paint borderPaint;
	private final Paint checkerPaint;
	private int w;
	private int h;
	private final Path borderPath;
	private Bitmap bitmap;
	private final Path pointerPath;
	private final Paint pointerPaint;

	private float currentPos;

	public SliderViewBase(Context context, AttributeSet attrs) {
		super(context, attrs);
		checkerPaint = Resources.makeCheckerPaint(context);
		borderPaint = Resources.makeLinePaint(context);
		pointerPaint = Resources.makeLinePaint(context);
		pointerPath = Resources.makePointerPath(context);
		borderPath = new Path();
	}

	protected abstract void notifyListener(float currentPos);

	protected abstract Bitmap makeBitmap(int w, int h);

	protected abstract int getPointerColor(float currentPos);

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		this.w = w;
		this.h = h;
		borderPath.reset();
		borderPath.addRect(new RectF(0, 0, w, h), Path.Direction.CW);
		updateBitmap();
	}

	protected void setPos(float pos) {
		currentPos = pos;
		optimisePointerColour();
	}

	protected void updateBitmap() {
		bitmap = makeBitmap(w, h);
		optimisePointerColour();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		int action = event.getActionMasked();
		switch (action) {
			case MotionEvent.ACTION_DOWN:
			case MotionEvent.ACTION_MOVE:
				currentPos = valueForTouchPos(event.getX(), event.getY());
				optimisePointerColour();
				notifyListener(currentPos);
				invalidate();
				return true;
		}
		return super.onTouchEvent(event);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		canvas.drawPath(borderPath, checkerPaint);
		canvas.drawBitmap(bitmap, null, new Rect(0, 0, w, h), null);
		canvas.drawPath(borderPath, borderPaint);

		canvas.save(Canvas.MATRIX_SAVE_FLAG);
		if (isWide()) {
			canvas.translate(w * currentPos, h / 2);
		}
		else {
			canvas.translate(w / 2, h * (1 - currentPos));
		}
		canvas.drawPath(pointerPath, pointerPaint);
		canvas.restore();
	}

	private boolean isWide() {
		return w > h;
	}

	private float valueForTouchPos(float x, float y) {
		final float val = isWide() ? x / w : 1 - y / h;
		return Math.max(0, Math.min(1, val));
	}

	private void optimisePointerColour() {
		pointerPaint.setColor(getPointerColor(currentPos));
	}
}
