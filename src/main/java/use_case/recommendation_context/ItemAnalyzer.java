package use_case.recommendation_context;

import entity.AbstractWear;

/**
 * Evaluates one independent criterion for a single garment.
 *
 * <p>Where an {@link OutfitAnalyzer} judges a finished outfit, this judges one garment at a time.
 * Because the contributions add up, the interactor can rank a wardrobe slot by itself and never
 * has to assemble every combination of slots to find out which garments deserve consideration.
 *
 * <p>An implementation must honour that bargain: a garment's contribution may not depend on what
 * else the outfit contains, and may never be negative. A criterion that breaks either rule — an
 * average, or a rule about how two garments look together — is an {@link OutfitAnalyzer} instead.
 */
public interface ItemAnalyzer {
    /**
     * Evaluates a garment in the current recommendation context.
     *
     * @param item    the garment
     * @param context the current recommendation context
     * @return the garment's contribution to the score of any outfit containing it
     */
    ItemScore analyze(AbstractWear item, RecommendationContext context);
}
