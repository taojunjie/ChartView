package com.michaelpardo.chartview.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Rect;
import android.util.AttributeSet;

import com.michaelpardo.chartview.R;
import com.michaelpardo.chartview.series.AbstractSeries;

public class ChartSeriesView extends ChartChildView {
	//////////////////////////////////////////////////////////////////////////////////////
	// PRIVATE MEMBERS
	//////////////////////////////////////////////////////////////////////////////////////

	private AbstractSeries mSeries;

	private Paint mStrokePaint;
	private Paint mFillPaint;

	private Path mStrokePath;
	private Path mFillPath;

	private boolean mPathValid = false;

	//////////////////////////////////////////////////////////////////////////////////////
	// CONSTRUCTORS
	//////////////////////////////////////////////////////////////////////////////////////

	public ChartSeriesView(Context context) {
		this(context, null, 0);
	}

	public ChartSeriesView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public ChartSeriesView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ChartSeriesView);

		int strokeColor = a.getColor(R.styleable.ChartSeriesView_strokeColor, Color.BLACK);
		int fillColor = a.getColor(R.styleable.ChartSeriesView_fillColor, Color.DKGRAY);
		float strokeWidth = a.getDimension(R.styleable.ChartSeriesView_strokeWidth, 1);

		a.recycle();

		mStrokePaint = new Paint();
		mStrokePaint.setAntiAlias(true);
		mStrokePaint.setColor(strokeColor);
		mStrokePaint.setStrokeWidth(strokeWidth);
		mStrokePaint.setStyle(Style.STROKE);

		mFillPaint = new Paint();
		mFillPaint.setAntiAlias(true);
		mFillPaint.setColor(fillColor);
		mFillPaint.setStyle(Style.FILL);

		mStrokePath = new Path();
		mFillPath = new Path();
	}

	//////////////////////////////////////////////////////////////////////////////////////
	// OVERRIDES
	//////////////////////////////////////////////////////////////////////////////////////

	public void invalidatePath() {
		mPathValid = false;
		invalidate();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if (!mInitialized || mSeries == null) {
			return;
		}

		if (!mPathValid) {
			generatePath();
		}

		int save = canvas.save();
		canvas.clipRect(0, 0, getWidth(), getHeight());
		canvas.drawPath(mFillPath, mFillPaint);
		canvas.drawPath(mStrokePath, mStrokePaint);
		canvas.restoreToCount(save);
	}

	//////////////////////////////////////////////////////////////////////////////////////
	// PRIVATE METHODS
	//////////////////////////////////////////////////////////////////////////////////////

	private void generatePath() {
		mStrokePath.reset();
		mFillPath.reset();

		final Rect viewBounds = new Rect(0, 0, getWidth(), getHeight());
		mSeries.generatePath(mHoriz, mVert, viewBounds, mStrokePath, mFillPath);
	}

	//////////////////////////////////////////////////////////////////////////////////////
	// PUBLIC METHODS
	//////////////////////////////////////////////////////////////////////////////////////

	public void bindSeries(AbstractSeries series) {
		mSeries = series;
		invalidatePath();
	}
}
