package it.polito.ezshop.data;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

import it.polito.ezshop.exceptions.*;

public class EZShopControllerImplTest {
    private static EZShopController controller;
    private static UserRepository userRepository;
    private static ProductTypeRepository productTypeRepository;
    private static OrderRepository orderRepository; 
    private static BalanceOperationRepository balanceOperationRepository;
    private static CustomerRepository customerRepository;
    
    @BeforeClass
    static public void init() {
    	controller =  EZShopControllerFactory.create();
    	userRepository = new UserRepository();
    	productTypeRepository = new ProductTypeRepository();
    	orderRepository = new OrderRepository();
        balanceOperationRepository = new BalanceOperationRepository();
        customerRepository  = new CustomerRepository();
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
		
		Integer id1 = userRepository.findByUsername("Marco").getId();
		Integer id2 = userRepository.findByUsername("Giovanna").getId();
	    
		// unauth attempt to delete user
		controller.login("Franco", "1234");
		assertThrows(UnauthorizedException.class, () -> controller.deleteUser(id1));
		controller.logout();
		
		controller.login("Marco", "1234");
		// id null, negative or 0
		assertThrows(InvalidUserIdException.class, () -> controller.deleteUser(null));
		assertThrows(InvalidUserIdException.class, () -> controller.deleteUser(-1));
		assertThrows(InvalidUserIdException.class, () -> controller.deleteUser(0));
        
		// user doesn't exist
		assertFalse(controller.deleteUser(10));
		// correct delete
		assertTrue(controller.deleteUser(id2));
	}
	
	@Test
	public void testGetAllUsers() throws InvalidUsernameException, InvalidPasswordException, UnauthorizedException {
		controller.login("Franco", "1234");
		assertThrows(UnauthorizedException.class, () -> controller.getAllUsers());
		controller.logout();
		
		controller.login("Marco", "1234");
		assertTrue(controller.getAllUsers().stream().allMatch(p -> p instanceof User));
	}
	
	@Test
	public void testGetUser() throws InvalidUsernameException, InvalidPasswordException, InvalidUserIdException, UnauthorizedException {
		Integer id = userRepository.findByUsername("Anna").getId();
		
		controller.login("Franco", "1234");
		assertThrows(UnauthorizedException.class, () -> controller.getUser(id));
		controller.logout();
		
		controller.login("Marco", "1234");
		
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
		controller.login("Franco", "1234");
		assertThrows(UnauthorizedException.class, () -> controller.updateUserRights(id, newRole));
		controller.logout();
		
		// attempt with auth user
		controller.login("Marco", "1234");
		
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
		String description = "Pasta";
		String productCode = "012345678967";
		double pricePerUnit = 1.23;
		String note = "";
		
		// unauth 
		controller.login("Franco", "1234");
		assertThrows(UnauthorizedException.class, () -> controller.createProductType(description, productCode, pricePerUnit, note));
		controller.logout();

		// auth
		controller.login("Marco", "1234");
				
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
		
		
		// correct product creation
		assert(controller.createProductType(description, productCode, pricePerUnit, note) != -1);
		// attempt to duplicate
		assert(controller.createProductType(description, productCode, pricePerUnit, note) == -1);
	}
	
	@Test
	public void testUpdateProduct() throws InvalidUsernameException, InvalidPasswordException, InvalidProductIdException, InvalidProductDescriptionException, InvalidProductCodeException, InvalidPricePerUnitException, UnauthorizedException {
		String newDescription = "pizza";
		String newCode = "012345678998";
		double newPrice = 1.15;
		String newNote = "foo";
		
		Integer id = productTypeRepository.findByBarcode("012345678905").getId();
		
		// unauth 
		controller.login("Franco", "1234");
		assertThrows(UnauthorizedException.class, () -> controller.updateProduct(id, newDescription, newCode, newPrice, newNote));
		controller.logout();

		controller.login("Marco", "1234");
		
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
	}
	
