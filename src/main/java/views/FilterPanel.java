package views;

import java.awt.Component;
import java.awt.GridLayout;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class FilterPanel extends JPanel {
    private final JTextField nameTextField = new JTextField(20);
    private final JComboBox<String> categoryComboBox;
    private final JTextField monthsTextField = new JTextField(20);
    private final JComboBox<String> conditionComboBox;
    private final JTextField tagTextField = new JTextField(20);
    private final JButton applyButton = new JButton("Apply Filter");

    /**
     * Constructs a new FilterPanel.
     *
     * @param listener   the listener when the filter is applied
     * @param categories the list of categories to populate the dropdown
     * @param conditions the list of conditions to populate the dropdown
     */
    public FilterPanel(FilterListener listener, List<String> categories, List<String> conditions) {
        this.categoryComboBox = new JComboBox<>(categories.toArray(new String[0]));
        this.conditionComboBox = new JComboBox<>(conditions.toArray(new String[0]));

        createFields();

        applyButton.addActionListener(event -> {
            final String selectedCategory = (String) categoryComboBox.getSelectedItem();
            final String selectedCondition = (String) conditionComboBox.getSelectedItem();
            final int purchaseMonths = parsePurchaseMonths();
            final String name = nameTextField.getText();
            final String tag = tagTextField.getText();

            listener.onFilterApplied(name, selectedCategory, purchaseMonths, selectedCondition, tag);
        });
    }

    private void createFields() {
        final Component[] components = {
            new JLabel("Name:"), nameTextField,
            new JLabel("Type:"), categoryComboBox,
            new JLabel("Condition:"), conditionComboBox,
            new JLabel("Tag:"), tagTextField,
            new JLabel("Months Since Purchase:"), monthsTextField,
            new JLabel(""), applyButton,
        };

        setLayout(new GridLayout(
            components.length / 2, 2, AbstractView.SIZE_SPACING_MD, AbstractView.SIZE_SPACING_XS
        ));

        for (Component component : components) {
            add(component);
        }
    }

    private int parsePurchaseMonths() {
        try {
            final String monthsText = monthsTextField.getText().trim();
            if (!monthsText.isEmpty()) {
                return Integer.parseInt(monthsText);
            }
        } catch (NumberFormatException ex) {
            return 0;
        }
        return 0;
    }

    /**
     * Interface for listening to filter application events.
     */
    public interface FilterListener {
        /**
         * Called when the apply filter button is clicked.
         *
         * @param name           the name filter string
         * @param category       the category filter string
         * @param purchaseMonths the purchase months filter integer
         * @param condition      the condition filter string
         * @param tag            the tag filter string
         */
        void onFilterApplied(String name,
                             String category,
                             int purchaseMonths,
                             String condition,
                             String tag);
    }
}
