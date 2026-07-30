package view;

import java.awt.GridBagLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JLabel;

import interface_adapter.recommendation.RecommendationViewModel;

/**
 * Represents the recommendation view.
 */
public class RecommendationView extends AbstractApplicationView implements PropertyChangeListener {
    private final RecommendationViewModel viewModel;

    /**
     * Constructs a new recommendation view.
     *
     * @param manager the application manager of the view
     */
    public RecommendationView(ApplicationManager manager) {
        super(manager);

        this.viewModel = new RecommendationViewModel();
        this.viewModel.addPropertyChangeListener(this);
        manager.register(RecommendationViewModel.class, this.viewModel);

        setLayout(new GridBagLayout());

        add(new JLabel("@TODO: recommendation view"));
    }

    @Override
    public String getTitle() {
        return "Recommendation";
    }

    @Override
    public void propertyChange(PropertyChangeEvent e) {

    }
}
