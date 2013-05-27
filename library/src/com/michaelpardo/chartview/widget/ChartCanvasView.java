package com.michaelpardo.chartview.widget;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.ScaleGestureDetector.SimpleOnScaleGestureListener;
import android.view.View;

import com.michaelpardo.chartview.graphics.RectD;

public class ChartCanvasView extends View {
	//////////////////////////////////////////////////////////////////////////////////////
	// PUBLIC INTERFACES
	//////////////////////////////////////////////////////////////////////////////////////

	public interface ChartCanvasViewListener {
		public void onViewportChanged(RectD viewport);
	}

	//////////////////////////////////////////////////////////////////////////////////////
	// PUBLIC CONSTANTS
	//////////////////////////////////////////////////////////////////////////////////////

	public static final float DEFAULT_ZOOM = 1;
	public static final float DEFAULT_MIN_ZOOM = 0.1f;
	public static final float DEFAULT_MAX_ZOOM = 5f;

	//////////////////////////////////////////////////////////////////////////////////////
	// PRIVATE CONSTANTS
	//////////////////////////////////////////////////////////////////////////////////////

	private static final int INVALID_POINTER_ID = -1;

	//////////////////////////////////////////////////////////////////////////////////////
	// PRIVATE MEMBERS
	//////////////////////////////////////////////////////////////////////////////////////

	// Listeners

	private ChartCanvasViewListener mChartCanvasViewListener;

	// Series

	private List<AbstractSeries> mSeries = new ArrayList<AbstractSeries>();

	private Paint mGridPaint = new Paint();

	private Rect mViewBounds = new Rect();

	private boolean mDrawBorder = true;
	private double mGridStepX;
	private double mGridStepY;

	// Value range
	private RectD mValueBounds = new RectD(Double.MAX_VALUE, Double.MAX_VALUE, -Double.MAX_VALUE, -Double.MAX_VALUE);
	// User range
	private RectD mUserBounds = new RectD(-Double.MAX_VALUE, -Double.MAX_VALUE, Double.MAX_VALUE, Double.MAX_VALUE);

	// Viewport
	private RectD mViewport = new RectD(-Double.MAX_VALUE, -Double.MAX_VALUE, Double.MAX_VALUE, Double.MAX_VALUE);
	private float mViewportOffsetX = 0;
	private float mViewportOffsetY = 0;

	// Zoom
	private float mZoom = DEFAULT_ZOOM;
	private float mMinZoom = DEFAULT_MIN_ZOOM;
	private float mMaxZoom = DEFAULT_MAX_ZOOM;

	// Touch
	private int mActivePointerId = INVALID_POINTER_ID;
	private float mLastTouchX;
	private float mLastTouchY;

	private ScaleGestureDetector mScaleGestureDetector;

	//////////////////////////////////////////////////////////////////////////////////////
	// CONSTRUCTORS
	//////////////////////////////////////////////////////////////////////////////////////

	public ChartCanvasView(Context context) {
		super(context);
	}

