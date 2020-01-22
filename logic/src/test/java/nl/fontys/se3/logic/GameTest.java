package nl.fontys.se3.logic;

import nl.fontys.se3.logic.Game;
import nl.fontys.se3.logic.RoomDifficulty;
import nl.fontys.se3.logic.User;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.*;

public class GameTest {
    private Game game;

    @BeforeEach
    public void prep() {
        game = new Game();
    }

    @Test
    public void testFindRoomNoRooms() {
        User user = new User("test", "test", 0);
        assertNull(game.findRoom(user, RoomDifficulty.EASY));
    }


    @Test
    public void testFindRoomOneRoom() {
        game.createRoom(2, 10, RoomDifficulty.EASY);
        User user = new User("test", "test", 0);
        assertNotNull(game.findRoom(user, RoomDifficulty.EASY));
    }


    @Test
    public void testFindRoomNoMatchingDifficulty() {
        game.createRoom(2, 10, RoomDifficulty.HARD);
        User user = new User("test", "test", 0);
        assertNull(game.findRoom(user, RoomDifficulty.EASY));
    }

    @Test
    public void testFindRoomMultipleRooms() {
        game.createRoom(2, 10, RoomDifficulty.EASY);
        game.createRoom(2, 20, RoomDifficulty.EASY);

        User user = new User("test", "test", 16);
        assertEquals(game.findRoom(user, RoomDifficulty.EASY).getId(), 2);
    }

    @Test
    public void testCreateRoom() {
       game.createRoom(2, 50, RoomDifficulty.EASY);
       assertEquals(RoomDifficulty.EASY, game.getRoom(1).getDifficulty());
       assertEquals(50, game.getRoom(1).getRoomScore());
    }

    @Test
    public void removeRoom() {
        game.createRoom(2, 50, RoomDifficulty.EASY);
        game.removeRoom(game.getRoom(1));

        assertNull(game.getRoom(1));
    }

}
