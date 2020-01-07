package nl.fontys.se3.presentation.models;

public class PlayerJoinMessage extends StringMessage{
    public PlayerJoinMessage(String username) {
        super(username, MessageType.PLAYER_JOINED);
        setNotification(false);
    }
}
