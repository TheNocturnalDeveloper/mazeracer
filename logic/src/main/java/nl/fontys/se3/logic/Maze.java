package nl.fontys.se3.logic;

import java.util.*;

public class Maze {
    private Cell[] cells;
    private int width;
    private int height;


    public Maze(int width, int height) {
        this.width = width;
        this.height = height;
        this.cells = new Cell[width * height];

        for(int y = 0; y < height; y++) {
            for(int x = 0; x < width; x++) {
                this.cells[getIndex(x, y)] = new Cell(x, y);
            }
        }
    }

    public void generateMaze() {
        generateMaze(new Random());
    }


    public void generateMaze(Random random) {
        Cell current  = cells[random.nextInt(cells.length)];

        var cellStack = new Stack<Cell>();

        var neighbours = new ArrayList<Cell>(4);

        //loops while there are empty (open) cells that have not been visited
        while (Arrays.stream(cells).filter(c -> !c.isVisited()).count() > 0)
        {
            //marks the current cell as visited
            current.setVisited(true);

            //loads potential neighbours of the current cell into the neighbours list
            checkLeftNeighbour(current, neighbours);
            checkRightNeighbour(current, neighbours);
            checkTopNeighbour(current, neighbours);
            checkBottomNeighbour(current, neighbours);

            if (neighbours.isEmpty())
            {
                //remove the top value of the stack and assign the new top value of the stack to the current cell
                cellStack.pop();
                current = cellStack.peek();
                continue;
            }

            var neighbour = neighbours.get(random.nextInt(neighbours.size()));
            //get the cell between the current cell and the neighbour, and remove the wall from the cell.

            removeWalls(current, neighbour);

            //mark the neighbour and the wall as visited
            neighbour.setVisited(true);


            //set the amount of steps from the start on the wall and the neighbour.
            neighbour.setSteps(current.getSteps() + 1);

            //put the neighbour on the stack and set the current cell to the neighbour.
            cellStack.push(neighbour);
            current = neighbour;

            //clear the neighbours (it will be filled again the next cycle).
            neighbours.clear();
        }

        markEndPoints();

    }

    public int getIndex(int x, int y) {
        return   x + y * width;
    }


    public static void removeWalls(Cell a, Cell b) {
        if(a.getX() == b.getX()) {
            //the x-axis of the current cell and the neighbour are the same,
            // so that means they have a different y-axis

            //if the current cell has a higher Y-axis it's below the neighbour cell
            if(a.getY() - b.getY() > 0) {
                a.removeWall(Wall.TOP);
                b.removeWall(Wall.BOTTOM);
            }
            else {
                a.removeWall(Wall.BOTTOM);
                b.removeWall(Wall.TOP);
            }
        }
        else {
            //if the current cell has a higher x-axis it's on the right side of the neighbour
            if(a.getX() - b.getX() > 0) {
                a.removeWall(Wall.LEFT);
                b.removeWall(Wall.RIGHT);
            }
            else {
                a.removeWall(Wall.RIGHT);
                b.removeWall(Wall.LEFT);
            }
        }
    }

    public void checkLeftNeighbour(Cell currentCell, List<Cell> neighbours)
    {
        if (currentCell.getX() >= 1)
        {
            var potentialNeighbour = cells[getIndex(currentCell.getX() - 1, currentCell.getY())];

            if (!potentialNeighbour.isVisited())
            {
                neighbours.add(potentialNeighbour);
            }
        }
    }


    public void checkRightNeighbour(Cell currentCell, List<Cell> neighbours)
    {
        if (currentCell.getX() < width - 1)
        {
            var potentialNeighbour = cells[getIndex(currentCell.getX() + 1, currentCell.getY())];

            if (!potentialNeighbour.isVisited())
            {
                neighbours.add(potentialNeighbour);
            }
        }
    }


    public void checkTopNeighbour(Cell currentCell, List<Cell> neighbours)
    {
        if (currentCell.getY() >= 1)
        {
            var potentialNeighbour = cells[getIndex(currentCell.getX(), currentCell.getY() - 1)];

            if (!potentialNeighbour.isVisited())
            {
                neighbours.add(potentialNeighbour);
            }
        }

    }



    public void checkBottomNeighbour(Cell currentCell, List<Cell> neighbours)
    {
        if (currentCell.getY() < height - 1)
        {
            var potentialNeighbour = cells[getIndex(currentCell.getX(), currentCell.getY() + 1)];

            if (!potentialNeighbour.isVisited())
            {
                neighbours.add(potentialNeighbour);
            }
        }
    }


    public void resetGrid() {
        for(Cell cell: cells) {
            cell.setWall(Wall.ALL);
            cell.setSteps(0);
            cell.setType(CellType.EMPTY);
            cell.setVisited(false);
        }
    }

    private void markEndPoints() {
        Arrays.stream(cells)
                .max(Comparator.comparingInt(Cell::getSteps))
                .get()
                .setType(CellType.EXIT);

        Arrays.stream(cells)
                .min(Comparator.comparingInt(Cell::getSteps))
                .get()
                .setType(CellType.START);
    }


    public Cell[] getCells() {
        return cells;
    }

    public Cell get(int x, int y) {
        return cells[getIndex(x,y)];
    }

}
