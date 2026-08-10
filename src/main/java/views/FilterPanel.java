package views;

import java.awt.Component;
import java.awt.GridLayout;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import interface_adapter.wardrobe_filterer.WardrobeFiltererController;

public class FilterPanel extends JPanel {
    private final JTextField nameTextField = new JTextField(20);
    private final JComboBox<String> categoryComboBox;
    private final JTextField monthsTextField = new JTextField(20);
    private final JComboBox<String> conditionComboBox;
    private final JTextField tagTextField = new JTextField(20);

    /**
     * Constructs a new FilterPanel.
     *
     * @param categories the list of categories to populate the dropdown
     * @param conditions the list of conditions to populate the dropdown
     */
    public FilterPanel(List<String> categories, List<String> conditions) {
        this.categoryComboBox = new JComboBox<>(categories.toArray(new String[0]));
        this.conditionComboBox = new JComboBox<>(conditions.toArray(new String[0]));

        createFields();
    }

    private void createFields() {
        final Component[] components = {
            new JLabel("Name:"), nameTextField,
            new JLabel("Type:"), categoryComboBox,
            new JLabel("Condition:"), conditionComboBox,
            new JLabel("Tag:"), tagTextField,
            new JLabel("Months Since Purchase:"), monthsTextField,
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
     * Applies the filter.
     *
     * @param controller the filterer controller
     */
    public void apply(WardrobeFiltererController controller) {
        String selectedCategory = null;
        if (categoryComboBox.getSelectedIndex() > 0) {
            selectedCategory = (String) categoryComboBox.getSelectedItem();
        }
        String selectedCondition = null;
        if (conditionComboBox.getSelectedIndex() > 0) {
            selectedCondition = (String) conditionComboBox.getSelectedItem();
        }
        final int purchaseMonths = parsePurchaseMonths();
        final String name = nameTextField.getText();
        final String tag = tagTextField.getText();

        controller.filterWardrobe(name, selectedCategory, selectedCondition, purchaseMonths, tag);
    }
}
