package com.michaelpardo.chartview.sample;

import android.app.Activity;
import android.os.Bundle;

import com.michaelpardo.chartview.sample.widget.ValueLabelAdapter;
import com.michaelpardo.chartview.sample.widget.ValueLabelAdapter.Orientation;
import com.michaelpardo.chartview.widget.ChartView;
import com.michaelpardo.chartview.widget.LinearSeries;
import com.michaelpardo.chartview.widget.LinearSeries.LinearPoint;

public class MainActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// Find the chart view
		ChartView chartView = (ChartView) findViewById(R.id.chart_view);

		// Create the data points
		LinearSeries series = new LinearSeries();
		series.setLineColor(0xFF0099CC);
		series.setLineWidth(2);

		for (double i = -(20d * Math.PI); i <= (20d * Math.PI); i += 0.1d) {
			series.addPoint(new LinearPoint(i, Math.sin(i)));
		}

		// Add chart view data
		chartView.setGridStepX((float) (Math.PI / 4));
		chartView.setGridStepY((float) (Math.PI / 4));

		chartView.addSeries(series);
		chartView.setLabelAdapter(new ValueLabelAdapter(this, Orientation.VERTICAL), ChartView.POSITION_LEFT);
		chartView.setLabelAdapter(new ValueLabelAdapter(this, Orientation.HORIZONTAL), ChartView.POSITION_BOTTOM);
	}
}