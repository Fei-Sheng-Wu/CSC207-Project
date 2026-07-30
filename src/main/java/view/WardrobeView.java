package view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JButton;
import javax.swing.JLabel;

import interface_adapter.wardrobe.WardrobeViewModel;

/**
 * Represents the wardrobe view.
 */
public class WardrobeView extends AbstractApplicationView implements PropertyChangeListener {
    private final WardrobeViewModel viewModel;

    /**
     * Constructs a new wardrobe view.
     *
     * @param manager the application manager of the view
     */
    public WardrobeView(ApplicationManager manager) {
        super(manager);

        this.viewModel = new WardrobeViewModel();
        this.viewModel.addPropertyChangeListener(this);
        manager.register(WardrobeViewModel.class, this.viewModel);

        add(new JLabel("@TODO: wardrobe view"));

        // @TODO: temporary layout for testing
        final JButton adder = new JButton("Add a Clothing Item");
        adder.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                manager.showView(ItemEditingView.class);
            }
        });
        add(adder);
    }

    @Override
    public String getTitle() {
        return "My Wardrobe";
    }

    @Override
    public void propertyChange(PropertyChangeEvent e) {

    }
}
