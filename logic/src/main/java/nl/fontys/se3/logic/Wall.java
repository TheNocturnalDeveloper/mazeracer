package nl.fontys.se3.logic;

public enum Wall {
    TOP(0b1000),
    BOTTOM(0b0100),
    LEFT(0b0010),
    RIGHT(0b0001),
    ALL(0b1111),
    NONE(0b0000);

    public final int value;

    Wall(int value) {
        this.value = value;
    }

}
