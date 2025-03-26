package org.archery.simulation.view;

import java.awt.BasicStroke;
import java.awt.Color;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.archery.simulation.model.Archer;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

/**
 * The `GraphUtils` class contains utilities for generating graphs related to archers
 * in the archery simulation. It uses the JFreeChart library to create and customize
 * XY charts that display the total scores of each archer per match.
 */
public class GraphUtils {

	private static final int NUMBER_MATCH = 100; // Total number of matches to simulate

	/**
	 * Creates a dataset that represents the accumulated scores of each archer
	 * across matches. The dataset will be used to plot the evolution of scores.
	 *
	 * @param archers The list of archers with their respective scores.
	 * @return An XYDataset object that contains the scoring data for each archer by match.
	 */
	public static XYDataset createDataset(List<Archer> archers) {
		XYSeriesCollection dataset = new XYSeriesCollection();

		// Map that associates each archer (by their ID) with a data series in the graph
		Map<Integer, XYSeries> archerSeriesMap = IntStream.range(1, 11)
				.boxed()
				.collect(Collectors.toMap(id -> id, id -> new XYSeries("Arquero " + id)));

		// Adds the scores of each archer per match to the dataset
		for (int i = 0; i < NUMBER_MATCH; i++) {
			Archer archer = archers.get(i);
			int archerId = archer.getArcherId();
			if (archerSeriesMap.containsKey(archerId)) {
				archerSeriesMap.get(archerId).add(i + 1, archer.getTotalScore());
			}
		}

		// Adds all the archer series to the dataset
		archerSeriesMap.values().forEach(dataset::addSeries);

		return dataset;
	}

	/**
	 * Creates an XYLineChart based on the provided dataset.
	 * The chart shows the total score of each archer in each match.
	 *
	 * @param dataset The dataset containing the information to be plotted.
	 * @return A JFreeChart object that represents the created chart.
	 */
	public static JFreeChart createChart(XYDataset dataset) {
		JFreeChart chart = ChartFactory.createXYLineChart(
				"Puntuación total del arquero por partido",  // Chart title
				"Match",                          // X-axis label
				"Total de puntos por partido",         // Y-axis label
				dataset,                          // Dataset
				PlotOrientation.VERTICAL,         // Chart orientation
				true,                             // Show legend
				true,                             // Generate tooltips
				false                             // Do not generate URLs
		);

		// Customize the chart before returning it
		customizeChart(chart);
		return chart;
	}

	/**
	 * Customizes the style of the chart, including colors and strokes of the lines,
	 * as well as the background and grid lines.
	 *
	 * @param chart The chart to be customized.
	 */
	private static void customizeChart(JFreeChart chart) {
		XYPlot plot = chart.getXYPlot();
		XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();

		// Define custom colors for each data series (archer)
		Color[] colors = {
				new Color(255, 0, 0),      // Red
				new Color(0, 255, 255),    // Cyan
				new Color(0, 255, 0),      // Green
				new Color(255, 165, 0),    // Orange
				new Color(0, 0, 255),      // Blue
				new Color(255, 0, 255),    // Magenta
				new Color(255, 255, 0),    // Yellow
				new Color(169, 169, 169),  // Dark Gray
				new Color(105, 105, 105),  // Dim Gray
				new Color(128, 0, 128)     // Purple
		};

		// Apply colors and strokes to the data series
		for (int i = 0; i < colors.length; i++) {
			renderer.setSeriesPaint(i, colors[i]);
			renderer.setSeriesStroke(i, new BasicStroke(1.0f));
		}

		// Customize other properties of the chart
		renderer.setBaseShapesFilled(false); // Do not fill shapes
		renderer.setDrawOutlines(false);      // Do not draw outlines around shapes

		plot.setRenderer(renderer);
		plot.setBackgroundPaint(Color.WHITE);        // White background
		plot.setRangeGridlinesVisible(true);         // Show grid lines
		plot.setRangeGridlinePaint(Color.BLACK);     // Color of Y-axis grid lines
		plot.setDomainGridlinesVisible(true);        // Show vertical grid lines
		plot.setDomainGridlinePaint(Color.BLACK);    // Color of X-axis grid lines
	}
}