	@Test
	public void testDeleteProduct() throws InvalidUsernameException, InvalidPasswordException {
		Integer id = productTypeRepository.findByBarcode("012345678929").getId();
		
		// unauth
		controller.login("Franco", "1234");
		assertThrows(UnauthorizedException.class, () -> controller.deleteProductType(id));
		controller.logout();
		
		controller.login("Marco", "1234");
		// invalid product type (null, 0 , negative)
		assertThrows(InvalidProductIdException.class, () -> controller.deleteProductType(-1));
		// change annotation to Min(1)
//		assertThrows(InvalidProductIdException.class, () -> controller.deleteProductType(0)); 
		assertThrows(InvalidProductIdException.class, () -> controller.deleteProductType(null));

	}
	
	@Test
	public void testGetAllProducts() throws UnauthorizedException, InvalidUsernameException, InvalidPasswordException, InvalidRoleException, InvalidUserIdException {
		// nobody logged
		assertThrows(UnauthorizedException.class, () -> controller.getAllProductTypes());
		
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
		controller.login("Franco", "1234");
		assertThrows(UnauthorizedException.class, () -> controller.getProductTypeByBarCode(barCode));
				
		controller.login("Marco", "1234");
		// invalid bar code (null, empty, invalid gtin)
		assertThrows(InvalidProductCodeException.class, () -> controller.getProductTypeByBarCode(""));
		assertThrows(InvalidProductCodeException.class, () -> controller.getProductTypeByBarCode("null"));
		assertThrows(InvalidProductCodeException.class, () -> controller.getProductTypeByBarCode("012345678906"));
		
		// inexisting product
		assertNull(controller.getProductTypeByBarCode("012345678943"));
		assertNotNull(controller.getProductTypeByBarCode(barCode));
	}
	
	@Test
	public void testGetProductTypesByDescription() throws InvalidUsernameException, InvalidPasswordException, UnauthorizedException {
		String description = "Glasses";
		// unauth 
		controller.login("Franco", "1234");
		assertThrows(UnauthorizedException.class, () -> controller.getProductTypesByDescription(description));
				
		controller.login("Marco", "1234");
		List<ProductType> products = controller.getProductTypesByDescription(description);
		
		assertTrue(products.isEmpty() || products.stream().allMatch( p -> p instanceof ProductType));
	}
	
	@Test
	public void testUpdateQuantity() throws InvalidUsernameException, InvalidPasswordException, InvalidProductIdException, UnauthorizedException {
		// unauth 
		controller.login("Franco", "1234");
		assertThrows(UnauthorizedException.class, () -> controller.updateQuantity(1, 10));
		controller.logout();
		
		controller.login("Marco", "1234");
		
		// invalid id (negative, 0, null)
		assertThrows(InvalidProductIdException.class, () -> controller.updateQuantity(-1, 10));
		assertThrows(InvalidProductIdException.class, () -> controller.updateQuantity(0, 10));
		assertThrows(InvalidProductIdException.class, () -> controller.updateQuantity(null, 10));
		
		// negative quantity implying negative total quantity 
		assertFalse(controller.updateQuantity(1, -200));
		
		// update product without given location
		assertFalse(controller.updateQuantity(2, 5));
		
		// correct update
		assertTrue(controller.updateQuantity(1, 10));
		
	}
	
	@Test
	public void testUpdatePosition() throws InvalidUsernameException, InvalidPasswordException, InvalidProductIdException, InvalidLocationException, UnauthorizedException {
		Integer productId = productTypeRepository.findByBarcode("012345678929").getId();
		String newPos = "18-C-9";
		
		// unauth 
		controller.login("Franco", "1234");
		assertThrows(UnauthorizedException.class, () -> controller.updatePosition(productId, newPos));
		controller.logout();
		
		controller.login("Marco", "1234");
		
		// invalid id (negative, 0, null)
		assertThrows(InvalidProductIdException.class, () -> controller.updatePosition(-1, newPos));
		assertThrows(InvalidProductIdException.class, () -> controller.updatePosition(0, newPos));
		assertThrows(InvalidProductIdException.class, () -> controller.updatePosition(null, newPos));
		
		// invalid location
		assertThrows(InvalidLocationException.class, () -> controller.updatePosition(productId, "aa-1-1"));
		assertThrows(InvalidLocationException.class, () -> controller.updatePosition(productId, "1-ddc-88"));
		assertThrows(InvalidLocationException.class, () -> controller.updatePosition(productId, "1A88"));

		// correct update
		assertTrue(controller.updatePosition(productId, newPos));
	}
	
