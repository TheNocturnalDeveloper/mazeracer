package nl.fontys.se3.logic.stubs;

import nl.fontys.se3.data.IUserDAO;
import nl.fontys.se3.data.UserDTO;

import java.util.ArrayList;
import java.util.List;

public class StubUserDAO implements IUserDAO {

    private List<UserDTO> users;

    public StubUserDAO() {
        users = new ArrayList();
    }

    @Override
    public List<UserDTO> getAllUsers() {
        return users;
    }

    @Override
    public UserDTO getPlayerByName(String username) {
        var optionalUser = users.stream().filter(u -> u.getUsername().equals(username)).findFirst();
        return optionalUser.orElse(null);
    }

    @Override
    public void updatePlayerScore(String username, int score) {
        var player = getPlayerByName(username);

        if(player != null) {
            player.setScore(player.getScore() + score);
        }
    }

    @Override
    public void insertPlayer(UserDTO player) {
        users.add(player);
    }

    @Override
    public UserDTO checkCredentials(String username, String password) {
        var player = getPlayerByName(username);

        if(player != null && player.getPassword().equals(password)) {
            return player;
        }

        return null;
    }
}
