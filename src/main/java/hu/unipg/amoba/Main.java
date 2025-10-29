package hu.unipg.amoba;

import hu.unipg.amoba.game.Game;
import hu.unipg.amoba.io.BoardIO;
import hu.unipg.amoba.model.Board;
import hu.unipg.amoba.model.Move;
import hu.unipg.amoba.model.Player;
import hu.unipg.amoba.model.Position;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;


public class Main {
    public static void main(String[] args) throws IOException {
        Scanner sc = new Scanner(System.in);
        System.out.println("Amőba NxM (4 in a row wins) — Java CLI");
        System.out.print("Játékos neve: ");
        String name = sc.nextLine().trim();

        Board board;
        System.out.print("Betöltés fájlnév (üres = új 10x10): ");
        String f = sc.nextLine().trim();
        if (!f.isEmpty() && Files.exists(Path.of(f))) {
            board=BoardIO.load(Path.of(f));
        } else {
            board = new Board(10,10); // default
        }

        Game game = new Game(board);
        Move lastMove = null;

        while (true) {
            System.out.println(board);
            if (game.getCurrentPlayer() == Player.X) {
                System.out.print("Lépés (pl a5) vagy 'save <file>' vagy 'quit': ");
                String line = sc.nextLine().trim();
                if (line.startsWith("save")) {
                    String[] p = line.split("\\s+",2);
                    BoardIO.save(board, Path.of(p.length>1?p[1]:"board.txt"));
                    System.out.println("Mentve.");
                    continue;
                } else if (line.equals("quit")) {
                    System.out.println("Kilépés.");
                    break;
                } else {
                    // parse e.g. "a5" -> col 'a' -> 0, row 5 -> index 4
                    try {
                        char colCh = line.charAt(0);
                        int col = colCh - 'a';
                        int row = Integer.parseInt(line.substring(1)) - 1;
                        boolean ok = game.playHumanMove(Position.of(row, col));
                        if (!ok) {
                            System.out.println("Érvénytelen lépés!");
                        } else {
                            lastMove = new Move(Position.of(row,col), Player.X);
                            if (board.isWinningMove(lastMove)) {
                                System.out.println(board);
                                System.out.println("Nyert: " + Player.X);
                                break;
                            }
                        }
                    } catch (Exception ex) {
                        System.out.println("Hibás input.");
                    }
                }
            } else {
                System.out.println("Gép lép...");
                Move aiMove = game.playAIMove();
                System.out.println("Gép lépett: " + (char)('a'+aiMove.pos().col()) + (aiMove.pos().row()+1));
                lastMove = aiMove;
                if (board.isWinningMove(aiMove)) {
                    System.out.println(board);
                    System.out.println("Nyert: " + Player.O);
                    break;
                }
            }
        }
        sc.close();
    }
}