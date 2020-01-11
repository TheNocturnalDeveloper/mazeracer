package nl.fontys.se3.data;

import java.util.List;

public interface IUserDAO {
    List<UserDTO> getAllUsers();
    UserDTO getPlayerByName(String username);
    void updatePlayerScore(String username, int score);
    void insertPlayer(UserDTO player);
    UserDTO checkCredentials(String username, String password);
}
