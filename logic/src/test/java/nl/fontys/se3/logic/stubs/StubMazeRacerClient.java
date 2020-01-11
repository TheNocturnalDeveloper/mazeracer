package nl.fontys.se3.logic.stubs;

import nl.fontys.se3.logic.Cell;
import nl.fontys.se3.logic.Coord;
import nl.fontys.se3.logic.IMazeRacerClient;

import java.util.ArrayList;
import java.util.List;

public class StubMazeRacerClient implements IMazeRacerClient {

    private boolean started;
    private List<String> opponents;
    private Cell[] maze;
    private Coord pos;

    public StubMazeRacerClient() {
        started = false;
        opponents = new ArrayList();
    }

    @Override
    public void addEnemy(String username) {
        opponents.add(username);
    }

    @Override
    public void moveEnemy(String username, Coord coord) {

    }

    @Override
    public void notifyPlayerWon(String username) {

    }

    @Override
    public void removeBonus(Coord coord) {

    }

    @Override
    public void resetPosition(Coord coord) {
        this.pos = coord;
    }

    @Override
    public void loadMaze(Cell[] cells) {
        this.maze = cells;
        this.started = true;
    }

    @Override
    public void removeEnemy(String username) {
        opponents.remove(username);
    }


    public boolean isStarted() {
        return started;
    }

    public Cell[] getMaze() {
        return maze;
    }

    public Coord getPos() {
        return pos;
    }

    public List<String> getOpponents() {
        return opponents;
    }
}
