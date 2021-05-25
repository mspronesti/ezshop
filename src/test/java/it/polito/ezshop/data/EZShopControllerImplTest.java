package it.polito.ezshop.data;

import static org.junit.Assert.*;



import org.junit.AfterClass;
import org.junit.BeforeClass;
//import org.junit.FixMethodOrder;
import org.junit.Test;
//import org.junit.runners.MethodSorters;

import it.polito.ezshop.exceptions.*;

public class EZShopControllerImplTest {
    private static EZShopController controller;
    private static Integer idCashier, idShopManager, idAdministrator;
    
    @BeforeClass
    static public void init() throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException {
    	controller =  EZShopControllerFactory.create();
    
		idCashier = controller.createUser("cashguy", "1234", "Cashier");
		idShopManager = controller.createUser("managerguy", "1234", "ShopManager");
		idAdministrator = controller.createUser("administratorguy", "1234", "Administrator");
    
    }

	@Test
	public void testCreateUser() throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException {
		String username = "johndoe";
		String password = "1234";
		String role = "Administrator";
		
		// username empty or null
		assertThrows(InvalidUsernameException.class, () -> controller.createUser("", password, role));
		assertThrows(InvalidUsernameException.class, () -> controller.createUser(null, password, role));
		
		// password empty or null
		assertThrows(InvalidPasswordException.class, () -> controller.createUser(username, "", role));
		assertThrows(InvalidPasswordException.class, () -> controller.createUser(username, null, role));
		
		// role empty or null or not respecting pattern
		assertThrows(InvalidRoleException.class, () -> controller.createUser(username, password, ""));
		assertThrows(InvalidRoleException.class, () -> controller.createUser(username, password, null));
		assertThrows(InvalidRoleException.class, () -> controller.createUser(username, password, "fakeRole"));
		
		// correct user creation
	    assert(controller.createUser(username, password, role) != -1);
	    // test duplicate user
	    assert(controller.createUser(username, password, role) == -1);
	 }
		
	@Test
	public void testLoginLogout() throws InvalidUsernameException, InvalidPasswordException {
		String username = "administratorguy";
		String password = "1234";
		
		assertThrows(InvalidUsernameException.class, () -> controller.login("", password));
		assertThrows(InvalidPasswordException.class, () -> controller.login(username, ""));
		
		User user = controller.login(username, password);
		assertNotNull(user);
		assertTrue(controller.logout());
	}
	
	@Test
	public void testDeleteUser() throws InvalidUsernameException, InvalidPasswordException, InvalidUserIdException, UnauthorizedException {
		User logged = controller.login("cashguy", "1234");
	    
		assertThrows(UnauthorizedException.class, () -> controller.deleteUser(1));
		controller.logout();
		
		logged = controller.login("administratorguy", "1234");
		// id null, negative or 0
		assertThrows(InvalidUserIdException.class, () -> controller.deleteUser(null));
		assertThrows(InvalidUserIdException.class, () -> controller.deleteUser(-1));
		assertThrows(InvalidUserIdException.class, () -> controller.deleteUser(0));
        
		// user doesn't exist
		assertFalse(controller.deleteUser(10));
		// correct delete
		assertTrue(controller.deleteUser(idCashier));
		controller.logout();	
	}
	
	@Test
	public void testGetAllUsers() throws InvalidUsernameException, InvalidPasswordException {
		User loggedUser = controller.login("cashguy", "1234");
		assertThrows(UnauthorizedException.class, () -> controller.getAllUsers());
		controller.logout();
		loggedUser = controller.login("administratorguy", "1234");
		// TODO: to be completed
		
	}
	
	@Test
	public void testGetUser() throws InvalidUsernameException, InvalidPasswordException, InvalidUserIdException, UnauthorizedException {
		User loggedUser = controller.login("cashguy", "1234");
		assertThrows(UnauthorizedException.class, () -> controller.getUser(idShopManager));
		controller.logout();
		loggedUser = controller.login("administratorguy", "1234");
		
		// wrong id (negative, 0, null)
		assertThrows(InvalidUserIdException.class, () -> controller.getUser(-1));
		assertThrows(InvalidUserIdException.class, () -> controller.getUser(0));
		assertThrows(InvalidUserIdException.class, () -> controller.getUser(null));
		
		// unexisting id
		assertNull(controller.getUser(10));
		// correct id
		assertNotNull(controller.getUser(idShopManager));
	}
	
	@Test 
	public void testUpdateUserRight() throws InvalidUsernameException, InvalidPasswordException, InvalidUserIdException, InvalidRoleException, UnauthorizedException {
		String newRole = "Administrator";
		
		// attempt with unauth user
		User loggedUser = controller.login("cashguy", "1234");
		assertThrows(UnauthorizedException.class, () -> controller.updateUserRights(idShopManager, newRole));
		controller.logout();
		
		// attempt with auth user
		loggedUser = controller.login("administratorguy", "1234");
		
		// invalid id
		assertThrows(InvalidUserIdException.class, () -> controller.updateUserRights(null, newRole));
		assertThrows(InvalidUserIdException.class, () -> controller.updateUserRights(-1, newRole));
		assertThrows(InvalidUserIdException.class, () -> controller.updateUserRights(0, newRole));
		
		// invalid role
		assertThrows(InvalidRoleException.class,  () -> controller.updateUserRights(idShopManager, "fakeRole"));
		
		assertFalse(controller.updateUserRights( 10, newRole));
		assertTrue(controller.updateUserRights(idShopManager, newRole));
	}
	

	
	@AfterClass
	static public void clear() {
	   controller.reset();
	}
	
	

}
