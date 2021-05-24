package it.polito.ezshop.data;

import org.junit.Test;


import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class UserRepositoryTest {

    @Test
    public void find() {
        UserImpl user= new UserImpl();
        UserRepository repo = new UserRepository();
        Integer id=repo.create(user);
        assertEquals(id,repo.find(id).getId());
        repo.delete(user);

    }

    @Test
    public void findByUsername() {
        UserImpl user= new UserImpl();
        UserRepository repo = new UserRepository();
        repo.create(user);
        String username = "DavideM";
        repo.update(user);
        assertEquals(username,repo.findByUsername(username).getUsername());
        repo.delete(user);
    }

    @Test
    public void findAll() {
        UserRepository repo = new UserRepository();
        List<User> userList = new ArrayList<>();

        UserImpl user1 = new UserImpl();
        UserImpl user2 = new UserImpl();
        UserImpl user3 = new UserImpl();

        repo.create(user1);
        repo.create(user2);
        repo.create(user3);
        userList.add(user1);
        userList.add(user2);
        userList.add(user3);
        
        assertEquals(userList,repo.findAll());
        repo.delete(user1);
        repo.delete(user2);
        repo.delete(user3);
    }

    @Test
    public void create() {
        UserImpl user= new UserImpl();
        UserRepository repo = new UserRepository();
        Integer id=repo.create(user);
        assertEquals(id,user.getId());
        repo.delete(user);
    }

    @Test
    public void update() {
        UserImpl user= new UserImpl();
        UserRepository repo = new UserRepository();
        Integer id=repo.create(user);
        String role = "Cashier";
        String username = "DavideM";
        String password = "Fe29dqh^ad3_ad";
        user.setRole(role);
        user.setUsername("DavideM");
        user.setPassword("Fe29dqh^ad3_ad");
        User u1= repo.update(user);
        assertEquals(id,u1.getId());
        assertEquals(role,u1.getRole());
        assertEquals(username,u1.getUsername());
        assertEquals(password,u1.getPassword());
        repo.delete(user);
    }

    @Test
    public void delete() {
        UserImpl user= new UserImpl();
        UserRepository repo = new UserRepository();
        Integer id=repo.create(user);
        repo.delete(user);
        assertNull(repo.find(id));
    }
}