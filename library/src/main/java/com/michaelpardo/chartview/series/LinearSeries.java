package com.michaelpardo.chartview.series;

import android.graphics.Path;
import android.graphics.Rect;

import com.michaelpardo.chartview.axis.ChartAxis;

public class LinearSeries extends AbstractSeries {
	@Override
	public void onGeneratePath(ChartAxis horiz, ChartAxis vert, Rect viewBounds, Path strokePath, Path fillPath) {
		final int size = mPoints.size();
		int location = 0;

		for (AbstractPoint point : mPoints) {
			final float x = horiz.convertToPoint(point.getX());
			final float y = vert.convertToPoint(point.getY());

			if (location == 0) {
				strokePath.moveTo(x, y);
				fillPath.moveTo(x, y);
			}
			else if (location == size - 1) {
				fillPath.lineTo(x, viewBounds.bottom);
				fillPath.lineTo(0, viewBounds.bottom);
			}
			else {
				strokePath.lineTo(x, y);
				fillPath.lineTo(x, y);
			}

			location++;
		}
	}

	public static class LinearPoint extends AbstractPoint {
		public LinearPoint() {
			super();
		}

		public LinearPoint(float x, float y) {
			super(x, y);
		}
	}
}
