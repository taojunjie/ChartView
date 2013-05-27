package com.michaelpardo.chartview.widget;

import android.graphics.Canvas;
import android.graphics.PointF;
import android.graphics.Rect;

import com.michaelpardo.chartview.graphics.RectD;

public class LinearSeries extends AbstractSeries {
	private PointF mLastPoint;

	private float mScaleX = Float.NaN;
	private float mScaleY = Float.NaN;

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
			canvas.drawLine(mLastPoint.x, mLastPoint.y, x, y, mPaint);
		}
		else {
			mLastPoint = new PointF();
		}

		mLastPoint.set(x, y);
	}

	@Override
	protected void onDrawingComplete() {
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