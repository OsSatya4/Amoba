package hu.unipg.amoba.io;

import hu.unipg.amoba.model.Board;
import hu.unipg.amoba.model.Move;
import hu.unipg.amoba.model.Player;
import hu.unipg.amoba.model.Position;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

public class BoardIO {

    public static void save(Board board, Path file) throws IOException {
        try (BufferedWriter w = Files.newBufferedWriter(file)) {
            w.write(board.getRows() + " " + board.getCols());
            w.newLine();
            for (int r=0;r<board.getRows();r++) {
                for (int c=0;c<board.getCols();c++) {
                    Optional<Player> p = board.getOwner(r,c);
                    w.write(String.valueOf(p.map(Player::getSymbol).orElse('.')));
                }
                w.newLine();
            }
        }
    }

    public static Board load(Path file) throws IOException {
        List<String> lines = Files.readAllLines(file);
        if (lines.isEmpty()) throw new IOException("Empty file");
        String[] parts = lines.get(0).trim().split("\\s+");
        int rows = Integer.parseInt(parts[0]);
        int cols = Integer.parseInt(parts[1]);
        Board b = new Board(rows, cols);
        for (int r=0;r<rows && r+1<lines.size(); r++) {
            String line = lines.get(r+1);
            for (int c=0;c<cols && c<line.length(); c++) {
                char ch = line.charAt(c);
                if (ch == 'x' || ch == 'X') b.applyMove(new Move(new Position(r,c), Player.X));
                else if (ch == 'o' || ch == 'O') b.applyMove(new Move(new Position(r,c), Player.O));
            }
        }
        return b;
    }
}
