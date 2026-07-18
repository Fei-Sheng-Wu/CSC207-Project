package app;

import javax.swing.JFrame;

public class Main {
    /**
     * Rune the application.
     *
     * @param args the unused arguments
     */
    public static void main(String[] args) {
        final JFrame application = new Application()
//            .registerView(WardrobeView.class) // @TODO: requires the objects to be implemented
//            .register(WardrobeViewModel.class)
//            .register(AddWearController.class)
//            .registerView(RecommendationView.class)
//            .register(RecommendationViewModel.class)
//            .showView(WardrobeView.class)
            .build();
        application.setVisible(true);
    }
}
