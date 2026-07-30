package app;

import java.util.List;

import javax.swing.JFrame;

import view.InspirationView;
import view.ItemEditingView;
import view.RecommendationView;
import view.SettingsView;
import view.WardrobeView;
import view.WelcomeView;

public class Main {
    /**
     * Run the application.
     *
     * @param args the unused arguments
     */
    public static void main(String[] args) {
        final JFrame application = new ApplicationBuilder()
            .registerView(WelcomeView.class)
            .registerView(WardrobeView.class)
            .registerView(ItemEditingView.class)
            .registerView(InspirationView.class)
            .registerView(RecommendationView.class)
            .registerView(SettingsView.class)
            .setTopNavigations(List.of(
                WelcomeView.class,
                WardrobeView.class,
                RecommendationView.class
            ))
            .setBottomNavigations(List.of(
                SettingsView.class
            ))
            .setInitialView(WelcomeView.class)
            .build();
        application.setVisible(true);
    }
}
