package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

import entity.AbstractWear;
import entity.Outfit;
import entity.WearColor;
import entity.WearStyle;
import interface_adapter.recommendation.RecommendationViewModel;
import interface_adapter.recommendation_context.ContextBasedRecommendationController;

/**
 * Represents the recommendation view.
 *
 * <p>The view collects optional colour and style preferences and hands them to the controller.
 * It never asks the interactor for anything and never reads a repository; it simply renders the
 * two properties the presenter writes into the view model.
 */
public class RecommendationView extends AbstractView implements PropertyChangeListener {
    private static final String ANY_OPTION = "No preference";
    private static final String EMPTY_SLOT = "\u2014";
    private static final String PROMPT = "Choose optional preferences, then press Get recommendation.";
    private static final String WORKING = "Looking for an outfit...";
    private static final String UNEXPECTED_FAILURE = "Something went wrong while looking for an outfit.";
    private static final String[] SLOT_LABELS = {
        "Inner top", "Outer top", "Bottom", "Footwear", "Headwear", "Accessories",
    };

    private static final Color COLOR_MUTED = new Color(110, 116, 128);
    private static final Color COLOR_LINE = new Color(214, 216, 220);
    private static final Color COLOR_ERROR = new Color(163, 44, 32);
    private static final float FONT_SIZE_TITLE = 22f;
    private static final float FONT_SIZE_SECTION = 13f;
    private static final int REASON_ROWS = 8;
    private static final int REASON_COLUMNS = 24;

    private final RecommendationViewModel viewModel;
    private final ContextBasedRecommendationController controller;

    private final JComboBox<String> colorChoice = createChoice(WearColor.values());
    private final JComboBox<String> styleChoice = createChoice(WearStyle.values());
    private final JLabel[] slotValues = new JLabel[SLOT_LABELS.length];
    private final JTextArea reason = createReason();

    /**
     * Constructs a new recommendation view.
     *
     * @param manager the application manager of the view
     */
    public RecommendationView(ApplicationManager manager) {
        super(manager);

        // Retrieve the shared resources.
        this.viewModel = manager.get(RecommendationViewModel.class);
        this.viewModel.addPropertyChangeListener(this);
        this.controller = manager.get(ContextBasedRecommendationController.class);

        // Initialize the layout.
        setLayout(new BorderLayout(SIZE_SPACING_LG, SIZE_SPACING_LG));
        setBorder(BorderFactory.createEmptyBorder(
            SIZE_SPACING_LG, SIZE_SPACING_LG, SIZE_SPACING_LG, SIZE_SPACING_LG));

        add(createHeader(), BorderLayout.NORTH);
        add(createResults(), BorderLayout.CENTER);
    }

