package nl.fontys.se3.logic;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import nl.fontys.se3.logic.stubs.StubUserDAO;

import static org.junit.jupiter.api.Assertions.*;

public class UserServiceTest {
    private UserService service;

    @BeforeEach
    public void prep() {
        service = new UserService(new StubUserDAO());
    }

    @Test
    public void testGetUserByUsername() {
        service.insertUser(new User("test", "pass", 10));

        assertNotNull(service.getUserByName("test"));
    }

    @Test
    public void testGetUserByUsernameNull() {
        assertNull(service.getUserByName("test"));
    }

    @Test
    public void testCheckCredentials() {
        service.insertUser(new User("test", "pass", 10));

        assertNotNull(service.checkCredentials("test", "pass"));
    }

    @Test
    public void testUpdateScore() {
        User user = new User("test", "pass", 10);

        service.insertUser(user);
        service.updateScore(user, 10);

        assertEquals(20, service.getUserByName("test").getScore());
    }

    @Test
    public void testGetAllUsers() {
        service.insertUser(new User("test", "pass", 10));
        service.insertUser(new User("test2", "pass", 10));

        assertEquals(2, service.getAllUsers().size());
    }

}
