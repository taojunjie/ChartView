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
import com.michaelpardo.chartview.graphics.RectD;

public class ChartView extends RelativeLayout implements ChartCanvasView.ChartCanvasViewListener {
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

    // Listeners

    private ChartCanvasView.ChartCanvasViewListener mChartCanvasViewListener;

    // Views

    private ChartCanvasView mChartCanvasView;
    private LinearLayout mLeftLabelLayout;
    private LinearLayout mTopLabelLayout;
    private LinearLayout mBottomLabelLayout;
    private LinearLayout mRightLabelLayout;

    // Adapters

    private LabelAdapter mLeftLabelAdapter;
    private LabelAdapter mTopLabelAdapter;
    private LabelAdapter mBottomLabelAdapter;
    private LabelAdapter mRightLabelAdapter;

    // Grid lines

    private double mGridStepX;
    private double mGridStepY;

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

        mChartCanvasView = (ChartCanvasView) findViewById(R.id.chart_surface_view);
        mLeftLabelLayout = (LinearLayout) findViewById(R.id.left_label_layout);
        mTopLabelLayout = (LinearLayout) findViewById(R.id.top_label_layout);
        mRightLabelLayout = (LinearLayout) findViewById(R.id.right_label_layout);
        mBottomLabelLayout = (LinearLayout) findViewById(R.id.bottom_label_layout);

