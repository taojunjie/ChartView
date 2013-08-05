package com.michaelpardo.chartview.axis;

import android.content.res.Resources;
import android.text.SpannableStringBuilder;

public class TimeAxis extends AbstractAxis {
	@Override
	public float buildLabel(Resources res, SpannableStringBuilder builder, float value) {
		return value;
	}
}
