package nl.fontys.se3.presentation.models;

public class BasicMessage {
    private MessageType type;
    private boolean isNotification = false;

    public BasicMessage() {

    }

    public BasicMessage(MessageType type) { this.type = type; }

    public MessageType getType() {
        return type;
    }

    public void setType(MessageType type) { this.type = type; }

    public boolean isNotification() {
        return isNotification;
    }

    public void setNotification(boolean notification) {
        isNotification = notification;
    }
}
