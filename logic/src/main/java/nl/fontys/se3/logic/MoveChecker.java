package nl.fontys.se3.logic;

public class MoveChecker {
    private Maze maze;

    public MoveChecker(Maze maze) {
        this.maze = maze;
    }

    public boolean checkMove(Coord startCoord, Coord endCoord) {
        int startSum = startCoord.getX() + startCoord.getY();
        int endSum = endCoord.getX() + endCoord.getY();
        int diff = Math.abs(startSum - endSum);

        if(diff != 1) {
            return  false;
        }

        Cell startCell  =   maze.get(startCoord.getX(), startCoord.getY());
        Cell endCell    =   maze.get(endCoord.getX(), endCoord.getY());

        //extract into method to get direction?
        if(startCell.getX() == endCell.getX()) {
            if(startCell.getY() - endCell.getY() > 0) {
                return !startCell.hasWall(Wall.TOP);
            }
            else {
                return !startCell.hasWall(Wall.BOTTOM);
            }
        }
        else {
            if(startCell.getX() - endCell.getX() > 0) {
                return !startCell.hasWall(Wall.LEFT);
            }
            else {
                return !startCell.hasWall(Wall.RIGHT);
            }
        }

    }
}