        final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ChartView);

        setDrawBorder(a.getBoolean(R.styleable.ChartView_drawBorder, true));
        setGridLineColor(a.getInt(R.styleable.ChartView_gridLineColor, Color.BLACK));
        setGridLineWidth(a.getDimensionPixelSize(R.styleable.ChartView_gridLineWidth, 1));
        setGridStepX(a.getFloat(R.styleable.ChartView_gridStepX, -1));
        setGridStepY(a.getFloat(R.styleable.ChartView_gridStepY, -1));

        if (a.hasValue(R.styleable.ChartView_minX)) {
            setMinX(a.getFloat(R.styleable.ChartView_minX, -Float.MAX_VALUE));
        }
        if (a.hasValue(R.styleable.ChartView_minY)) {
            setMinY(a.getFloat(R.styleable.ChartView_minY, -Float.MAX_VALUE));
        }
        if (a.hasValue(R.styleable.ChartView_maxX)) {
            setMaxX(a.getFloat(R.styleable.ChartView_maxX, -Float.MAX_VALUE));
        }
        if (a.hasValue(R.styleable.ChartView_maxY)) {
            setMaxY(a.getFloat(R.styleable.ChartView_maxY, -Float.MAX_VALUE));
        }

        setZoom(a.getFloat(R.styleable.ChartView_zoom, ChartCanvasView.DEFAULT_ZOOM));
        setMinZoom(a.getFloat(R.styleable.ChartView_minZoom, ChartCanvasView.DEFAULT_MIN_ZOOM));
        setMaxZoom(a.getFloat(R.styleable.ChartView_maxZoom, ChartCanvasView.DEFAULT_MAX_ZOOM));

        a.recycle();

        mChartCanvasView.setChartCanvasViewListener(this);
    }

    //////////////////////////////////////////////////////////////////////////////////////
    // OVERRIDES
    //////////////////////////////////////////////////////////////////////////////////////

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawLabels(canvas);
    }

    //////////////////////////////////////////////////////////////////////////////////////
    // PUBLIC METHODS
    //////////////////////////////////////////////////////////////////////////////////////

    // Listeners

    public ChartCanvasView.ChartCanvasViewListener getChartCanvasViewListener() {
        return mChartCanvasViewListener;
    }

    public void setChartCanvasViewListener(ChartCanvasView.ChartCanvasViewListener listener) {
        mChartCanvasViewListener = listener;
    }

    // Series

    public void clearSeries() {
        mChartCanvasView.clearSeries();
        invalidate();
    }

    public void addSeries(AbstractSeries series) {
        mChartCanvasView.addSeries(series);
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

    public boolean getDrawBorder() {
        return mChartCanvasView.getDrawBorder();
    }

    public int getGridColor() {
        return mChartCanvasView.getGridColor();
    }

    public float getGridLineWidth() {
        return mChartCanvasView.getGridLineWidth();
    }

    public double getGridStepX() {
        return mChartCanvasView.getGridStepX();
    }

    public double getGridStepY() {
        return mChartCanvasView.getGridStepY();
    }

    public void setDrawBorder(boolean drawBorder) {
        mChartCanvasView.setDrawBorder(drawBorder);
    }

    public void setGridLineColor(int color) {
        mChartCanvasView.setGridLineColor(color);
        invalidate();
    }

    public void setGridLineWidth(float width) {
        mChartCanvasView.setGridLineWidth(width);
        invalidate();
    }

    public void setGridStepX(double step) {
        mGridStepX = step;
        mChartCanvasView.setGridStepX(step);
        invalidate();
    }

    public void setGridStepY(double step) {
        mGridStepY = step;
        mChartCanvasView.setGridStepY(step);
        invalidate();
    }

    // Chart methods

    public double getMinX() {
        return mChartCanvasView.getMinX();
    }

    public double getMaxX() {
        return mChartCanvasView.getMaxX();
    }

    public double getMinY() {
        return mChartCanvasView.getMinY();
    }

    public double getMaxY() {
        return mChartCanvasView.getMaxY();
    }

    public float getZoom() {
        return mChartCanvasView.getZoom();
    }

    public float getMinZoom() {
        return mChartCanvasView.getMinZoom();
    }

    public float getMaxZoom() {
        return mChartCanvasView.getMaxZoom();
    }

    public boolean getStretchToFit() {
        return mChartCanvasView.getStretchToFit();
    }

    public void setMinX(double minX) {
        mChartCanvasView.setMinX(minX);
        invalidate();
    }

    public void setMaxX(double maxX) {
        mChartCanvasView.setMaxX(maxX);
        invalidate();
    }

    public void setMinY(double minY) {
        mChartCanvasView.setMinY(minY);
        invalidate();
    }

    public void setMaxY(double maxY) {
        mChartCanvasView.setMaxY(maxY);
        invalidate();
    }

    public void setZoom(float zoom) {
        mChartCanvasView.setZoom(zoom);
    }

    public void setMinZoom(float minZoom) {
        mChartCanvasView.setMinZoom(minZoom);
    }

    public void setMaxZoom(float maxZoom) {
        mChartCanvasView.setMaxZoom(maxZoom);
    }

    public void setStretchToFit(boolean stretchToFit) {
        mChartCanvasView.setStretchToFit(stretchToFit);
    }

    //////////////////////////////////////////////////////////////////////////////////////
    // PRIVATE METHODS
    //////////////////////////////////////////////////////////////////////////////////////

    private void setLeftLabelAdapter(LabelAdapter adapter) {
        mLeftLabelAdapter = adapter;
        mLeftLabelLayout.setVisibility(adapter == null ? View.GONE : View.VISIBLE);
    }

    private void setTopLabelAdapter(LabelAdapter adapter) {
//        mTopLabelAdapter = adapter;
//        mTopLabelLayout.setVisibility(adapter == null ? View.GONE : View.VISIBLE);
    }

    private void setRightLabelAdapter(LabelAdapter adapter) {
//        mRightLabelAdapter = adapter;
//        mRightLabelLayout.setVisibility(adapter == null ? View.GONE : View.VISIBLE);
    }

    private void setBottomLabelAdapter(LabelAdapter adapter) {
//        mBottomLabelAdapter = adapter;
//        mBottomLabelLayout.setVisibility(adapter == null ? View.GONE : View.VISIBLE);
    }

    // Drawing methods

    private void drawLabels(Canvas canvas) {
        if (mLeftLabelAdapter != null && mLeftLabelLayout.getVisibility() == View.VISIBLE) {
            drawLeftLabels(canvas);
        }
        if (mTopLabelAdapter != null && mTopLabelLayout.getVisibility() == View.VISIBLE) {
            drawTopLabels(canvas);
        }
        if (mRightLabelAdapter != null && mRightLabelLayout.getVisibility() == View.VISIBLE) {
            drawRightLabels(canvas);
        }
        if (mBottomLabelAdapter != null && mBottomLabelLayout.getVisibility() == View.VISIBLE) {
            drawBottomLabels(canvas);
        }
    }

    // Label drawing routines

    private void updateLabelValues(RectD viewport) {
        // Vertical values
        final int stepsY = (int) (viewport.height() / mGridStepY);
        final double[] valuesY = new double[stepsY];
        final int startStepY = (int) (viewport.bottom / mGridStepY);
        for (int i = 0; i < stepsY; i++) {
            valuesY[i] = (startStepY + i) * mGridStepY;
        }

        if (mLeftLabelAdapter != null) {
            mLeftLabelAdapter.setValues(valuesY);
        }
        if (mRightLabelAdapter != null) {
            mRightLabelAdapter.setValues(valuesY);
        }

        // Horizontal values
        final int stepsX = (int) (viewport.height() / mGridStepX);
        final double[] valuesX = new double[stepsX];
        final int startStepX = (int) (viewport.left / mGridStepX);
        for (int i = 0; i < stepsX; i++) {
            valuesX[i] = (startStepX + i) * mGridStepX;
        }

        if (mTopLabelAdapter != null) {
            mTopLabelAdapter.setValues(valuesX);
        }
        if (mBottomLabelAdapter != null) {
            mBottomLabelAdapter.setValues(valuesX);
        }
    }

    private void drawLeftLabels(Canvas canvas) {
        mLeftLabelLayout.scrollTo(0, (int) mChartCanvasView.getViewportOffsetY());

        if (mLeftLabelAdapter.getDatasetChanged()) {
            return;
        }

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

        mLeftLabelAdapter.clearDatasetChanged();
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


    //////////////////////////////////////////////////////////////////////////////////////
    // INTERFACE IMPLEMENTATIONS
    //////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void onViewportChanged(RectD viewport) {
        //updateLabelValues(viewport);

        if (mChartCanvasViewListener != null) {
            mChartCanvasViewListener.onViewportChanged(viewport);
        }
    }
}