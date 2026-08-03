package view;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import interface_adapter.settings.SettingsViewModel;
import interface_adapter.settings_retriever.SettingsRetrieverController;
import interface_adapter.settings_updater.SettingsUpdaterController;

/**
 * Represents the settings view.
 */
public class SettingsView extends AbstractView implements PropertyChangeListener {
    private final SettingsViewModel viewModel;
    private final SettingsRetrieverController retrieverController;
    private final SettingsUpdaterController updaterController;

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
        this.retrieverController = manager.get(SettingsRetrieverController.class);
        this.updaterController = manager.get(SettingsUpdaterController.class);

        // Initialize the layout.
        setLayout(new GridBagLayout());

        // Add the settings items.
        final Component[][] gridRows = {
            {new JLabel("Your city:"), this.locationCity},
            {new JLabel("Your 2-digit country code:"), this.locationCountryCode},
        };
        final JPanel grid = new JPanel(new GridLayout(
            gridRows.length + 1,
            2,
            SIZE_SPACING_MD,
            SIZE_SPACING_MD
        ));
        add(grid);

        for (Component[] gridRow : gridRows) {
            for (Component gridItem : gridRow) {
                grid.add(gridItem);
            }
        }

        grid.add(new JPanel());
        final JButton saveButton = new JButton("Save Settings");
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updaterController.update(locationCity.getText(), locationCountryCode.getText());
            }
        });
        grid.add(saveButton);

        this.retrieverController.retrieve();
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
