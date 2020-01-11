package nl.fontys.se3.logic;

import nl.fontys.se3.logic.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UserTest {
    User user;

    @BeforeEach
    public void prep() {
        user = new User("test", "pass", 10);
    }

    @Test
    public void testGetUsername() {
        assertEquals("test", user.getUsername());
    }

    @Test
    public void testGetPassword() {
        assertEquals("pass", user.getPassword());
    }

    @Test
    public void testGetScore() {
        assertEquals(10, user.getScore());
    }

    @Test
    public void testAddScore() {
        user.addScore(10);

        assertEquals(20, user.getScore());
    }


}
