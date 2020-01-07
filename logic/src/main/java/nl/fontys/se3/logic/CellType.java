package nl.fontys.se3.logic;

public enum CellType {
    EMPTY(0),
    BONUS(1),
    EXIT(2),
    START(3);

    public final int value;

    CellType(int value) {
        this.value = value;
    }
}
