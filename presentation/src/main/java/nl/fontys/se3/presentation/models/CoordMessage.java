package nl.fontys.se3.presentation.models;

public class CoordMessage extends BasicMessage {
    private int x;
    private int y;

    public CoordMessage() {
    }

    public CoordMessage(int x, int y, MessageType type) {
        super(type);
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }
}
