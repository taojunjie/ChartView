package com.michaelpardo.chartview.widget;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;

import com.michaelpardo.chartview.axis.ChartAxis;
import com.michaelpardo.chartview.axis.ValueAxis;

public class ChartView extends FrameLayout {
	//////////////////////////////////////////////////////////////////////////////////////
	// PRIVATE MEMBERS
	//////////////////////////////////////////////////////////////////////////////////////

	private ChartAxis mHorizontalAxis;
	private ChartAxis mVerticalAxis;

	private Rect mContent = new Rect();

	//////////////////////////////////////////////////////////////////////////////////////
	// CONSTRUCTORS
	//////////////////////////////////////////////////////////////////////////////////////

	public ChartView(Context context) {
		this(context, null, 0);
	}

	public ChartView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public ChartView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

		init(new ValueAxis(), new ValueAxis());

		setClipToPadding(false);
		setClipChildren(false);
	}

	//////////////////////////////////////////////////////////////////////////////////////
	// OVERRIDES
	//////////////////////////////////////////////////////////////////////////////////////


	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		mContent.set(getPaddingLeft(), getPaddingTop(), r - l - getPaddingRight(), b - t - getPaddingBottom());

		final int width = mContent.width();
		final int height = mContent.height();

		if (mHorizontalAxis != null && mVerticalAxis != null) {
			mHorizontalAxis.setSize(width);
			mVerticalAxis.setSize(height);
		}

		final Rect parentRect = new Rect();
		final Rect childRect = new Rect();

		for (int i = 0; i < getChildCount(); i++) {
			final View child = getChildAt(i);
			final LayoutParams params = (LayoutParams) child.getLayoutParams();

			parentRect.set(mContent);

			if (child instanceof ChartChildView) {
				Gravity.apply(params.gravity, width, height, parentRect, childRect);
				child.layout(childRect.left, childRect.top, childRect.right, childRect.bottom);
			}
		}
	}

	//////////////////////////////////////////////////////////////////////////////////////
	// PUBLIC METHODS
	//////////////////////////////////////////////////////////////////////////////////////

	public void init(ChartAxis horizontalAxis, ChartAxis verticalAxis) {
		mHorizontalAxis = horizontalAxis;
		mVerticalAxis = verticalAxis;

		mHorizontalAxis.setSize(mContent.width());
		mVerticalAxis.setSize(mContent.height());

		for (int i = 0; i < getChildCount(); i++) {
			final View child = getChildAt(i);
			if (child instanceof ChartChildView) {
				((ChartChildView) child).init(mHorizontalAxis, mVerticalAxis);
			}
		}
	}
}
