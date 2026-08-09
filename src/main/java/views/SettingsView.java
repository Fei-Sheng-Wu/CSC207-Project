package views;

import java.awt.Component;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.Box;
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

    private final JTextField fieldLocationCity = new JTextField();
    private final JTextField fieldLocationCountryCode = new JTextField();

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
        final JButton save = new JButton("Save Settings");
        save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updaterController.update(fieldLocationCity.getText(), fieldLocationCountryCode.getText());
            }
        });

        final Component[] components = {
            new JLabel("Your city:"), this.fieldLocationCity,
            new JLabel("Your 2-digit country code:"), this.fieldLocationCountryCode,
            Box.createHorizontalGlue(), save,
        };
        final JPanel fields = new JPanel(new GridLayout(
            components.length / 2, 2, SIZE_SPACING_MD, SIZE_SPACING_MD
        ));
        fields.setOpaque(false);
        for (Component component : components) {
            fields.add(component);
        }
        add(fields);

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                retrieverController.retrieve();
            }
        });
    }

    @Override
    public String getTitle() {
        return "Settings";
    }

    @Override
    public void propertyChange(PropertyChangeEvent e) {
        switch (e.getPropertyName()) {
            case SettingsViewModel.PROPERTY_LOCATION_CITY:
                fieldLocationCity.setText(viewModel.getLocationCity());
                break;
            case SettingsViewModel.PROPERTY_LOCATION_COUNTRY_CODE:
                fieldLocationCountryCode.setText(viewModel.getLocationCountryCode());
                break;
            default:
                break;
        }
    }
}
