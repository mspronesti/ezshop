package it.polito.ezshop.data;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

import it.polito.ezshop.exceptions.*;

public class EZShopControllerImplTest {
    private static EZShopController controller;
    private static UserRepository userRepository;
    private static ProductTypeRepository productTypeRepository;
    
    @BeforeClass
    static public void init() {
    	controller =  EZShopControllerFactory.create();
    	userRepository = new UserRepository();
    	productTypeRepository = new ProductTypeRepository();
    
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
		String username = "Marco";
		String password = "1234";
		
		// empty username or empty password
		assertThrows(InvalidUsernameException.class, () -> controller.login("", password));
		assertThrows(InvalidPasswordException.class, () -> controller.login(username, ""));
		
		// wrong password
		assertNull(controller.login(username, "12345"));
		
		User user = controller.login(username, password);
		assertNotNull(user);
		assertTrue(controller.logout());
	}
	
	@Test
	public void testDeleteUser() throws InvalidUsernameException, InvalidPasswordException, InvalidUserIdException, UnauthorizedException {
		User logged = controller.login("Franco", "1234");
		Integer id1 = userRepository.findByUsername("Marco").getId();
		Integer id2 = userRepository.findByUsername("Giovanna").getId();
	    
		// unauth attempt to delete user
		assertThrows(UnauthorizedException.class, () -> controller.deleteUser(id1));
		controller.logout();
		
		logged = controller.login("Marco", "1234");
		// id null, negative or 0
		assertThrows(InvalidUserIdException.class, () -> controller.deleteUser(null));
		assertThrows(InvalidUserIdException.class, () -> controller.deleteUser(-1));
		assertThrows(InvalidUserIdException.class, () -> controller.deleteUser(0));
        
		// user doesn't exist
		assertFalse(controller.deleteUser(10));
		// correct delete
		assertTrue(controller.deleteUser(id2));
		controller.logout();	
	}
	
	@Test
	public void testGetAllUsers() throws InvalidUsernameException, InvalidPasswordException, UnauthorizedException {
		User loggedUser = controller.login("Franco", "1234");
		assertThrows(UnauthorizedException.class, () -> controller.getAllUsers());
		controller.logout();
		
		loggedUser = controller.login("Marco", "1234");
		assertTrue(controller.getAllUsers().stream().allMatch(p -> p instanceof User));
	}
	
	@Test
	public void testGetUser() throws InvalidUsernameException, InvalidPasswordException, InvalidUserIdException, UnauthorizedException {
		User logged = controller.login("Franco", "1234");
		Integer id = userRepository.findByUsername("Anna").getId();
		
		assertThrows(UnauthorizedException.class, () -> controller.getUser(id));
		controller.logout();
		
		logged = controller.login("Marco", "1234");
		
		// wrong id (negative, 0, null)
		assertThrows(InvalidUserIdException.class, () -> controller.getUser(-1));
		assertThrows(InvalidUserIdException.class, () -> controller.getUser(0));
		assertThrows(InvalidUserIdException.class, () -> controller.getUser(null));
		
		// inexistent id
		assertNull(controller.getUser(10));
		// correct id
		assertNotNull(controller.getUser(id));
	}

	@Test 
	public void testUpdateUserRight() throws InvalidUsernameException, InvalidPasswordException, InvalidUserIdException, InvalidRoleException, UnauthorizedException {
		String newRole = "Administrator";
		Integer id = userRepository.findByUsername("Anna").getId();
		
		// attempt with unauth user
		User loggedUser = controller.login("Franco", "1234");
		assertThrows(UnauthorizedException.class, () -> controller.updateUserRights(id, newRole));
		controller.logout();
		
		// attempt with auth user
		loggedUser = controller.login("Marco", "1234");
		
		// invalid id
		assertThrows(InvalidUserIdException.class, () -> controller.updateUserRights(null, newRole));
		assertThrows(InvalidUserIdException.class, () -> controller.updateUserRights(-1, newRole));
		assertThrows(InvalidUserIdException.class, () -> controller.updateUserRights(0, newRole));
		
		// invalid role
		assertThrows(InvalidRoleException.class,  () -> controller.updateUserRights(id, "fakeRole"));
		
		// inexistent user
		assertFalse(controller.updateUserRights( 10, newRole));
		
		assertTrue(controller.updateUserRights(id, newRole));
	}
	
