package com.michaelpardo.chartview.widget;

import android.widget.BaseAdapter;

import java.util.Arrays;

public abstract class LabelAdapter extends BaseAdapter {
    private double[] mValues = new double[0];
    private boolean mDatasetChanged = false;

    void setValues(double[] values) {
        mDatasetChanged = !Arrays.equals(values, mValues);
        mValues = values;
    }

    boolean getDatasetChanged() {
        return mDatasetChanged;
    }

    void clearDatasetChanged() {
        mDatasetChanged = false;
    }

    @Override
    public int getCount() {
        return mValues.length;
    }

    public Double getItem(int position) {
        return mValues[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}