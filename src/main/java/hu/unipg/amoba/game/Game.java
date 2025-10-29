package hu.unipg.amoba.game;

import hu.unipg.amoba.model.Board;
import hu.unipg.amoba.model.Move;
import hu.unipg.amoba.model.Player;
import hu.unipg.amoba.model.Position;

import java.util.Optional;

public class Game {
    private final Board board;
    private Player current;
    private final AiPlayer aiPlayer;

    public Game (Board board) {
        this.board = board;
        this.current=Player.X;
        this.aiPlayer=new AiPlayer();
    }

    public Board getBoard() {
        return board;
    }
    public Player getCurrentPlayer() {
        return current;
    }

    public boolean playHumanMove(Position pos) {
        Move move = new Move(pos, Player.X);
        boolean ok = board.applyMove(move);
        if(ok) current =Player.O;
        return ok;
    }

    public Move playAIMove() {
        Move m= aiPlayer.pickRandomMove(board,Player.O);
        boolean ok = board.applyMove(m);
        if(ok) current =Player.X;
        return ok?m:null;
    }

    public Optional<Player> checkWinner(Move lastMove) {
        if (lastMove == null) return Optional.empty();
        return board.isWinningMove(lastMove) ? Optional.of(lastMove.player()) : Optional.empty();
    }
}