	@Test 
	public void testIssueOrder() throws InvalidUsernameException, InvalidPasswordException, InvalidProductCodeException, InvalidQuantityException, InvalidPricePerUnitException, UnauthorizedException {
		String productCode = "012345678912";
		Integer quantity = 5;
		double pricePerUnit = 1.78;
		
		controller.login("Franco", "1234"); // unauth
		assertThrows(UnauthorizedException.class, () -> controller.issueOrder(productCode, quantity, pricePerUnit));
		
		controller.login("Marco", "1234");
		
		// invalid bar code (null, empty, invalid gtin)
		assertThrows(InvalidProductCodeException.class, () -> controller.issueOrder("", quantity, pricePerUnit));
		assertThrows(InvalidProductCodeException.class, () -> controller.issueOrder("null", quantity, pricePerUnit));
		assertThrows(InvalidProductCodeException.class, () -> controller.issueOrder("012345678906", quantity, pricePerUnit));
		
		// negative or 0 quantity
		assertThrows(InvalidQuantityException.class, () -> controller.issueOrder(productCode, -1, pricePerUnit));
		assertThrows(InvalidQuantityException.class, () -> controller.issueOrder(productCode, 0, pricePerUnit));

		//negative or 0 price
		assertThrows(InvalidPricePerUnitException.class, () -> controller.issueOrder(productCode, quantity, -1.84));
		assertThrows(InvalidPricePerUnitException.class, () -> controller.issueOrder(productCode, quantity, 0));
		
		// inexistent product
		assert( controller.issueOrder("123456789999", 10, 10.24) == -1 );
		
		// correct order
		assert(controller.issueOrder(productCode, quantity, pricePerUnit) != -1);
	}
	
	@Test
	public void testPayOrderFor() throws InvalidUsernameException, InvalidPasswordException, InvalidProductCodeException, InvalidQuantityException, InvalidPricePerUnitException, UnauthorizedException {
		String productCode = "012345678912";
		Integer quantity = 5;
		double pricePerUnit = 1.78;
		
		controller.login("Franco", "1234"); // unauth
		assertThrows(UnauthorizedException.class, () -> controller.payOrderFor(productCode, quantity, pricePerUnit));
		
		controller.login("Marco", "1234");
		
		// invalid bar code (null, empty, invalid gtin)
		assertThrows(InvalidProductCodeException.class, () -> controller.payOrderFor("", quantity, pricePerUnit));
		assertThrows(InvalidProductCodeException.class, () -> controller.payOrderFor("null", quantity, pricePerUnit));
		assertThrows(InvalidProductCodeException.class, () -> controller.payOrderFor("012345678906", quantity, pricePerUnit));
		
		// negative or 0 quantity
		assertThrows(InvalidQuantityException.class, () -> controller.payOrderFor(productCode, -1, pricePerUnit));
		assertThrows(InvalidQuantityException.class, () -> controller.payOrderFor(productCode, 0, pricePerUnit));

		//negative or 0 price
		assertThrows(InvalidPricePerUnitException.class, () -> controller.payOrderFor(productCode, quantity, -1.84));
		assertThrows(InvalidPricePerUnitException.class, () -> controller.payOrderFor(productCode, quantity, 0));
		
		// inexistent product
		assert( controller.payOrderFor("123456789999", 10, 10.24) == -1 );
		
		// not enough balance
		assert(controller.payOrderFor(productCode, quantity*100, pricePerUnit) == -1);
		
		// correct order
		assert(controller.payOrderFor(productCode, quantity, pricePerUnit) != -1);
		
	}
	
