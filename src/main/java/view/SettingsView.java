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
public class SettingsView extends AbstractApplicationView implements PropertyChangeListener {
    private static final int GRID_GAP_HOR = 12;
    private static final int GRID_GAP_VER = 8;
    private static final int TEXT_FIELD_WIDTH = 192;
    private static final int TEXT_FIELD_HEIGHT = 24;

    private final SettingsViewModel viewModel;

    private final JTextField locationCity;
    private final JTextField locationCountryCode;

    /**
     * Constructs a new settings view.
     *
     * @param manager the application manager of the view
     */
    public SettingsView(ApplicationManager manager) {
        super(manager);

        this.viewModel = new SettingsViewModel();
        this.viewModel.addPropertyChangeListener(this);
        manager.register(SettingsViewModel.class, this.viewModel);

        setLayout(new GridBagLayout());

        this.locationCity = new JTextField();
        this.locationCity.setPreferredSize(new Dimension(TEXT_FIELD_WIDTH, TEXT_FIELD_HEIGHT));
        this.locationCountryCode = new JTextField();
        this.locationCountryCode.setPreferredSize(new Dimension(TEXT_FIELD_WIDTH, TEXT_FIELD_HEIGHT));
        final Component[][] gridItems = {
            {new JLabel("Your city:"), this.locationCity},
            {new JLabel("Your 2-digit country code:"), this.locationCountryCode},
        };

        final JPanel grid = new JPanel(new GridLayout(
            gridItems.length,
            gridItems[0].length,
            GRID_GAP_HOR,
            GRID_GAP_VER
        ));
        add(grid);

        for (Component[] gridItemRow : gridItems) {
            for (Component gridItem : gridItemRow) {
                grid.add(gridItem);
            }
        }
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
