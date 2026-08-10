package views;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import interface_adapter.wardrobe.WardrobeViewModel;
import interface_adapter.wardrobe_analyzer.WardrobeAnalyzerController;

/**
 * Represents the wardrobe view.
 */
public class WardrobeDetailsView extends AbstractView implements PropertyChangeListener {
    private static final int PERCENTAGE_MULTIPLIER = 100;

    private final WardrobeViewModel viewModel;
    private final WardrobeAnalyzerController analyzerController;

    private final JLabel totalItemsLabel;
    private final JLabel averageFondnessLabel;
    private final JLabel donationCandidatesLabel;
    private final JLabel oldestAgeLabel;
    private final JLabel newestAgeLabel;

    private final JPanel categoryBreakdownPanel;
    private final JPanel conditionBreakdownPanel;

    /**
     * Constructs a new wardrobe view.
     *
     * @param manager the application manager of the view
     */
    public WardrobeDetailsView(ApplicationManager manager) {
        super(manager);

        // Retrieve the shared resources.
        this.viewModel = manager.get(WardrobeViewModel.class);
        this.viewModel.addPropertyChangeListener(this);
        this.analyzerController = manager.get(WardrobeAnalyzerController.class);

        // Initialize the layout.
        setLayout(new BorderLayout(SIZE_SPACING_MD, SIZE_SPACING_MD));

        // Initialize Analyzer Labels
        this.totalItemsLabel = new JLabel("Total Items: --");
        this.averageFondnessLabel = new JLabel("Average Fondness: --");
        this.donationCandidatesLabel = new JLabel("Donation Candidates: --");
        this.oldestAgeLabel = new JLabel("Oldest Item: --");
        this.newestAgeLabel = new JLabel("Newest Item: --");

        // Initialize Category / Condition Breakdown Panels
        this.categoryBreakdownPanel = createDistributionPanel("Category Distribution");
        this.conditionBreakdownPanel = createDistributionPanel("Condition Distribution");

        add(createHeader(), BorderLayout.PAGE_START);
        add(createStatsGrid(), BorderLayout.CENTER);

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                analyzerController.analyzeWardrobe();
            }
        });
    }

    @Override
    public String getTitle() {
        return "Wardrobe Statisics Summary";
    }

    private JPanel createHeader() {
        final JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);

        final JLabel headerTitle = new JLabel("Wardrobe Details");
        headerTitle.setFont(FONT_TITLE);
        header.add(headerTitle, BorderLayout.LINE_START);

        final JPanel right = new JPanel(new FlowLayout(FlowLayout.TRAILING, SIZE_SPACING_SM, 0));
        right.setOpaque(false);
        right.setBorder(BorderFactory.createEmptyBorder(0, -SIZE_SPACING_SM, 0, -SIZE_SPACING_SM));
        header.add(right, BorderLayout.LINE_END);

        return header;
    }

    private JPanel createStatsGrid() {
        final JPanel statsGrid = new JPanel(new GridLayout(1, 2, SIZE_SPACING_MD, SIZE_SPACING_MD));
        statsGrid.setOpaque(false);

        final JPanel metricsCard = createCardPanel("Overview");
        metricsCard.setLayout(new BoxLayout(metricsCard, BoxLayout.Y_AXIS));
        metricsCard.add(totalItemsLabel);
        metricsCard.add(Box.createVerticalStrut(SIZE_SPACING_SM));
        metricsCard.add(averageFondnessLabel);
        metricsCard.add(Box.createVerticalStrut(SIZE_SPACING_SM));
        metricsCard.add(donationCandidatesLabel);
        metricsCard.add(Box.createVerticalStrut(SIZE_SPACING_SM));
        metricsCard.add(oldestAgeLabel);
        metricsCard.add(Box.createVerticalStrut(SIZE_SPACING_SM));
        metricsCard.add(newestAgeLabel);

        final JPanel distributionsCard = createCardPanel("Breakdowns");
        distributionsCard.setLayout(new GridLayout(2, 1, SIZE_SPACING_SM, SIZE_SPACING_SM));
        distributionsCard.add(categoryBreakdownPanel);
        distributionsCard.add(conditionBreakdownPanel);

        statsGrid.add(metricsCard);
        statsGrid.add(distributionsCard);
        return statsGrid;
    }

    private JPanel createCardPanel(String title) {
        final JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder(title),
            BorderFactory.createEmptyBorder(SIZE_SPACING_SM, SIZE_SPACING_SM, SIZE_SPACING_SM, SIZE_SPACING_SM)
        ));
        return panel;
    }

    @Override
    public void propertyChange(PropertyChangeEvent e) {
        switch (e.getPropertyName()) {
            case WardrobeViewModel.PROPERTY_ERROR:
                if (viewModel.getError() != null && isVisible()) {
                    JOptionPane.showMessageDialog(this, viewModel.getError());
                }
                break;
            case WardrobeViewModel.PROPERTY_ANALYZER_STATISTICS:
                buildAnalyzerUi(viewModel.getAnalyzerStatistics());
                break;
            default:
                break;
        }
    }

    private void buildAnalyzerUi(Map<String, Object> stat) {
        if (stat == null) {
            return;
        }

        final int totalItems = (int) stat.getOrDefault("totalItems", 0);
        final double meanFondness = (double) stat.getOrDefault("meanFondness", 0.0);
        final int donationCount = (int) stat.getOrDefault("donationCandidateCount", 0);
        final int oldestAge = (int) stat.getOrDefault("oldestItemAge", 0);
        final int newestAge = (int) stat.getOrDefault("newestItemAge", 0);

        totalItemsLabel.setText("Total Items: " + totalItems);
        averageFondnessLabel.setText(String.format("Average Fondness: %.0f%%", meanFondness * PERCENTAGE_MULTIPLIER));
        donationCandidatesLabel.setText("Donation Candidates (> 1 year; <0.5 fondness): " + donationCount);
        oldestAgeLabel.setText("Oldest Item Age: " + oldestAge + " months");
        newestAgeLabel.setText("Newest Item Age: " + newestAge + " months");

        final Map<String, Integer> categoryDist = (Map<String, Integer>)
            stat.getOrDefault("categoryCounts", Map.of());
        final Map<String, Integer> conditionDist = (Map<String, Integer>)
            stat.getOrDefault("conditionCounts", Map.of());

        populateDistributionPanel(categoryBreakdownPanel, categoryDist, totalItems);
        populateDistributionPanel(conditionBreakdownPanel, conditionDist, totalItems);
    }

    /**
     * Helper method for panel creation.
     *
     * @param title the title of this distribution panel
     * @return the created JPanel
     */
    private JPanel createDistributionPanel(String title) {
        final JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder(title),
            BorderFactory.createEmptyBorder(SIZE_SPACING_SM, SIZE_SPACING_SM, SIZE_SPACING_SM, SIZE_SPACING_SM)
        ));
        return panel;
    }

    /**
     * Populates the distribution of any Map<>.
     *
     * @param panel the JPanel to populate with distribution labels
     * @param distributionMap the map containing the items and their counts
     * @param totalItems the total number of items used to calculate percentages
     */
    private void populateDistributionPanel(JPanel panel, Map<String, Integer> distributionMap, int totalItems) {
        panel.removeAll();

        if (distributionMap != null) {
            for (Map.Entry<String, Integer> entry : distributionMap.entrySet()) {
                final String key = entry.getKey();
                final int count = entry.getValue();
                final int percentage;
                if (totalItems > 0) {
                    percentage = (count * PERCENTAGE_MULTIPLIER) / totalItems;
                } else {
                    percentage = 0;
                }

                final JLabel label = new JLabel(String.format("• %s: %d items (%d%%)", key, count, percentage));
                panel.add(label);
            }
        }

        panel.revalidate();
        panel.repaint();
    }
}
