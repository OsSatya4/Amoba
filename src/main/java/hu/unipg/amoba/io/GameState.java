package hu.unipg.amoba.io;

public class GameState {
    public int rows;
    public int cols;
    public char[][] grid;
    public char currentPlayer;
    public GameState() {}

    public GameState(int rows, int cols, char[][] grid, char currentPlayer) {
        this.rows = rows;
        this.cols = cols;
        this.grid = grid;
        this.currentPlayer = currentPlayer;
    }
}