package nl.fontys.se3.presentation.models;

public class PlayerMoveMessage extends CoordMessage {

    public PlayerMoveMessage() {
        setType(MessageType.MOVE);
    }

    public PlayerMoveMessage(int x, int y) {
        super(x, y, MessageType.MOVE);
    }
}
