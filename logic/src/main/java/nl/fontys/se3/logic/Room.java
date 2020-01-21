package nl.fontys.se3.logic;


import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class Room {
    private final int id;
    private final int limit;
    private final int roomScore;

    private Map<String, ClientContext> players;
    private Maze maze;
    private  MoveChecker moveChecker;
    private RoomDifficulty difficulty;
    private GameStatus status;
    private List<String> matchResult = null;

    private static final int bonusValue = 5_000;

    public Room(int id, int limit, int roomScore, RoomDifficulty difficulty) {
        this.id = id;
        this.limit = limit;
        this.roomScore = roomScore;
        this.difficulty = difficulty;

        players = new ConcurrentHashMap();

        int mazeSize = difficulty.getMazeSize();

        maze = new Maze(mazeSize, mazeSize);
        moveChecker = new MoveChecker(maze);

        status = GameStatus.PENDING;
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

    public Map<String, ClientContext> getPlayers() {
        return Collections.unmodifiableMap(players);
    }
    public GameStatus getStatus() { return status; }


    public synchronized boolean addPlayer(String username) {
        if(players.size() >= limit) return false;
        players.put(username, new ClientContext());
        return true;
    }

    public boolean enter(String username, IMazeRacerClient client) {
        if(!players.containsKey(username)) return false;
        players.get(username).setClient(client);

        return true;
    }

    public boolean isFull() {
        return players.values().stream().filter(c -> c.getClient() != null).count() >= limit;
    }

    public void start() {
        maze.generateMaze();
        for (var client: players.entrySet()) {
            client.getValue().getClient().loadMaze(maze.getCells());
            //TODO: CLEAN THIS UP
            client.getValue().getPos().setX(maze.getStart().getX());
            client.getValue().getPos().setY(maze.getStart().getY());

            for(String name: players.keySet()) {
                if(!name.equals(client.getKey())) {
                    client.getValue().getClient().addEnemy(name);
                }
            }
        }
        status = GameStatus.STARTED;
    }

    public void removePlayer(String username) {

        players.remove(username);

        for (var client: players.entrySet()) {
            client.getValue().getClient().removeEnemy(username);
        }
    }


    private void updatePlayers(String username, Coord coord, Cell cell) {
        for (var client: players.entrySet()) {
            if(!username.equals(client.getKey())) {
                client.getValue().getClient().moveEnemy(username, coord);

                if(cell.getType() == CellType.BONUS) {
                    client.getValue().getClient().removeBonus(coord);
                }
            }
        }
    }


    public synchronized void movePlayer(String username, Coord coord) {
        ClientContext player = players.get(username);

        if(!moveChecker.checkMove(player.getPos(), coord)) {
            player.getClient().resetPosition(player.getPos());
            return;
        }

        player.getPos().setX(coord.getX());
        player.getPos().setY(coord.getY());

        Cell cell = maze.get(coord.getX(), coord.getY());

        switch (cell.getType()) {
            case EXIT:
                player.setEndTime(System.currentTimeMillis());
                break;
            case BONUS:
                player.incrementBonusCount();
                maze.get(coord.getX(), coord.getY()).setType(CellType.EMPTY);
                break;
            default:
                break;
        }

        updatePlayers(username, coord, cell);

        if(players.values().stream().allMatch(ClientContext::hasFinished)) {
            status = GameStatus.OVER;

            matchResult = players.entrySet().stream().sorted((a, b) -> {
                long scoreA = a.getValue().calculateScore(bonusValue);
                long scoreB = b.getValue().calculateScore(bonusValue);
                return  Long.compare(scoreA, scoreB);
            }).map(Map.Entry::getKey).collect(Collectors.toList()); //method update match result

            for (var client: players.entrySet()) {
                client.getValue().getClient().notifyPlayerWon(matchResult.get(0)); //move this to update players
            }
        }
    }

    public List<String> getMatchResult() {
        return Collections.unmodifiableList(matchResult);
    }
}
