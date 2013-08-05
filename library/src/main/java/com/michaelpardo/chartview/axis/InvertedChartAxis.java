/*
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.michaelpardo.chartview.axis;

import android.content.res.Resources;
import android.text.SpannableStringBuilder;

/**
 * Utility to invert another {@link ChartAxis}.
 */
public class InvertedChartAxis implements ChartAxis {
    private final ChartAxis mWrapped;
    private float mSize;

    public InvertedChartAxis(ChartAxis wrapped) {
        mWrapped = wrapped;
    }

    @Override
    public boolean setBounds(float min, float max) {
        return mWrapped.setBounds(min, max);
    }

    @Override
    public boolean setSize(float size) {
        mSize = size;
        return mWrapped.setSize(size);
    }

    @Override
    public float convertToPoint(float value) {
        return mSize - mWrapped.convertToPoint(value);
    }

    @Override
    public float convertToValue(float point) {
        return mWrapped.convertToValue(mSize - point);
    }

    @Override
    public float buildLabel(Resources res, SpannableStringBuilder builder, float value) {
        return mWrapped.buildLabel(res, builder, value);
    }

    @Override
    public float[] getTickPoints() {
        final float[] points = mWrapped.getTickPoints();
        for (int i = 0; i < points.length; i++) {
            points[i] = mSize - points[i];
        }
        return points;
    }
}
