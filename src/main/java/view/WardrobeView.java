package view;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.UUID;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import data_access.wardrobe.JsonWardrobeDataAccessObject;
import entity.AbstractWear;
import entity.Wardrobe;
import interface_adapter.wardrobe.WardrobeViewModel;
import interface_adapter.wardrobe_remover.WardrobeRemoverController;
import interface_adapter.wardrobe_remover.WardrobeRemoverPresenter;
import interface_adapter.wardrobe_updater.WardrobeUpdaterController;
import interface_adapter.wardrobe_updater.WardrobeUpdaterPresenter;
import use_case.wardrobe.WardrobeDataAccessInterface;
import use_case.wardrobe_remover.WardrobeRemoverInputBoundary;
import use_case.wardrobe_remover.WardrobeRemoverInteractor;
import use_case.wardrobe_updater.WardrobeUpdaterInputBoundary;
import use_case.wardrobe_updater.WardrobeUpdaterInteractor;

/**
 * Represents the wardrobe view.
 */
public class WardrobeView extends AbstractView implements PropertyChangeListener {
    private static final String LIST_CARD = "list";
    private static final String EDIT_CARD = "edit";

    private static final Color BACKGROUND_BLUE = new Color(11, 31, 58);
    private static final Color PANEL_BLUE = new Color(18, 52, 86);
    private static final Color FIELD_BLUE = new Color(230, 240, 250);
    private static final Color BUTTON_BLUE = new Color(30, 92, 150);
    private static final Color EDIT_BUTTON_BACKGROUND = new Color(226, 238, 250);
    private static final Color TEXT_WHITE = Color.WHITE;
    private static final Color ROW_BACKGROUND = new Color(240, 246, 252);
    private static final Color ROW_BORDER = new Color(198, 215, 230);

    private static final String[] WEAR_TYPES = {
        "InnerTopwear",
        "OuterTopwear",
        "Bottomwear",
        "Footwear",
        "Headwear",
        "Accessory",
    };

    private static final String[] COLORS = {
        "",
        "BLACK",
        "WHITE",
        "GREY",
        "BROWN",
        "RED",
        "ORANGE",
        "YELLOW",
        "GREEN",
        "BLUE",
        "PURPLE",
        "PINK",
        "MULTI",
    };

    private static final String[] STYLES = {
        "",
        "PROFESSIONAL",
        "CASUAL",
        "SPORTY",
        "ROMANTIC",
        "FORMAL",
        "INDOOR",
    };

    private static final String[] CONDITIONS = {
        "",
        "NEW",
        "FAIR",
        "DAMAGED",
    };

    private final WardrobeViewModel viewModel;
    private final WardrobeDataAccessInterface wardrobeDataAccessObject;
    private final WardrobeUpdaterController updaterController;
    private final WardrobeRemoverController removerController;

    private final JLabel pageTitle = new JLabel("Current Wardrobe");
    private final JPanel cardsPanel = new JPanel(new CardLayout());
    private final JPanel listPanel = new JPanel();
    private final JPanel editPanel = new JPanel(new GridBagLayout());

    private final JComboBox<String> typeBox = new JComboBox<>(WEAR_TYPES);
    private final JTextField nameField = new JTextField(22);
    private final JTextField brandField = new JTextField(22);
    private final JComboBox<String> colorBox = new JComboBox<>(COLORS);
    private final JComboBox<String> styleBox = new JComboBox<>(STYLES);
    private final JComboBox<String> conditionBox = new JComboBox<>(CONDITIONS);

    private UUID selectedUuid;
    private String selectedOriginalType;

