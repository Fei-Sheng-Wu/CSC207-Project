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
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import interface_adapter.wardrobe.WardrobeViewModel;
import interface_adapter.wardrobe_analyzer.WardrobeAnalyzerController;
import interface_adapter.wardrobe_analyzer.WardrobeAnalyzerState;

/**
 * Represents the wardrobe view.
 */
public class WardrobeDetailsView extends AbstractView implements PropertyChangeListener {
    private static final int PERCENTAGE_MULTIPLIER = 100;

    private final WardrobeViewModel viewModel;
    private final WardrobeAnalyzerController analyzerController;

    private final DefaultListModel<String> allItemsModel;
    private final DefaultListModel<String> oldItemsModel;

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

        // Initialize UI Models
        this.allItemsModel = new DefaultListModel<>();
        this.oldItemsModel = new DefaultListModel<>();

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
        add(createMainContentPanel(), BorderLayout.CENTER);

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

    // We should actually delete this if we find no use for it!!
    private JPanel createListsPanel() {
        final JList<String> allItemsList = new JList<>(allItemsModel);
        final JList<String> oldItemsList = new JList<>(oldItemsModel);

        final JScrollPane allItemsScroll = new JScrollPane(allItemsList);
        allItemsScroll.setBorder(BorderFactory.createTitledBorder("All Items"));
        allItemsScroll.setOpaque(false);
        allItemsScroll.getViewport().setOpaque(false);

        final JScrollPane oldItemsScroll = new JScrollPane(oldItemsList);
        oldItemsScroll.setBorder(BorderFactory.createTitledBorder("Old Items (Donation Candidates)"));
        oldItemsScroll.setOpaque(false);
        oldItemsScroll.getViewport().setOpaque(false);

        final JPanel listsPanel = new JPanel(new GridLayout(1, 2, SIZE_SPACING_MD, SIZE_SPACING_MD));
        listsPanel.setOpaque(false);
        listsPanel.add(allItemsScroll);
        listsPanel.add(oldItemsScroll);

        return listsPanel;
    }

    private JPanel createMainContentPanel() {
        final JPanel wrapper = new JPanel(new BorderLayout(SIZE_SPACING_MD, SIZE_SPACING_MD));
        wrapper.setOpaque(false);

        wrapper.add(createStatsGrid(), BorderLayout.PAGE_START);
        wrapper.add(createListsPanel(), BorderLayout.CENTER);

        return wrapper;
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
            case WardrobeViewModel.PROPERTY_ANALYZER_STATE:
                buildAnalyzerUi(viewModel.getAnalyzerState());
                break;
            case "items":
                // @TODO: update content
                // If we cannot finish this on time, it is fine to just remove the two lists from this view -Jet
            case "itemsOld":
                // @TODO: update content
            default:
                break;
        }
    }

    private void buildAnalyzerUi(WardrobeAnalyzerState state) {
        if (state.getError() != null && isVisible()) {
            JOptionPane.showMessageDialog(this, state.getError(), "Analyzer Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        totalItemsLabel.setText("Total Items: " + state.getTotalItemsCount());
        averageFondnessLabel.setText("Average Fondness: " + state.getAverageFondnessString());
        donationCandidatesLabel.setText("Donation Candidates (> 1 year; <0.5 fondness): "
                                                                    + state.getDonationCandidateCount());
        oldestAgeLabel.setText("Oldest Item Age: " + state.getOldestItemAge());
        newestAgeLabel.setText("Newest Item Age: " + state.getNewestItemAge());

        final int totalItems = state.getTotalItemsCount();
        populateDistributionPanel(categoryBreakdownPanel, state.getCategoryDistribution(), totalItems);
        populateDistributionPanel(conditionBreakdownPanel, state.getConditionDistribution(), totalItems);
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

    /**
     * Builds reporting view for old items and other stats.
     */
    private void buildReportingUi() {
        final JList<String> allItemsList = new JList<>(allItemsModel);
        final JList<String> oldItemsList = new JList<>(oldItemsModel);

        final JScrollPane allItemsScroll = new JScrollPane(allItemsList);
        allItemsScroll.setBorder(BorderFactory.createTitledBorder("All Items"));

        final JScrollPane oldItemsScroll = new JScrollPane(oldItemsList);
        oldItemsScroll.setBorder(BorderFactory.createTitledBorder("Old Items (Needs Review)"));

        final JPanel listsPanel = new JPanel(new GridLayout(1, 2, SIZE_SPACING_MD, SIZE_SPACING_MD));
        listsPanel.add(allItemsScroll);
        listsPanel.add(oldItemsScroll);

        final JPanel statsPanel = new JPanel(new GridLayout(5, 1, 5, 5));
        statsPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder("Wardrobe Stats"),
            BorderFactory.createEmptyBorder(SIZE_SPACING_SM, SIZE_SPACING_SM, SIZE_SPACING_SM, SIZE_SPACING_SM)
        ));

        statsPanel.add(totalItemsLabel);
        statsPanel.add(averageFondnessLabel);
        statsPanel.add(donationCandidatesLabel);
        statsPanel.add(oldestAgeLabel);
        statsPanel.add(newestAgeLabel);
        statsPanel.add(categoryBreakdownPanel);
        statsPanel.add(conditionBreakdownPanel);

        add(statsPanel, BorderLayout.NORTH);
        add(listsPanel, BorderLayout.CENTER);
    }
}
