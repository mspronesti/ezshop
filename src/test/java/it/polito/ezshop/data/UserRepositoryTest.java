package it.polito.ezshop.data;

import org.junit.*;


import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class UserRepositoryTest {

    private static UserRepository repo = new UserRepository();
    private static List<User> userList=new ArrayList<>();
    private static UserImpl user;
    private static Integer userId;
    private static String username="MarcoC";

    @Before
    public void init(){
        user = new UserImpl();
        user.setUsername(username);
        user.setPassword("password");
        user.setRole("Cashier");
        userId=repo.create(user);
    }


    @Test
    public void find() {
        assertEquals(userId,repo.find(userId).getId());
    }

    @Test
    public void findByUsername() {
        assertEquals(username,repo.findByUsername(username).getUsername());
    }

    @Test
    public void findAll() {
        List<Integer> idArray = new ArrayList<>();
        UserImpl user2 = new UserImpl();
        UserImpl user3 = new UserImpl();

        userList.add(user2);
        userList.add(user3);

        idArray.add(userId);

        for (User entry:userList) {
            idArray.add((repo.create(entry)));
        }

        userList=repo.findAll();

        for (User entry:userList) {
            assertTrue(idArray.contains(entry.getId()));
        }

    }

    @Test
    public void create() {
        assertTrue(userId>0);
    }

    @Test
    public void update() {
        String newRole = "Administrator";
        String newUsername = "DavideM";
        String newPassword = "Fe29dqh^ad3_ad";

        user.setRole(newRole);
        user.setUsername(newUsername);
        user.setPassword(newPassword);

        User updated = repo.update(user);

        assertEquals(newRole,updated.getRole());
        assertEquals(newUsername,updated.getUsername());
        assertEquals(newPassword,updated.getPassword());
    }

    @Test
    public void delete() {
        repo.delete(user);
        assertNull(repo.find(userId));
    }

    @After
    public void stop(){
        userList=repo.findAll();
        for (User u:userList) {
            repo.delete(u);
        }
        userList.clear();
    }
}