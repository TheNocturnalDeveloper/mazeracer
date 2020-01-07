package nl.fontys.se3.presentation.models;

public class ResetPositionMessage extends CoordMessage {
    public ResetPositionMessage() {
        setType(MessageType.RESET_POSITION);
    }
    public ResetPositionMessage (int x, int y){
        super(x,y, MessageType.RESET_POSITION);
    }
}
