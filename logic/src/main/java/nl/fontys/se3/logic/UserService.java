package nl.fontys.se3.logic;

import nl.fontys.se3.data.UserDAO;
import nl.fontys.se3.data.UserDTO;

import java.util.List;
import java.util.stream.Collectors;

public class UserService {
    private final UserDAO db;

    public UserService(UserDAO db) {
        this.db = db;
    }

    public void insertUser(User user) {
        db.insertPlayer(new UserDTO(user.getUsername(), user.getPassword()));
    }

    public void updateScore(User user, int score) {
        db.updatePlayerScore(user.getUsername(), user.getScore() + score);
    }

    public User getUserByName(String username) {
       UserDTO user = db.getPlayerByName(username);
       return  new User(user.getUsername(), user.getPassword(), user.getScore());
    }

    public List<User> getAllUsers() {
        return db.getAllUsers()
                .stream().map(u -> new User(u.getUsername(), u.getPassword(), u.getScore()))
                .collect(Collectors.toList());
    }

    public User checkCredentials(String username, String password) {
        UserDTO user = db.checkCredentials(username, password);

        if(user == null) return null;

        return new User(user.getUsername(), user.getPassword(), user.getScore());
    }
}
