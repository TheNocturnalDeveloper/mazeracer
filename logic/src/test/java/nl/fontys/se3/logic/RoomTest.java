package nl.fontys.se3.logic;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import nl.fontys.se3.logic.stubs.StubMazeRacerClient;

import static org.junit.jupiter.api.Assertions.*;

public class RoomTest {
    Room room;

    @BeforeEach
    public void prep() {
        room = new Room(1, 2, 10, RoomDifficulty.EASY);
    }

    @Test
    public void testAddPlayer() {
       boolean joined = room.addPlayer("player1");
       assertTrue(joined);
    }

    @Test
    public void testAddPlayerRoomFull() {
        room.addPlayer("player1");
        room.addPlayer("player2");

        boolean joined = room.addPlayer("player3");
        assertFalse(joined);
    }

    @Test
    public void testEnter() {
        room.addPlayer("player1");

        boolean entered = room.enter("player1", new StubMazeRacerClient());

        assertTrue(entered);
    }

    @Test
    public void testIsFull() {
        room.addPlayer("player1");
        room.addPlayer("player2");

        room.enter("player1", new StubMazeRacerClient());
        room.enter("player2", new StubMazeRacerClient());

        assertTrue(room.isFull());
    }

    @Test
    public void testRemovePlayer() {
        var playerClient1 = new StubMazeRacerClient();
        var playerClient2 = new StubMazeRacerClient();

        room.addPlayer("player1");
        room.addPlayer("player2");

        room.enter("player1", playerClient1);
        room.enter("player2", playerClient2);

        room.removePlayer("user2");

        assertEquals(0, playerClient1.getOpponents().size());
    }

    @Test
    public void testStart() {
        var playerClient1 = new StubMazeRacerClient();
        var playerClient2 = new StubMazeRacerClient();

        room.addPlayer("player1");
        room.addPlayer("player2");

        room.enter("player1", playerClient1);
        room.enter("player2", playerClient2);

        room.start();

        assertTrue(playerClient1.isStarted());

    }

    //TODO: add asserts

    @Test
    public void testMovePlayer() {
        var playerClient1 = new StubMazeRacerClient();
        var playerClient2 = new StubMazeRacerClient();

        room.addPlayer("player1");
        room.addPlayer("player2");

        room.enter("player1", playerClient1);
        room.enter("player2", playerClient2);

        room.start();

        room.movePlayer("player1", new Coord(0, 1));
    }

    @Test
    public void testMovePlayerFinish() {
        var playerClient1 = new StubMazeRacerClient();
        var playerClient2 = new StubMazeRacerClient();

        room.addPlayer("player1");
        room.addPlayer("player2");

        room.enter("player1", playerClient1);
        room.enter("player2", playerClient2);

        room.start();

        room.movePlayer("player1", new Coord(0, 1));
    }

    @Test
    public void testMovePlayerBonus() {
        var playerClient1 = new StubMazeRacerClient();
        var playerClient2 = new StubMazeRacerClient();

        room.addPlayer("player1");
        room.addPlayer("player2");

        room.enter("player1", playerClient1);
        room.enter("player2", playerClient2);

        room.start();

        room.movePlayer("player1", new Coord(0, 1));
    }


}
