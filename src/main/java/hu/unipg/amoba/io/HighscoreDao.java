package hu.unipg.amoba.io;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HighscoreDao {

    // Fájl alapú H2 adatbázis URL. A ./gomoku_db azt jelenti, hogy a projekt gyökerében jön létre.
    // Ez a fájl a "amobaMain" mappában fog megjelenni "gomoku_db.mv.db" néven.
    private static final String DB_URL = "jdbc:h2:./gomoku_db";
    private static final String USER = "sa";
    private static final String PASS = "";

    public HighscoreDao() {
        // Konstruktorban biztosítjuk, hogy a tábla létezzen
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             Statement stmt = conn.createStatement()) {

            String sql = "CREATE TABLE IF NOT EXISTS HIGHSCORE " +
                    "(NAME VARCHAR(255) PRIMARY KEY, " +
                    " WINS INTEGER DEFAULT 0)";
            stmt.executeUpdate(sql);

        } catch (SQLException e) {
            System.err.println("Adatbázis inicializálási hiba: " + e.getMessage());
        }
    }


    public void recordWin(String playerName) {
        String mergeSql = "MERGE INTO HIGHSCORE (NAME, WINS) KEY (NAME) " +
                "VALUES (?, COALESCE((SELECT WINS FROM HIGHSCORE WHERE NAME = ?), 0) + 1)";

        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement pstmt = conn.prepareStatement(mergeSql)) {

            pstmt.setString(1, playerName);
            pstmt.setString(2, playerName);
            pstmt.executeUpdate();
            System.out.println("Eredmény mentve az adatbázisba.");

        } catch (SQLException e) {
            System.err.println("Hiba az eredmény mentésekor: " + e.getMessage());
        }
    }


    public void printHighScores() {
        String query = "SELECT NAME, WINS FROM HIGHSCORE ORDER BY WINS DESC LIMIT 10";

        System.out.println("\n=== HIGHSCORE TÁBLÁZAT ===");
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            int rank = 1;
            System.out.printf("%-5s %-20s %s%n", "Hely", "Név", "Győzelmek");
            System.out.println("------------------------------------");

            boolean hasData = false;
            while (rs.next()) {
                hasData = true;
                String name = rs.getString("NAME");
                int wins = rs.getInt("WINS");
                System.out.printf("%-5d %-20s %d%n", rank++, name, wins);
            }

            if (!hasData) {
                System.out.println("(Még nincs mentett eredmény)");
            }
            System.out.println("==========================\n");

        } catch (SQLException e) {
            System.err.println("Hiba a lista lekérdezésekor: " + e.getMessage());
        }
    }
}