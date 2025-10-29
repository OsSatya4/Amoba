package hu.unipg.amoba.model;

import java.util.*;
import java.util.stream.Collectors;

public class Board {
    private final int rows;
    private final int cols;
    private final Cell[][] grid;

    public Board(int rows, int cols) {
        if(cols<5||rows<5||cols>rows||rows>25) throw new IllegalArgumentException("5 <= M <= N <= 25");
        this.rows = rows;
        this.cols = cols;
        this.grid = new Cell[rows][cols];
        for(int r=0;r<rows;r++){
            for(int c=0;c<cols;c++){
                grid[r][c] = new Cell();
            }
        }
    }

    public int getRows() {
        return rows;
    }
    public int getCols() {
        return cols;
    }

    public Optional<Player> getOwner(int r, int c){
        checkBounds(r,c);
        return grid[r][c].isEmpty() ? Optional.empty() : Optional.of(grid[r][c].getOwner());
    }

    private void checkBounds(int r, int c){
        if (r<0||c<0||r>=rows||c>=cols) throw new IndexOutOfBoundsException();
    }

    public boolean isEmpty(){
        for(int r=0; r<rows;r++){
            for(int c=0;c<cols;c++){
                if(!grid[r][c].isEmpty())
                    return false;
            }
        }
        return true;
    }

    public boolean applyMove(Move move) {
        Position p = move.pos();
        int r = p.row(); int c = p.col();
        checkBounds(r,c);
        if (!grid[r][c].isEmpty()) return false;
        if (isEmpty()) {
            int midR = rows/2; int midC = cols/2;
            if (!(r==midR && c==midC)) return false;
        } else {
            boolean touches = false;
            for (int dr=-1; dr<=1; dr++) for (int dc=-1; dc<=1; dc++) {
                if (dr==0 && dc==0) continue;
                int nr=r+dr, nc=c+dc;
                if (nr>=0 && nc>=0 && nr<rows && nc<cols && !grid[nr][nc].isEmpty()) touches = true;
            }
            if (!touches) return false;
        }
        grid[r][c].setOwner(move.player());
        return true;
    }

    public boolean isWinningMove(Move move) {
        Position p = move.pos();
        Player player = move.player();
        int r=p.row(), c=p.col();
        int[][] deltas = {{0,1},{1,0},{1,1},{1,-1}};
        for (var d : deltas) {
            int count = 1;
            count += countDir(r,c,d[0],d[1],player);
            count += countDir(r,c,-d[0],-d[1],player);
            if (count >= 4) return true;
        }
        return false;
    }

    private int countDir(int r,int c,int dr,int dc,Player player){
        int cnt=0;
        int nr=r+dr, nc=c+dc;
        while (nr>=0 && nc>=0 && nr<rows && nc<cols) {
            Player owner = grid[nr][nc].getOwner();
            if (owner == player) { cnt++; nr+=dr; nc+=dc; } else break;
        }
        return cnt;
    }

    public List<Position> allEmptyNeighbors() {
        Set<Position> result = new HashSet<>();
        for (int r=0;r<rows;r++) for (int c=0;c<cols;c++) {
            if (!grid[r][c].isEmpty()) {
                for (int dr=-1; dr<=1; dr++) for (int dc=-1; dc<=1; dc++) {
                    int nr=r+dr, nc=c+dc;
                    if (nr>=0 && nc>=0 && nr<rows && nc<cols && grid[nr][nc].isEmpty()) {
                        result.add(Position.of(nr,nc));
                    }
                }
            }
        }

        if (result.isEmpty() && isEmpty()) {
            int midR = rows/2, midC=cols/2;
            result.add(Position.of(midR,midC));
        }
        return new ArrayList<>(result);
    }

    public List<Position> allEmptyCells(){
        List<Position> out = new ArrayList<>();
        for (int r=0;r<rows;r++) for (int c=0;c<cols;c++) if (grid[r][c].isEmpty()) out.add(Position.of(r,c));
        return out;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("   ");
        for (int c=0;c<cols;c++) sb.append((char)('a'+c)).append(' ');
        sb.append('\n');
        for (int r=0;r<rows;r++) {
            sb.append(String.format("%2d ", r+1));
            for (int c=0;c<cols;c++) sb.append(grid[r][c].toString()).append(' ');
            sb.append('\n');
        }
        return sb.toString();
    }




}
