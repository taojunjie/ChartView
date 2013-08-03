package com.michaelpardo.chartview.axis;

import android.content.res.Resources;
import android.text.SpannableStringBuilder;

public interface ChartAxis {
	public boolean setBounds(double min, double max);

	public boolean setSize(float size);

	public float convertToPoint(double value);

	public double convertToValue(float point);

	public double buildLabel(Resources res, SpannableStringBuilder builder, double value);

	public float[] getTickPoints();
}