	@Test
	public void testPayOrder() throws InvalidUsernameException, InvalidPasswordException, InvalidOrderIdException, UnauthorizedException {
		controller.login("Franco", "1234"); // unauth
		assertThrows(UnauthorizedException.class, () -> controller.payOrder(6));
		
		controller.login("Marco", "1234");
		// wrong id
		assertThrows(InvalidOrderIdException.class, () -> controller.payOrder(-1));
		assertThrows(InvalidOrderIdException.class, () -> controller.payOrder(0));
		assertThrows(InvalidOrderIdException.class, () -> controller.payOrder(null));

		
		// inexistent order
		assertFalse(controller.payOrder(8));
		// order not in ISSUED state (ne va creato un'altro nel db)
		// ...
		// correct pay
		assertTrue(controller.payOrder(6)); 
	}
	
	@Test
	public void testRecordOrderArrival() throws InvalidUsernameException, InvalidPasswordException {
		// creare nel db un prodotto nello stato PAYED e CON LOCATION ASSEGNATA
	}
	
	@Test
	public void testGetAllOrders() throws InvalidUsernameException, InvalidPasswordException, UnauthorizedException {
		controller.login("Franco", "1234"); // unauth
		assertThrows(UnauthorizedException.class, () -> controller.payOrder(6));
		
		controller.login("Marco", "1234");
		assertTrue(controller.getAllOrders().stream().allMatch(p -> p instanceof Order));
	}
	
	@Test
	public void testDefineCustomer() throws InvalidUsernameException, InvalidPasswordException, InvalidCustomerNameException, UnauthorizedException {
		String customerName = "Bob";
		
		// unauth (nobody logged)
		assertThrows(UnauthorizedException.class, () -> controller.defineCustomer(customerName));
		
		// attempt with cashier (minimum permissions)
		controller.login("Franco", "1234");
		// null or empty name
		assertThrows(InvalidCustomerNameException.class, () -> controller.defineCustomer(""));
		assertThrows(InvalidCustomerNameException.class, () -> controller.defineCustomer(null));
		
		// correct creation
		assert(controller.defineCustomer(customerName) != -1);
		
		// duplication attempt
		assert(controller.defineCustomer(customerName) == -1);
	}
	
	@Test
	public void testModifyCustomer() throws InvalidUsernameException, InvalidPasswordException {
		String newCustomerName = "John";
		String newCustomerCard = "1111111111";
		
		// unauth (nobody logged)
		assertThrows(UnauthorizedException.class, () -> controller.modifyCustomer(1, newCustomerName, newCustomerCard));
		
		// attempt with cashier (minimum permissions)
		controller.login("Franco", "1234");
		
		// null or empty name
		assertThrows(InvalidCustomerNameException.class, () -> controller.modifyCustomer(1, "", newCustomerCard));
		assertThrows(InvalidCustomerNameException.class, () -> controller.modifyCustomer(1, null, newCustomerCard));
		
		// invalid card lenght (notice: null and empty are accepted)
		assertThrows(InvalidCustomerCardException.class, () -> controller.modifyCustomer(1, newCustomerName, "11"));
		
	}
	
	@Test
	public void testDeleteCustomer() throws InvalidUsernameException, InvalidPasswordException, InvalidCustomerIdException, UnauthorizedException {
		Integer id = customerRepository.findByName("Fabiana").getId();
		
		// unauth (nobody logged)
		assertThrows(UnauthorizedException.class, () -> controller.deleteCustomer(id));
		
		// attempt with cashier (minimum permissions)
		controller.login("Franco", "1234");
		
		// not positive id
		assertThrows(InvalidCustomerIdException.class, () -> controller.deleteCustomer(-1));
		assertThrows(InvalidCustomerIdException.class, () -> controller.deleteCustomer(0));
		assertThrows(InvalidCustomerIdException.class, () -> controller.deleteCustomer(null));

		
		// inexistent customer
		assertFalse(controller.deleteCustomer(6));
		
		// correct delete
		assertTrue(controller.deleteCustomer(id));
	}
	
	@Test
	public void testGetCustomer() throws InvalidUsernameException, InvalidPasswordException, InvalidCustomerIdException, UnauthorizedException {
		Integer id = customerRepository.findByName("Giovanni").getId();
		
		// unauth (nobody logged)
		assertThrows(UnauthorizedException.class, () -> controller.getCustomer(id));
		
		// attempt with cashier (minimum permissions)
		controller.login("Franco", "1234");
		
		// not positive id
		assertThrows(InvalidCustomerIdException.class, () -> controller.getCustomer(-1));
		assertThrows(InvalidCustomerIdException.class, () -> controller.getCustomer(0));
		assertThrows(InvalidCustomerIdException.class, () -> controller.getCustomer(null));
		
		// inexistent customer
		assertNull(controller.getCustomer(6));
		// corrrect get
		assertNotNull(controller.getCustomer(id));
	}

