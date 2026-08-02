package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;

import entity.AbstractWear;
import entity.Wardrobe;
import interface_adapter.wardrobe_remover.WardrobeRemoverController;
import interface_adapter.wardrobe_updater.WardrobeUpdaterController;
import use_case.wardrobe.WardrobeDataAccessInterface;

/**
 * View for updating and removing wardrobe items.
 */
public class WardrobeManagementView extends JPanel {
    private static final Color BACKGROUND_BLUE = new Color(11, 31, 58);
    private static final Color PANEL_BLUE = new Color(18, 52, 86);
    private static final Color FIELD_BLUE = new Color(230, 240, 250);
    private static final Color BUTTON_BLUE = new Color(30, 92, 150);
    private static final Color BUTTON_HOVER_BLUE = new Color(42, 120, 190);
    private static final Color TEXT_WHITE = Color.WHITE;
    private static final Color TABLE_HEADER_BLUE = new Color(8, 40, 75);
    private static final Color TABLE_ROW_BLUE = new Color(225, 235, 245);

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

    private static final String[] TABLE_COLUMNS = {
        "UUID",
        "Type",
        "Name",
        "Brand",
        "Color",
        "Style",
        "Condition",
    };

    private final JTextField uuidField = new JTextField(32);
    private final JComboBox<String> typeBox = new JComboBox<>(WEAR_TYPES);
    private final JTextField nameField = new JTextField(20);
    private final JTextField brandField = new JTextField(20);
    private final JComboBox<String> colorBox = new JComboBox<>(COLORS);
    private final JComboBox<String> styleBox = new JComboBox<>(STYLES);
    private final JComboBox<String> conditionBox = new JComboBox<>(CONDITIONS);

    private final DefaultTableModel tableModel = new DefaultTableModel(TABLE_COLUMNS, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };

    private final JTable wardrobeTable = new JTable(tableModel);

    private final WardrobeUpdaterController updaterController;
    private final WardrobeRemoverController removerController;
    private final WardrobeDataAccessInterface wardrobeDataAccessObject;

    public WardrobeManagementView(WardrobeUpdaterController updaterController,
                                  WardrobeRemoverController removerController,
                                  WardrobeDataAccessInterface wardrobeDataAccessObject) {
        this.updaterController = updaterController;
        this.removerController = removerController;
        this.wardrobeDataAccessObject = wardrobeDataAccessObject;

        setLayout(new BorderLayout(12, 12));
        setBorder(BorderFactory.createEmptyBorder(14, 14, 14, 14));
        setBackground(BACKGROUND_BLUE);

        add(createTitlePanel(), BorderLayout.NORTH);
        add(createMainPanel(), BorderLayout.CENTER);

        refreshTable();
    }

