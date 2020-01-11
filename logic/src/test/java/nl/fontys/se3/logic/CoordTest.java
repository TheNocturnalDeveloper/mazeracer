package nl.fontys.se3.logic;

import nl.fontys.se3.logic.Coord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CoordTest {

    private Coord coord;

    @BeforeEach
    public void prep() {
        coord = new Coord(10,  5);
    }

    @Test
    public void testGetX() {
        assertEquals(10, coord.getX());
    }

    @Test
    public void testGetY() {
        assertEquals(5, coord.getY());
    }

    @Test
    public void testSetX() {
        coord.setX(9);
        assertEquals(9, coord.getX());
    }

    @Test
    public void testSetY() {
        coord.setY(10);
        assertEquals(10, coord.getY());
    }
}
