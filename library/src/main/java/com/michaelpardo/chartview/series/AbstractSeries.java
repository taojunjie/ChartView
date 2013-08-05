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

	private float mMinX = Float.MAX_VALUE;
	private float mMaxX = -Float.MAX_VALUE;
	private float mMinY = Float.MAX_VALUE;
	private float mMaxY = -Float.MAX_VALUE;

	private float mRangeX = 0;
	private float mRangeY = 0;

	protected abstract void onGeneratePath(ChartAxis horiz, ChartAxis vert, Rect viewBounds, Path strokePath,
			Path fillPath);

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
		mMinX = Float.MAX_VALUE;
		mMaxX = -Float.MAX_VALUE;
		mMinY = Float.MAX_VALUE;
		mMaxY = -Float.MAX_VALUE;

		mRangeX = 0;
		mRangeY = 0;
	}

	private void extendRange(float x, float y) {
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

	public float getMinX() {
		return mMinX;
	}

	public float getMaxX() {
		return mMaxX;
	}

	public float getMinY() {
		return mMinY;
	}

	public float getMaxY() {
		return mMaxY;
	}

	public float getRangeX() {
		return mRangeX;
	}

	public float getRangeY() {
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
		private float mX;
		private float mY;

		public AbstractPoint() {
		}

		public AbstractPoint(float x, float y) {
			mX = x;
			mY = y;
		}

		public float getX() {
			return mX;
		}

		public float getY() {
			return mY;
		}

		public void set(float x, float y) {
			mX = x;
			mY = y;
		}

		@Override
		public int compareTo(AbstractPoint another) {
			return Float.compare(mX, another.mX);
		}
	}
}
