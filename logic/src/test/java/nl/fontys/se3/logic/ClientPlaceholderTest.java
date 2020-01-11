package nl.fontys.se3.logic;

import nl.fontys.se3.logic.Cell;
import nl.fontys.se3.logic.ClientPlaceHolder;
import nl.fontys.se3.logic.Coord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class ClientPlaceholderTest {
    ClientPlaceHolder placeHolder = new ClientPlaceHolder();

    @Test
    public void testAddEnemy() {

       assertThrows(UnsupportedOperationException.class, () -> {
            placeHolder.addEnemy("");
       });
    }

    @Test
    public void testMoveEnemy() {

        assertThrows(UnsupportedOperationException.class, () -> {
            placeHolder.moveEnemy("", new Coord(0,0));
        });
    }

    @Test
    public void testNotifyPlayerWon() {
        assertThrows(UnsupportedOperationException.class, () -> {
            placeHolder.notifyPlayerWon("");
        });
    }

    @Test
    public void testRemoveBonus() {
        assertThrows(UnsupportedOperationException.class, () -> {
            placeHolder.removeBonus(new Coord(0,0));
        });
    }

    @Test
    public void testResetPosition() {
        assertThrows(UnsupportedOperationException.class, () -> {
            placeHolder.resetPosition(new Coord(0,0));
        });
    }

    @Test
    public void testLoadMaze() {

        assertThrows(UnsupportedOperationException.class, () -> {
            placeHolder.loadMaze(new Cell[10]);
        });
    }

    @Test
    public void testRemoveEnemy() {
        assertThrows(UnsupportedOperationException.class, () -> {
            placeHolder.removeEnemy("");
        });
    }


}
