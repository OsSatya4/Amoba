package hu.unipg.amoba.model;

public final class Cell {

    private Player owner;

    public Cell() {this.owner = null;}

    public boolean isEmpty(){return owner == null;}

    public Player getOwner(){return owner;}

    public void setOwner(Player p){this.owner = p;}

    @Override
    public String toString(){
        return isEmpty() ? "." : String.valueOf(owner.getSymbol());
    }
}
