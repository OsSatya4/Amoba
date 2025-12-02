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
        Board b = new Board(10, 10);

        b.applyMove(new Move(Position.of(4, 4), Player.X));

        b.applyMove(new Move(Position.of(5, 5), Player.O));


        assertTrue(b.applyMove(new Move(Position.of(4, 5), Player.X))); // X 2.
        assertTrue(b.applyMove(new Move(Position.of(5, 6), Player.O))); // O
        assertTrue(b.applyMove(new Move(Position.of(4, 6), Player.X))); // X 3.
        assertTrue(b.applyMove(new Move(Position.of(5, 7), Player.O))); // O

        Move winningMove = new Move(Position.of(4, 7), Player.X);
        assertTrue(b.applyMove(winningMove), "A nyerő lépésnek érvényesnek kell lennie.");

        assertTrue(b.isWinningMove(winningMove), "Vízszintes 4-esnek nyernie kell.");
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

