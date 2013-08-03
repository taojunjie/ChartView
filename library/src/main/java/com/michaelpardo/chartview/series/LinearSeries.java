package com.michaelpardo.chartview.series;

import android.graphics.Path;
import android.graphics.Rect;

import com.michaelpardo.chartview.axis.ChartAxis;

public class LinearSeries extends AbstractSeries {
	@Override
	public void onGeneratePath(ChartAxis horiz, ChartAxis vert, Rect viewBounds, Path strokePath, Path fillPath) {
		final int size = mPoints.size();
		for (int i = 0; i < size; i++) {
			AbstractPoint point = mPoints.get(i);
			final float x = horiz.convertToPoint(point.getX());
			final float y = vert.convertToPoint(point.getY());

			if (i == 0) {
				strokePath.moveTo(x, y);
				fillPath.moveTo(x, y);
				continue;
			}

			strokePath.lineTo(x, y);
			fillPath.lineTo(x, y);

			if (i == size - 1) {
				fillPath.lineTo(x, viewBounds.bottom);
				fillPath.lineTo(0, viewBounds.bottom);
			}
		}
	}

	public static class LinearPoint extends AbstractPoint {
		public LinearPoint() {
			super();
		}

		public LinearPoint(double x, double y) {
			super(x, y);
		}
	}
}
