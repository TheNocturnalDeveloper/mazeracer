package nl.fontys.se3.presentation.models;

public class PlayerLeaveMessage extends StringMessage {

    public PlayerLeaveMessage(String username) {
        super(username, MessageType.PLAYER_LEFT);
    }
}
