package nl.fontys.se3.data;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserDAO {

    private static String host = "127.0.01";
    private static String user = "root";
    private static String dbName = "maze_racer";
    private static String pass = "test";
    private static int port = 3306;

    private static final Logger LOGGER = Logger.getLogger( UserDAO.class.getName() );



    private Connection getConnection() throws SQLException {


        return DriverManager.getConnection(
                "jdbc:mysql://" + host + ":"+port+"/"+dbName+"?serverTimezone=UTC",
                user, pass);   // For MySQL only

    }


    public List<UserDTO> getAllUsers() {

        List<UserDTO> users = new ArrayList();
        ResultSet rset = null;

        try (
                Connection conn = getConnection();
                Statement stmt = conn.createStatement();
        ) {
            String strSelect = "select * from user";
            rset = stmt.executeQuery(strSelect);

            int rowCount = 0;
            while (rset.next()) {

                UserDTO p = new UserDTO(
                        rset.getString("username"),
                        rset.getString("password"),
                        rset.getInt("score")
                );

                users.add(p);

                LOGGER.log( Level.FINE, "userDTO GetAll: {0}" , p);
                ++rowCount;
            }
            LOGGER.log( Level.FINE, "Total number of records = {0}", rowCount);

        } catch (SQLException ex) {
            LOGGER.log( Level.SEVERE, ex.toString(), ex );
        } finally {
            if ( rset != null) {
                try {
                    rset.close();
                } catch (SQLException ex) {
                    LOGGER.log( Level.SEVERE, ex.toString(), ex );
                }
            }
        }

        return users;

    }

    public UserDTO getPlayerByName(String username) {
        UserDTO user = null;
        ResultSet rset = null;

        try (
                Connection conn = this.getConnection();
                Statement stmt = conn.createStatement();
        ) {
            String strSelect = String.format("select * from user WHERE username = '%s';", username);
            rset = stmt.executeQuery(strSelect);

            LOGGER.log( Level.FINEST, "The records selected are:");

            while (rset.next()) {

                UserDTO p = new UserDTO(
                        rset.getString("username"),
                        rset.getString("password"),
                        rset.getInt("score")
                );

                LOGGER.log( Level.FINE, "UserDTO GetByName: {0}" , p);

                user = p;
            }

        } catch (SQLException ex) {
            LOGGER.log( Level.SEVERE, ex.toString(), ex );
        } finally {
            if ( rset != null) {
                try {
                    rset.close();
                } catch (SQLException ex) {
                    LOGGER.log( Level.SEVERE, ex.toString(), ex );
                }
            }
        }

        return user;

    }

    public void updatePlayerScore(String username, int score) {
        try (
                Connection conn = this.getConnection();
                Statement stmt = conn.createStatement();
        ) {
            stmt.executeUpdate(
                    String.format("UPDATE user SET score = %d WHERE username = '%s'",
                            username, score)
            );

            LOGGER.log( Level.FINE, "Affected rows: {0}", stmt.getUpdateCount());

        } catch (SQLException ex) {
            LOGGER.log( Level.SEVERE, ex.toString(), ex );
        }

    }

    public void insertPlayer(UserDTO player) {
        try (
                Connection conn = this.getConnection();
                Statement stmt = conn.createStatement();
        ) {
            stmt.executeUpdate(
                    String.format("INSERT INTO user (username, password, score) VALUES ('%s', '%s', '%d')",
                    player.getUsername(), player.getPassword(), player.getScore())
            );

            LOGGER.log( Level.FINE, "Affected rows: {0}", stmt.getUpdateCount());

        } catch (SQLException ex) {
            LOGGER.log( Level.SEVERE, ex.toString(), ex );
        }

    }

    public UserDTO checkCredentials(String username, String password) {
        var player = getPlayerByName(username);

        if(player != null && player.getPassword().equals(password)) {
            return player;
        }

        return null;
    }
}
