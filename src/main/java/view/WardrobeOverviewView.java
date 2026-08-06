package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import entity.AbstractWear;
import entity.InnerTopwear;
import entity.WearFactory;
import interface_adapter.item.ItemViewModel;
import interface_adapter.wardrobe.WardrobeViewModel;
import interface_adapter.wardrobe_adder.WardrobeAdderController;
import interface_adapter.wardrobe_remover.WardrobeRemoverController;
import interface_adapter.wardrobe_reporter.WardrobeReporterController;

/**
 * Represents the wardrobe view.
 */
public class WardrobeOverviewView extends AbstractView implements PropertyChangeListener {
    private static final String DEFAULT_ITEM_TYPE = InnerTopwear.class.getSimpleName();

    private final ApplicationManager manager;
    private final WardrobeViewModel wardrobeViewModel;
    private final ItemViewModel itemViewModel;
    private final WardrobeReporterController reporterController;
    private final WardrobeAdderController adderController;
    private final WardrobeRemoverController removerController;

    private final JPanel list;

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
        this.removerController = manager.get(WardrobeRemoverController.class);

        // Initialize the layout.
        setLayout(new BorderLayout(SIZE_SPACING_MD, SIZE_SPACING_MD));

        // Add the header bar.
        add(createHeader(), BorderLayout.PAGE_START);

        // Add the list.
        this.list = new JPanel();
        this.list.setLayout(new BoxLayout(this.list, BoxLayout.PAGE_AXIS));

        final JScrollPane scroll = new JScrollPane(this.list);
        scroll.setBackground(COLOR_NONE);
        scroll.setBorder(BorderFactory.createLineBorder(COLOR_BORDER, 1));
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        add(scroll, BorderLayout.CENTER);

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                reporterController.reportWardrobe();
            }
        });
    }

    private JPanel createHeader() {
        final JPanel header = new JPanel(new BorderLayout());

        final JLabel headerTitle = new JLabel("Wardrobe");
        headerTitle.setFont(FONT_TITLE);
        header.add(headerTitle, BorderLayout.LINE_START);
        final JPanel right = new JPanel(new FlowLayout(FlowLayout.TRAILING, SIZE_SPACING_SM, 0));
        right.setBorder(BorderFactory.createEmptyBorder(0, -SIZE_SPACING_SM, 0, -SIZE_SPACING_SM));
        header.add(right, BorderLayout.LINE_END);

        final JButton report = new JButton("Report");
        report.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                manager.showView(WardrobeDetailsView.class);
            }
        });
        right.add(report);
        final JButton add = new JButton("Add Item");
        add.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                adderController.addItem(DEFAULT_ITEM_TYPE);
                manager.showView(ItemView.class);
            }
        });
        right.add(add);

        return header;
    }

    private void addCard(AbstractWear wear) {
        final JPanel card = new JPanel(new BorderLayout());
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, SIZE_HEIGHT_XL));
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, COLOR_BORDER),
            BorderFactory.createEmptyBorder(SIZE_SPACING_SM, SIZE_SPACING_SM, SIZE_SPACING_SM, SIZE_SPACING_SM)
        ));

        final JPanel left = new JPanel(new FlowLayout(FlowLayout.LEADING, SIZE_SPACING_SM, 0));
        left.setBorder(BorderFactory.createEmptyBorder(SIZE_SPACING_XS, -SIZE_SPACING_SM, 0, -SIZE_SPACING_SM));
        card.add(left, BorderLayout.LINE_START);
        final JLabel icon = new JLabel(WearFactory.getIcon(wear.getClass()));
        icon.setFont(FONT_EMOJI);
        left.add(icon);
        left.add(new JLabel(wear.getDisplayString()));

        final JPanel right = new JPanel(new FlowLayout(FlowLayout.TRAILING, SIZE_SPACING_SM, 0));
        right.setBorder(BorderFactory.createEmptyBorder(0, -SIZE_SPACING_SM, 0, -SIZE_SPACING_SM));
        card.add(right, BorderLayout.LINE_END);

        final JButton update = new JButton("Edit Item");
        update.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                itemViewModel.setCurrentItem(wear);
                manager.showView(ItemView.class);
            }
        });
        right.add(update);

        final JButton remove = new JButton("Remove Item");
        remove.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                removerController.removeItem(wear);
                reporterController.reportWardrobe();
            }
        });
        right.add(remove);

        list.add(card);
    }

    @Override
    public String getTitle() {
        return "My Wardrobe";
    }

    @Override
    public void propertyChange(PropertyChangeEvent e) {
        switch (e.getPropertyName()) {
            case WardrobeViewModel.PROPERTY_ERROR:
                if (e.getNewValue() != null && isVisible()) {
                    JOptionPane.showMessageDialog(this, e.getNewValue());
                }
                break;
            case WardrobeViewModel.PROPERTY_ITEMS:
                list.removeAll();
                for (AbstractWear wear : wardrobeViewModel.getItems()) {
                    addCard(wear);
                }
                list.revalidate();
                repaint();
                break;
            default:
                break;
        }
    }
}
