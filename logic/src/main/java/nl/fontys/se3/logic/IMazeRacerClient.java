package nl.fontys.se3.logic;


public interface IMazeRacerClient {

    /**
     * adds an enemy to the client. the name will be used when moving or removing the player.
     *
     * @param username the name of the user that joined
     */
    void addEnemy(String username);

    /**
     * moves an enemy to a specific coordinate
     *
     * @param username the username of the enemy that will be moved
     * @param coord the coordinate where the player will be moved
     */
    void moveEnemy(String username, Coord coord);

    /**
     * notifies the client which player has won
     *
     * @param username the name of the user that joined
     */
    void notifyPlayerWon(String username);

    /**
     * removes a bonus from the client
     *
     * @param coord the coordinate of the bonus that will be removed
     */
    void removeBonus(Coord coord);

    /**
     * Resets the client to a specific coordinate
     *
     * @param coord the new position of the client
     */
    void resetPosition(Coord coord);

    /**
     * Reads given resource file as a string.
     *
     * @param cells the name of the user that joined
     */
    void loadMaze(Cell[] cells);

    /**
     * Removes an enemy from the client
     *
     * @param username the name of the user that left
     */
    void removeEnemy(String username);

}
