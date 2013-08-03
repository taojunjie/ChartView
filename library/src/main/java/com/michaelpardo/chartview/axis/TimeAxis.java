package com.michaelpardo.chartview.axis;

import android.content.res.Resources;
import android.text.SpannableStringBuilder;

public class TimeAxis extends AbstractAxis {
	@Override
	public double buildLabel(Resources res, SpannableStringBuilder builder, double value) {
		return value;
	}
}
