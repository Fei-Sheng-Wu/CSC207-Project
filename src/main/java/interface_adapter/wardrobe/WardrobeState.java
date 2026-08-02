package interface_adapter.wardrobe;

public class WardrobeState {
    private int totalClothesCount = 0;
    private int oldClothesCount = 0;
    private int laundryNeededCount = 0;
    private String reportError = null;

    public int getTotalClothesCount() {
        return totalClothesCount;
    }

    public void setTotalClothesCount(int totalClothesCount) {
        this.totalClothesCount = totalClothesCount;
    }

    public int getOldClothesCount() {
        return oldClothesCount;
    }

    public void setOldClothesCount(int oldClothesCount) {
        this.oldClothesCount = oldClothesCount;
    }

    public int getLaundryNeededCount() {
        return laundryNeededCount;
    }

    public void setLaundryNeededCount(int laundryNeededCount) {
        this.laundryNeededCount = laundryNeededCount;
    }

    public String getReportError() {
        return reportError;
    }

    public void setReportError(String reportError) {
        this.reportError = reportError;
    }
}
