package com.michaelpardo.chartview.widget;

import android.graphics.*;

import com.michaelpardo.chartview.graphics.RectD;

public class LinearSeries extends AbstractSeries {
	private boolean mSmooth;

	private PointF mLastPoint;

	private float mScaleX = Float.NaN;
	private float mScaleY = Float.NaN;

	private Path mPath = new Path();

	public LinearSeries() {
		this(true);
	}

	public LinearSeries(boolean smooth) {
		super();

		mSmooth = smooth;
	}

	@Override
	public void drawPoint(Canvas canvas, AbstractPoint point, Rect viewBounds, RectD viewport) {
		if (Float.isNaN(mScaleX)) {
			mScaleX = (float) viewBounds.width() / (float) viewport.width();
		}
		if (Float.isNaN(mScaleY)) {
			mScaleY = (float) viewBounds.height() / (float) viewport.height();
		}

		final float x = (float) (viewBounds.left + (mScaleX * (point.getX() - getMinX())));
		final float y = (float) (viewBounds.bottom - (mScaleY * (point.getY() - getMinY())));

		if (mLastPoint != null) {
			if (mSmooth) {
				mPath.quadTo(mLastPoint.x, mLastPoint.y, (x + mLastPoint.x) / 2, (y + mLastPoint.y) / 2);
			}
			else {
                mPath.lineTo(x, y);
			}
		}
		else {
			mLastPoint = new PointF();
			mPath.moveTo(x, y);
		}

		mLastPoint.set(x, y);
	}

	@Override
	protected void onDrawingStarted(Canvas canvas) {
		mPaint.setStyle(Paint.Style.STROKE);
		mPaint.setStrokeCap(Paint.Cap.ROUND);
	}

	@Override
	protected void onDrawingComplete(Canvas canvas) {
		mPath.lineTo(mLastPoint.x, mLastPoint.y);

		canvas.drawPath(mPath, mPaint);

		mLastPoint = null;
		mScaleX = Float.NaN;
		mScaleY = Float.NaN;
	}

	public static class LinearPoint extends AbstractPoint {
		public LinearPoint() {
			super();
		}

		public LinearPoint(double x, double y) {
			super(x, y);
		}
	}
}