	public ChartCanvasView(Context context, AttributeSet attrs) {
		super(context, attrs);

		setWillNotDraw(false);

		mScaleGestureDetector = new ScaleGestureDetector(context, new ScaleListener());
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

		//drawGrid(canvas);

		for (AbstractSeries series : mSeries) {
			series.draw(canvas, mViewBounds, mViewport);
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		mScaleGestureDetector.onTouchEvent(event);

		final int action = event.getAction();
		switch (action & MotionEvent.ACTION_MASK) {
		case MotionEvent.ACTION_DOWN: {
			final float x = event.getX();
			final float y = event.getY();

			mLastTouchX = x;
			mLastTouchY = y;
			mActivePointerId = event.getPointerId(0);

			break;
		}
		case MotionEvent.ACTION_MOVE: {
			final int pointerIndex = event.findPointerIndex(mActivePointerId);
			final float x = event.getX(pointerIndex);
			final float y = event.getY(pointerIndex);

			if (!mScaleGestureDetector.isInProgress()) {
				final float dx = x - mLastTouchX;
				final float dy = y - mLastTouchY;

				mViewportOffsetX += dx;
				mViewportOffsetY += dy;

				updateViewport();
				invalidate();
			}

			mLastTouchX = x;
			mLastTouchY = y;

			break;
		}
		case MotionEvent.ACTION_UP: {
			mActivePointerId = INVALID_POINTER_ID;
			break;
		}
		case MotionEvent.ACTION_CANCEL: {
			mActivePointerId = INVALID_POINTER_ID;
			break;
		}
		case MotionEvent.ACTION_POINTER_UP: {
			final int pointerIndex = (action & MotionEvent.ACTION_POINTER_INDEX_MASK) >> MotionEvent.ACTION_POINTER_INDEX_SHIFT;
			final int pointerId = event.getPointerId(pointerIndex);
			if (pointerId == mActivePointerId) {
				final int newPointerIndex = pointerIndex == 0 ? 1 : 0;
				mLastTouchX = event.getX(newPointerIndex);
				mLastTouchY = event.getY(newPointerIndex);
				mActivePointerId = event.getPointerId(newPointerIndex);
			}
			break;
		}
		}

		return true;
	}

	//////////////////////////////////////////////////////////////////////////////////////
	// PUBLIC METHODS
	//////////////////////////////////////////////////////////////////////////////////////

	// Listeners

	public ChartCanvasViewListener getChartCanvasViewListener() {
		return mChartCanvasViewListener;
	}

	public void setChartCanvasViewListener(ChartCanvasViewListener listener) {
		mChartCanvasViewListener = listener;
	}

	// Grid properties

	public boolean getDrawBorder() {
		return mDrawBorder;
	}

	public int getGridColor() {
		return mGridPaint.getColor();
	}

	public float getGridLineWidth() {
		return mGridPaint.getStrokeWidth();
	}

	public double getGridStepX() {
		return mGridStepX;
	}

	public double getGridStepY() {
		return mGridStepY;
	}

	public void setDrawBorder(boolean drawBorder) {
		mDrawBorder = drawBorder;
		invalidate();
	}

	public void setGridLineColor(int color) {
		mGridPaint.setColor(color);
		invalidate();
	}

	public void setGridLineWidth(float width) {
		mGridPaint.setStrokeWidth(width);
		invalidate();
	}

	public void setGridStepX(double step) {
		mGridStepX = step;
		invalidate();
	}

	public void setGridStepY(double step) {
		mGridStepY = step;
		invalidate();
	}

	// Series methods

	public double getMinX() {
		return mUserBounds.left;
	}

	public double getMaxX() {
		return mUserBounds.right;
	}

	public double getMinY() {
		return mUserBounds.top;
	}

	public double getMaxY() {
		return mUserBounds.bottom;
	}

	public float getZoom() {
		return mZoom;
	}

	public float getMinZoom() {
		return mMinZoom;
	}

	public float getMaxZoom() {
		return mMaxZoom;
	}

	public void setMinX(double minX) {
		mUserBounds.left = minX;
		updateViewport();
		invalidate();
	}

	public void setMaxX(double maxX) {
		mUserBounds.right = maxX;
		updateViewport();
		invalidate();
	}

	public void setMinY(double minY) {
		mUserBounds.top = minY;
		updateViewport();
		invalidate();
	}

	public void setMaxY(double maxY) {
		mUserBounds.bottom = maxY;
		updateViewport();
		invalidate();
	}

	public void setZoom(float zoom) {
		mZoom = zoom;
	}

	public void setMinZoom(float minZoom) {
		mMinZoom = minZoom;
	}

	public void setMaxZoom(float maxZoom) {
		mMaxZoom = maxZoom;
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

		updateViewport();
		invalidate();
	}

	//////////////////////////////////////////////////////////////////////////////////////
	// PRIVATE METHODS
	//////////////////////////////////////////////////////////////////////////////////////

	private void extendRange(double x, double y) {
		mValueBounds.left = Math.min(x, mValueBounds.left);
		mValueBounds.top = Math.min(y, mValueBounds.top);
		mValueBounds.right = Math.max(x, mValueBounds.right);
		mValueBounds.bottom = Math.max(y, mValueBounds.bottom);
	}

	private void resetRange() {
		mValueBounds.set(Double.MAX_VALUE, Double.MAX_VALUE, -Double.MAX_VALUE, -Double.MAX_VALUE);
		updateViewport();
	}

	private void updateViewport() {
		mViewport.set(-Double.MAX_VALUE, -Double.MAX_VALUE, Double.MAX_VALUE, Double.MAX_VALUE);

		mViewport.left = Math.max(mUserBounds.left, Math.max(mValueBounds.left, mViewport.left));
		mViewport.top = Math.max(mUserBounds.top, Math.max(mValueBounds.top, mViewport.top));
		mViewport.right = Math.min(mUserBounds.right, Math.min(mValueBounds.right, mViewport.right));
		mViewport.bottom = Math.min(mUserBounds.bottom, Math.min(mValueBounds.bottom, mViewport.bottom));

		mViewport.offset(mViewportOffsetX, mViewportOffsetY);

		double adjustWidth = 0;
		double adjustHeight = 0;

		// Zoom amount
		adjustWidth += ((mViewport.width() / mZoom) - mViewport.width()) / 2;
		adjustHeight += ((mViewport.height() / mZoom) - mViewport.height()) / 2;

		mViewport.left -= adjustWidth;
		mViewport.top -= adjustHeight;
		mViewport.right += adjustWidth;
		mViewport.bottom += adjustHeight;

		if (mChartCanvasViewListener != null) {
			mChartCanvasViewListener.onViewportChanged(mViewport);
		}
	}

	// Drawing

	private void drawGrid(Canvas canvas) {
		final float stepX = (float) (mViewBounds.width() / mValueBounds.width() / mGridStepX);
		final float stepY = (float) (mViewBounds.width() / mValueBounds.height() / mGridStepY);

		final float left = mViewBounds.left;
		final float top = mViewBounds.top;
		final float bottom = mViewBounds.bottom;
		final float right = mViewBounds.right;

		// draw border
		if (mDrawBorder) {
			canvas.drawLine(left, top, left, bottom, mGridPaint);
			canvas.drawLine(left, top, right, top, mGridPaint);
			canvas.drawLine(right, top, right, bottom, mGridPaint);
			canvas.drawLine(left, bottom, right, bottom, mGridPaint);
		}

		// draw grid
		for (int i = 0; i < mGridStepX + 2; i++) {
			canvas.drawLine(left + (stepX * i), top, left + (stepX * i), bottom, mGridPaint);
		}

		for (int i = 0; i < mGridStepY + 2; i++) {
			canvas.drawLine(left, top + (stepY * i), right, top + (stepY * i), mGridPaint);
		}
	}

	//////////////////////////////////////////////////////////////////////////////////////
	// PACKAGE METHODS
	//////////////////////////////////////////////////////////////////////////////////////

	float getViewportOffsetX() {
		return mViewportOffsetX;
	}

	float getViewportOffsetY() {
		return mViewportOffsetY;
	}

	//////////////////////////////////////////////////////////////////////////////////////
	// LISTENERS
	//////////////////////////////////////////////////////////////////////////////////////

	private class ScaleListener extends SimpleOnScaleGestureListener {
		@Override
		public boolean onScale(ScaleGestureDetector detector) {
			mZoom *= detector.getScaleFactor();
			mZoom = Math.max(mMinZoom, Math.min(mZoom, mMaxZoom));

			updateViewport();
			invalidate();

			return true;
		}
	}
}
