package hu.unipg.amoba.model;

import hu.unipg.amoba.game.Game;
import hu.unipg.amoba.io.BoardIO;
import hu.unipg.amoba.io.HighscoreDao;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {
        Scanner sc = new Scanner(System.in);

        HighscoreDao highscoreDao = new HighscoreDao();

        System.out.println("Amőba NxM (4 in a row wins) — Java CLI");
        System.out.print("Játékos neve: ");
        String name = sc.nextLine().trim();
        if (name.isEmpty()) name = "Névtelen";

        Game game;
        Board board;

        System.out.print("Betöltés fájlnév (üres = új 10x10): ");
        String f = sc.nextLine().trim();

        if (!f.isEmpty() && Files.exists(Path.of(f))) {
            try {
                Path filePath = Path.of(f);

                if (f.toLowerCase().endsWith(".json")) {
                    game = BoardIO.loadJson(filePath);
                    board = game.getBoard();
                    System.out.println("Játékállás sikeresen betöltve JSON fájlból.");
                } else {
                    board = BoardIO.load(filePath);
                    game = new Game(board);
                    System.out.println("Pálya betöltve szöveges fájlból.");
                }
            } catch (Exception e) {
                System.out.println("Hiba a betöltésnél, új pálya indul: " + e.getMessage());
                board = new Board(10, 10);
                game = new Game(board);
            }
        } else {
            board = new Board(10,10);
            game = new Game(board);
        }

        Move lastMove = null;

        while (true) {
            System.out.println("\n" + board);

            if (lastMove != null && game.checkWinner(lastMove).isPresent()) {
                Player winner = lastMove.player();
                System.out.println("A JÁTÉK VÉGET ÉRT! Nyertes: " + winner);

                if (winner == Player.X) {
                    highscoreDao.recordWin(name);
                }
                highscoreDao.printHighScores();
                break;
            }

            if (game.getCurrentPlayer() == Player.X) {
                System.out.print("Lépés (pl.f6) vagy 'save <file>' vagy 'quit': ");
                String line = sc.nextLine().trim().toLowerCase();

                if (line.isEmpty()) continue;

                if (line.startsWith("save")) {
                    String[] p = line.split("\\s+", 2);
                    String filename = p.length > 1 ? p[1] : "mentes.json"; // Alapértelmezett mentés JSON-be
                    Path path = Path.of(filename);

                    try {
                        if (filename.toLowerCase().endsWith(".json")) {
                            BoardIO.saveJson(board, game.getCurrentPlayer(), path);
                            System.out.println("Játékállás mentve JSON formátumban ide: " + path);
                        } else {
                            BoardIO.save(board, path);
                            System.out.println("Pálya mentve szövegesen ide: " + path);
                        }
                    } catch (IOException e) {
                        System.out.println("Mentési hiba: " + e.getMessage());
                    }
                    continue;
                } else if (line.equals("quit") || line.equals("kilep")) {
                    System.out.println("Kilépés.");
                    break;
                } else {
                    try {
                        char colCh = line.charAt(0);
                        if (colCh < 'a' || colCh > 'z') {
                            throw new IllegalArgumentException("Az oszlopnak betűnek kell lennie (a-z).");
                        }

                        int col = colCh - 'a';
                        int row = Integer.parseInt(line.substring(1)) - 1;

                        boolean ok = game.playHumanMove(Position.of(row, col));
                        if (!ok) {
                            System.out.println("Érvénytelen lépés! (Szabályok: Kezdés középen, utána érintkezve)");
                        } else {
                            lastMove = new Move(Position.of(row,col), Player.X);
                        }
                    } catch (NumberFormatException ex) {
                        System.out.println("Hibás formátum! Helyes példa: a5, b10");
                    } catch (Exception ex) {
                        System.out.println("Hiba: " + ex.getMessage());
                    }
                }
            } else {
                System.out.println("Gép lép...");
                try {
                    Move aiMove = game.playAIMove();
                    if (aiMove == null) {
                        System.out.println("A gép nem tud lépni (betelt a tábla?). Döntetlen.");
                        break;
                    }
                    System.out.println("Gép lépett: " + (char)('a' + aiMove.pos().col()) + (aiMove.pos().row() + 1));
                    lastMove = aiMove;
                } catch (Exception e) {
                    System.out.println("Hiba a gépi lépés során: " + e.getMessage());
                    break;
                }
            }
        }
        sc.close();
    }
}
