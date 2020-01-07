package nl.fontys.se3.presentation.models;

public class LeaderboardRecord {
    private String username;
    private int score;

    public LeaderboardRecord() {

    }

    public LeaderboardRecord(String username, int score) {
        this.username = username;
        this.score = score;
    }

    public String getUsername() {
        return username;
    }

    public int getScore() {
        return score;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
