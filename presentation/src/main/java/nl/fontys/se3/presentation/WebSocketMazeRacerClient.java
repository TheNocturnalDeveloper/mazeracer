package nl.fontys.se3.presentation;

import io.javalin.websocket.WsContext;
import nl.fontys.se3.logic.Cell;
import nl.fontys.se3.logic.Coord;
import nl.fontys.se3.logic.IMazeRacerClient;
import nl.fontys.se3.presentation.models.*;

import java.util.Arrays;
import java.util.stream.Collectors;

public class WebSocketMazeRacerClient implements IMazeRacerClient {

    private  WsContext context;

    public WebSocketMazeRacerClient(WsContext context) {

        this.context = context;
    }

    @Override
    public void addEnemy(String username) {
        context.send(new PlayerJoinMessage(username));
    }

    @Override
    public void moveEnemy(String username, Coord coord) {
        context.send(new EnemyMoveMessage(username, coord.getX(), coord.getY()));
    }

    @Override
    public void NotifyPlayerWon(String username) {
        context.send(new PlayerWonMessage(username));
    }

    @Override
    public void removeBonus(Coord coord) {
        context.send(new RemoveBonusMessage(coord.getX(), coord.getY()));
    }

    @Override
    public void resetPosition(Coord coord) {
        context.send(new ResetPositionMessage(coord.getX(), coord.getY()));
    }

    @Override
    public void loadMaze(Cell[] cells) {

        var maze = Arrays.stream(cells)
                .map(c -> new CellModel(c.getX(), c.getY(), c.getType().value, c.getWalls()))
                .collect(Collectors.toList());

        context.send(new LoadMazeMessage(maze));
    }

    @Override
    public void removeEnemy(String username) {
        context.send(new PlayerLeaveMessage(username));
    }

}
