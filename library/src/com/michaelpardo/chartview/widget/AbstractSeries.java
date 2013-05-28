package com.michaelpardo.chartview.widget;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import com.michaelpardo.chartview.graphics.RectD;

public abstract class AbstractSeries {
	//////////////////////////////////////////////////////////////////////////////////////
	// PRIVATE MEMBERS
	//////////////////////////////////////////////////////////////////////////////////////

	protected Paint mPaint = new Paint();

	private List<AbstractPoint> mPoints;
	private boolean mPointsSorted = false;

	private double mMinX = Double.MAX_VALUE;
	private double mMaxX = -Double.MAX_VALUE;
	private double mMinY = Double.MAX_VALUE;
	private double mMaxY = -Double.MAX_VALUE;

	private double mRangeX = 0;
	private double mRangeY = 0;

	protected abstract void drawPoint(Canvas canvas, AbstractPoint point, Rect viewBounds, RectD viewport);

	//////////////////////////////////////////////////////////////////////////////////////
	// CONSTRUCTORS
	//////////////////////////////////////////////////////////////////////////////////////

	public AbstractSeries() {
		mPaint.setAntiAlias(true);
	}

	//////////////////////////////////////////////////////////////////////////////////////
	// PUBLIC METHODS
	//////////////////////////////////////////////////////////////////////////////////////

	public List<AbstractPoint> getPoints() {
		Collections.sort(mPoints);
		return mPoints;
	}

	public void setPoints(List<? extends AbstractPoint> points) {
		mPoints = new ArrayList<AbstractPoint>();
		mPoints.addAll(points);

		sortPoints();
		resetRange();

		for (AbstractPoint point : mPoints) {
			extendRange(point.getX(), point.getY());
		}
	}

	public void addPoint(AbstractPoint point) {
		if (mPoints == null) {
			mPoints = new ArrayList<AbstractPoint>();
		}

		extendRange(point.getX(), point.getY());
		mPoints.add(point);

		mPointsSorted = false;
	}

	// Line properties

	public void setLineColor(int color) {
		mPaint.setColor(color);
	}

	public void setLineWidth(float width) {
		mPaint.setStrokeWidth(width);
	}

	//////////////////////////////////////////////////////////////////////////////////////
	// PRIVATE METHODS
	//////////////////////////////////////////////////////////////////////////////////////

	private void resetRange() {
		mMinX = Double.MAX_VALUE;
		mMaxX = -Double.MAX_VALUE;
		mMinY = Double.MAX_VALUE;
		mMaxY = -Double.MAX_VALUE;

		mRangeX = 0;
		mRangeY = 0;
	}

	private void extendRange(double x, double y) {
		if (x < mMinX) {
			mMinX = x;
		}

		if (x > mMaxX) {
			mMaxX = x;
		}

		if (y < mMinY) {
			mMinY = y;
		}

		if (y > mMaxY) {
			mMaxY = y;
		}

		mRangeX = mMaxX - mMinX;
		mRangeY = mMaxY - mMinY;
	}

	double getMinX() {
		return mMinX;
	}

	double getMaxX() {
		return mMaxX;
	}

	double getMinY() {
		return mMinY;
	}

	double getMaxY() {
		return mMaxY;
	}

	double getRangeX() {
		return mRangeX;
	}

	double getRangeY() {
		return mRangeY;
	}

	protected void draw(Canvas canvas, Rect viewBounds, RectD viewport) {
		sortPoints();

        onDrawingStarted(canvas);

		canvas.save();
		canvas.clipRect(viewBounds);

		for (AbstractPoint point : mPoints) {
			drawPoint(canvas, point, viewBounds, viewport);
		}

		canvas.restore();

		onDrawingComplete(canvas);
	}

	protected void sortPoints() {
		if (!mPointsSorted) {
			Collections.sort(mPoints);
			mPointsSorted = true;
		}
	}

	protected void onDrawingStarted(Canvas canvas) {
	}

	protected void onDrawingComplete(Canvas canvas) {
	}

	//////////////////////////////////////////////////////////////////////////////////////
	// PUBLIC CLASSES
	//////////////////////////////////////////////////////////////////////////////////////

	public static abstract class AbstractPoint implements Comparable<AbstractPoint> {
		private double mX;
		private double mY;

		public AbstractPoint() {
		}

		public AbstractPoint(double x, double y) {
			mX = x;
			mY = y;
		}

		public double getX() {
			return mX;
		}

		public double getY() {
			return mY;
		}

		public void set(double x, double y) {
			mX = x;
			mY = y;
		}

		@Override
		public int compareTo(AbstractPoint another) {
			return Double.compare(mX, another.mX);
		}
	}
}
