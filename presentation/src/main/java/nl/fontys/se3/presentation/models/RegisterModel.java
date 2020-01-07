package nl.fontys.se3.presentation.models;

public class RegisterModel {
    private String username;
    private String password;


    public RegisterModel() {

    }

    public RegisterModel(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
