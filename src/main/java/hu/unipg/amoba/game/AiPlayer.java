package hu.unipg.amoba.game;

import hu.unipg.amoba.model.Board;
import hu.unipg.amoba.model.Move;
import hu.unipg.amoba.model.Player;
import hu.unipg.amoba.model.Position;

import java.util.List;
import java.util.Random;

public class AiPlayer {
    private final Random rand = new Random();

    public Move pickRandomMove(Board board, Player aiPlayer) {
        List<Position> candidates = board.allEmptyNeighbors();
        if (candidates.isEmpty()) {
            candidates = board.allEmptyCells();
        }
        Position chosen =candidates.get(rand.nextInt(candidates.size()));
        return new Move(chosen,aiPlayer);
    }
}
