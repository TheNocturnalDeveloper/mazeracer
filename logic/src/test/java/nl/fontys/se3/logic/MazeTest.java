package nl.fontys.se3.logic;

import nl.fontys.se3.logic.Cell;
import nl.fontys.se3.logic.CellType;
import nl.fontys.se3.logic.Maze;
import nl.fontys.se3.logic.Wall;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

public class MazeTest {
    private Maze maze;

    @BeforeEach
    public void prep() {
        maze = new Maze(10, 10);
    }


    @Test
    public void TestCheckLeftNeighbour() {
        var cell = maze.get(1, 0);
        var results = new ArrayList<Cell>();

        maze.checkLeftNeighbour(cell, results);

        assertEquals(1, results.size());

    }

    @Test
    public void TestCheckRightNeighbour() {
        var cell = maze.get(0, 0);
        var results = new ArrayList<Cell>();

        maze.checkRightNeighbour(cell, results);

        assertEquals(1, results.size());

    }

    @Test
    public void TestCheckTopNeighbour() {
        var cell = maze.get(0, 1);
        var results = new ArrayList<Cell>();

        maze.checkTopNeighbour(cell, results);

        assertEquals(1, results.size());

    }

    @Test
    public void TestCheckBottomNeighbour() {
        var cell = maze.get(0, 0);
        var results = new ArrayList<Cell>();

        maze.checkBottomNeighbour(cell, results);

        assertEquals(1, results.size());

    }

    @Test
    public void testRemoveWallRightLeft() {
        Cell cellA = maze.get(1,0);
        Cell cellB = maze.get(0,0);

        Maze.removeWalls(cellA, cellB);

        assertFalse(cellA.hasWall(Wall.LEFT));
        assertFalse(cellB.hasWall(Wall.RIGHT));
    }

    @Test
    public void testRemoveWallTopBottom() {
        Cell cellA = maze.get(0,0);
        Cell cellB = maze.get(0,1);

        Maze.removeWalls(cellA, cellB);

        assertFalse(cellA.hasWall(Wall.BOTTOM));
        assertFalse(cellB.hasWall(Wall.TOP));
    }

    @Test
    public void testRemoveWallBottomTop() {
        Cell cellA = maze.get(0,1);
        Cell cellB = maze.get(0,0);

        Maze.removeWalls(cellA, cellB);

        assertFalse(cellA.hasWall(Wall.TOP));
        assertFalse(cellB.hasWall(Wall.BOTTOM));
    }

    @Test
    public void testRemoveWallLeftRight() {
        Cell cellA = maze.get(0,0);
        Cell cellB = maze.get(1,0);

        Maze.removeWalls(cellA, cellB);

        assertFalse(cellA.hasWall(Wall.RIGHT));
        assertFalse(cellB.hasWall(Wall.LEFT));
    }

    @Test
    public void testGenerateMaze() {
        maze.generateMaze();

        Boolean allVisited = Arrays.stream(maze.getCells()).allMatch(c -> c.isVisited());
        long startCount = Arrays.stream(maze.getCells()).filter(c -> c.getType() == CellType.START).count();
        long exitCount = Arrays.stream(maze.getCells()).filter(c -> c.getType() == CellType.EXIT).count();

        assertTrue(allVisited);
        assertEquals(1, startCount);
        assertEquals(1, exitCount);
    }

    @Test
    public void testResetGrid() {
        maze.generateMaze();

        maze.resetGrid();

        Boolean noneVisited = Arrays.stream(maze.getCells()).noneMatch(c -> c.isVisited());
        Boolean allWallsIntact = Arrays.stream(maze.getCells()).allMatch(c -> c.getWalls() == Wall.ALL.value);
        long emptyCount = Arrays.stream(maze.getCells()).filter(c -> c.getType() == CellType.EMPTY).count();


        assertTrue(noneVisited);
        assertTrue(allWallsIntact);
        assertEquals(maze.getCells().length, emptyCount);
    }
}
