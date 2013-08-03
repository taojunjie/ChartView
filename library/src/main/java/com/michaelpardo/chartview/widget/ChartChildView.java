package com.michaelpardo.chartview.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import com.michaelpardo.chartview.axis.ChartAxis;

public abstract class ChartChildView extends View {
	protected ChartAxis mHoriz;
	protected ChartAxis mVert;

	protected boolean mInitialized = false;

	public ChartChildView(Context context) {
		super(context);
	}

	public ChartChildView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public ChartChildView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public final void init(ChartAxis horizontalAxis, ChartAxis verticalAxis) {
		mHoriz = horizontalAxis;
		mVert = verticalAxis;
		mInitialized = true;
	}
}
