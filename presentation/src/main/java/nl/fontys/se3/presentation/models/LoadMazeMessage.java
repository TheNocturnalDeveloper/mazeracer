package nl.fontys.se3.presentation.models;

import java.util.ArrayList;
import java.util.List;

public class LoadMazeMessage extends BasicMessage {
    private List<CellModel> cells;

    public LoadMazeMessage(List<CellModel> cells) {
        setType(MessageType.LOAD_MAZE);
        this.cells = cells;
    }

    public LoadMazeMessage() {
        setType(MessageType.LOAD_MAZE);
        cells = new ArrayList();
    }

    public List<CellModel> getCells() {
        return cells;
    }

    public void addCell(CellModel cell) {
        cells.add(cell);
    }
}
