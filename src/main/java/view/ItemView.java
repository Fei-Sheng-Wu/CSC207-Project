package view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;

import org.jdatepicker.JDatePicker;

import entity.AbstractWear;
import entity.Accessory;
import entity.Bottomwear;
import entity.Footwear;
import entity.Headwear;
import entity.InnerTopwear;
import entity.OuterTopwear;
import entity.WearColor;
import entity.WearCondition;
import entity.WearFactory;
import entity.WearStyle;
import interface_adapter.item.ItemViewModel;
import interface_adapter.wardrobe_remover.WardrobeRemoverController;
import interface_adapter.wardrobe_updater.WardrobeUpdaterController;

/**
 * Represents the item editing view.
 */
public class ItemView extends AbstractView implements PropertyChangeListener {
    private static final String OPTION_NONE = "(No Input)";
    private static final Class<?>[] ITEM_TYPES = new Class<?>[]{
        InnerTopwear.class, OuterTopwear.class, Bottomwear.class, Footwear.class, Headwear.class, Accessory.class,
    };

    private final ApplicationManager manager;
    private final ItemViewModel viewModel;
    private final WardrobeUpdaterController updaterController;
    private final WardrobeRemoverController removerController;

    private JComboBox<String> choiceType = new JComboBox<>(
        new String[]{"Inner Topwear", "Outer Topwear", "Bottomwear", "Footwear", "Headwear", "Accessories"}
    );
    private final JLabel icon = new JLabel();
    private final JTextField fieldName = new JTextField();
    private final JTextField fieldBrand = new JTextField();
    private final JComboBox<String> choiceColor = createChoice(WearColor.values());
    private final JComboBox<String> choiceStyle = createChoice(WearStyle.values());
    private final JComboBox<String> choiceCondition = createChoice(WearCondition.values());
    private final JDatePicker pickerPurchaseDate = new JDatePicker();
    private final JSlider sliderFondness = new JSlider(JSlider.HORIZONTAL, 0, 100, 100);
    private final JTextField fieldTags = new JTextField();

    private AbstractWear item;

    /**
     * Constructs a new item editing view.
     *
     * @param manager the application manager of the view
     */
    public ItemView(ApplicationManager manager) {
        super(manager);

        // Retrieve the shared resources.
        this.manager = manager;
        this.viewModel = manager.get(ItemViewModel.class);
        this.viewModel.addPropertyChangeListener(this);
        this.updaterController = manager.get(WardrobeUpdaterController.class);
        this.removerController = manager.get(WardrobeRemoverController.class);

        // Initialize the layout.
        setLayout(new BorderLayout(SIZE_SPACING_MD, SIZE_SPACING_MD));

        // Add the header bar.
        add(createHeader(), BorderLayout.PAGE_START);

        // Add the fields.
        add(createFields(), BorderLayout.CENTER);
    }

