package nl.fontys.se3.presentation.models;

public class CellModel {
    private int x;
    private int y;
    private int type;
    private int walls;

    public CellModel() {

    }

    public CellModel(int x, int y, int type, int walls) {
        this.x = x;
        this.y = y;
        this.type = type;
        this.walls = walls;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getType() {
        return type;
    }

    public int getWalls() {
        return walls;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setWalls(int walls) {
        this.walls = walls;
    }
}
