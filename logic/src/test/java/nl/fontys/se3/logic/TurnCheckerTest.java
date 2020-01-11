package nl.fontys.se3.logic;

import nl.fontys.se3.logic.TurnChecker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TurnCheckerTest {

    private TurnChecker checker;

    @BeforeEach
    public void prep() {
        var players = new ArrayList<String>();
        players.add("player1");
        players.add("player2");

        checker = new TurnChecker(players);
    }

    @Test
    public void testGetCurrentPlayerName() {
        assertEquals("player1", checker.getCurrentPlayerName());
    }

    @Test
    public void testFirstTurn() {
        assertTrue(checker.checkTurn("player1"));
    }

    @Test
    public void testTakeTurn() {
        checker.takeTurn();
        assertTrue(checker.checkTurn("player2"));
    }

    @Test
    public void testRoundReset() {
        checker.takeTurn();
        checker.takeTurn();

        assertTrue(checker.checkTurn("player1"));
    }


}