    private JPanel createHeader() {
        final JPanel header = new JPanel();
        header.setOpaque(false);
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, COLOR_LINE),
            BorderFactory.createEmptyBorder(0, 0, SIZE_SPACING_MD, 0)));

        final JLabel title = new JLabel(getTitle());
        title.setFont(title.getFont().deriveFont(Font.BOLD, FONT_SIZE_TITLE));
        title.setAlignmentX(LEFT_ALIGNMENT);

        final JLabel subtitle = new JLabel("An outfit from your wardrobe for today's weather and events.");
        subtitle.setForeground(COLOR_MUTED);
        subtitle.setAlignmentX(LEFT_ALIGNMENT);
        subtitle.setBorder(BorderFactory.createEmptyBorder(SIZE_SPACING_XS, 0, SIZE_SPACING_MD, 0));

        header.add(title);
        header.add(subtitle);
        header.add(createControls());

        return header;
    }

    private JPanel createControls() {
        final JPanel controls = new JPanel(new BorderLayout(SIZE_SPACING_MD, 0));
        controls.setOpaque(false);
        controls.setAlignmentX(LEFT_ALIGNMENT);

        final JPanel choices = new JPanel(new FlowLayout(FlowLayout.LEFT, SIZE_SPACING_SM, 0));
        choices.setOpaque(false);
        choices.add(createHeadingLabel("Colour"));
        choices.add(colorChoice);
        choices.add(createHeadingLabel("Style"));
        choices.add(styleChoice);

        final JButton recommend = new JButton("Get recommendation");
        recommend.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                requestRecommendation(recommend);
            }
        });

        controls.add(choices, BorderLayout.CENTER);
        controls.add(recommend, BorderLayout.EAST);

        return controls;
    }

    private JPanel createResults() {
        final JPanel results = new JPanel(new GridLayout(1, 2, SIZE_SPACING_XL, 0));
        results.setOpaque(false);
        results.add(createSection("Outfit", createSlots()));
        results.add(createSection("Why this outfit?", reason));

        return results;
    }

    private JPanel createSlots() {
        final JPanel slots = new JPanel(new GridLayout(SLOT_LABELS.length, 2, SIZE_SPACING_MD, 0));
        slots.setOpaque(false);

        for (int index = 0; index < SLOT_LABELS.length; index++) {
            final JLabel name = new JLabel(SLOT_LABELS[index]);
            name.setForeground(COLOR_MUTED);
            name.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, COLOR_LINE));

            final JLabel value = new JLabel(EMPTY_SLOT);
            value.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, COLOR_LINE));
            slotValues[index] = value;

            slots.add(name);
            slots.add(value);
        }

        return slots;
    }

    private static JPanel createSection(String heading, Component body) {
        final JPanel section = new JPanel(new BorderLayout(0, SIZE_SPACING_MD));
        section.setOpaque(false);
        section.add(createHeadingLabel(heading), BorderLayout.NORTH);
        section.add(body, BorderLayout.CENTER);

        return section;
    }

    private static JLabel createHeadingLabel(String text) {
        final JLabel label = new JLabel(text);
        label.setFont(label.getFont().deriveFont(Font.BOLD, FONT_SIZE_SECTION));

        return label;
    }

    private static JTextArea createReason() {
        final JTextArea area = new JTextArea(REASON_ROWS, REASON_COLUMNS);
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setEditable(false);
        area.setFocusable(false);
        area.setOpaque(false);
        area.setForeground(COLOR_MUTED);
        area.setText(PROMPT);

        return area;
    }

    private static JComboBox<String> createChoice(Object[] values) {
        final String[] options = new String[values.length + 1];
        options[0] = ANY_OPTION;
        for (int index = 0; index < values.length; index++) {
            options[index + 1] = displayNameOf(values[index]);
        }

        return new JComboBox<>(options);
    }

    private static String displayNameOf(Object value) {
        String result = value.toString();
        if (value instanceof WearColor) {
            result = ((WearColor) value).getDisplayName();
        }
        else if (value instanceof WearStyle) {
            result = ((WearStyle) value).getDisplayName();
        }

        return result;
    }

    private static List<String> selected(JComboBox<String> choice, Object[] values) {
        final List<String> names = new ArrayList<>();
        final int index = choice.getSelectedIndex();
        if (index > 0) {
            names.add(((Enum<?>) values[index - 1]).name());
        }

        return names;
    }

    private void showOutfit(Outfit outfit) {
        final String[] values = {
            nameOf(outfit.getTopwearInner()),
            nameOf(outfit.getTopwearOuter()),
            nameOf(outfit.getBottomwear()),
            nameOf(outfit.getFootwear()),
            nameOf(outfit.getHeadwear()),
            accessoriesOf(outfit),
        };
        for (int index = 0; index < slotValues.length; index++) {
            slotValues[index].setText(values[index]);
        }
    }

    private void clearOutfit() {
        for (JLabel value : slotValues) {
            value.setText(EMPTY_SLOT);
        }
    }

    private static String nameOf(AbstractWear item) {
        String result = EMPTY_SLOT;
        if (item != null) {
            result = item.getName();
            if (result == null || result.isBlank()) {
                result = item.getClass().getSimpleName();
            }
        }

        return result;
    }

    private static String accessoriesOf(Outfit outfit) {
        final List<String> names = new ArrayList<>();
        for (AbstractWear accessory : outfit.getAccessories()) {
            names.add(nameOf(accessory));
        }

        String result = EMPTY_SLOT;
        if (!names.isEmpty()) {
            result = String.join(", ", names);
        }

        return result;
    }

    @Override
    public String getTitle() {
        return "Recommendation";
    }

    /**
     * Asks for a recommendation without occupying the event dispatch thread.
     *
     * <p>The use case reads today's weather and events over the network before it so much as looks
     * at the wardrobe. Doing that on the event dispatch thread would freeze the window for as long
     * as those services take to answer, and would leave no thread free to paint a notice saying so
     * — the progress message below only appears because this returns immediately.
     *
     * @param trigger the button that asked for the recommendation
     */
    private void requestRecommendation(JButton trigger) {
        final List<String> colors = selected(colorChoice, WearColor.values());
        final List<String> styles = selected(styleChoice, WearStyle.values());

        trigger.setEnabled(false);
        reason.setForeground(COLOR_MUTED);
        reason.setText(WORKING);

        BackgroundRequest.run(
            () -> controller.recommend(colors, styles),
            succeeded -> finishRecommendation(trigger, succeeded));
    }

    /**
     * Re-admits further requests, and reports a failure the use case could not present itself.
     *
     * @param trigger   the button that asked for the recommendation
     * @param succeeded whether the request completed without throwing
     */
    private void finishRecommendation(JButton trigger, boolean succeeded) {
        trigger.setEnabled(true);
        if (!succeeded) {
            reason.setForeground(COLOR_ERROR);
            reason.setText(UNEXPECTED_FAILURE);
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent e) {
        // The request runs off the event dispatch thread, so the presenter's notification arrives
        // on whichever thread produced it. Swing may only be touched on the event dispatch thread,
        // so the update is handed back to it here, at the last moment before it is applied.
        if (SwingUtilities.isEventDispatchThread()) {
            render(e.getPropertyName());
        }
        else {
            SwingUtilities.invokeLater(() -> render(e.getPropertyName()));
        }
    }

    private void render(String propertyName) {
        switch (propertyName) {
            case RecommendationViewModel.PROPERTY_RECOMMENDATION:
                showOutfit(viewModel.getOutfit());
                reason.setForeground(COLOR_MUTED);
                reason.setText(viewModel.getReason());
                break;
            case RecommendationViewModel.PROPERTY_ERROR_MESSAGE:
                clearOutfit();
                reason.setForeground(COLOR_ERROR);
                reason.setText(viewModel.getErrorMessage());
                break;
            default:
                break;
        }
    }
}
