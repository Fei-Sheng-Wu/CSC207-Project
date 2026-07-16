package use_case.item_action;

/**
 * Output data for actions performed on a wardrobe item.
 */
public class ItemActionResponse {
    private final boolean successful;
    private final String message;

    public ItemActionResponse(boolean successful, String message) {
        this.successful = successful;
        this.message = message;
    }

    public boolean isSuccessful() {
        return successful;
    }

    public String getMessage() {
        return message;
    }
}
