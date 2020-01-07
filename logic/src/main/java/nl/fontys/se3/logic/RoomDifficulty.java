package nl.fontys.se3.logic;

public enum RoomDifficulty {
    EASY(10), MEDIUM(11), HARD(12);

    private final int mazeSize;

    RoomDifficulty(int mazeSize) {
        this.mazeSize = mazeSize;
    }


    public int getMazeSize() {
        return mazeSize;
    }
}
