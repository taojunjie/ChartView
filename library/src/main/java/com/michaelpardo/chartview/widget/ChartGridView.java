package com.michaelpardo.chartview.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.util.AttributeSet;

import com.michaelpardo.chartview.R;

public class ChartGridView extends ChartChildView {
	private Paint mLinePaint;
	private Paint mBorderPaint;

	public ChartGridView(Context context) {
		this(context, null, 0);
	}

	public ChartGridView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public ChartGridView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

		setWillNotDraw(false);

		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ChartGridView);

		int lineColor = a.getColor(R.styleable.ChartGridView_lineColor, Color.LTGRAY);
		int borderColor = a.getColor(R.styleable.ChartGridView_borderColor, Color.LTGRAY);
		float lineWidth = a.getDimension(R.styleable.ChartGridView_lineWidth, 1);
		float borderWidth = a.getDimension(R.styleable.ChartGridView_lineWidth, 1);

		a.recycle();

		mLinePaint = new Paint();
		mLinePaint.setAntiAlias(true);
		mLinePaint.setColor(lineColor);
		mLinePaint.setStrokeWidth(lineWidth);
		mLinePaint.setStyle(Style.STROKE);

		mBorderPaint = new Paint();
		mBorderPaint.setAntiAlias(true);
		mBorderPaint.setColor(borderColor);
		mBorderPaint.setStrokeWidth(borderWidth);
		mBorderPaint.setStyle(Style.STROKE);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		if (!mInitialized) {
			return;
		}

		final int width = getWidth();
		final int height = getHeight();

		for (float point : mHoriz.getTickPoints()) {
			canvas.drawLine(point, 0, point, height, mLinePaint);
		}

		for (float point : mVert.getTickPoints()) {
			canvas.drawLine(0, point, width, point, mLinePaint);
		}

		canvas.drawRect(0, 0, width, height, mBorderPaint);
	}
}
