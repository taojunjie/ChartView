package com.michaelpardo.chartview.widget;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import com.michaelpardo.chartview.graphics.RectD;

public class ChartCanvasView extends View {
	//////////////////////////////////////////////////////////////////////////////////////
	// PRIVATE MEMBERS
	//////////////////////////////////////////////////////////////////////////////////////

	// Series

	private List<AbstractSeries> mSeries = new ArrayList<AbstractSeries>();

	private Paint mGridPaint = new Paint();

	private Rect mViewBounds = new Rect();

	private int mGridLinesHorizontal;
	private int mGridLinesVertical;

	// Value range
	private RectD mValueBounds = new RectD();
	private double mMinX = Double.MAX_VALUE;
	private double mMinY = Double.MAX_VALUE;
	private double mMaxX = Double.MIN_VALUE;
	private double mMaxY = Double.MIN_VALUE;

	// Viewport range
	private RectD mViewportBounds = new RectD(Double.NaN, Double.NaN, Double.NaN, Double.NaN);

	//////////////////////////////////////////////////////////////////////////////////////
	// CONSTRUCTORS
	//////////////////////////////////////////////////////////////////////////////////////

	public ChartCanvasView(Context context) {
		super(context);
	}

	public ChartCanvasView(Context context, AttributeSet attrs) {
		super(context, attrs);
		setWillNotDraw(false);
	}

	//////////////////////////////////////////////////////////////////////////////////////
	// OVERRIDES
	//////////////////////////////////////////////////////////////////////////////////////

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		mViewBounds = new Rect(0, 0, w, h);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		drawGrid(canvas);

		for (AbstractSeries series : mSeries) {
			series.draw(canvas, mViewBounds, mViewportBounds, mValueBounds);
		}
	}

	//////////////////////////////////////////////////////////////////////////////////////
	// PUBLIC METHODS
	//////////////////////////////////////////////////////////////////////////////////////

	// Grid properties

	public int getGridColor() {
		return mGridPaint.getColor();
	}

	public float getGridLineWidth() {
		return mGridPaint.getStrokeWidth();
	}

	public int getGridLinesHorizontal() {
		return mGridLinesHorizontal;
	}

	public int getGridLinesVertical() {
		return mGridLinesVertical;
	}

	public void setGridLineColor(int color) {
		mGridPaint.setColor(color);
		invalidate();
	}

	public void setGridLineWidth(float width) {
		mGridPaint.setStrokeWidth(width);
		invalidate();
	}

	public void setGridLinesHorizontal(int count) {
		mGridLinesHorizontal = count;
		invalidate();
	}

	public void setGridLinesVertical(int count) {
		mGridLinesVertical = count;
		invalidate();
	}

	// Series methods

	public double getMinX() {
		return mViewportBounds.left;
	}

	public double getMaxX() {
		return mViewportBounds.right;
	}

	public double getMinY() {
		return mViewportBounds.top;
	}

	public double getMaxY() {
		return mViewportBounds.bottom;
	}

	public void setMinX(double minX) {
		mViewportBounds.left = minX;
		invalidate();
	}

	public void setMaxX(double maxX) {
		mViewportBounds.right = maxX;
		invalidate();
	}

	public void setMinY(double minY) {
		mViewportBounds.top = minY;
		invalidate();
	}

	public void setMaxY(double maxY) {
		mViewportBounds.bottom = maxY;
		invalidate();
	}

	public void clearSeries() {
		mSeries = new ArrayList<AbstractSeries>();
		resetRange();
		invalidate();
	}

	public void addSeries(AbstractSeries series) {
		if (mSeries == null) {
			mSeries = new ArrayList<AbstractSeries>();
		}

		extendRange(series.getMinX(), series.getMinY());
		extendRange(series.getMaxX(), series.getMaxY());

		mSeries.add(series);

		invalidate();
	}

	//////////////////////////////////////////////////////////////////////////////////////
	// PRIVATE METHODS
	//////////////////////////////////////////////////////////////////////////////////////

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

		mValueBounds.set(mMinX, mMinY, mMaxX, mMaxY);
	}

	private void resetRange() {
		mMinX = Double.MAX_VALUE;
		mMaxX = Double.MIN_VALUE;
		mMinY = Double.MAX_VALUE;
		mMaxY = Double.MIN_VALUE;

		mValueBounds.set(mMinX, mMinY, mMaxX, mMaxY);
	}

	// Drawing

	private void drawGrid(Canvas canvas) {
		final float stepX = mViewBounds.width() / (float) (mGridLinesHorizontal + 1);
		final float stepY = mViewBounds.height() / (float) (mGridLinesVertical + 1);

		final float left = mViewBounds.left;
		final float top = mViewBounds.top;
		final float bottom = mViewBounds.bottom;
		final float right = mViewBounds.right;

		for (int i = 0; i < mGridLinesHorizontal + 2; i++) {
			canvas.drawLine(left + (stepX * i), top, left + (stepX * i), bottom, mGridPaint);
		}

		for (int i = 0; i < mGridLinesVertical + 2; i++) {
			canvas.drawLine(left, top + (stepY * i), right, top + (stepY * i), mGridPaint);
		}
	}
}