    private JPanel createTitlePanel() {
        final JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(BACKGROUND_BLUE);

        final JLabel titleLabel = new JLabel("Suitable Wardrobe Management");
        titleLabel.setForeground(TEXT_WHITE);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 22));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 12, 0));

        titlePanel.add(titleLabel, BorderLayout.CENTER);
        return titlePanel;
    }

    private JPanel createMainPanel() {
        final JPanel mainPanel = new JPanel(new BorderLayout(12, 12));
        mainPanel.setBackground(BACKGROUND_BLUE);
        mainPanel.add(createFormPanel(), BorderLayout.NORTH);
        mainPanel.add(createTablePanel(), BorderLayout.CENTER);
        return mainPanel;
    }

    private JPanel createFormPanel() {
        final JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(PANEL_BLUE);
        formPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(TEXT_WHITE),
                "Manage Clothing Item",
                0,
                0,
                new Font("SansSerif", Font.BOLD, 14),
                TEXT_WHITE
        ));
        styleInput(uuidField);
        styleInput(nameField);
        styleInput(brandField);
        styleComboBox(typeBox);
        styleComboBox(colorBox);
        styleComboBox(styleBox);
        styleComboBox(conditionBox);

        addFormRow(formPanel, "Item UUID:", uuidField, 0);
        addFormRow(formPanel, "Type:", typeBox, 1);
        addFormRow(formPanel, "Name:", nameField, 2);
        addFormRow(formPanel, "Brand:", brandField, 3);
        addFormRow(formPanel, "Color:", colorBox, 4);
        addFormRow(formPanel, "Style:", styleBox, 5);
        addFormRow(formPanel, "Condition:", conditionBox, 6);

        final JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(PANEL_BLUE);
        final JButton updateButton = createStyledButton("Update Selected Item");
        final JButton removeButton = createStyledButton("Remove Selected Item");
        final JButton clearButton = createStyledButton("Clear Form");
        final JButton refreshButton = createStyledButton("Refresh Wardrobe");

        updateButton.addActionListener(event -> updateItem());
        removeButton.addActionListener(event -> removeItem());
        clearButton.addActionListener(event -> clearForm());
        refreshButton.addActionListener(event -> refreshTable());

        buttonPanel.add(updateButton);
        buttonPanel.add(removeButton);
        buttonPanel.add(clearButton);
        buttonPanel.add(refreshButton);

        final GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 7;
        constraints.gridwidth = 2;
        constraints.insets = new Insets(8, 4, 4, 4);
        constraints.anchor = GridBagConstraints.CENTER;
        formPanel.add(buttonPanel, constraints);

        return formPanel;
    }

    private JPanel createTablePanel() {
        final JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(PANEL_BLUE);
        tablePanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(TEXT_WHITE),
                "Current Wardrobe",
                0,
                0,
                new Font("SansSerif", Font.BOLD, 14),
                TEXT_WHITE
        ));

        wardrobeTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        wardrobeTable.setBackground(TABLE_ROW_BLUE);
        wardrobeTable.setForeground(Color.BLACK);
        wardrobeTable.setGridColor(PANEL_BLUE);
        wardrobeTable.setRowHeight(24);
        wardrobeTable.getTableHeader().setBackground(TABLE_HEADER_BLUE);
        wardrobeTable.getTableHeader().setForeground(TEXT_WHITE);
        wardrobeTable.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 12));
        wardrobeTable.setSelectionBackground(BUTTON_HOVER_BLUE);
        wardrobeTable.setSelectionForeground(TEXT_WHITE);
        wardrobeTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(javax.swing.JTable table,
                                                           Object value,
                                                           boolean isSelected,
                                                           boolean hasFocus,
                                                           int row,
                                                           int column) {
                final Component component = super.getTableCellRendererComponent(
                        table, value, isSelected, hasFocus, row, column);
                if (!isSelected) {
                    component.setBackground(TABLE_ROW_BLUE);
                    component.setForeground(Color.BLACK);
                }
                return component;
            }
        });
        wardrobeTable.getSelectionModel().addListSelectionListener(event -> {
            if (!event.getValueIsAdjusting()) {
                fillFormFromSelectedRow();
            }
        });

        final JScrollPane scrollPane = new JScrollPane(wardrobeTable);
        scrollPane.getViewport().setBackground(TABLE_ROW_BLUE);
        tablePanel.add(scrollPane, BorderLayout.CENTER);
        return tablePanel;
    }

    private static JButton createStyledButton(String text) {
        final JButton button = new JButton(text);
        button.setBackground(BUTTON_BLUE);
        button.setForeground(TEXT_WHITE);
        button.setFocusPainted(false);
        button.setFont(new Font("SansSerif", Font.BOLD, 12));
        button.setBorder(BorderFactory.createEmptyBorder(6, 12, 6, 12));
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent event) {
                button.setBackground(BUTTON_HOVER_BLUE);
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent event) {
                button.setBackground(BUTTON_BLUE);
            }
        });
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
        labelConstraints.insets = new Insets(4, 4, 4, 8);
        labelConstraints.anchor = GridBagConstraints.EAST;
        final JLabel rowLabel = new JLabel(label);
        rowLabel.setForeground(TEXT_WHITE);
        rowLabel.setFont(new Font("SansSerif", Font.BOLD, 12));
        panel.add(rowLabel, labelConstraints);

        final GridBagConstraints fieldConstraints = new GridBagConstraints();
        fieldConstraints.gridx = 1;
        fieldConstraints.gridy = row;
        fieldConstraints.insets = new Insets(4, 4, 4, 4);
        fieldConstraints.fill = GridBagConstraints.HORIZONTAL;
        fieldConstraints.weightx = 1.0;
        panel.add(field, fieldConstraints);
    }

    private void updateItem() {
        try {
            updaterController.updateItem(
                    uuidField.getText(),
                    getSelected(typeBox),
                    nameField.getText(),
                    brandField.getText(),
                    getSelected(colorBox),
                    getSelected(styleBox),
                    getSelected(conditionBox)
            );
        }
        catch (IllegalArgumentException ex) {
            showMessage("Invalid update input. Please check the UUID and selected fields.");
        }
    }

    private void removeItem() {
        final int result = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to remove this clothing item?",
                "Confirm Removal",
                JOptionPane.YES_NO_OPTION
        );

        if (result != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            removerController.removeItem(uuidField.getText(), getSelected(typeBox));
        }
        catch (IllegalArgumentException ex) {
            showMessage("Invalid remove input. Please check the UUID and item type.");
        }
    }

    private void fillFormFromSelectedRow() {
        final int row = wardrobeTable.getSelectedRow();
        if (row < 0) {
            return;
        }

        uuidField.setText(String.valueOf(tableModel.getValueAt(row, 0)));
        typeBox.setSelectedItem(String.valueOf(tableModel.getValueAt(row, 1)));
        nameField.setText(String.valueOf(tableModel.getValueAt(row, 2)));
        brandField.setText(String.valueOf(tableModel.getValueAt(row, 3)));
        colorBox.setSelectedItem(String.valueOf(tableModel.getValueAt(row, 4)));
        styleBox.setSelectedItem(String.valueOf(tableModel.getValueAt(row, 5)));
        conditionBox.setSelectedItem(String.valueOf(tableModel.getValueAt(row, 6)));
    }

    private void clearForm() {
        uuidField.setText("");
        typeBox.setSelectedIndex(0);
        nameField.setText("");
        brandField.setText("");
        colorBox.setSelectedIndex(0);
        styleBox.setSelectedIndex(0);
        conditionBox.setSelectedIndex(0);
        wardrobeTable.clearSelection();
    }

    /**
     * Refreshes the wardrobe table.
     */
    public void refreshTable() {
        tableModel.setRowCount(0);

        final Wardrobe wardrobe = wardrobeDataAccessObject.fetchWardrobe();
        for (AbstractWear wear : wardrobe.getItems()) {
            tableModel.addRow(new Object[] {
                wear.getUuid(),
                wear.getClass().getSimpleName(),
                wear.getName(),
                wear.getBrand(),
                wear.getColor() == null ? "" : wear.getColor().name(),
                wear.getStyle() == null ? "" : wear.getStyle().name(),
                wear.getCondition() == null ? "" : wear.getCondition().name(),
            });
        }
    }

    private static String getSelected(JComboBox<String> comboBox) {
        return String.valueOf(comboBox.getSelectedItem());
    }

    /**
     * Displays a message to the user.
     *
     * @param message the message
     */
    public void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message);
        refreshTable();
    }
}