    /**
     * Constructs a new wardrobe view.
     *
     * @param manager the application manager of the view
     */
    public WardrobeView(ApplicationManager manager) {
        super(manager);
        setLayout(new BorderLayout(12, 12));
        setBorder(BorderFactory.createEmptyBorder(14, 14, 14, 14));
        setBackground(BACKGROUND_BLUE);

        this.viewModel = new WardrobeViewModel();
        this.viewModel.addPropertyChangeListener(this);
        manager.register(WardrobeViewModel.class, this.viewModel);

        this.wardrobeDataAccessObject = new JsonWardrobeDataAccessObject();

        final WardrobeUpdaterPresenter updaterPresenter = new WardrobeUpdaterPresenter(this::showMessage);
        final WardrobeRemoverPresenter removerPresenter = new WardrobeRemoverPresenter(this::showMessage);

        final WardrobeUpdaterInputBoundary updaterInteractor = new WardrobeUpdaterInteractor(
            wardrobeDataAccessObject,
            updaterPresenter
        );
        final WardrobeRemoverInputBoundary removerInteractor = new WardrobeRemoverInteractor(
            wardrobeDataAccessObject,
            removerPresenter
        );

        this.updaterController = new WardrobeUpdaterController(updaterInteractor);
        this.removerController = new WardrobeRemoverController(removerInteractor);

        add(createHeaderPanel(), BorderLayout.NORTH);
        add(createCardsPanel(), BorderLayout.CENTER);

        refreshWardrobeList();
        showListCard();
    }

    private JPanel createHeaderPanel() {
        final JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(BACKGROUND_BLUE);

        pageTitle.setForeground(TEXT_WHITE);
        pageTitle.setFont(new Font("SansSerif", Font.BOLD, 22));

        final JButton refreshButton = createPrimaryButton("Refresh");
        refreshButton.addActionListener(event -> {
            refreshWardrobeList();
            showListCard();
        });

        headerPanel.add(pageTitle, BorderLayout.WEST);
        headerPanel.add(refreshButton, BorderLayout.EAST);

        return headerPanel;
    }

    private JPanel createCardsPanel() {
        cardsPanel.setBackground(BACKGROUND_BLUE);
        cardsPanel.add(createListCard(), LIST_CARD);
        cardsPanel.add(createEditCard(), EDIT_CARD);
        return cardsPanel;
    }