    private JPanel createHeader() {
        final JPanel header = new JPanel(new BorderLayout());
        header.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, COLOR_BORDER),
            BorderFactory.createEmptyBorder(0, 0, SIZE_SPACING_MD, 0)
        ));

        final JLabel headerTitle = new JLabel("Edit Item");
        headerTitle.setFont(FONT_TITLE);
        header.add(headerTitle, BorderLayout.LINE_START);
        final JPanel right = new JPanel(new FlowLayout(FlowLayout.TRAILING, SIZE_SPACING_SM, 0));
        right.setBorder(BorderFactory.createEmptyBorder(0, -SIZE_SPACING_SM, 0, -SIZE_SPACING_SM));
        header.add(right, BorderLayout.LINE_END);

        final JButton cancel = new JButton("Cancel");
        cancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                manager.showView(WardrobeOverviewView.class);
            }
        });
        right.add(cancel);
        final JButton remove = new JButton("Remove Item");
        remove.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (item == null) {
                    return;
                }

                removerController.removeItem(item);
                manager.showView(WardrobeOverviewView.class);
            }
        });
        right.add(remove);
        final JButton update = new JButton("Update Item");
        update.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                update();
                manager.showView(WardrobeOverviewView.class);
            }
        });
        right.add(update);

        return header;
    }

    private JPanel createFields() {
        final JPanel wrapper = new JPanel(new BorderLayout());

        final JButton inspire = new JButton("Get Inspired");
        inspire.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                manager.showView(InspirationView.class);
            }
        });

        final Component[] components = {
            new JLabel("Type:"), choiceType,
            icon, new JPanel(),
            new JLabel("Name:"), fieldName,
            new JLabel("Brand:"), fieldBrand,
            new JLabel("Color:"), choiceColor,
            new JLabel("Style:"), choiceStyle,
            new JLabel("Condition:"), choiceCondition,
            new JLabel("Purchase Date:"), pickerPurchaseDate,
            new JLabel("Fondness:"), sliderFondness,
            new JLabel("Tags:"), fieldTags,
            new JPanel(), new JPanel(), new JPanel(), inspire,
        };
        final JPanel fields = new JPanel(new GridLayout(
            components.length / 4, 4, SIZE_SPACING_MD, SIZE_SPACING_MD
        ));
        wrapper.add(fields, BorderLayout.PAGE_START);

        choiceType.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                icon.setText(WearFactory.getIcon(ITEM_TYPES[choiceType.getSelectedIndex()]));
                repaint();
            }
        });
        icon.setFont(FONT_EMOJI);

        for (Component component : components) {
            fields.add(component);
        }

        return wrapper;
    }

    private static JComboBox<String> createChoice(Object[] values) {
        final String[] options = new String[values.length + 1];
        options[0] = OPTION_NONE;
        for (int index = 0; index < values.length; index++) {
            String option = values[index].toString();
            if (values[index] instanceof WearColor) {
                option = ((WearColor) values[index]).getDisplayName();
            } else if (values[index] instanceof WearStyle) {
                option = ((WearStyle) values[index]).getDisplayName();
            } else if (values[index] instanceof WearCondition) {
                option = ((WearCondition) values[index]).getDisplayName();
            }
            options[index + 1] = option;
        }

        return new JComboBox<>(options);
    }

    private static int selectEnum(Enum<?> value) {
        if (value == null) {
            return 0;
        }

        return value.ordinal() + 1;
    }

    private static String selectedEnum(JComboBox<String> choice, Object[] values) {
        final int index = choice.getSelectedIndex();
        if (index > 0) {
            return ((Enum<?>) values[index - 1]).name();
        }

        return "";
    }

    private void update() {
        if (item == null) {
            return;
        }

        Integer purchaseDateYear = null;
        Integer purchaseDateMonth = null;
        Integer purchaseDateDay = null;
        if (pickerPurchaseDate.getModel().isSelected()) {
            purchaseDateYear = pickerPurchaseDate.getModel().getYear();
            purchaseDateMonth = pickerPurchaseDate.getModel().getMonth();
            purchaseDateDay = pickerPurchaseDate.getModel().getDay();
        }

        updaterController.updateItem(
            item,
            ITEM_TYPES[choiceType.getSelectedIndex()].getSimpleName(),
            fieldName.getText(),
            fieldBrand.getText(),
            selectedEnum(choiceColor, WearColor.values()),
            selectedEnum(choiceStyle, WearStyle.values()),
            selectedEnum(choiceCondition, WearCondition.values()),
            purchaseDateYear,
            purchaseDateMonth,
            purchaseDateDay,
            sliderFondness.getValue(),
            fieldTags.getText()
        );
    }

    @Override
    public String getTitle() {
        return "My Clothing Item";
    }

    @Override
    public void propertyChange(PropertyChangeEvent e) {
        switch (e.getPropertyName()) {
            case ItemViewModel.PROPERTY_ERROR:
                if (viewModel.getError() != null && isVisible()) {
                    JOptionPane.showMessageDialog(this, viewModel.getError());
                }
                break;
            case ItemViewModel.PROPERTY_CURRENT_ITEM:
                item = viewModel.getCurrentItem();
                if (item == null) {
                    break;
                }

                for (int i = 0; i < ITEM_TYPES.length; i++) {
                    if (ITEM_TYPES[i].equals(item.getClass())) {
                        choiceType.setSelectedIndex(i);
                    }
                }
                icon.setText(WearFactory.getIcon(item.getClass()));
                fieldName.setText(item.getName());
                fieldBrand.setText(item.getBrand());
                choiceColor.setSelectedIndex(selectEnum(item.getColor()));
                choiceStyle.setSelectedIndex(selectEnum(item.getStyle()));
                choiceCondition.setSelectedIndex(selectEnum(item.getCondition()));
                if (item.getPurchaseDate() == null) {
                    pickerPurchaseDate.getModel().setSelected(false);
                } else {
                    pickerPurchaseDate.getModel().setDate(
                        item.getPurchaseDate().getYear(),
                        item.getPurchaseDate().getMonthValue(),
                        item.getPurchaseDate().getDayOfMonth()
                    );
                    pickerPurchaseDate.getModel().setSelected(true);
                }
                sliderFondness.setValue((int) (item.getFondness() * sliderFondness.getMaximum()));
                fieldTags.setText(String.join(", ", item.getTags()));
                repaint();

                break;
            default:
                break;
        }
    }
}
