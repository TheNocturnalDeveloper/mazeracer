package nl.fontys.se3.presentation.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LeaderboardMessage extends BasicMessage {
    private List<LeaderboardRecord> records;

    public LeaderboardMessage() {
        records = new ArrayList();
    }

    public LeaderboardMessage(List<LeaderboardRecord> records) {
        this.records = records;
    }

    public List<LeaderboardRecord> getRecords() {
        return Collections.unmodifiableList(records);
    }

    public void addRecord(String username, int score) {
        records.add(new LeaderboardRecord(username, score));
    }

    public void addRecord(LeaderboardRecord record) {
        records.add(record);
    }
}
