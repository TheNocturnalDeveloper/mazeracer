package nl.fontys.se3.logic;

import nl.fontys.se3.logic.Cell;
import nl.fontys.se3.logic.Wall;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CellTest {
    private Cell cell;

    @BeforeEach
    public void prep() {
        cell = new Cell(5,5);
    }

    @Test
    public void testHasWall() {
        boolean hasWall = cell.hasWall(Wall.ALL);
        boolean hasWallInt = cell.hasWall(Wall.ALL.value);

        assertTrue(hasWall);
        assertTrue(hasWallInt);
    }

    @Test
    public void testRemoveWall() {
        cell.removeWall(Wall.TOP);

        boolean hasWall = cell.hasWall(Wall.TOP);

        assertFalse(hasWall);
    }

    @Test
    public void testAddWall() {
        cell.removeWall(Wall.ALL);

        cell.addWall(Wall.BOTTOM);
        cell.addWall(Wall.TOP);

        boolean hasWallTop = cell.hasWall(Wall.TOP);
        boolean hasWallBottom = cell.hasWall(Wall.BOTTOM);


        assertTrue(hasWallTop);
        assertTrue(hasWallBottom);

    }

    @Test
    public void testSetWall() {
        cell.removeWall(Wall.ALL);

        cell.setWall(Wall.BOTTOM);
        cell.setWall(Wall.TOP);

        boolean hasWallTop = cell.hasWall(Wall.TOP);
        boolean hasWallBottom = cell.hasWall(Wall.BOTTOM);


        assertTrue(hasWallTop);
        assertFalse(hasWallBottom);
    }




}
