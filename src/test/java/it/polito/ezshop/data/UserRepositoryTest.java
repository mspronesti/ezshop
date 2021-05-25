package it.polito.ezshop.data;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;


import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class UserRepositoryTest {

    private static UserRepository repo = new UserRepository();
    private static List<User> userList=new ArrayList<>();
    private static UserImpl user = new UserImpl();
    private static Integer userId;
    private static String username="MarcoC";

    @BeforeClass
    static public void init(){
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

        repo.update(user);

        assertEquals(newRole,user.getRole());
        assertEquals(newUsername,user.getUsername());
        assertEquals(newPassword,user.getPassword());
    }

    @Test
    public void delete() {
        repo.delete(user);
        assertNull(repo.find(userId));
    }

    @AfterClass
    static public void stop(){

        if((userList=repo.findAll())!=null)
            for (User u:userList) {
                repo.delete(u);
            }
    }
}