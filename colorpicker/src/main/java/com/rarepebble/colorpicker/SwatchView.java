package com.rarepebble.colorpicker;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

public class SwatchView extends View {

	private final Paint borderPaint;
	private final Path borderPath;
	private final Paint checkerPaint;
	private final Path oldFillPath;
	private final Path newFillPath;
	private final Paint oldFillPaint;
	private final Paint newFillPaint;

	public SwatchView(Context context) {
		this(context, null);
	}

	public SwatchView(Context context, AttributeSet attrs) {
		super(context, attrs);

		borderPaint = Resources.makeLinePaint(context);
		checkerPaint = Resources.makeCheckerPaint(context);
		oldFillPaint = new Paint();
		newFillPaint = new Paint();

		borderPath = new Path();
		oldFillPath = new Path();
		newFillPath = new Path();
	}

	void setOldColor(int color) {
		oldFillPaint.setColor(color);
		invalidate();
	}

	void setNewColor(int color) {
		newFillPaint.setColor(color);
		invalidate();
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
//		this.w = w;
//		this.h = h;
		final float inset = borderPaint.getStrokeWidth() / 2;
		final float r = Math.min(w, h);
		// We expect to be stacked behind a square HueSatView and fill the top left corner.
		final float margin = 24; //XXX dip
		final float diagonal = r + 2 * margin;
		final float opp = (float)Math.sqrt(diagonal * diagonal - r * r);
		final float edgeLen = r - opp;
		final float innerX = edgeLen + margin * opp / diagonal;
		final float innerY = margin * r / diagonal;

		borderPath.reset();
		borderPath.moveTo(inset, inset);
		borderPath.lineTo(edgeLen, inset);
		borderPath.lineTo(innerX, innerY);

		final float outerAngle = (float)Math.toDegrees(Math.atan2(opp, r));
		final float startAngle = 270 - outerAngle;
		final float sweepAngle = -90 + 2 * outerAngle;
		arcTo(borderPath, r, margin, startAngle, sweepAngle);
		borderPath.lineTo(innerY, innerX);
		borderPath.lineTo(inset, edgeLen);
		borderPath.lineTo(inset, inset);
		borderPath.close();

		oldFillPath.reset();
		oldFillPath.moveTo(inset, inset);
		final float sin45 = 1.0f/(float)Math.sqrt(2);
		final float midCurve = (r + margin) * sin45;
		oldFillPath.lineTo(midCurve, midCurve);
		arcTo(oldFillPath, r, margin, 225, sweepAngle / 2);
		oldFillPath.lineTo(innerY, innerX);
		oldFillPath.lineTo(inset, edgeLen);
		oldFillPath.lineTo(inset, inset);
		oldFillPath.close();

		newFillPath.reset();
		newFillPath.moveTo(inset, inset);
		newFillPath.lineTo(edgeLen, inset);
		newFillPath.lineTo(innerX, innerY);
		arcTo(newFillPath, r, margin, startAngle, sweepAngle / 2);
		newFillPath.lineTo(inset, inset);
		newFillPath.close();
	}

	private static void arcTo(Path path, float r, float margin, float startAngle, float sweepAngle) {
		path.arcTo(new RectF(-margin, -margin, 2 * r + margin, 2 * r + margin), startAngle, sweepAngle);
	}

	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		canvas.drawPath(borderPath, checkerPaint);
		canvas.drawPath(oldFillPath, oldFillPaint);
		canvas.drawPath(newFillPath, newFillPaint);
		canvas.drawPath(borderPath, borderPaint);
	}
}
