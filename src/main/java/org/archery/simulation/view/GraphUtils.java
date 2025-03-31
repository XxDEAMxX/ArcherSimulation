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

// La clase `GraphUtils` contiene utilidades para generar gráficos relacionados con arqueros
// en la simulación de tiro con arco. Utiliza la biblioteca JFreeChart para crear y personalizar
// gráficos XY que muestran las puntuaciones totales de cada arquero por partido.
public class GraphUtils {

	private static final int NUMBER_MATCH = 100; // Número total de partidos a simular

	// Crea un conjunto de datos que representa las puntuaciones acumuladas de cada arquero
	// a lo largo de los partidos. El conjunto de datos se usará para graficar la evolución de las puntuaciones.
	public static XYDataset createDataset(List<Archer> archers) {
		XYSeriesCollection dataset = new XYSeriesCollection();

		// Mapa que asocia cada arquero (por su ID) con una serie de datos en el gráfico
		Map<Integer, XYSeries> archerSeriesMap = IntStream.range(1, 11)
				.boxed()
				.collect(Collectors.toMap(id -> id, id -> new XYSeries("Arquero " + id)));

		// Agrega las puntuaciones de cada arquero por partido al conjunto de datos
		for (int i = 0; i < NUMBER_MATCH; i++) {
			Archer archer = archers.get(i);
			int archerId = archer.getArcherId();
			if (archerSeriesMap.containsKey(archerId)) {
				archerSeriesMap.get(archerId).add(i + 1, archer.getTotalScore());
			}
		}

		// Agrega todas las series de arqueros al conjunto de datos
		archerSeriesMap.values().forEach(dataset::addSeries);

		return dataset;
	}

	// Crea un gráfico XYLineChart basado en el conjunto de datos proporcionado.
	// El gráfico muestra la puntuación total de cada arquero en cada partido.
	public static JFreeChart createChart(XYDataset dataset) {
		JFreeChart chart = ChartFactory.createXYLineChart(
				"Puntuación total del arquero por partido",  // Título del gráfico
				"Partido",                          // Etiqueta del eje X
				"Total de puntos por partido",         // Etiqueta del eje Y
				dataset,                          // Conjunto de datos
				PlotOrientation.VERTICAL,         // Orientación del gráfico
				true,                             // Mostrar leyenda
				true,                             // Generar tooltips
				false                             // No generar URLs
		);

		// Personaliza el gráfico antes de devolverlo
		customizeChart(chart);
		return chart;
	}

	// Personaliza el estilo del gráfico, incluyendo colores y trazos de las líneas,
	// así como el fondo y las líneas de la cuadrícula.
	private static void customizeChart(JFreeChart chart) {
		XYPlot plot = chart.getXYPlot();
		XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();

		// Define colores personalizados para cada serie de datos (arquero)
		Color[] colors = {
				new Color(255, 0, 0),      // Rojo
				new Color(0, 255, 255),    // Cian
				new Color(0, 255, 0),      // Verde
				new Color(255, 165, 0),    // Naranja
				new Color(0, 0, 255),      // Azul
				new Color(255, 0, 255),    // Magenta
				new Color(255, 255, 0),    // Amarillo
				new Color(169, 169, 169),  // Gris oscuro
				new Color(105, 105, 105),  // Gris tenue
				new Color(128, 0, 128)     // Púrpura
		};

		// Aplica colores y trazos a las series de datos
		for (int i = 0; i < colors.length; i++) {
			renderer.setSeriesPaint(i, colors[i]);
			renderer.setSeriesStroke(i, new BasicStroke(1.0f));
		}

		// Personaliza otras propiedades del gráfico
		renderer.setBaseShapesFilled(false); // No rellenar formas
		renderer.setDrawOutlines(false);      // No dibujar contornos en las formas

		plot.setRenderer(renderer);
		plot.setBackgroundPaint(Color.WHITE);        // Fondo blanco
		plot.setRangeGridlinesVisible(true);         // Mostrar líneas de la cuadrícula
		plot.setRangeGridlinePaint(Color.BLACK);     // Color de las líneas del eje Y
		plot.setDomainGridlinesVisible(true);        // Mostrar líneas de la cuadrícula verticales
		plot.setDomainGridlinePaint(Color.BLACK);    // Color de las líneas del eje X
	}
}
