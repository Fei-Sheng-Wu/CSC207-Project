package use_case.wardrobe_analyzer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import entity.AbstractWear;
import entity.Wardrobe;
import use_case.wardrobe.WardrobeDataAccessInterface;

public class WardrobeAnalyzerInteractor implements WardrobeAnalyzerInputBoundary {
    private final WardrobeDataAccessInterface repository;
    private final WardrobeAnalyzerOutputBoundary outputBoundary;

    public WardrobeAnalyzerInteractor(WardrobeDataAccessInterface repository,
                                      WardrobeAnalyzerOutputBoundary outputBoundary) {
        this.repository = repository;
        this.outputBoundary = outputBoundary;
    }

    @Override
    public void analyze() {
        final Wardrobe wardrobe = repository.fetchWardrobe();
        final List<AbstractWear> items = wardrobe.getItems();

        if (wardrobe == null || wardrobe.getItems() == null) {
            outputBoundary.prepareFailView("Failed to load wardrobe data for analysis.");
            return;
        }

        final Map<String, Object> resultsMap = new HashMap<>();
        final List<WardrobeStatistic> statistics = AnalyzerFactory.createStatistics();

        for (WardrobeStatistic statistic : statistics) {
            statistic.calculate(items, resultsMap);
        }
        final WardrobeAnalyzerOutputData outputData = new WardrobeAnalyzerOutputData(resultsMap);
        outputBoundary.prepareSuccessView(outputData);
    }
}
