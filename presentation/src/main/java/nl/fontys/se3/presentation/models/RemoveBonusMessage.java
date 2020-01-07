package nl.fontys.se3.presentation.models;


public class RemoveBonusMessage extends CoordMessage {

    public RemoveBonusMessage() {
        setType(MessageType.REMOVE_BONUS);
    }

    public RemoveBonusMessage(int x, int y) {
        super(x, y, MessageType.REMOVE_BONUS);
    }
}
