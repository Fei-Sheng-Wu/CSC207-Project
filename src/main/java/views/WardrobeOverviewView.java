package views;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.jetbrains.annotations.NotNull;

import entity.AbstractWear;
import entity.InnerTopwear;
import entity.WardrobeSort;
import entity.WearFactory;
import interface_adapter.item.ItemViewModel;
import interface_adapter.wardrobe.WardrobeViewModel;
import interface_adapter.wardrobe_adder.WardrobeAdderController;
import interface_adapter.wardrobe_filterer.WardrobeFiltererController;
import interface_adapter.wardrobe_remover.WardrobeRemoverController;
import interface_adapter.wardrobe_reporter.WardrobeReporterController;
import interface_adapter.wardrobe_sorter.WardrobeSorterController;

/**
 * Represents the wardrobe view.
 */
public class WardrobeOverviewView extends AbstractView implements PropertyChangeListener {
    private static final String DEFAULT_ITEM_TYPE = InnerTopwear.class.getSimpleName();

    private final ApplicationManager manager;
    private final WardrobeViewModel wardrobeViewModel;
    private final ItemViewModel itemViewModel;
    private final WardrobeReporterController reporterController;
    private final WardrobeFiltererController filtererController;
    private final WardrobeSorterController sorterController;
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
        this.filtererController = manager.get(WardrobeFiltererController.class);
        this.sorterController = manager.get(WardrobeSorterController.class);
        this.adderController = manager.get(WardrobeAdderController.class);
        this.removerController = manager.get(WardrobeRemoverController.class);

        // Initialize the layout.
        setLayout(new BorderLayout(SIZE_SPACING_MD, SIZE_SPACING_MD));

        // Add the header bar.
        add(createHeader(), BorderLayout.PAGE_START);

        // Add the list.
        this.list = new JPanel();
        this.list.setOpaque(false);
        this.list.setLayout(new BoxLayout(this.list, BoxLayout.PAGE_AXIS));

        final JScrollPane scroll = new JScrollPane(this.list);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
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

        reporterController.reportWardrobe();
    }

    private JPanel createHeader() {
        final JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);

        final JLabel headerTitle = new JLabel("Wardrobe");
        headerTitle.setFont(FONT_TITLE);
        header.add(headerTitle, BorderLayout.LINE_START);
        final JPanel right = new JPanel(new FlowLayout(FlowLayout.TRAILING, SIZE_SPACING_SM, 0));
        right.setOpaque(false);
        right.setBorder(BorderFactory.createEmptyBorder(0, -SIZE_SPACING_SM, 0, -SIZE_SPACING_SM));
        header.add(right, BorderLayout.LINE_END);

        final JButton report = new JButton("Report Statistics");
        report.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                manager.showView(WardrobeDetailsView.class);
            }
        });
        right.add(report);

        final JButton filter = new JButton("Filter");
        filter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleFilterButton();
            }
        });
        right.add(filter);

        final JButton sortBy = new JButton("Sort By");
        sortBy.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleSortByButton();
            }
        });
        right.add(sortBy);

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

    private void handleSortByButton() {
        final String[] options = new String[WardrobeSort.values().length];
        for (int i = 0; i < options.length; i++) {
            options[i] = WardrobeSort.values()[i].getDisplayName();
        }

        final String choice = (String) JOptionPane.showInputDialog(
            WardrobeOverviewView.this,
            "Please select sorting criteria:",
            "Sort Wardrobe",
            JOptionPane.PLAIN_MESSAGE,
            null,
            options,
            options[0]
        );

        if (choice != null) {
            sorterController.sortWardrobe(choice);
        }
    }

    private void handleFilterButton() {
        final List<String> categories = getCategories();
        final List<String> conditions = getConditions();

        final FilterPanel filterPanel = new FilterPanel((name, category, purchaseMonths, condition, tag) -> {
            filtererController.filterWardrobe(category, condition, name, purchaseMonths, tag);
        }, categories, conditions);

        JOptionPane.showOptionDialog(
            WardrobeOverviewView.this,
            filterPanel,
            "Filter Wardrobe",
            JOptionPane.DEFAULT_OPTION,
            JOptionPane.PLAIN_MESSAGE,
            null,
            null,
            null
        );
    }

    @NotNull
    private List<String> getCategories() {
        final java.util.Set<String> uniqueCategories = new java.util.LinkedHashSet<>();
        uniqueCategories.add("All Categories");
        for (AbstractWear wear : wardrobeViewModel.getItems()) {
            uniqueCategories.add(wear.getClass().getSimpleName());
        }
        final List<String> categories = new java.util.ArrayList<>(uniqueCategories);
        return categories;
    }

    @NotNull
    private List<String> getConditions() {
        final java.util.Set<String> uniqueConditions = new java.util.LinkedHashSet<>();
        uniqueConditions.add("All Conditions");
        for (AbstractWear wear : wardrobeViewModel.getItems()) {
            if (wear.getCondition() != null) {
                uniqueConditions.add(wear.getCondition().name());
            }
        }
        return new java.util.ArrayList<>(uniqueConditions);
    }

    private void addCard(AbstractWear wear) {
        final JPanel card = new JPanel(new BorderLayout());
        card.setOpaque(false);
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, SIZE_HEIGHT_XL));
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, COLOR_BORDER),
            BorderFactory.createEmptyBorder(SIZE_SPACING_SM, SIZE_SPACING_SM, SIZE_SPACING_SM, SIZE_SPACING_SM)
        ));

        final JPanel left = new JPanel(new FlowLayout(FlowLayout.LEADING, SIZE_SPACING_SM, 0));
        left.setOpaque(false);
        left.setBorder(BorderFactory.createEmptyBorder(SIZE_SPACING_XS, -SIZE_SPACING_SM, 0, -SIZE_SPACING_SM));
        card.add(left, BorderLayout.LINE_START);
        final JLabel icon = new JLabel(WearFactory.getIcon(wear.getClass()));
        icon.setFont(FONT_EMOJI);
        left.add(icon);
        left.add(new JLabel(wear.getDisplayString()));

        final JPanel right = new JPanel(new FlowLayout(FlowLayout.TRAILING, SIZE_SPACING_SM, 0));
        right.setOpaque(false);
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
                if (wardrobeViewModel.getError() != null && isVisible()) {
                    JOptionPane.showMessageDialog(this, wardrobeViewModel.getError());
                }
                break;
            case WardrobeViewModel.PROPERTY_ITEMS:
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
