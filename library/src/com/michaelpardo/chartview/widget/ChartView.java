package com.michaelpardo.chartview.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.michaelpardo.chartview.R;

public class ChartView extends RelativeLayout {
	//////////////////////////////////////////////////////////////////////////////////////
	// PUBLIC CONSTANTS
	//////////////////////////////////////////////////////////////////////////////////////

	public static final int POSITION_LEFT = 0;
	public static final int POSITION_TOP = 1;
	public static final int POSITION_RIGHT = 2;
	public static final int POSITION_BOTTOM = 3;

	//////////////////////////////////////////////////////////////////////////////////////
	// PRIVATE MEMBERS
	//////////////////////////////////////////////////////////////////////////////////////

	// Views

	private ChartCanvasView mChartSurfaceView;
	private LinearLayout mLeftLabelLayout;
	private LinearLayout mTopLabelLayout;
	private LinearLayout mBottomLabelLayout;
	private LinearLayout mRightLabelLayout;

	// Adapters

	private LabelAdapter mLeftLabelAdapter;
	private LabelAdapter mTopLabelAdapter;
	private LabelAdapter mBottomLabelAdapter;
	private LabelAdapter mRightLabelAdapter;

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

		setWillNotDraw(false);

		inflate(context, R.layout.widget_chart_view, this);

		mChartSurfaceView = (ChartCanvasView) findViewById(R.id.chart_surface_view);
		mLeftLabelLayout = (LinearLayout) findViewById(R.id.left_label_layout);
		mTopLabelLayout = (LinearLayout) findViewById(R.id.top_label_layout);
		mRightLabelLayout = (LinearLayout) findViewById(R.id.right_label_layout);
		mBottomLabelLayout = (LinearLayout) findViewById(R.id.bottom_label_layout);

