package view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.*;

import interface_adapter.item_editing.ItemEditingViewModel;

/**
 * Represents the item editing view.
 */
public class ItemEditingView extends AbstractView implements PropertyChangeListener {
    private final ItemEditingViewModel viewModel;

    /**
     * Constructs a new item editing view.
     *
     * @param manager the application manager of the view
     */
    public ItemEditingView(ApplicationManager manager) {
        super(manager);

        this.viewModel = new ItemEditingViewModel();
        this.viewModel.addPropertyChangeListener(this);
        manager.register(ItemEditingViewModel.class, this.viewModel);

        add(new JLabel("@TODO: item editing view"));

        // @TODO: temporary layout for testing
        final JButton inspirer = new JButton("Explore Outfit Inspirations");
        inspirer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                manager.showView(InspirationView.class);
            }
        });
        add(inspirer);
    }

    @Override
    public String getTitle() {
        return "My Clothing Item";
    }

    @Override
    public void propertyChange(PropertyChangeEvent e) {

    }
}
