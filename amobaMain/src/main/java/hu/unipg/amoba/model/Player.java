package hu.unipg.amoba.model;

public enum Player {
    X('x'),
    O('o');
    private final char symbol;
    Player(char symbol) {this.symbol = symbol;}
    public char getSymbol(){return symbol;}
    public Player other(){return this ==X ? O : X;}
}
