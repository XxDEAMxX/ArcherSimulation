package org.archery.simulation.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;

import org.archery.simulation.model.Archer;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYDataset;

public class MainView extends JFrame {

    private final Font CUSTOM_FONT = new Font("Georgia", Font.BOLD, 14);
    private final Color BACKGROUND_COLOR = new Color(34, 40, 49);
    private final Color TEXT_COLOR = new Color(238, 238, 238);
    private final Color TEXT_COLOR_TAB = new Color(0, 0, 160);
    private final Color BUTTON_COLOR = new Color(50, 173, 181);
    private final Color TABLE_BACKGROUND = new Color(57, 62, 70);
    private JPanel statsPanel;
    private JPanel graphPanel;
    private JLabel scoreLabel;
    private JLabel scoreLabel2;
    private JLabel genderWinnerLabel;

    public MainView(String luckiestArchersStats, String mostExperienceArcherStats,
                    String scoreWinningTeamStats, String winningGenderStats,
                    String genderGameWinnerStats, List<Archer> archers, String points) {
        super("Simulación de tiro con arco");
        setExtendedState(MAXIMIZED_BOTH);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(true);
        setLookAndFeel();
        initComponents(luckiestArchersStats, mostExperienceArcherStats,
                scoreWinningTeamStats, winningGenderStats,
                genderGameWinnerStats, archers, points);
        setVisible(true);
        showStatsPanel(luckiestArchersStats);
    }

    private void setLookAndFeel() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initComponents(String luckiestArchersStats, String mostExperienceArcherStats,
                                String scoreWinningTeamStats, String winningGenderStats,
                                String genderGameWinnerStats, List<Archer> archers, String points) {
        JPanel mainPanel = new JPanel(new BorderLayout());
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JPanel contentPanel = new JPanel(new GridLayout(1, 2));

        statsPanel = new JPanel(new BorderLayout());
		
        graphPanel = createGraphPanel(archers);

        JPanel fixedStatsPanel = new JPanel(new GridLayout(2, 1));
        fixedStatsPanel.setBackground(BACKGROUND_COLOR);

        scoreLabel = new JLabel("Equipo Ganador: " + scoreWinningTeamStats);
        scoreLabel2 = new JLabel("Puntuación: " + points);
        scoreLabel.setForeground(TEXT_COLOR);
        scoreLabel.setFont(CUSTOM_FONT);
        scoreLabel2.setForeground(TEXT_COLOR);
        scoreLabel2.setFont(CUSTOM_FONT);
        
        genderWinnerLabel = new JLabel("Ganador del juego de género: " + genderGameWinnerStats);
        genderWinnerLabel.setForeground(TEXT_COLOR);
        genderWinnerLabel.setFont(CUSTOM_FONT);

        fixedStatsPanel.add(scoreLabel);
        fixedStatsPanel.add(scoreLabel2);
        fixedStatsPanel.add(genderWinnerLabel);
        
        JButton luckiestButton = createStyledButton("Arqueros con más suerte");
        JButton experienceButton = createStyledButton("Arquero con más experiencia");
        JButton genderWinButton = createStyledButton("Género ganador por partido");

        luckiestButton.addActionListener(e -> showStatsPanel(luckiestArchersStats));
        experienceButton.addActionListener(e -> showStatsPanel(mostExperienceArcherStats));
        genderWinButton.addActionListener(e -> showStatsPanel(winningGenderStats));

        buttonPanel.add(luckiestButton);
        buttonPanel.add(experienceButton);
        buttonPanel.add(genderWinButton);

        statsPanel.add(fixedStatsPanel, BorderLayout.NORTH);

        contentPanel.add(statsPanel);
        contentPanel.add(graphPanel);

        mainPanel.add(buttonPanel, BorderLayout.NORTH);
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        
        setContentPane(mainPanel);
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(BUTTON_COLOR);
        button.setForeground(TEXT_COLOR_TAB);
        button.setFont(CUSTOM_FONT);
        button.setFocusPainted(false);
        return button;
    }

    private void showStatsPanel(String statisticText) {
        statsPanel.removeAll();
        JPanel fixedStatsPanel = new JPanel(new GridLayout(3, 1));
        fixedStatsPanel.setBackground(BACKGROUND_COLOR);
        fixedStatsPanel.add(scoreLabel);
		fixedStatsPanel.add(scoreLabel2);
        fixedStatsPanel.add(genderWinnerLabel);
        statsPanel.add(fixedStatsPanel, BorderLayout.NORTH);
        
        JScrollPane scrollPane = new JScrollPane(createTablePanel(statisticText));
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        statsPanel.add(scrollPane, BorderLayout.CENTER);
        statsPanel.revalidate();
        statsPanel.repaint();
    }

    private JPanel createTablePanel(String statisticText) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BACKGROUND_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        String[] columnNames = {"Datos"};
        String[][] data = statisticText.lines().map(line -> new String[]{line}).toArray(String[][]::new);
        
        JTable table = new JTable(new DefaultTableModel(data, columnNames));
        table.setFont(new Font("Verdana", Font.PLAIN, 12));
        table.setBackground(TABLE_BACKGROUND);
        table.setForeground(TEXT_COLOR);
        table.setRowHeight(25);
        
        JScrollPane tableScrollPane = new JScrollPane(table);
        tableScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        panel.add(tableScrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createGraphPanel(List<Archer> archers) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BACKGROUND_COLOR);

        XYDataset dataset = createDataset(archers);
        JFreeChart chart = createChart(dataset);

        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(600, 600));
        panel.add(chartPanel, BorderLayout.CENTER);

        return panel;
    }

    private XYDataset createDataset(List<Archer> archers) {
        return GraphUtils.createDataset(archers);
    }

    private JFreeChart createChart(XYDataset dataset) {
        return GraphUtils.createChart(dataset);
    }
}