package nl.fontys.se3.data;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserDAO implements IUserDAO {

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

    @Override
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
                        rset.getInt("easy_score"),
                        rset.getInt("medium_score"),
                        rset.getInt("hard_score")
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

    @Override
    public UserDTO getPlayerByName(String username) {
        UserDTO user = null;
        ResultSet rset = null;

        try (
                Connection conn = this.getConnection();
                PreparedStatement selectStatement = conn.prepareStatement("select * from user WHERE username = ?;");
        ) {
            selectStatement.setString(1, username);
            rset = selectStatement.executeQuery();

            LOGGER.log( Level.FINEST, "The records selected are:");

            while (rset.next()) {

                UserDTO p = new UserDTO(
                        rset.getString("username"),
                        rset.getString("password"),
                        rset.getInt("easy_score"),
                        rset.getInt("medium_score"),
                        rset.getInt("hard_score")
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

    @Override
    public void updatePlayerScore(String username, int score) {
        try (
                Connection conn = this.getConnection();
                PreparedStatement updateStatement = conn.prepareStatement("UPDATE user SET easy_score = ? WHERE username = ?");
        ) {
            updateStatement.setInt(1 ,score);
            updateStatement.setString(2, username);
            updateStatement.executeUpdate();
            LOGGER.log( Level.FINE, "Affected rows: {0}", updateStatement.getUpdateCount());

        } catch (SQLException ex) {
            LOGGER.log( Level.SEVERE, ex.toString(), ex );
        }

    }

    @Override
    public void insertPlayer(UserDTO player) {
        try (
                Connection conn = this.getConnection();
                PreparedStatement insertStatement = conn.prepareStatement("INSERT INTO user (username, password) VALUES (?, ?)");

        ) {
          insertStatement.setString(1, player.getUsername());
          insertStatement.setString(2, player.getPassword());
          insertStatement.execute();
            LOGGER.log( Level.FINE, "Affected rows: {0}", insertStatement.getUpdateCount());

        } catch (SQLException ex) {
            LOGGER.log( Level.SEVERE, ex.toString(), ex );
        }

    }

    @Override
    public UserDTO checkCredentials(String username, String password) {
        var player = getPlayerByName(username);

        if(player != null && player.getPassword().equals(password)) {
            return player;
        }

        return null;
    }
}
