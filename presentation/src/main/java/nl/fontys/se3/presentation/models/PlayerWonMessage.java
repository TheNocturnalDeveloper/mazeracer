package nl.fontys.se3.presentation.models;

public class PlayerWonMessage extends StringMessage {
    public PlayerWonMessage() {
        setNotification(true);
        setType(MessageType.PLAYER_WON);
    }

    public PlayerWonMessage(String message) {
        super(message, MessageType.PLAYER_WON);
        setNotification(true);
    }
}
