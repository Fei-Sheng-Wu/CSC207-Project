package views;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
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
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import entity.AbstractWear;
import entity.Accessory;
import entity.Bottomwear;
import entity.Footwear;
import entity.Headwear;
import entity.InnerTopwear;
import entity.OuterTopwear;
import entity.Outfit;
import entity.WearColor;
import entity.WearFactory;
import entity.WearStyle;
import interface_adapter.recommendation.RecommendationViewModel;
import interface_adapter.recommendation_context.ContextBasedRecommendationController;
import interface_adapter.recommendation_tag.TagBasedRecommendationController;

/**
 * Represents the recommendation view.
 *
 * <p>The view collects optional colour and style preferences and hands them to the controller.
 * It never asks the interactor for anything and never reads a repository; it simply renders the
 * two properties the presenter writes into the view model.
 */
public class RecommendationView extends AbstractView implements PropertyChangeListener {
    private static final String OPTION_NONE = "(No Preference)";
    private static final String OPTION_CONTEXT_BASED = "Use Current Weather & Events";
    private static final String OPTION_TAG_BASED = "Use My Custom Tags";
    private static final String SLOT_EMPTY = "—";
    private static final String OUTPUT_WORKING = "Looking for an outfit...";
    private static final String OUTPUT_FAILURE = "Something went wrong while looking for an outfit!";
    private static final String[] SLOT_LABELS = {
        "Inner Topwear", "Outer Topwear", "Bottomwear", "Footwear", "Headwear", "Accessories",
    };
    private static final Class<?>[] SLOT_TYPES = {
        InnerTopwear.class, OuterTopwear.class, Bottomwear.class, Footwear.class, Headwear.class, Accessory.class,
    };

    private final RecommendationViewModel viewModel;
    private final ContextBasedRecommendationController contextBasedController;
    private final TagBasedRecommendationController tagBasedController;

    private final JComboBox<String> choiceColor = createChoice(WearColor.values());
    private final JComboBox<String> choiceStyle = createChoice(WearStyle.values());
    private final JComboBox<String> choiceMode = new JComboBox<>(new String[]{OPTION_CONTEXT_BASED, OPTION_TAG_BASED});
    private final JTextField fieldTags = new JTextField();
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
        this.contextBasedController = manager.get(ContextBasedRecommendationController.class);
        this.tagBasedController = manager.get(TagBasedRecommendationController.class);

        // Initialize the layout.
        setLayout(new BorderLayout(SIZE_SPACING_MD, SIZE_SPACING_MD));

