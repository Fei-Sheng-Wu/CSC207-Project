package view;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import interface_adapter.settings.SettingsViewModel;

/**
 * Represents the settings view.
 */
public class SettingsView extends AbstractView implements PropertyChangeListener {
    private final SettingsViewModel viewModel;

    private final JTextField locationCity = createInputText();
    private final JTextField locationCountryCode = createInputText();

    /**
     * Constructs a new settings view.
     *
     * @param manager the application manager of the view
     */
    public SettingsView(ApplicationManager manager) {
        super(manager);

        // Retrieve the shared resources.
        this.viewModel = manager.get(SettingsViewModel.class);
        this.viewModel.addPropertyChangeListener(this);

        // Initialize the layout.
        setLayout(new GridBagLayout());

        // Add the settings items.
        final Component[][] gridRows = {
            {new JLabel("Your city:"), this.locationCity},
            {new JLabel("Your 2-digit country code:"), this.locationCountryCode},
        };
        final JPanel grid = new JPanel(new GridLayout(
            gridRows.length,
            gridRows[0].length,
            SIZE_SPACING_MD,
            SIZE_SPACING_MD
        ));
        add(grid);

        for (Component[] gridRow : gridRows) {
            for (Component gridItem : gridRow) {
                grid.add(gridItem);
            }
        }

        // @TODO: add a save button to update settings
    }

    private JTextField createInputText() {
        final JTextField input = new JTextField();
        input.setPreferredSize(new Dimension(SIZE_WIDTH_XL, SIZE_HEIGHT_MD));

        return input;
    }

    @Override
    public String getTitle() {
        return "Settings";
    }

    @Override
    public void propertyChange(PropertyChangeEvent e) {
        switch (e.getPropertyName()) {
            case "locationCity":
                locationCity.setText(viewModel.getLocationCity());
                break;
            case "locationCountryCode":
                locationCountryCode.setText(viewModel.getLocationCountryCode());
                break;
            default:
                break;
        }
    }
}
