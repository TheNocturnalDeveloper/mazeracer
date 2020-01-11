package nl.fontys.se3.logic;

public class ClientPlaceHolder implements IMazeRacerClient {
    @Override
    public void addEnemy(String username) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void moveEnemy(String username, Coord coord) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void notifyPlayerWon(String username) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void removeBonus(Coord coord) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void resetPosition(Coord coord) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void loadMaze(Cell[] cells) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void removeEnemy(String username) {
        throw new UnsupportedOperationException();
    }
}
