package view;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.io.IOException;
import java.io.InputStream;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.UIManager;

import org.jetbrains.annotations.Nullable;

/**
 * Represents a view of the application.
 */
public abstract class AbstractView extends JPanel {
    protected static final int SIZE_SPACING_XS = 4;
    protected static final int SIZE_SPACING_SM = 8;
    protected static final int SIZE_SPACING_MD = 12;
    protected static final int SIZE_SPACING_LG = 24;
    protected static final int SIZE_SPACING_XL = 32;
    protected static final int SIZE_WIDTH_SM = 24;
    protected static final int SIZE_WIDTH_MD = 48;
    protected static final int SIZE_WIDTH_LG = 96;
    protected static final int SIZE_WIDTH_XL = 128;
    protected static final int SIZE_WIDTH_XXL = 184;
    protected static final int SIZE_HEIGHT_SM = 12;
    protected static final int SIZE_HEIGHT_MD = 24;
    protected static final int SIZE_HEIGHT_LG = 36;
    protected static final int SIZE_HEIGHT_XL = 52;
    protected static final int SIZE_HEIGHT_XXL = 72;

    protected static final Color COLOR_NONE = new Color(0, 0, 0, 0);
    protected static final Color COLOR_BACKGROUND = new Color(251, 251, 251);
    protected static final Color COLOR_AREA = new Color(247, 248, 250);
    protected static final Color COLOR_MUTED = new Color(110, 116, 128);
    protected static final Color COLOR_BORDER = new Color(225, 227, 232);
    protected static final Color COLOR_ERROR = new Color(163, 44, 32);

    protected static final Font FONT_TITLE = loadFont(null, Font.BOLD, 17);
    protected static final Font FONT_SUBTITLE = loadFont(null, Font.BOLD, 15);
    protected static final Font FONT_EMOJI = loadFont("/font_noto_emoji/regular.ttf", Font.PLAIN, 16);

    private final ApplicationManager manager;

    /**
     * Constructs a new view.
     *
     * @param manager the application manager of the view
     */
    public AbstractView(ApplicationManager manager) {
        this.manager = manager;

        setBackground(COLOR_BACKGROUND);
        setBorder(BorderFactory.createEmptyBorder(SIZE_SPACING_MD, SIZE_SPACING_MD, SIZE_SPACING_MD, SIZE_SPACING_MD));
    }

    private static Font loadFont(@Nullable String file, int style, int size) {
        if (file == null) {
            return UIManager.getFont("Label.font").deriveFont(style, size);
        }

        try (InputStream stream = AbstractView.class.getResourceAsStream(file)) {
            if (stream == null) {
                throw new RuntimeException("The resources cannot be loaded.");
            }

            return Font.createFont(Font.TRUETYPE_FONT, stream).deriveFont(style, size);
        } catch (IOException | FontFormatException ex) {
            throw new RuntimeException("The font cannot be loaded.");
        }
    }

    /**
     * Returns the title of the view.
     *
     * @return the title of the view
     */
    public abstract String getTitle();

    /**
     * Returns the application manager of the view.
     *
     * @return the application manager of the view
     */
    public ApplicationManager getApplicationManager() {
        return manager;
    }
}
