package nl.fontys.se3.logic;

public interface IMazeRacerClient {

    void addEnemy(String username);

    void moveEnemy(String username, Coord coord);

    void notifyPlayerWon(String username);

    void removeBonus(Coord coord);

    void resetPosition(Coord coord);

    void loadMaze(Cell[] cells);

    void removeEnemy(String username);

}
