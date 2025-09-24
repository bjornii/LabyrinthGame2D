package main;
import java.sql.*;

public class MySQLConnection {

    public static Connection connect() throws SQLException {
        return DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/mazegame_schema", "root", "root");
    }

    public boolean insertUser(String username, String password) {
        String insertsql = "INSERT INTO users (username, password) VALUES (?, ?)";
        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(insertsql)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Error insertUser: " + e.getMessage());
            return false;
        }
    }

    public boolean selectCredintials(String username, String password) {
        String selectCredintialssql = "SELECT username, password FROM users WHERE username = ? AND password = ?";
        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(selectCredintialssql)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            return rs.next();

        } catch (SQLException e) {
            System.err.println("Error selectCredintials: " + e.getMessage());
            return false;
        }
    }

    public boolean selectUsername(String username) {
        String selectsql = "SELECT username FROM users WHERE username = ?";
        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(selectsql)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            return rs.next();

        } catch (SQLException e) {
            System.err.println("Error selectUsername: " + e.getMessage());
            return false;
        }
    }

    public void saveMapToDatabase() {
        String deleteSQL = "DELETE FROM map WHERE username = ?";
        String insertSQL = "INSERT INTO map (username, `row`, `col`, tilevalue) VALUES (?, ?, ?, ?)";

        try (Connection conn = MySQLConnection.connect();
             PreparedStatement deleteStmt = conn.prepareStatement(deleteSQL);
             PreparedStatement stmt = conn.prepareStatement(insertSQL)) {

            deleteStmt.setString(1, main.LoginSignup.getU());
            deleteStmt.executeUpdate();


            for (int i = 0; i < main.GamePanel.maxScreenRow; i++) {
                for (int j = 0; j < main.GamePanel.maxScreenCol; j++) {
                    stmt.setString(1, main.LoginSignup.getU());     // Map ID
                    stmt.setInt(2, i);        // Row index
                    stmt.setInt(3, j);        // Column index
                    stmt.setInt(4, main.GamePanel.map[i][j]); // Tile value
                    stmt.addBatch();
                }
            }

            stmt.executeBatch();
            System.out.println("Map saved successfully with username: " + main.LoginSignup.getU());

        } catch (SQLException e) {
            System.err.println("Error saving map to database: " + e.getMessage());
        }
    }

    public int[][] loadMapFromDatabase(String u) {
        int[][] loadedMap = new int[main.GamePanel.maxScreenRow][main.GamePanel.maxScreenCol];
        String selectSQL = "SELECT `row`, `col`, tilevalue FROM map WHERE username = ?";

        try (Connection conn = MySQLConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(selectSQL)) {

            stmt.setString(1, main.LoginSignup.getU());
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int row = rs.getInt("row");
                int col = rs.getInt("col");
                int tileValue = rs.getInt("tilevalue");
                loadedMap[row][col] = tileValue;
            }

            System.out.println("Map loaded successfully with ID: " + main.LoginSignup.getU());

        } catch (SQLException e) {
            System.err.println("Error loading map from database: " + e.getMessage());
        }
        return loadedMap;
    }

    public boolean insertUserIntoGameInfo(String user) {
        String insertsql = "INSERT INTO gameinfo (username, playerx, playery, score) VALUES (?, 72, 40, 0) " +
                "ON DUPLICATE KEY UPDATE username = ?, playerx = 72, playery = 40, score = 0";

        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(insertsql)) {

            System.out.println("Attempting to insert default values:");

            stmt.setString(1, user);
            stmt.setString(2, user);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Error insertUserIntoGameInfo: " + e.getMessage());
        }
        return false;
    }

    public boolean insertUserIntoSavedGame(String user) {
        String insertsql = "INSERT INTO savedgame (username, playerx, playery, score) VALUES (?, 72, 40, 0) " +
                "ON DUPLICATE KEY UPDATE username = ?, playerx = 72, playery = 40, score = 0";

        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(insertsql)) {

            System.out.println("Attempting to insert default values:");

            stmt.setString(1, user);
            stmt.setString(2, user);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Error insertUserIntoGameInfo: " + e.getMessage());
        }
        return false;
    }

    public void insertGameInfo(int x, int y, int score) {

        String insertSGS = "UPDATE gameinfo SET playerx = ?, playery = ?, score = ? WHERE username = ?";
        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(insertSGS)) {

            String user = main.LoginSignup.getU();

            System.out.println("Attempting to insert save game state:");
            System.out.println("Player X: " + x);
            System.out.println("Player Y: " + y);
            System.out.println("Score: " + score);
            System.out.println("Login: " + user);

            stmt.setInt(1, x);
            stmt.setInt(2, y);
            stmt.setInt(3, score);
            stmt.setString(4, user);
            stmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error insertSaveGameState: " + e.getMessage());
        }
    }

    public void insertSavedGame(int x, int y, int score) {

        String insertSGS = "UPDATE savedgame SET playerx = ?, playery = ?, score = ? WHERE username = ?";
        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(insertSGS)) {

            String user = main.LoginSignup.getU();

            System.out.println("Attempting to insert save game state:");
            System.out.println("Player X: " + x);
            System.out.println("Player Y: " + y);
            System.out.println("Score: " + score);
            System.out.println("Login: " + user);

            stmt.setInt(1, x);
            stmt.setInt(2, y);
            stmt.setInt(3, score);
            stmt.setString(4, user);
            stmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error insertSaveGameState: " + e.getMessage());
        }
    }

    public int[] a = new int[3];
    public int[] restoreSavedGame() {
        String restoresql = "SELECT playerx, playery, score FROM savedgame WHERE username = ?";
        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(restoresql)) {
            String user = main.LoginSignup.getU();
            stmt.setString(1, user);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                a[0] = rs.getInt("playerx");
                a[1] = rs.getInt("playery");
                a[2] = rs.getInt("score");
            }
            System.out.println("Game state restored for user: " + user);
            System.out.println("Coordinates: (" + a[0] + ", " + a[1] + ")");
            System.out.println("Score: " + a[2]);

        } catch (SQLException e) {
            System.err.println("Error restoreGameState: " + e.getMessage());
        }
        return a;

    }
    public int[] restoreGameInfo() {
        String restoresql = "SELECT playerx, playery, score FROM gameinfo WHERE username = ?";
        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(restoresql)) {
            String user = main.LoginSignup.getU();
            stmt.setString(1, user);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                a[0] = rs.getInt("playerx");
                a[1] = rs.getInt("playery");
                a[2] = rs.getInt("score");
            }
            System.out.println("Game state restored for user: " + user);
            System.out.println("Coordinates: (" + a[0] + ", " + a[1] + ")");
            System.out.println("Score: " + a[2]);

        } catch (SQLException e) {
            System.err.println("Error restoreGameState: " + e.getMessage());
        }
        return a;

    }

    public void updateScoreInGameInfo(int score){

        String insertSGS = "UPDATE gameinfo SET score = ? WHERE username = ?";
        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(insertSGS)) {

            String user = main.LoginSignup.getU();

            System.out.println("Score: " + score);
            System.out.println("Login: " + user);

            stmt.setInt(1, score);
            stmt.setString(2, user);
            stmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error insertSaveGameState: " + e.getMessage());
        }

    }

    public void insertScoreIntoAllTimeScore(int score) {
        String insertsql = "INSERT INTO alltimescore (username, score) VALUES (? ,?) " +
                "ON DUPLICATE KEY UPDATE username = ?, score = score + ?";

        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(insertsql)) {

            String user = main.LoginSignup.getU();

            stmt.setString(1, user);
            stmt.setInt(2, score);
            stmt.setString(3, user);
            stmt.setInt(4, score);

            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error inserting score into alltimescore: " + e.getMessage());
        }
    }

    public int selectAllTimeScore() {
        String selectsql = "SELECT score FROM alltimescore WHERE username = ?";
        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(selectsql)) {

            String username = main.LoginSignup.getU();

            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("score");
            }
            return 0;

        } catch (SQLException e) {
            System.err.println("Error selectUsername: " + e.getMessage());
        }
        return -1;
    }

    public void flag() {
        String deleteSQL = "DELETE FROM flag WHERE username = ?";
        String insertSQL = "INSERT INTO flag (username) VALUES (?)";

        try (Connection conn = MySQLConnection.connect();
             PreparedStatement deleteStmt = conn.prepareStatement(deleteSQL);
             PreparedStatement stmt = conn.prepareStatement(insertSQL)) {

            deleteStmt.setString(1, main.LoginSignup.getU());
            deleteStmt.executeUpdate();

            stmt.setString(1, main.LoginSignup.getU());
            stmt.executeUpdate();

            System.out.println("Flag inserted succesfully: " + main.LoginSignup.getU());

        } catch (SQLException e) {
            System.err.println("Error inserting flag: " + e.getMessage());
        }
    }

    public boolean selectflag() {

        String selectSQL = "SELECT username FROM flag WHERE username = ?";

        try (Connection conn = MySQLConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(selectSQL)) {

            stmt.setString(1, main.LoginSignup.getU());
            ResultSet rs = stmt.executeQuery();

            return rs.next();

        } catch (SQLException e) {
            System.err.println("Error selecting flag from database: " + e.getMessage());
        }
        return false;
    }

    public void deleteflag(){
        String deleteSQL = "DELETE FROM flag WHERE username = ?";

        try(Connection conn = MySQLConnection.connect();
            PreparedStatement stmt = conn.prepareStatement(deleteSQL)){

            stmt.setString(1, main.LoginSignup.getU());
            stmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error selecting flag from database: " + e.getMessage());
        }
    }

}


