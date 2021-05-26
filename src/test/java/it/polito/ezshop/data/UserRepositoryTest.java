package it.polito.ezshop.data;

import org.junit.*;


import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class UserRepositoryTest {
    private static final UserRepository repo = new UserRepository();

    @Test
    public void find() {
        Integer userId=5;
        assertEquals(userId,repo.find(userId).getId());
    }

    @Test
    public void findAll() {
        assertEquals(repo.findAll().getClass(), ArrayList.class);
        assertEquals(repo.findAll().get(0).getClass(), UserImpl.class);
    }

    @Test
    public void findByUsername() {
        String username = "Beatrice";
        assertEquals(username, repo.findByUsername(username).getUsername());
    }


    @Test
    public void update() {
        String newUsername="PaoloM";
        String newPassword="2345";
        String newRole="ShopManager";

        User user = repo.find(6);

        user.setUsername(newUsername);
        user.setRole(newRole);
        user.setPassword(newPassword);

        User updated=repo.update(user);

        assertEquals(newPassword, updated.getPassword());
        assertEquals(newRole, updated.getRole());
        assertEquals(newUsername, updated.getUsername());
    }

    @Test
    public void create() {
        assertTrue(repo.create(new UserImpl())>0);
    }

    @Test
    public void delete() {
        int id;
        repo.delete(repo.find(id=repo.create(new UserImpl())));
        assertNull(repo.find(id));
    }
}