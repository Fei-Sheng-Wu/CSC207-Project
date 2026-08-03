package use_case.recommendation_context;

/**
 * Signals that the context a recommendation depends on could not be obtained.
 *
 * <p>The weather and the day's events come from outside the application, so they can be missing
 * for reasons that have nothing to do with the wardrobe: no network, no credentials configured, a
 * service that is refusing requests. Those repositories report such a failure with this exception
 * so that the use case can present it through its output boundary like any other failure, instead
 * of letting an infrastructure error escape to whatever happened to invoke the use case.
 */
public final class ContextUnavailableException extends RuntimeException {
    /**
     * Constructs a new exception.
     *
     * @param message the explanation of the failure
     * @param cause   the underlying failure
     */
    public ContextUnavailableException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs a new exception.
     *
     * @param message the explanation of the failure
     */
    public ContextUnavailableException(String message) {
        super(message);
    }
}
