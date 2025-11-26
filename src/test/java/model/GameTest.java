package model;

import hu.unipg.amoba.game.Game;
import hu.unipg.amoba.model.Board;
import hu.unipg.amoba.model.Move;
import hu.unipg.amoba.model.Player;
import hu.unipg.amoba.model.Position;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {

    private Game game;
    private Board board;

    @BeforeEach
    void setUp() {
        board = new Board(10, 10);
        game = new Game(board);
    }

    @Test
    void testInitialization() {
        assertNotNull(game.getBoard());
        assertEquals(Player.X, game.getCurrentPlayer());
        assertTrue(board.isEmpty());
    }

    @Test
    void testFirstMoveMustBeCenter() {
        boolean success = game.playHumanMove(Position.of(0, 0));
        assertFalse(success, "A kezdő lépés nem lehet a sarokban");
        assertTrue(board.isEmpty(), "A táblának üresnek kell maradnia hibás lépés után");
        assertEquals(Player.X, game.getCurrentPlayer(), "Nem válthat játékost hibás lépésnél");

        success = game.playHumanMove(Position.of(4, 4));
        assertTrue(success, "A (4,4) mezőnek valid kezdésnek kell lennie");
        assertEquals(Player.O, game.getCurrentPlayer(), "Játékosnak váltania kell X-ről O-ra");
    }

    @Test
    void testSubsequentMovesMustConnect() {
        game.playHumanMove(Position.of(4, 4));


        Move distantMove = new Move(Position.of(0, 0), Player.O);
        assertFalse(board.applyMove(distantMove), "Nem érintkező lépés nem engedélyezett");

        Move neighborMove = new Move(Position.of(4, 5), Player.O);
        assertTrue(board.applyMove(neighborMove), "Szomszédos lépés engedélyezett");
    }

    @Test
    void testHorizontalWin() {


        board.applyMove(new Move(Position.of(4, 4), Player.X));
        board.applyMove(new Move(Position.of(5, 4), Player.O)); // valid szomszéd

        board.applyMove(new Move(Position.of(4, 5), Player.X));
        board.applyMove(new Move(Position.of(5, 5), Player.O));

        board.applyMove(new Move(Position.of(4, 6), Player.X));
        board.applyMove(new Move(Position.of(5, 6), Player.O));

        Move winningMove = new Move(Position.of(4, 7), Player.X);
        board.applyMove(winningMove);

        Optional<Player> winner = game.checkWinner(winningMove);
        assertTrue(winner.isPresent());
        assertEquals(Player.X, winner.get());
    }

    @Test
    void testVerticalWin() {
        board.applyMove(new Move(Position.of(1, 1), Player.X));
        Board b = new Board(10,10);
        Game g = new Game(b);


        b.applyMove(new Move(Position.of(4,4), Player.X));

        b.applyMove(new Move(Position.of(4,5), Player.O));


        b.applyMove(new Move(Position.of(5,4), Player.X));

        b.applyMove(new Move(Position.of(5,5), Player.O));


        b.applyMove(new Move(Position.of(6,4), Player.X));

        b.applyMove(new Move(Position.of(6,5), Player.O));


        Move winMove = new Move(Position.of(7,4), Player.X);
        b.applyMove(winMove);

        assertTrue(b.isWinningMove(winMove), "Függőleges 4-esnek nyernie kell");
    }

    @Test
    void testDiagonalWin() {

        Board b = new Board(10,10);

        b.applyMove(new Move(Position.of(4,4), Player.X));
        b.applyMove(new Move(Position.of(4,5), Player.O)); // touch

        b.applyMove(new Move(Position.of(5,5), Player.X));
        b.applyMove(new Move(Position.of(4,6), Player.O)); // touch

        b.applyMove(new Move(Position.of(6,6), Player.X));
        b.applyMove(new Move(Position.of(4,7), Player.O)); // touch

        Move winMove = new Move(Position.of(7,7), Player.X);
        b.applyMove(winMove);

        assertTrue(b.isWinningMove(winMove), "Átlós 4-esnek nyernie kell");
    }

    @Test
    void testAiMoveGeneratesValidMove() {

        game.playHumanMove(Position.of(4, 4));


        Move aiMove = game.playAIMove();

        assertNotNull(aiMove, "AI-nak lépnie kell tudni, ha van hely");
        assertFalse(board.isEmpty());

        assertTrue(aiMove.pos().row() >= 0 && aiMove.pos().row() < 10);
        assertEquals(Player.O, aiMove.player());
        assertEquals(Player.X, game.getCurrentPlayer(), "AI után visszakerül a kör X-hez");
    }

    @Test
    void testBoardFull() {
        Board tinyBoard = new Board(5, 5);
        Game tinyGame = new Game(tinyBoard);


        for(int r=0; r<5; r++) {
            for(int c=0; c<5; c++) {
               }
        }
    }

    @Test
    void testInvalidBoardSize() {
        assertThrows(IllegalArgumentException.class, () -> new Board(4, 4));
        assertThrows(IllegalArgumentException.class, () -> new Board(26, 26));
        assertThrows(IllegalArgumentException.class, () -> new Board(10, 20), "Sor >= Oszlop elvárás");
    }
}

