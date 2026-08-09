package views;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.UIManager;

import entity.AbstractWear;
import entity.OutfitIdea;
import interface_adapter.inspiration.InspirationViewModel;
import interface_adapter.inspiration_curator.InspirationCuratorController;

/**
 * Represents the inspiration view.
 */
public class InspirationView extends AbstractView implements PropertyChangeListener {
    private final InspirationViewModel viewModel;
    private final InspirationCuratorController curatorController;

    private final JPanel list;

    private AbstractWear item;

    /**
     * Constructs a new inspiration view.
     *
     * @param manager the application manager of the view
     */
    public InspirationView(ApplicationManager manager) {
        super(manager);

        // Retrieve the shared resources.
        this.viewModel = manager.get(InspirationViewModel.class);
        this.viewModel.addPropertyChangeListener(this);
        this.curatorController = manager.get(InspirationCuratorController.class);

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
    }

    private JPanel createHeader() {
        final JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        header.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, COLOR_BORDER),
            BorderFactory.createEmptyBorder(0, 0, SIZE_SPACING_MD, 0)
        ));

        final JLabel headerTitle = new JLabel("Inspirations");
        headerTitle.setFont(FONT_TITLE);
        header.add(headerTitle, BorderLayout.LINE_START);

        final JButton requery = new JButton("Requery");
        requery.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                curatorController.curate(item);
            }
        });
        header.add(requery, BorderLayout.LINE_END);

        return header;
    }

    private void addCard(OutfitIdea idea) {
        final JPanel card = new JPanel(new BorderLayout());
        card.setOpaque(false);
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, SIZE_HEIGHT_XL));
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, COLOR_BORDER),
            BorderFactory.createEmptyBorder(SIZE_SPACING_SM, SIZE_SPACING_SM, SIZE_SPACING_SM, SIZE_SPACING_SM)
        ));

        final JTextArea label = new JTextArea(idea.getDescription());
        label.setOpaque(false);
        label.setLineWrap(true);
        label.setWrapStyleWord(true);
        label.setEditable(false);
        label.setFocusable(false);
        label.setFont(UIManager.getFont("Label.font"));
        card.add(label, BorderLayout.CENTER);

        final JButton update = new JButton("Open");
        final Component parent = this;
        update.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Desktop.getDesktop().browse(new URI(idea.getUrl()));
                } catch (IOException | URISyntaxException ex) {
                    JOptionPane.showMessageDialog(parent, "Sorry! The URL is invalid.");
                }
            }
        });
        card.add(update, BorderLayout.LINE_END);

        list.add(card);
    }

    @Override
    public String getTitle() {
        return "Inspirations";
    }

    @Override
    public void propertyChange(PropertyChangeEvent e) {
        switch (e.getPropertyName()) {
            case InspirationViewModel.PROPERTY_ERROR:
                if (viewModel.getError() != null && isVisible()) {
                    JOptionPane.showMessageDialog(this, viewModel.getError());
                }
                break;
            case InspirationViewModel.PROPERTY_CURRENT_ITEM:
                item = viewModel.getCurrentItem();
                curatorController.curate(item);
                break;
            case InspirationViewModel.PROPERTY_IDEAS:
                list.removeAll();
                for (OutfitIdea idea : viewModel.getIdeas()) {
                    addCard(idea);
                }
                list.revalidate();
                list.repaint();
                break;
            default:
                break;
        }
    }
}
