package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

import entity.AbstractWear;
import entity.InnerTopwear;
import interface_adapter.item.ItemViewModel;
import interface_adapter.wardrobe.WardrobeViewModel;
import interface_adapter.wardrobe_adder.WardrobeAdderController;
import interface_adapter.wardrobe_remover.WardrobeRemoverController;
import interface_adapter.wardrobe_reporter.WardrobeReporterController;
import interface_adapter.wardrobe_updater.WardrobeUpdaterController;

/**
 * Represents the wardrobe view.
 */
public class WardrobeOverviewView extends AbstractView implements PropertyChangeListener {
    private static final String DEFAULT_ITEM_TYPE = InnerTopwear.class.getSimpleName();

    private final WardrobeViewModel wardrobeViewModel;
    private final ItemViewModel itemViewModel;
    private final WardrobeReporterController reporterController;
    private final WardrobeAdderController adderController;
    private final WardrobeUpdaterController updaterController;
    private final WardrobeRemoverController removerController;

    private final JPanel list;
    private final ApplicationManager manager;

    /**
     * Constructs a new wardrobe view.
     *
     * @param manager the application manager of the view
     */
    public WardrobeOverviewView(ApplicationManager manager) {
        super(manager);

        // Retrieve the shared resources.
        this.manager = manager;
        this.wardrobeViewModel = manager.get(WardrobeViewModel.class);
        this.wardrobeViewModel.addPropertyChangeListener(this);
        this.itemViewModel = manager.get(ItemViewModel.class);
        this.itemViewModel.addPropertyChangeListener(this);
        this.reporterController = manager.get(WardrobeReporterController.class);
        this.adderController = manager.get(WardrobeAdderController.class);
        this.updaterController = manager.get(WardrobeUpdaterController.class);
        this.removerController = manager.get(WardrobeRemoverController.class);

        // Initialize the layout.
        setLayout(new BorderLayout(SIZE_SPACING_MD, SIZE_SPACING_MD));
        setBorder(new EmptyBorder(SIZE_SPACING_MD, SIZE_SPACING_MD, SIZE_SPACING_MD, SIZE_SPACING_MD));

        // Add the header bar.
        final JPanel header = new JPanel(new BorderLayout());
        add(header, BorderLayout.NORTH);

        header.add(new JLabel("Wardrobe"), BorderLayout.WEST);
        final JPanel headerButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        header.add(headerButtons, BorderLayout.EAST);

        final JButton reportButton = new JButton("Report");
        reportButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                manager.showView(WardrobeDetailsView.class);
            }
        });
        headerButtons.add(reportButton);
        final JButton addItemButton = new JButton("Add Item");
        addItemButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                adderController.addItem(DEFAULT_ITEM_TYPE);
                manager.showView(ItemView.class);
            }
        });
        headerButtons.add(addItemButton);

        // Add the list.
        this.list = new JPanel();
        this.list.setLayout(new BoxLayout(this.list, BoxLayout.Y_AXIS));

        final JScrollPane scroll = new JScrollPane(this.list);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        add(scroll, BorderLayout.CENTER);

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                reporterController.reportWardrobe();
            }

            @Override
            public void componentHidden(ComponentEvent e) {
                // Do nothing.
            }
        });
    }

    private void addCard(AbstractWear wear) {
        final JPanel card = new JPanel(new BorderLayout());
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, SIZE_HEIGHT_XXL));
        card.setPreferredSize(new Dimension(0, SIZE_HEIGHT_XL));
        card.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, Color.gray),
            new EmptyBorder(SIZE_SPACING_MD, SIZE_SPACING_MD, SIZE_SPACING_MD, SIZE_SPACING_MD)
        ));

        final JPanel left = new JPanel();
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
        card.add(left, BorderLayout.WEST);

        if (wear.getBrand().isBlank()) {
            left.add(new JLabel(wear.getName()));
        } else {
            left.add(new JLabel(String.format("%s (%s)", wear.getName(), wear.getBrand())));
        }
        left.add(Box.createVerticalStrut(SIZE_SPACING_XS));
        left.add(new JLabel(wear.getClass().getSimpleName()));

        final JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, SIZE_SPACING_SM, 0));
        card.add(right, BorderLayout.EAST);

        final JButton editButton = new JButton("Edit Item");
        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                itemViewModel.setCurrentItem(wear);
                manager.showView(ItemView.class);
            }
        });
        final JButton removeButton = new JButton("Remove Item");
        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                removerController.removeItem(wear.getUuid(), wear.getClass().getSimpleName());
                reporterController.reportWardrobe();
            }
        });

        right.add(editButton);
        right.add(removeButton);

        list.add(card);
    }

    @Override
    public String getTitle() {
        return "My Wardrobe";
    }

    @Override
    public void propertyChange(PropertyChangeEvent e) {
        switch (e.getPropertyName()) {
            case "error":
                if (e.getNewValue() != null) {
                    JOptionPane.showMessageDialog(this, e.getNewValue());
                }
                break;
            case "items":
                list.removeAll();
                for (AbstractWear wear : wardrobeViewModel.getItems()) {
                    addCard(wear);
                }
                list.revalidate();
                list.repaint();
                break;
            default:
                break;
        }
    }
}
