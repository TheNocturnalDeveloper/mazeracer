package nl.fontys.se3.logic;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Game {
    private List<Room> rooms;
    private int currentMaxId;

    public Game() {
        rooms = new ArrayList();
        currentMaxId = 1;
    }

    public Room createRoom(int limit, int playerScore, RoomDifficulty difficulty) {
        Room room = new Room(currentMaxId++, limit, playerScore, difficulty);
        rooms.add(room);
        return room;
    }

    public void removeRoom(Room room) {
        rooms.remove(room);
    }


    public Room findRoom(User user) {
        var result = rooms
                .stream()
                .filter(room -> room.getPlayers().size() < room.getLimit())
                .min(Comparator.comparing(Room::getRoomScore, (r1, r2) -> {

            Integer diff1 = Math.abs(r1 - user.getScore());
            Integer diff2 = Math.abs(r2 - user.getScore());
            return diff1.compareTo(diff2);

        }));

        if(result.isEmpty()) {
            return null;
        }
        return result.get();
    }

    public Room getRoom(int id) {
        var roomOption = rooms.stream().filter(r -> r.getId() == id).findFirst();

        return roomOption.orElse(null);

    }
}
