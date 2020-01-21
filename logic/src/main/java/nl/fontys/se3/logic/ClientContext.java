package nl.fontys.se3.logic;

public class ClientContext {
    private IMazeRacerClient client;
    private int bonusCount;
    private Coord pos;
    private long endTime;

    public ClientContext() {
        client = null;
        bonusCount = 0;
        pos = new Coord(0,0);
        endTime = 0;
    }

    public Coord getPos() {
        return pos;
    }

    public int getBonusCount() {
        return bonusCount;
    }

    public IMazeRacerClient getClient() {
        return client;
    }

    public void incrementBonusCount() {
        bonusCount++;
    }

    public void setClient(IMazeRacerClient client) {
        this.client = client;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public long calculateScore(int bonusValue) {
        return endTime - (bonusValue * bonusCount);
    }

    public boolean hasFinished() {
        return endTime != 0;
    }
}
