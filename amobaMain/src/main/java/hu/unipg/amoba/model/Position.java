package hu.unipg.amoba.model;

public record Position(int row, int col) {
    public static Position of(int row, int col) {
        return new Position(row, col);
    }
}