    private JScrollPane createListCard() {
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        listPanel.setBackground(PANEL_BLUE);
        listPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(TEXT_WHITE),
            "Clothing Items",
            0,
            0,
            new Font("SansSerif", Font.BOLD, 14),
            TEXT_WHITE
        ));

        final JScrollPane scrollPane = new JScrollPane(listPanel);
        scrollPane.getViewport().setBackground(PANEL_BLUE);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        return scrollPane;
    }

    private JPanel createEditCard() {
        editPanel.setBackground(PANEL_BLUE);
        editPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(TEXT_WHITE),
            "Editing Selected Clothing Item",
            0,
            0,
            new Font("SansSerif", Font.BOLD, 14),
            TEXT_WHITE
        ));

        styleComboBox(typeBox);
        styleInput(nameField);
        styleInput(brandField);
        styleComboBox(colorBox);
        styleComboBox(styleBox);
        styleComboBox(conditionBox);

        addFormRow(editPanel, "Type:", typeBox, 0);
        addFormRow(editPanel, "Name:", nameField, 1);
        addFormRow(editPanel, "Brand:", brandField, 2);
        addFormRow(editPanel, "Color:", colorBox, 3);
        addFormRow(editPanel, "Style:", styleBox, 4);
        addFormRow(editPanel, "Condition:", conditionBox, 5);

        final JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(PANEL_BLUE);

        final JButton saveButton = createPrimaryButton("Save Changes");
        final JButton removeButton = createPrimaryButton("Remove Item");
        final JButton cancelButton = createPrimaryButton("Cancel");

        saveButton.addActionListener(event -> saveSelectedItem());
        removeButton.addActionListener(event -> removeSelectedItem());
        cancelButton.addActionListener(event -> showListCard());

        buttonPanel.add(saveButton);
        buttonPanel.add(removeButton);
        buttonPanel.add(cancelButton);

        final GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 6;
        constraints.gridwidth = 2;
        constraints.insets = new Insets(12, 4, 4, 4);
        constraints.anchor = GridBagConstraints.CENTER;
        editPanel.add(buttonPanel, constraints);

        return editPanel;
    }

    private void refreshWardrobeList() {
        listPanel.removeAll();

        try {
            final Wardrobe wardrobe = wardrobeDataAccessObject.fetchWardrobe();

            if (wardrobe.getItems().isEmpty()) {
                listPanel.add(createEmptyRow());
            }
            else {
                for (AbstractWear item : wardrobe.getItems()) {
                    listPanel.add(createWardrobeRow(item));
                }
            }
        }
        catch (RuntimeException ex) {
            JOptionPane.showMessageDialog(
                this,
                "Could not load wardrobe data. Please make sure wardrobe.json exists.",
                "Wardrobe Load Error",
                JOptionPane.ERROR_MESSAGE
            );
        }

        listPanel.revalidate();
        listPanel.repaint();
    }

    private JPanel createWardrobeRow(AbstractWear item) {
        final JPanel rowPanel = new JPanel(new BorderLayout(8, 0));
        rowPanel.setBackground(ROW_BACKGROUND);
        rowPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, ROW_BORDER),
            BorderFactory.createEmptyBorder(6, 12, 6, 12)
        ));
        rowPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        rowPanel.setPreferredSize(new Dimension(0, 42));

        final String itemName;
        if (item.getName() == null || item.getName().isBlank()) {
            itemName = "(Unnamed Item)";
        }
        else {
            itemName = item.getName();
        }

        final JLabel nameLabel = new JLabel(itemName);
        nameLabel.setForeground(Color.BLACK);
        nameLabel.setFont(new Font("SansSerif", Font.BOLD, 14));

        final JButton editButton = createEditButton();
        editButton.addActionListener(event -> loadItemIntoEditPanel(item));

        rowPanel.add(nameLabel, BorderLayout.WEST);
        rowPanel.add(editButton, BorderLayout.EAST);

        return rowPanel;
    }

    private JPanel createEmptyRow() {
        final JPanel rowPanel = new JPanel(new BorderLayout());
        rowPanel.setBackground(ROW_BACKGROUND);
        rowPanel.setBorder(BorderFactory.createEmptyBorder(10, 12, 10, 12));
        rowPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));

        final JLabel emptyLabel = new JLabel("No clothing items found.");
        emptyLabel.setForeground(Color.DARK_GRAY);
        emptyLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));

        rowPanel.add(emptyLabel, BorderLayout.WEST);
        return rowPanel;
    }

    private void loadItemIntoEditPanel(AbstractWear item) {
        this.selectedUuid = item.getUuid();
        this.selectedOriginalType = item.getClass().getSimpleName();

        typeBox.setSelectedItem(item.getClass().getSimpleName());
        nameField.setText(nullToEmpty(item.getName()));
        brandField.setText(nullToEmpty(item.getBrand()));
        colorBox.setSelectedItem(item.getColor() == null ? "" : item.getColor().name());
        styleBox.setSelectedItem(item.getStyle() == null ? "" : item.getStyle().name());
        conditionBox.setSelectedItem(item.getCondition() == null ? "" : item.getCondition().name());

        pageTitle.setText("Edit Clothing Item");
        ((CardLayout) cardsPanel.getLayout()).show(cardsPanel, EDIT_CARD);
        revalidate();
        repaint();
    }

    private void saveSelectedItem() {
        if (selectedUuid == null) {
            showMessage("Please choose an item to edit first.");
            return;
        }

        try {
            updaterController.updateItem(
                selectedUuid.toString(),
                (String) typeBox.getSelectedItem(),
                nameField.getText(),
                brandField.getText(),
                (String) colorBox.getSelectedItem(),
                (String) styleBox.getSelectedItem(),
                (String) conditionBox.getSelectedItem()
            );
            refreshWardrobeList();
            showListCard();
        }
        catch (RuntimeException ex) {
            showMessage("Could not update item: " + ex.getMessage());
        }
    }

    private void removeSelectedItem() {
        if (selectedUuid == null) {
            showMessage("Please choose an item to remove first.");
            return;
        }

        final int result = JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to remove this item?",
            "Confirm Remove",
            JOptionPane.YES_NO_OPTION
        );

        if (result == JOptionPane.YES_OPTION) {
            try {
                removerController.removeItem(selectedUuid.toString(), selectedOriginalType);
                refreshWardrobeList();
                showListCard();
            }
            catch (RuntimeException ex) {
                showMessage("Could not remove item: " + ex.getMessage());
            }
        }
    }

    private void showListCard() {
        selectedUuid = null;
        selectedOriginalType = null;

        typeBox.setSelectedIndex(0);
        nameField.setText("");
        brandField.setText("");
        colorBox.setSelectedIndex(0);
        styleBox.setSelectedIndex(0);
        conditionBox.setSelectedIndex(0);

        pageTitle.setText("Current Wardrobe");
        ((CardLayout) cardsPanel.getLayout()).show(cardsPanel, LIST_CARD);
        revalidate();
        repaint();
    }

    private void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message);
    }

    private static JButton createPrimaryButton(String text) {
        final JButton button = new JButton(text);
        button.setBackground(BUTTON_BLUE);
        button.setForeground(TEXT_WHITE);
        button.setFocusPainted(false);
        button.setFont(new Font("SansSerif", Font.BOLD, 12));
        button.setBorder(BorderFactory.createEmptyBorder(6, 12, 6, 12));
        return button;
    }

    private static JButton createEditButton() {
        final JButton button = new JButton("Edit");
        button.setToolTipText("Edit item");
        button.setBackground(EDIT_BUTTON_BACKGROUND);
        button.setForeground(new Color(8, 40, 75));
        button.setFocusPainted(false);
        button.setFont(new Font("SansSerif", Font.BOLD, 12));
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(110, 145, 180)),
            BorderFactory.createEmptyBorder(4, 12, 4, 12)
        ));
        return button;
    }

    private static void styleInput(JTextField field) {
        field.setBackground(FIELD_BLUE);
        field.setForeground(Color.BLACK);
        field.setCaretColor(Color.BLACK);
        field.setBorder(BorderFactory.createEmptyBorder(4, 6, 4, 6));
    }

    private static void styleComboBox(JComboBox<String> comboBox) {
        comboBox.setBackground(FIELD_BLUE);
        comboBox.setForeground(Color.BLACK);
    }

    private static void addFormRow(JPanel panel, String label, java.awt.Component field, int row) {
        final GridBagConstraints labelConstraints = new GridBagConstraints();
        labelConstraints.gridx = 0;
        labelConstraints.gridy = row;
        labelConstraints.insets = new Insets(5, 4, 5, 8);
        labelConstraints.anchor = GridBagConstraints.LINE_END;

        final JLabel rowLabel = new JLabel(label);
        rowLabel.setForeground(TEXT_WHITE);
        rowLabel.setFont(new Font("SansSerif", Font.BOLD, 12));
        panel.add(rowLabel, labelConstraints);

        final GridBagConstraints fieldConstraints = new GridBagConstraints();
        fieldConstraints.gridx = 1;
        fieldConstraints.gridy = row;
        fieldConstraints.insets = new Insets(5, 4, 5, 4);
        fieldConstraints.fill = GridBagConstraints.HORIZONTAL;
        fieldConstraints.weightx = 1.0;
        panel.add(field, fieldConstraints);
    }

    private static String nullToEmpty(String value) {
        return value == null ? "" : value;
    }

    @Override
    public String getTitle() {
        return "My Wardrobe";
    }

    @Override
    public void propertyChange(PropertyChangeEvent e) {

    }
}
