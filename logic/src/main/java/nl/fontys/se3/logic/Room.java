package nl.fontys.se3.logic;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class Room {
    private final int id;
    private final int limit;
    private final int roomScore;
    private Map<String, IMazeRacerClient> players;
    private Maze maze;
    private  MoveChecker moveChecker;
    private RoomDifficulty difficulty;
    //private TurnChecker turnChecker;


    public Room(int id, int limit, int roomScore, RoomDifficulty difficulty) {
        this.id = id;
        this.limit = limit;
        this.roomScore = roomScore;
        this.difficulty = difficulty;

        players = new ConcurrentHashMap();

        int mazeSize = difficulty.getMazeSize();

        maze = new Maze(mazeSize, mazeSize);
        moveChecker = new MoveChecker(maze);

        //TODO: add turn based mode?
    }

    public int getId() {
        return id;
    }

    public int getLimit() {
        return limit;
    }

    public int getRoomScore() {
        return roomScore;
    }

    public RoomDifficulty getDifficulty() {
        return difficulty;
    }

    public Map<String, IMazeRacerClient> getPlayers() {
        return Collections.unmodifiableMap(players);
    }


    public boolean addPlayer(String username) {
        if(players.size() >= limit) return false;
        players.put(username, new ClientPlaceHolder());

        return true;
    }

    public boolean enter(String username, IMazeRacerClient client) {
        if(!players.containsKey(username)) return false;

        players.replace(username, client);

        return true;
    }

    public boolean isFull() {
        return players.size() >= limit;
    }

    public void start() {
        maze.generateMaze();
        for (IMazeRacerClient client: players.values()) {
            client.loadMaze(maze.getCells());
        }
    }

    public void removePlayer(String username) {

        players.remove(username);

        for (var client: players.entrySet()) {
            client.getValue().removeEnemy(username);
        }
    }

    //TODO: CLEAN UP MOVE PLAYER (MAYBE DIVIDE INTO MULTIPLE METHODS)
    public void movePlayer(String username, Coord coord) {
        //TODO: think of a way to store player coords and bonuses
        //TODO: return game state, the websocket controller can remove the room and save changes based on the state


        IMazeRacerClient player = players.get(username);

        if(!moveChecker.checkMove(new Coord(0,0), coord)) {
            player.resetPosition(new Coord(0,0));
            return;
        }

        Cell cell = maze.get(coord.getX(), coord.getY());

        switch (cell.getType()) {
            case EXIT:
                break;
            case BONUS:
                break;
            default:
                break;
        }

        for (var client: players.entrySet()) {



            if(!username.equals(client.getKey())) {
                /*
                    TODO:
                        perhaps the add player message can include the coordinates of the first move?
                 */

                //client.getValue().addEnemy(client.getKey());
                client.getValue().moveEnemy(client.getKey(), coord);

                switch (cell.getType()) {
                    case EXIT:
                        client.getValue().NotifyPlayerWon(username);
                        break;
                    case BONUS:
                        client.getValue().removeBonus(coord);
                        break;
                    default:
                        break;
                }

            }
        }

        //return game state
    }
}
