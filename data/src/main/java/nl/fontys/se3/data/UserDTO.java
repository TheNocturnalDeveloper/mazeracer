package nl.fontys.se3.data;

public class UserDTO {
    private final String username;
    private final String password;
    private int easyScore;
    private int mediumScore;
    private int hardScore;

    public UserDTO(String username, String password, int easyScore, int mediumScore, int hardScore) {
        this.username = username;
        this.password = password;

        this.easyScore = easyScore;
        this.mediumScore = mediumScore;
        this.hardScore = hardScore;
    }

    public UserDTO(String username, String password) {
        this.username = username;
        this.password = password;
        this.easyScore = 0;
        this.mediumScore = 0;
        this.hardScore = 0;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }


    public int getScore() {
        return easyScore;
    }

    public void setScore(int score) {
       this.easyScore = score;
    }
}
