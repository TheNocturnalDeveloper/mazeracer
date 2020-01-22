package nl.fontys.se3.data;

import java.util.List;

public interface IUserDAO {

    /**
     * fetches all of the users.
     *
     * @return a list of all the users.
     */
    List<UserDTO> getAllUsers();

    /**
     * returns a player based on their username
     *
     * @param username the name of the user that will be returned
     * @return a user
     */
    UserDTO getPlayerByName(String username);

    /**
     * Reads given resource file as a string.
     *
     * @param username the name of the user whose score will be updated
     * @param score the new score of the player
     */
    void updatePlayerScore(String username, int score);

    /**
     * inserts a new player
     *
     * @param player the player that will be inserted
     */
    void insertPlayer(UserDTO player);

    /**
     * checks the provided credentials and returns the player
     *
     * @param username the username of the player that will be checked
     * @param password the password of the player that will be checked
     * @return the player that matched with the credentials
     */
    UserDTO checkCredentials(String username, String password);
}