	@Test
	public void testGetAllCustomers() throws InvalidUsernameException, InvalidPasswordException, UnauthorizedException {
		// unauth (nobody logged)
		assertThrows(UnauthorizedException.class, () -> controller.getAllCustomers());
		// attempt with cashier (minimum permissions)
        controller.login("Franco", "1234");
        
        assertTrue(controller.getAllCustomers().stream().allMatch(p -> p instanceof Customer));
	}
	
	@Test
	public void testCreateAndAttachCard() throws InvalidUsernameException, InvalidPasswordException, UnauthorizedException, InvalidCustomerIdException, InvalidCustomerCardException {
		Integer customerId = customerRepository.findByName("Gianni").getId();
		
		// unauth (nobody logged)
		assertThrows(UnauthorizedException.class, () -> controller.createCard());
		
		// attempt with cashier (minimum permissions)
        controller.login("Franco", "1234");
       
        String customerCard = controller.createCard(); 
		assertNotEquals(customerCard, "");
		
		// invalid customer id (null, negative, 0)
		assertThrows(InvalidCustomerIdException.class, () -> controller.attachCardToCustomer(customerCard, -1));
		assertThrows(InvalidCustomerIdException.class, () -> controller.attachCardToCustomer(customerCard, 0));
		assertThrows(InvalidCustomerIdException.class, () -> controller.attachCardToCustomer(customerCard, null));

		// attach
		// customer doesn't exist
		assertFalse(controller.attachCardToCustomer(customerCard, 6));
		
		// card already assigned
		assertFalse(controller.attachCardToCustomer("4017008113", customerId));
		
		// correct attach
		assertTrue(controller.attachCardToCustomer(customerCard, customerId));

	}
	
	@Test
	public void modifyPointsOnCard() throws InvalidUsernameException, InvalidPasswordException, InvalidCustomerCardException, UnauthorizedException {
		String customerCard = "4017008113";
		int pointsToBeAdded = 7;
		
		// unauth (nobody logged)
		assertThrows(UnauthorizedException.class, () -> controller.modifyPointsOnCard(customerCard, pointsToBeAdded));
		// attempt with cashier (minimum permissions)
	    controller.login("Franco", "1234");
	    
	    // invalid card( null, empty, invalid format)
	    assertThrows(InvalidCustomerCardException.class, () -> controller.modifyPointsOnCard("", pointsToBeAdded));
	    assertThrows(InvalidCustomerCardException.class, () -> controller.modifyPointsOnCard(null, pointsToBeAdded));
	    assertThrows(InvalidCustomerCardException.class, () -> controller.modifyPointsOnCard("11", pointsToBeAdded));
	    
	    // inexistent card number 
	    assertFalse(controller.modifyPointsOnCard("0000000000", pointsToBeAdded));
	    
	    // negative points implying negative total points
	    assertFalse(controller.modifyPointsOnCard(customerCard, -50));
	    
	    // correct modify
	    assertTrue(controller.modifyPointsOnCard(customerCard, pointsToBeAdded));
	}
	
	@Test
	public void testComputeBalance() throws InvalidUsernameException, InvalidPasswordException, UnauthorizedException {
		// nobody logged
		assertThrows(UnauthorizedException.class, () -> controller.computeBalance());
		
		// unauth
		controller.login("Franco", "1234");
		assertThrows(UnauthorizedException.class, () -> controller.computeBalance());
		controller.logout();
		
		controller.login("Marco", "1234");
		double currentBalance = balanceOperationRepository.findAll()
                .stream()
                .mapToDouble(BalanceOperation::getMoney)
                .sum();
		
		assertEquals(currentBalance, controller.computeBalance(), .1);
	}
	
	
	@After
	public void after() {
		controller.logout();
	}
	
	

}
