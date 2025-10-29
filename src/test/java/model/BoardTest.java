package model;

import hu.unipg.amoba.model.Board;
import hu.unipg.amoba.model.Move;
import hu.unipg.amoba.model.Player;
import hu.unipg.amoba.model.Position;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class BoardTest {

    @Test
    void fourInRowHorizontalWins() {
        Board b = new Board(6,6);

        for (int c=1;c<=4;c++) {
            boolean applied = b.applyMove(new Move(Position.of(1,c), Player.X));
            assertTrue(applied);
        }

        assertTrue(b.isWinningMove(new Move(Position.of(1,3), Player.X)));
    }

    @Test
    void initialMoveNotAllowedNotMiddle() {
        Board b = new Board(7,7);
        boolean ok = b.applyMove(new Move(Position.of(0,0), Player.X));
        assertFalse(ok);
        // allowed middle:
        int midR = b.getRows()/2, midC = b.getCols()/2;
        assertTrue(b.applyMove(new Move(Position.of(midR, midC), Player.X)));
    }
}

