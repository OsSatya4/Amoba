package org;

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
            try {
                board = BoardIO.load(Path.of(f));
                System.out.println("Pálya betöltve.");
            } catch (Exception e) {
                System.out.println("Hiba a betöltésnél, új pálya indul.");
                board = new Board(10, 10);
            }
        } else {
            board = new Board(10,10); // default
        }

        Game game = new Game(board);
        Move lastMove = null;

        while (true) {
            System.out.println("\n" + board);

            // Ellenőrizzük, van-e nyertes vagy betelt-e a pálya
            if (lastMove != null && game.checkWinner(lastMove).isPresent()) {
                System.out.println("A JÁTÉK VÉGET ÉRT! Nyertes: " + lastMove.player());
                break;
            }

            if (game.getCurrentPlayer() == Player.X) {
                System.out.print("Lépés (pl.f6) vagy 'save <file>' vagy 'quit': ");
                // Itt a javítás: toLowerCase(), hogy az 'A5' is működjön
                String line = sc.nextLine().trim().toLowerCase();

                if (line.isEmpty()) continue;

                if (line.startsWith("save")) {
                    String[] p = line.split("\\s+",2);
                    Path path = Path.of(p.length > 1 ? p[1] : "board.txt");
                    try {
                        BoardIO.save(board, path);
                        System.out.println("Mentve ide: " + path);
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
                        System.out.println("A gép nem tud lépni (betelt a tábla?). Vége.");
                        break;
                    }
                    System.out.println("Gép lépett: " + (char)('a' + aiMove.pos().col()) + (aiMove.pos().row() + 1));
                    lastMove = aiMove;
                } catch (Exception e) {
                    System.out.println("Hiba a gépi lépés során: " + e.getMessage());
                    e.printStackTrace();
                    break;
                }
            }
        }
        sc.close();
    }
}