package com.michaelpardo.chartview.sample;

import android.app.Activity;
import android.os.Bundle;

import com.michaelpardo.chartview.sample.widget.ValueLabelAdapter;
import com.michaelpardo.chartview.sample.widget.ValueLabelAdapter.LabelOrientation;
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

		for (double i = 0d; i <= (2d * Math.PI); i += 0.1d) {
			series.addPoint(new LinearPoint(i, Math.sin(i)));
		}

		// Add chart view data
		chartView.addSeries(series);
		chartView.setLeftLabelAdapter(new ValueLabelAdapter(this, LabelOrientation.VERTICAL));
		chartView.setBottomLabelAdapter(new ValueLabelAdapter(this, LabelOrientation.HORIZONTAL));
	}
}