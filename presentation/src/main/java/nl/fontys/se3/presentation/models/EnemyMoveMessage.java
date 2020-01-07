package nl.fontys.se3.presentation.models;

public class EnemyMoveMessage extends PlayerMoveMessage {
    private String username;

    public EnemyMoveMessage(String username, int x, int y) {
        super(x, y);

        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
