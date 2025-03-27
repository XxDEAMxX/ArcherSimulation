package org.archery.simulation.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.UIManager;

import org.archery.simulation.model.Archer;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYDataset;

/**
 * MainView is a graphical user interface (GUI) class that represents the main window
 * of the Archery Game Simulation application. It extends JFrame to create a window
 * for displaying various statistics and a graphical representation of the archers' scores.
 */
public class MainView extends JFrame {

	private JPanel contentPane; // Main panel for the GUI components
	private final Font CUSTOM_FONT = new Font("Georgia", Font.BOLD, 14); // Custom font for labels
	private final Color BACKGROUND_COLOR = new Color(60, 63, 65); // Dark background color
	private final Color TEXT_COLOR = Color.WHITE; // Text color for labels and text areas

	/**
	 * Constructs a MainView object and initializes the GUI components.
	 *
	 * @param luckiestArchersStats      Statistics about the luckiest archers
	 * @param mostExperienceArcherStats  Statistics about the most experienced archer
	 * @param scoreWinningTeamStats      Statistics about the winning team's score
	 * @param winningGenderStats         Statistics about the winning gender
	 * @param genderGameWinnerStats      Statistics about the gender of the game winner
	 * @param archers                    List of archers participating in the simulation
	 */
	public MainView(String luckiestArchersStats, String mostExperienceArcherStats,
					String scoreWinningTeamStats, String winningGenderStats,
					String genderGameWinnerStats, List<Archer> archers) {
		super("Simulación de tiro con arco"); // Set the title of the windowxv

		setExtendedState(MAXIMIZED_BOTH);// Set the size of the window
		setDefaultCloseOperation(EXIT_ON_CLOSE); // Close the application on window close
		setLocationRelativeTo(null); // Center the window on the screen
		setResizable(true); // Allow window resizing
		setLookAndFeel(); // Set the look and feel of the GUI
		initComponents(luckiestArchersStats, mostExperienceArcherStats,
				scoreWinningTeamStats, winningGenderStats,
				genderGameWinnerStats, archers); // Initialize components
		setVisible(true); // Make the window visible
	}

