package com.michaelpardo.chartview.axis;

import android.content.res.Resources;
import android.text.SpannableStringBuilder;

public interface ChartAxis {
	public boolean setBounds(float min, float max);

	public boolean setSize(float size);

	public float convertToPoint(float value);

	public float convertToValue(float point);

	public float buildLabel(Resources res, SpannableStringBuilder builder, float value);

	public float[] getTickPoints();
}
