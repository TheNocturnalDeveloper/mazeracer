package nl.fontys.se3.presentation.models;

public class StringMessage extends BasicMessage {

    private String message;

    public StringMessage() {

    }

    public StringMessage(String message, MessageType type) {
        super(type);
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
