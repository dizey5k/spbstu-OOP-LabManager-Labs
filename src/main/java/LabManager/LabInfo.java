package LabManager;

public record LabInfo(
        int number,
        String name,
        String description
) {
    @Override
    public String toString() {
        return String.format("%d. %s - %s", number, name, description);
    }
}