		final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ChartView);
		setGridLineColor(a.getInt(R.styleable.ChartView_gridLineColor, Color.BLACK));
		setGridLineWidth(a.getDimensionPixelSize(R.styleable.ChartView_gridLineWidth, 1));
		setGridLinesHorizontal(a.getInt(R.styleable.ChartView_gridLinesHorizontal, 5));
		setGridLinesVertical(a.getInt(R.styleable.ChartView_gridLinesVertical, 5));

		a.recycle();
	}

	//////////////////////////////////////////////////////////////////////////////////////
	// PUBLIC METHODS
	//////////////////////////////////////////////////////////////////////////////////////

	public void clearSeries() {
		mChartSurfaceView.clearSeries();
		invalidate();
	}

	public void addSeries(AbstractSeries series) {
		mChartSurfaceView.addSeries(series);
		invalidate();
	}

	// Label adapters

	public void setLabelAdapter(LabelAdapter adapter, int position) {
		switch (position) {
		case POSITION_LEFT: {
			setLeftLabelAdapter(adapter);
			break;
		}
		case POSITION_TOP: {
			setTopLabelAdapter(adapter);
			break;
		}
		case POSITION_RIGHT: {
			setRightLabelAdapter(adapter);
			break;
		}
		case POSITION_BOTTOM: {
			setBottomLabelAdapter(adapter);
			break;
		}
		}

		invalidate();
	}

	// Grid properties

	public int getGridColor() {
		return mChartSurfaceView.getGridColor();
	}

	public float getGridLineWidth() {
		return mChartSurfaceView.getGridLineWidth();
	}

	public int getGridLinesHorizontal() {
		return mChartSurfaceView.getGridLinesHorizontal();
	}

	public int getGridLinesVertical() {
		return mChartSurfaceView.getGridLinesVertical();
	}

	public void setGridLineColor(int color) {
		mChartSurfaceView.setGridLineColor(color);
		invalidate();
	}

	public void setGridLineWidth(float width) {
		mChartSurfaceView.setGridLineWidth(width);
		invalidate();
	}

	public void setGridLinesHorizontal(int count) {
		mChartSurfaceView.setGridLinesHorizontal(count);
		invalidate();
	}

	public void setGridLinesVertical(int count) {
		mChartSurfaceView.setGridLinesVertical(count);
		invalidate();
	}

	// Series methods

	public double getMinX() {
		return mChartSurfaceView.getMinX();
	}

	public double getMaxX() {
		return mChartSurfaceView.getMaxX();
	}

	public double getMinY() {
		return mChartSurfaceView.getMinY();
	}

	public double getMaxY() {
		return mChartSurfaceView.getMaxY();
	}

	public void setMinX(double minX) {
		mChartSurfaceView.setMinX(minX);
		invalidate();
	}

	public void setMaxX(double maxX) {
		mChartSurfaceView.setMaxX(maxX);
		invalidate();
	}

	public void setMinY(double minY) {
		mChartSurfaceView.setMinY(minY);
		invalidate();
	}

	public void setMaxY(double maxY) {
		mChartSurfaceView.setMaxY(maxY);
		invalidate();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		drawLabels(canvas);
	}

	//////////////////////////////////////////////////////////////////////////////////////
	// PRIVATE METHODS
	//////////////////////////////////////////////////////////////////////////////////////

	private void setLeftLabelAdapter(LabelAdapter adapter) {
		//		mLeftLabelAdapter = adapter;
		mLeftLabelLayout.setVisibility(adapter == null ? View.GONE : View.VISIBLE);

		//		final double[] values = new double[mGridLinesVertical + 2];
		//		final double step = mValueBounds.height() / (mGridLinesVertical + 1);
		//		for (int i = 0; i < values.length; i++) {
		//			values[i] = mValueBounds.top + (step * i);
		//		}
		//
		//		mLeftLabelAdapter.setValues(values);
	}

	private void setTopLabelAdapter(LabelAdapter adapter) {
		//		mTopLabelAdapter = adapter;
		mTopLabelLayout.setVisibility(adapter == null ? View.GONE : View.VISIBLE);

		//		final double[] values = new double[mGridLinesHorizontal + 2];
		//		final double step = mValueBounds.width() / (mGridLinesHorizontal + 1);
		//		for (int i = 0; i < values.length; i++) {
		//			values[i] = mValueBounds.left + (step * i);
		//		}
		//
		//		mTopLabelAdapter.setValues(values);
	}

	private void setRightLabelAdapter(LabelAdapter adapter) {
		//		mRightLabelAdapter = adapter;
		mRightLabelLayout.setVisibility(adapter == null ? View.GONE : View.VISIBLE);

		//		final double[] values = new double[mGridLinesVertical + 2];
		//		final double step = mValueBounds.height() / (mGridLinesVertical + 1);
		//		for (int i = 0; i < values.length; i++) {
		//			values[i] = mValueBounds.top + (step * i);
		//		}
		//
		//		mRightLabelAdapter.setValues(values);
	}

	private void setBottomLabelAdapter(LabelAdapter adapter) {
		//		mBottomLabelAdapter = adapter;
		mBottomLabelLayout.setVisibility(adapter == null ? View.GONE : View.VISIBLE);

		//		final double[] values = new double[mGridLinesHorizontal + 2];
		//		final double step = mValueBounds.width() / (mGridLinesHorizontal + 1);
		//		for (int i = 0; i < values.length; i++) {
		//			values[i] = mValueBounds.left + (step * i);
		//		}
		//
		//		mBottomLabelAdapter.setValues(values);
	}

	// Drawing methods

	private void drawLabels(Canvas canvas) {
		if (mLeftLabelAdapter != null) {
			drawLeftLabels(canvas);
		}
		if (mTopLabelAdapter != null) {
			drawTopLabels(canvas);
		}
		if (mRightLabelAdapter != null) {
			drawRightLabels(canvas);
		}
		if (mBottomLabelAdapter != null) {
			drawBottomLabels(canvas);
		}
	}

	// Label drawing routines

	private void drawLeftLabels(Canvas canvas) {
		// Add views from adapter
		final int labelCount = mLeftLabelAdapter.getCount();
		for (int i = 0; i < labelCount; i++) {
			View view = mLeftLabelLayout.getChildAt(i);

			if (view == null) {
				LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, 0);
				if (i == 0 || i == labelCount - 1) {
					params.weight = 0.5f;
				}
				else {
					params.weight = 1;
				}

				view = mLeftLabelAdapter.getView((labelCount - 1) - i, view, mLeftLabelLayout);
				view.setLayoutParams(params);

				mLeftLabelLayout.addView(view);
			}
			else {
				mLeftLabelAdapter.getView((labelCount - 1) - i, view, mLeftLabelLayout);
			}
		}

		// Remove extra views
		final int childCount = mLeftLabelLayout.getChildCount();
		for (int i = labelCount; i < childCount; i++) {
			mLeftLabelLayout.removeViewAt(i);
		}
	}

	private void drawTopLabels(final Canvas canvas) {
		// Add views from adapter
		final int labelCount = mTopLabelAdapter.getCount();
		for (int i = 0; i < labelCount; i++) {
			View view = mTopLabelLayout.getChildAt(i);

			if (view == null) {
				LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LayoutParams.MATCH_PARENT);
				if (i == 0 || i == labelCount - 1) {
					params.weight = 0.5f;
				}
				else {
					params.weight = 1;
				}

				view = mTopLabelAdapter.getView(i, view, mTopLabelLayout);
				view.setLayoutParams(params);

				mTopLabelLayout.addView(view);
			}
			else {
				mTopLabelAdapter.getView(i, view, mTopLabelLayout);
			}
		}

		// Remove extra views
		final int childCount = mTopLabelLayout.getChildCount();
		for (int i = labelCount; i < childCount; i++) {
			mTopLabelLayout.removeViewAt(i);
		}
	}

	private void drawRightLabels(Canvas canvas) {
		// Add views from adapter
		final int labelCount = mRightLabelAdapter.getCount();
		for (int i = 0; i < labelCount; i++) {
			View view = mRightLabelLayout.getChildAt(i);

			if (view == null) {
				LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, 0);
				if (i == 0 || i == labelCount - 1) {
					params.weight = 0.5f;
				}
				else {
					params.weight = 1;
				}

				view = mRightLabelAdapter.getView((labelCount - 1) - i, view, mRightLabelLayout);
				view.setLayoutParams(params);

				mRightLabelLayout.addView(view);
			}
			else {
				mRightLabelAdapter.getView((labelCount - 1) - i, view, mRightLabelLayout);
			}
		}

		// Remove extra views
		final int childCount = mRightLabelLayout.getChildCount();
		for (int i = labelCount; i < childCount; i++) {
			mRightLabelLayout.removeViewAt(i);
		}
	}

	private void drawBottomLabels(final Canvas canvas) {
		// Add views from adapter
		final int labelCount = mBottomLabelAdapter.getCount();
		for (int i = 0; i < labelCount; i++) {
			View view = mBottomLabelLayout.getChildAt(i);

			if (view == null) {
				LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LayoutParams.MATCH_PARENT);
				if (i == 0 || i == labelCount - 1) {
					params.weight = 0.5f;
				}
				else {
					params.weight = 1;
				}

				view = mBottomLabelAdapter.getView(i, view, mBottomLabelLayout);
				view.setLayoutParams(params);

				mBottomLabelLayout.addView(view);
			}
			else {
				mBottomLabelAdapter.getView(i, view, mBottomLabelLayout);
			}
		}

		// Remove extra views
		final int childCount = mBottomLabelLayout.getChildCount();
		for (int i = labelCount; i < childCount; i++) {
			mBottomLabelLayout.removeViewAt(i);
		}
	}
}