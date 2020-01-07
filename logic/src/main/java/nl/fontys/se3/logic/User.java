package nl.fontys.se3.logic;

public class User {
    private final String username;
    private final String password;
    private int score;

    public User(String username, String password, int score) {
        this.username = username;
        this.password = password;
        this.score = score;
    }

    public int getScore() {
        return score;
    }

    public void addScore(int score) {
        this.score += score;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