	/**
	 * Sets the look and feel of the GUI to the system's default.
	 */
	private void setLookAndFeel() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); // Set system look and feel
		} catch (Exception e) {
			e.printStackTrace(); // Print stack trace if an exception occurs
		}
	}

	/**
	 * Initializes the GUI components and adds them to the main content pane.
	 *
	 * @param luckiestArchersStats      Statistics about the luckiest archers
	 * @param mostExperienceArcherStats  Statistics about the most experienced archer
	 * @param scoreWinningTeamStats      Statistics about the winning team's score
	 * @param winningGenderStats         Statistics about the winning gender
	 * @param genderGameWinnerStats      Statistics about the gender of the game winner
	 * @param archers                    List of archers participating in the simulation
	 */
	private void initComponents(String luckiestArchersStats, String mostExperienceArcherStats,
								String scoreWinningTeamStats, String winningGenderStats,
								String genderGameWinnerStats, List<Archer> archers) {

		JTabbedPane tabbedPane = new JTabbedPane(); // Create a tabbed pane for organizing panels

		// Create and add statistics panel
		JPanel statsPanel = createStatsPanel(luckiestArchersStats, mostExperienceArcherStats,
				scoreWinningTeamStats, winningGenderStats,
				genderGameWinnerStats);
		tabbedPane.addTab("Statistics Game Simulation", statsPanel); // Add tab for statistics

		// Create and add graph panel
		JPanel plotPanel = createGraphPanel(archers);
		tabbedPane.addTab("Graph Total Archer Score Per March", plotPanel); // Add tab for graph

		setContentPane(tabbedPane); // Set the tabbed pane as the content pane
	}

	/**
	 * Creates a panel to display statistics about the archers and their performance.
	 *
	 * @param luckiestArchersStats      Statistics about the luckiest archers
	 * @param mostExperienceArcherStats  Statistics about the most experienced archer
	 * @param scoreWinningTeamStats      Statistics about the winning team's score
	 * @param winningGenderStats         Statistics about the winning gender
	 * @param genderGameWinnerStats      Statistics about the gender of the game winner
	 * @return JPanel containing the statistics
	 */
	private JPanel createStatsPanel(String luckiestArchersStats, String mostExperienceArcherStats,
									String scoreWinningTeamStats, String winningGenderStats,
									String genderGameWinnerStats) {
		contentPane = new JPanel();
		contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS)); // Set vertical layout
		contentPane.setBackground(BACKGROUND_COLOR); // Set background color
		contentPane.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Set margins

		// Add statistics vertically
		addStatisticLabel("Arqueros con más suerte", luckiestArchersStats);
		addStatisticLabel("Arquero con más experiencia por partido", mostExperienceArcherStats);

		// Panel for "Score and gender of the winning team"
		JPanel winnerGenderPerMarchPanel = new JPanel();
		winnerGenderPerMarchPanel.setLayout(new BorderLayout()); // Set horizontal layout
		winnerGenderPerMarchPanel.setBackground(BACKGROUND_COLOR);
		addStatisticLabelInRow(winnerGenderPerMarchPanel, "Puntuación del equipo ganador  ", scoreWinningTeamStats);
		contentPane.add(winnerGenderPerMarchPanel); // Add panel to content pane

		addStatisticLabel("Género ganador por partido", winningGenderStats); // Add winning gender label

		// Panel for "Winning gender per match"
		JPanel winningGenderPanel = new JPanel();
		winningGenderPanel.setLayout(new BorderLayout()); // Set horizontal layout
		winningGenderPanel.setBackground(BACKGROUND_COLOR);
		addStatisticLabelInRow(winningGenderPanel, "Ganador del juego de género  ", genderGameWinnerStats);
		contentPane.add(winningGenderPanel); // Add panel to content pane

		return contentPane; // Return the populated statistics panel
	}

	/**
	 * Adds a statistic label and text area in a horizontal layout.
	 *
	 * @param panel          The panel to add components to
	 * @param labelText     The label text to display
	 * @param statisticText The statistic text to display
	 */
	private void addStatisticLabelInRow(JPanel panel, String labelText, String statisticText) {
		// Label for the left
		JLabel label = new JLabel(labelText);
		label.setFont(CUSTOM_FONT);
		label.setForeground(TEXT_COLOR);
		panel.add(label, BorderLayout.WEST); // Add label to the left

		// JTextArea for the value on the right
		JTextArea textArea = new JTextArea(statisticText);
		textArea.setEditable(false); // Make the text area non-editable
		textArea.setWrapStyleWord(true); // Enable word wrapping
		textArea.setLineWrap(true); // Enable line wrapping
		textArea.setFont(new Font("Georgia", Font.PLAIN, 12));
		textArea.setBackground(new Color(60, 63, 65)); // Set dark background for text area
		textArea.setForeground(TEXT_COLOR);
		JScrollPane scrollPane = new JScrollPane(textArea); // Create scroll pane for the text area
		panel.add(scrollPane, BorderLayout.CENTER); // Add scroll pane to the center
	}

	/**
	 * Adds a statistic label and text area in a vertical layout.
	 *
	 * @param labelText     The label text to display
	 * @param statisticText The statistic text to display
	 */
	private void addStatisticLabel(String labelText, String statisticText) {
		JLabel label = new JLabel(labelText);
		label.setFont(CUSTOM_FONT);
		label.setForeground(TEXT_COLOR);
		label.setAlignmentX(Component.CENTER_ALIGNMENT);
		contentPane.add(label); // Add label to content pane

		JTextArea textArea = new JTextArea(statisticText);
		textArea.setEditable(false); // Make the text area non-editable
		textArea.setWrapStyleWord(true); // Enable word wrapping
		textArea.setLineWrap(true); // Enable line wrapping
		textArea.setFont(new Font("Verdana", Font.PLAIN, 12));
		textArea.setBackground(new Color(60, 63, 65)); // Set dark background for text area
		textArea.setForeground(TEXT_COLOR);
		JScrollPane scrollPane = new JScrollPane(textArea); // Create scroll pane for the text area
		contentPane.add(scrollPane); // Add scroll pane to content pane

		contentPane.add(Box.createRigidArea(new Dimension(0, 15))); // Add space between sections
	}

	/**
	 * Creates a panel to display a graph of total archer scores.
	 *
	 * @param archers List of archers participating in the simulation
	 * @return JPanel containing the graph
	 */
	private JPanel createGraphPanel(List<Archer> archers) {
		// Create a panel for the graph
		JPanel graphPanel = new JPanel();
		graphPanel.setLayout(new BorderLayout()); // Set layout
		graphPanel.setBackground(BACKGROUND_COLOR); // Set background color

		// Create dataset for the graph
		XYDataset dataset = createDataset(archers); // Create dataset based on archers
		JFreeChart chart = createChart(dataset); // Create chart based on dataset

		ChartPanel chartPanel = new ChartPanel(chart); // Create chart panel
		chartPanel.setPreferredSize(new Dimension(500, 500)); // Set preferred size for chart panel
		graphPanel.add(chartPanel, BorderLayout.CENTER); // Add chart panel to graph panel

		return graphPanel; // Return the populated graph panel
	}

	/**
	 * Creates a dataset for the graph based on the provided list of archers.
	 *
	 * @param archers List of archers participating in the simulation
	 * @return XYDataset representing the scores of the archers
	 */
	private XYDataset createDataset(List<Archer> archers) {

		return GraphUtils.createDataset(archers);
	}

	/**
	 * Creates a chart based on the provided dataset.
	 *
	 * @param dataset The dataset to use for creating the chart
	 * @return JFreeChart representing the graph
	 */
	private JFreeChart createChart(XYDataset dataset) {

		return GraphUtils.createChart(dataset);
	}

}