        add(createHeader(), BorderLayout.PAGE_START);
        add(createResults(), BorderLayout.CENTER);
    }

    private JPanel createHeader() {
        final JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        header.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, COLOR_BORDER),
            BorderFactory.createEmptyBorder(0, 0, SIZE_SPACING_MD, 0)
        ));

        final JPanel top = new JPanel();
        top.setOpaque(false);
        top.setLayout(new BoxLayout(top, BoxLayout.PAGE_AXIS));
        header.add(top, BorderLayout.PAGE_START);

        final JLabel title = new JLabel("Recommendation");
        title.setFont(FONT_TITLE);
        top.add(title);
        final JLabel subtitle = new JLabel("Let Suitable curate a perfect outfit for you!");
        subtitle.setBorder(BorderFactory.createEmptyBorder(SIZE_SPACING_SM, 0, SIZE_SPACING_MD, 0));
        top.add(subtitle);

        header.add(createControls(), BorderLayout.CENTER);

        return header;
    }

    private JPanel createControls() {
        final JPanel controls = new JPanel();
        controls.setOpaque(false);
        controls.setLayout(new BoxLayout(controls, BoxLayout.PAGE_AXIS));

        final JPanel top = new JPanel(new FlowLayout(FlowLayout.LEADING, SIZE_SPACING_SM, 0));
        top.setOpaque(false);
        top.setBorder(BorderFactory.createEmptyBorder(0, -SIZE_SPACING_SM, 0, -SIZE_SPACING_SM));
        top.add(new JLabel("Color:"));
        top.add(choiceColor);
        top.add(new JLabel("Style:"));
        top.add(choiceStyle);
        controls.add(top);

        final JPanel bottom = new JPanel(new BorderLayout());
        bottom.setOpaque(false);
        controls.add(bottom);

        final JPanel left = new JPanel(new FlowLayout(FlowLayout.LEADING, SIZE_SPACING_SM, 0));
        left.setOpaque(false);
        left.setBorder(BorderFactory.createEmptyBorder(SIZE_SPACING_SM, -SIZE_SPACING_SM, 0, -SIZE_SPACING_SM));
        left.add(new JLabel("Mode:"));
        choiceMode.setPreferredSize(new Dimension(SIZE_WIDTH_XXL, choiceMode.getPreferredSize().height));
        choiceMode.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                switch (selectedMode()) {
                    case OPTION_CONTEXT_BASED:
                        fieldTags.setVisible(false);
                        break;
                    case OPTION_TAG_BASED:
                        fieldTags.setVisible(true);
                        break;
                    default:
                        break;
                }
                left.revalidate();
                left.repaint();
            }
        });
        left.add(choiceMode);
        fieldTags.setPreferredSize(new Dimension(SIZE_WIDTH_XL, choiceMode.getPreferredSize().height));
        fieldTags.setVisible(false);
        left.add(fieldTags);
        bottom.add(left, BorderLayout.CENTER);

        final JButton recommend = new JButton("Get Recommendation");
        recommend.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                requestRecommendation(recommend);
            }
        });
        bottom.add(recommend, BorderLayout.LINE_END);

        return controls;
    }

    private JPanel createResults() {
        final JPanel results = new JPanel(new GridLayout(1, 2, SIZE_SPACING_LG, 0));
        results.setOpaque(false);
        results.add(createSection("Outfit", createSlots()));
        results.add(createSection("Why this outfit?", reason));

        return results;
    }

    private JPanel createSlots() {
        final JPanel slots = new JPanel(new GridLayout(SLOT_LABELS.length, 1, 0, 0));
        slots.setOpaque(false);

        for (int index = 0; index < SLOT_LABELS.length; index++) {
            final JPanel slot = new JPanel(new GridLayout(1, 2, SIZE_SPACING_MD, 0));
            slot.setOpaque(false);
            if (index < SLOT_LABELS.length - 1) {
                slot.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, COLOR_BORDER));
            }
            slots.add(slot);

            final JPanel left = new JPanel(new BorderLayout(SIZE_SPACING_XS, 0));
            left.setOpaque(false);
            slot.add(left);

            final JLabel icon = new JLabel(WearFactory.getIcon(SLOT_TYPES[index]));
            icon.setFont(FONT_EMOJI);
            left.add(icon, BorderLayout.LINE_START);
            left.add(new JLabel(SLOT_LABELS[index]), BorderLayout.CENTER);

            final JLabel value = new JLabel(SLOT_EMPTY);
            slotValues[index] = value;
            slot.add(value);
        }

        return slots;
    }

    private static JPanel createSection(String heading, Component body) {
        final JPanel section = new JPanel(new BorderLayout(0, SIZE_SPACING_MD));
        section.setOpaque(false);

        final JLabel header = new JLabel(heading);
        header.setFont(FONT_SUBTITLE);
        section.add(header, BorderLayout.PAGE_START);
        section.add(body, BorderLayout.CENTER);

        return section;
    }

    private static JTextArea createReason() {
        final JTextArea area = new JTextArea(
            "Please choose your preferences (optional), then press the \"Get Recommendation\" button."
        );
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setEditable(false);
        area.setFocusable(false);
        area.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(COLOR_BORDER, 1),
            BorderFactory.createEmptyBorder(SIZE_SPACING_XS, SIZE_SPACING_XS, SIZE_SPACING_XS, SIZE_SPACING_XS)
        ));

        return area;
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
            }
            options[index + 1] = option;
        }

        final JComboBox<String> combo = new JComboBox<>(options);
        combo.setPreferredSize(new Dimension(SIZE_WIDTH_XL, combo.getPreferredSize().height));

        return combo;
    }

    private static List<String> selectedEnum(JComboBox<String> choice, Object[] values) {
        final List<String> names = new ArrayList<>();
        final int index = choice.getSelectedIndex();
        if (index > 0) {
            names.add(((Enum<?>) values[index - 1]).name());
        }

        return names;
    }

    private String selectedMode() {
        if (choiceMode.getSelectedItem() == null) {
            return OPTION_CONTEXT_BASED;
        }

        return choiceMode.getSelectedItem().toString();
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
            value.setText(SLOT_EMPTY);
        }
    }

    private static String nameOf(AbstractWear item) {
        if (item == null) {
            return SLOT_EMPTY;
        }

        return item.getDisplayString();
    }

    private static String accessoriesOf(Outfit outfit) {
        final List<String> names = new ArrayList<>();
        for (AbstractWear accessory : outfit.getAccessories()) {
            names.add(nameOf(accessory));
        }

        String result = SLOT_EMPTY;
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
        final List<String> colors = selectedEnum(choiceColor, WearColor.values());
        final List<String> styles = selectedEnum(choiceStyle, WearStyle.values());
        final List<String> tags = List.of(fieldTags.getText().split(","));

        trigger.setEnabled(false);
        reason.setText(OUTPUT_WORKING);

        switch (selectedMode()) {
            case OPTION_CONTEXT_BASED:
                BackgroundRequest.run(
                    () -> contextBasedController.recommend(colors, styles),
                    succeeded -> finishRecommendation(trigger, succeeded));
                break;
            case OPTION_TAG_BASED:
                BackgroundRequest.run(
                    () -> tagBasedController.recommend(colors, styles, tags),
                    succeeded -> finishRecommendation(trigger, succeeded));
                break;
            default:
                break;
        }
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
            reason.setText(OUTPUT_FAILURE);
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent e) {
        // The request runs off the event dispatch thread, so the presenter's notification arrives
        // on whichever thread produced it. Swing may only be touched on the event dispatch thread,
        // so the update is handed back to it here, at the last moment before it is applied.
        if (SwingUtilities.isEventDispatchThread()) {
            render(e.getPropertyName());
        } else {
            SwingUtilities.invokeLater(() -> render(e.getPropertyName()));
        }
    }

    private void render(String propertyName) {
        switch (propertyName) {
            case RecommendationViewModel.PROPERTY_RECOMMENDATION:
                showOutfit(viewModel.getOutfit());
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
