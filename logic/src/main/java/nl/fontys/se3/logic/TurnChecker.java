package nl.fontys.se3.logic;

import java.util.List;

public class TurnChecker {
    private int turnIndex = 0;
    private List<String> players;

    public TurnChecker(List<String> players) {
        this.players = players;
    }

    public String getCurrentPlayerName() {
        return players.get(turnIndex);
    }

    public boolean checkTurn(String username) {
        return players.get(turnIndex).equals(username);
    }

    public void takeTurn() {
        if(turnIndex < players.size() - 1) {
            turnIndex++;
        }
        else {
            turnIndex = 0;
        }
    }
}