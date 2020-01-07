package nl.fontys.se3.data;

public class UserDTO {
    private final String username;
    private final String password;
    private final int score;


    public UserDTO(String username, String password, int score) {
        this.username = username;
        this.password = password;
        this.score = score;
    }

    public UserDTO(String username, String password) {
        this.username = username;
        this.password = password;
        this.score = 0;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }


    public int getScore() {
        return score;
    }
}
