package nl.fontys.se3.logic;

import nl.fontys.se3.logic.Cell;
import nl.fontys.se3.logic.Coord;
import nl.fontys.se3.logic.Maze;
import nl.fontys.se3.logic.MoveChecker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MoveCheckerTest {
    private Maze maze;

    @BeforeEach
    public void prep() {

        maze = new Maze(10, 10);

    }

    @Test
    public void testCheckMoveValidRight() {
        Cell cellA = maze.get(0,0);
        Cell cellB = maze.get(1,0);

        Coord coordA = new Coord(0,0);
        Coord coordB = new Coord(1,0);

        Maze.removeWalls(cellA, cellB);

        MoveChecker checker = new MoveChecker(maze);


        assertTrue(checker.checkMove(coordA, coordB));
    }

    @Test
    public void testCheckMoveValidLeft() {
        Cell cellA = maze.get(1,0);
        Cell cellB = maze.get(0,0);

        Coord coordA = new Coord(1,0);
        Coord coordB = new Coord(0,0);

        Maze.removeWalls(cellA, cellB);

        MoveChecker checker = new MoveChecker(maze);


        assertTrue(checker.checkMove(coordA, coordB));
    }

    @Test
    public void testCheckMoveValidTop() {
        Cell cellA = maze.get(0,1);
        Cell cellB = maze.get(0,0);

        Coord coordA = new Coord(0,1);
        Coord coordB = new Coord(0,0);

        Maze.removeWalls(cellA, cellB);

        MoveChecker checker = new MoveChecker(maze);


        assertTrue(checker.checkMove(coordA, coordB));
    }

    @Test
    public void testCheckMoveValidBottom() {
        Cell cellA = maze.get(0,0);
        Cell cellB = maze.get(0,1);

        Coord coordA = new Coord(0,0);
        Coord coordB = new Coord(0,1);

        Maze.removeWalls(cellA, cellB);

        MoveChecker checker = new MoveChecker(maze);


        assertTrue(checker.checkMove(coordA, coordB));
    }

    @Test
    public void testCheckMoveWithWall() {
        Coord coordA = new Coord(0,0);
        Coord coordB = new Coord(1,0);


        MoveChecker checker = new MoveChecker(maze);


        assertFalse(checker.checkMove(coordA, coordB));
    }

    @Test
    public void testCheckMoveNotAdjacent() {
        Coord coordA = new Coord(0,0);
        Coord coordB = new Coord(2,0);


        MoveChecker checker = new MoveChecker(maze);


        assertFalse(checker.checkMove(coordA, coordB));
    }



}
