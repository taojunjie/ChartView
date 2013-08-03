package com.michaelpardo.chartview.series;

import android.graphics.Path;
import android.graphics.Rect;

import com.michaelpardo.chartview.axis.ChartAxis;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class AbstractSeries {
	//////////////////////////////////////////////////////////////////////////////////////
	// PRIVATE MEMBERS
	//////////////////////////////////////////////////////////////////////////////////////

	protected List<AbstractPoint> mPoints;

	private boolean mPointsSorted = false;

	private double mMinX = Double.MAX_VALUE;
	private double mMaxX = -Double.MAX_VALUE;
	private double mMinY = Double.MAX_VALUE;
	private double mMaxY = -Double.MAX_VALUE;

	private double mRangeX = 0;
	private double mRangeY = 0;

	protected abstract void onGeneratePath(ChartAxis horiz, ChartAxis vert, Rect viewBounds, Path strokePath, Path fillPath);

	//////////////////////////////////////////////////////////////////////////////////////
	// CONSTRUCTORS
	//////////////////////////////////////////////////////////////////////////////////////

	public AbstractSeries() {
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

	public void generatePath(ChartAxis horiz, ChartAxis vert, Rect viewBounds, Path strokePath, Path fillPath) {
		sortPoints();
		onGeneratePath(horiz, vert, viewBounds, strokePath, fillPath);
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

	public double getMinX() {
		return mMinX;
	}

	public double getMaxX() {
		return mMaxX;
	}

	public double getMinY() {
		return mMinY;
	}

	public double getMaxY() {
		return mMaxY;
	}

	public double getRangeX() {
		return mRangeX;
	}

	public double getRangeY() {
		return mRangeY;
	}

	protected void sortPoints() {
		if (!mPointsSorted) {
			Collections.sort(mPoints);
			mPointsSorted = true;
		}
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
