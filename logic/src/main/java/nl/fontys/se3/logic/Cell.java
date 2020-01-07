package nl.fontys.se3.logic;

public class Cell {
    private  int steps;
    private CellType type;
    private boolean visited;
    private final int x;
    private final int y;
    private int walls;


    //TODO: USE COORD CLASS

    public Cell(int x, int y) {
        this.type = CellType.EMPTY;
        this.x = x;
        this.y = y;
        this.visited = false;
        this.steps = 0;
        this.walls = Wall.ALL.value;
    }

    public int getSteps() {
        return steps;
    }

    public CellType getType() {
        return type;
    }

    public void setType(CellType type) {
        this.type = type;
    }

    public int getY() {
        return y;
    }

    public int getX() {
        return x;
    }

    public boolean isVisited() {
        return visited;
    }

    public void setSteps(int steps) {
        this.steps = steps;
    }

    public void setVisited(boolean visited) {
        this.visited = visited;
    }

    public int getWalls() {
        return walls;
    }

    public void addWall(Wall wall) {
        this.walls |= wall.value;
    }

    public  void removeWall(Wall wall) {
        this.walls ^= wall.value;
    }

    public void setWall(Wall wall) {
        this.walls = wall.value;
    }

    public boolean hasWall(Wall wall) {
        return (walls & wall.value) != 0;
    }

    public boolean hasWall(int wall) {
        return (walls & wall) != 0;
    }
}