	@Test 
	public void testCreateProductType() throws InvalidUsernameException, InvalidPasswordException, InvalidProductDescriptionException, InvalidProductCodeException, InvalidPricePerUnitException, UnauthorizedException {
		String description = "pasta";
		String productCode = "012345678967";
		double pricePerUnit = 1.23;
		String note = "";
		
		// unauth 
		User loggedUser = controller.login("Franco", "1234");
		assertThrows(UnauthorizedException.class, () -> controller.createProductType(description, productCode, pricePerUnit, note));
		
		// auth
		loggedUser = controller.login("Marco", "1234");
				
		// empty/null description
		assertThrows(InvalidProductDescriptionException.class, () -> controller.createProductType("", productCode, pricePerUnit, note));
		assertThrows(InvalidProductDescriptionException.class, () -> controller.createProductType(null, productCode, pricePerUnit, note));
		
		// empty/null/notGTin barcode
		assertThrows(InvalidProductCodeException.class, () -> controller.createProductType(description, "", pricePerUnit, note));
		assertThrows(InvalidProductCodeException.class, () -> controller.createProductType(description, "null", pricePerUnit, note));
		assertThrows(InvalidProductCodeException.class, () -> controller.createProductType(description, "012345678901", pricePerUnit, note));
		
		// negative or 0 price
		assertThrows(InvalidPricePerUnitException.class, () -> controller.createProductType(description, productCode, -5, note));
		assertThrows(InvalidPricePerUnitException.class, () -> controller.createProductType(description, productCode, 0, note));
		
		
		// correct product creating
		Integer id = controller.createProductType(description, productCode, pricePerUnit, note);
		assert(id != -1);
		// attempt to duplicate
		assert(controller.createProductType(description, productCode, pricePerUnit, note) == -1);
		controller.logout();
	}
	
	@Test
	public void testUpdateProduct() throws InvalidUsernameException, InvalidPasswordException, InvalidProductIdException, InvalidProductDescriptionException, InvalidProductCodeException, InvalidPricePerUnitException, UnauthorizedException {
		String newDescription = "pizza";
		String newCode = "012345678998";
		double newPrice = 1.15;
		String newNote = "foo";
		
		Integer id = productTypeRepository.findByBarcode("012345678905").getId();
		
		// unauth 
		User loggedUser = controller.login("Franco", "1234");
		assertThrows(UnauthorizedException.class, () -> controller.updateProduct(id, newDescription, newCode, newPrice, newNote));
				
		loggedUser = controller.login("Marco", "1234");
		
		// invalid id (null, negative, 0)
		assertThrows(InvalidProductIdException.class, () -> controller.updateProduct(null, newDescription, newCode, newPrice, newNote));
		assertThrows(InvalidProductIdException.class, () -> controller.updateProduct(-1, newDescription, newCode, newPrice, newNote));
		assertThrows(InvalidProductIdException.class, () -> controller.updateProduct(0, newDescription, newCode, newPrice, newNote));
		
		// invalid description (null, empty)
		assertThrows(InvalidProductDescriptionException.class, () -> controller.updateProduct(id, "", newCode, newPrice, newNote));
//		assertThrows(InvalidProductDescriptionException.class, () -> controller.updateProduct(id, "null", newCode, newPrice, newNote));
		
		// invalid barcode (null, empty, invalid gtin)
		assertThrows(InvalidProductCodeException.class, () -> controller.updateProduct(id, newDescription, "", newPrice, newNote));
		assertThrows(InvalidProductCodeException.class, () -> controller.updateProduct(id, newDescription, "null", newPrice, newNote));
		assertThrows(InvalidProductCodeException.class, () -> controller.updateProduct(id, newDescription, "012345678990", newPrice, newNote));

		// invalid price (negative)
		assertThrows(InvalidPricePerUnitException.class, () -> controller.updateProduct(id, newDescription, newCode, -10.5, newNote));
		
		// product doesn't exist
		assertFalse(controller.updateProduct(10, newDescription, newCode, newPrice, newNote));
		// barcode already assigned
//		assertFalse(controller.updateProduct(id, newDescription, "012345678912", newPrice, newNote));
		// correct update
		assertTrue(controller.updateProduct(id, newDescription, newCode, newPrice, newNote));
		controller.logout();
	}
	
	@Test
	public void testDeleteProduct() {
		
	}
	
	@Test
	public void testGetAllProducts() throws UnauthorizedException, InvalidUsernameException, InvalidPasswordException, InvalidRoleException, InvalidUserIdException {
		
		String[] roles = {"Marco", "Anna", "Franco"}; // Administrator, ShopManager, Cashier
		for (String role : roles) {
			controller.login(role, "1234");
			assertTrue(controller.getAllProductTypes().stream().allMatch(p -> p instanceof ProductType));
			controller.logout();
		}
		
	}
	
	@Test
	public void testGetProductByBarCode() throws InvalidUsernameException, InvalidPasswordException, InvalidProductCodeException, UnauthorizedException {
		String barCode = "012345678912";
		// unauth 
		User loggedUser = controller.login("Franco", "1234");
		assertThrows(UnauthorizedException.class, () -> controller.getProductTypeByBarCode(barCode));
				
		loggedUser = controller.login("Marco", "1234");
		// invalid bar code (null, empty, invalid gtin)
		assertThrows(InvalidProductCodeException.class, () -> controller.getProductTypeByBarCode(""));
		assertThrows(InvalidProductCodeException.class, () -> controller.getProductTypeByBarCode("null"));
		assertThrows(InvalidProductCodeException.class, () -> controller.getProductTypeByBarCode("012345678906"));
		
		// inexisting product
		assertNull(controller.getProductTypeByBarCode("012345678943"));
		assertNotNull(controller.getProductTypeByBarCode(barCode));
		controller.logout();
	}
	
	@Test
	public void testgetProductTypesByDescription() {
		
	}
	
	@Test
	public void testUpdateQuantity() {
		
	}
	
	@Test
	public void testUpdatePosition() {
		
	}
	
	

}
