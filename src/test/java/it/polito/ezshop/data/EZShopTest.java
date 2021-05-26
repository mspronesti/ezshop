package it.polito.ezshop.data;

import static org.junit.Assert.*;

import java.time.LocalDate;
import java.util.List;

import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

import it.polito.ezshop.exceptions.*;

public class EZShopTest {
    private static EZShop ezshop;
    
    private static UserRepository userRepository;
    private static ProductTypeRepository productTypeRepository;
    private static BalanceOperationRepository balanceOperationRepository;
	private static SaleTransactionRepository saleTransactionRepository;
    private static CustomerRepository customerRepository;
    private static OrderRepository orderRepository ;
    
    @BeforeClass
    static public void init() {
    	ezshop = new EZShop();
    	userRepository = new UserRepository();
    	productTypeRepository = new ProductTypeRepository();
        balanceOperationRepository = new BalanceOperationRepository();
		saleTransactionRepository = new SaleTransactionRepository();
        customerRepository  = new CustomerRepository();
        orderRepository = new OrderRepository();
    }

	@Test
	public void testCreateUser() throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException {
		String username = "johndoe";
		String password = "1234";
		String role = "Administrator";
		
		// username empty or null
		assertThrows(InvalidUsernameException.class, () -> ezshop.createUser("", password, role));
		assertThrows(InvalidUsernameException.class, () -> ezshop.createUser(null, password, role));
		
		// password empty or null
		assertThrows(InvalidPasswordException.class, () -> ezshop.createUser(username, "", role));
		assertThrows(InvalidPasswordException.class, () -> ezshop.createUser(username, null, role));
		
		// role empty or null or not respecting pattern
		assertThrows(InvalidRoleException.class, () -> ezshop.createUser(username, password, ""));
		assertThrows(InvalidRoleException.class, () -> ezshop.createUser(username, password, null));
		assertThrows(InvalidRoleException.class, () -> ezshop.createUser(username, password, "fakeRole"));
		
		// correct user creation
	    assert(ezshop.createUser(username, password, role) != -1);
	    // test duplicate user
	    assert(ezshop.createUser(username, password, role) == -1);
	 }
		
	@Test
	public void testLoginLogout() throws InvalidUsernameException, InvalidPasswordException {
		String username = "Marco";
		String password = "1234";
		
		// empty username or empty password
		assertThrows(InvalidUsernameException.class, () -> ezshop.login("", password));
		assertThrows(InvalidPasswordException.class, () -> ezshop.login(username, ""));
		
		// wrong password
		assertNull(ezshop.login(username, "12345"));
		
		User user = ezshop.login(username, password);
		assertNotNull(user);
		assertTrue(ezshop.logout());
	}
	
	@Test
	public void testDeleteUser() throws InvalidUsernameException, InvalidPasswordException, InvalidUserIdException, UnauthorizedException {
		
		Integer id1 = userRepository.findByUsername("Marco").getId();
		Integer id2 = userRepository.findByUsername("Giovanna").getId();
	    
		// unauth attempt to delete user
		ezshop.login("Franco", "1234");
		assertThrows(UnauthorizedException.class, () -> ezshop.deleteUser(id1));
		ezshop.logout();
		
		ezshop.login("Marco", "1234");
		// id null, negative or 0
		assertThrows(InvalidUserIdException.class, () -> ezshop.deleteUser(null));
		assertThrows(InvalidUserIdException.class, () -> ezshop.deleteUser(-1));
		assertThrows(InvalidUserIdException.class, () -> ezshop.deleteUser(0));
        
		// user doesn't exist
		assertFalse(ezshop.deleteUser(13));
		// correct delete
		assertTrue(ezshop.deleteUser(id2));
	}
	
	@Test
	public void testGetAllUsers() throws InvalidUsernameException, InvalidPasswordException, UnauthorizedException {
		ezshop.login("Franco", "1234");
		assertThrows(UnauthorizedException.class, () -> ezshop.getAllUsers());
		ezshop.logout();
		
		ezshop.login("Marco", "1234");
		assertTrue(ezshop.getAllUsers().stream().allMatch(p -> p instanceof User));
	}
	
	@Test
	public void testGetUser() throws InvalidUsernameException, InvalidPasswordException, InvalidUserIdException, UnauthorizedException {
		Integer id = userRepository.findByUsername("Anna").getId();
		
		ezshop.login("Franco", "1234");
		assertThrows(UnauthorizedException.class, () -> ezshop.getUser(id));
		ezshop.logout();
		
		ezshop.login("Marco", "1234");
		
		// wrong id (negative, 0, null)
		assertThrows(InvalidUserIdException.class, () -> ezshop.getUser(-1));
		assertThrows(InvalidUserIdException.class, () -> ezshop.getUser(0));
		assertThrows(InvalidUserIdException.class, () -> ezshop.getUser(null));
		
		// inexistent id
		assertNull(ezshop.getUser(13));
		// correct id
		assertNotNull(ezshop.getUser(id));
	}

	@Test 
	public void testUpdateUserRight() throws InvalidUsernameException, InvalidPasswordException, InvalidUserIdException, InvalidRoleException, UnauthorizedException {
		String newRole = "Administrator";
		Integer id = userRepository.findByUsername("Anna").getId();
		
		// attempt with unauth user
		ezshop.login("Franco", "1234");
		assertThrows(UnauthorizedException.class, () -> ezshop.updateUserRights(id, newRole));
		ezshop.logout();
		
		// attempt with auth user
		ezshop.login("Marco", "1234");
		
		// invalid id
		assertThrows(InvalidUserIdException.class, () -> ezshop.updateUserRights(null, newRole));
		assertThrows(InvalidUserIdException.class, () -> ezshop.updateUserRights(-1, newRole));
		assertThrows(InvalidUserIdException.class, () -> ezshop.updateUserRights(0, newRole));
		
		// invalid role
		assertThrows(InvalidRoleException.class,  () -> ezshop.updateUserRights(id, "fakeRole"));
		
		// inexistent user
		assertFalse(ezshop.updateUserRights( 13, newRole));
		// correct update
		assertTrue(ezshop.updateUserRights(id, newRole));
	}
	
	@Test 
	public void testCreateProductType() throws InvalidUsernameException, InvalidPasswordException, InvalidProductDescriptionException, InvalidProductCodeException, InvalidPricePerUnitException, UnauthorizedException {
		String description = "Pasta";
		String productCode = "012345678967";
		double pricePerUnit = 1.23;
		String note = "";
		
		// unauth 
		ezshop.login("Franco", "1234");
		assertThrows(UnauthorizedException.class, () -> ezshop.createProductType(description, productCode, pricePerUnit, note));
		ezshop.logout();

		// auth
		ezshop.login("Marco", "1234");
				
		// empty/null description
		assertThrows(InvalidProductDescriptionException.class, () -> ezshop.createProductType("", productCode, pricePerUnit, note));
		assertThrows(InvalidProductDescriptionException.class, () -> ezshop.createProductType(null, productCode, pricePerUnit, note));
		
		// empty/null/notGTin barcode
		assertThrows(InvalidProductCodeException.class, () -> ezshop.createProductType(description, "", pricePerUnit, note));
		assertThrows(InvalidProductCodeException.class, () -> ezshop.createProductType(description, "null", pricePerUnit, note));
		assertThrows(InvalidProductCodeException.class, () -> ezshop.createProductType(description, "012345678901", pricePerUnit, note));
		
		// negative or 0 price
		assertThrows(InvalidPricePerUnitException.class, () -> ezshop.createProductType(description, productCode, -5, note));
		assertThrows(InvalidPricePerUnitException.class, () -> ezshop.createProductType(description, productCode, 0, note));
		
		
		// attempt to duplicate
		assert(ezshop.createProductType(description, productCode, pricePerUnit, note) == -1);
	}
	
	@Test
	public void testUpdateProduct() throws InvalidUsernameException, InvalidPasswordException, InvalidProductIdException, InvalidProductDescriptionException, InvalidProductCodeException, InvalidPricePerUnitException, UnauthorizedException {
		String newDescription = "pizza";
		String newCode = "012345678998";
		double newPrice = 1.15;
		String newNote = "foo";
		
		Integer id = productTypeRepository.findByBarcode("012345678905").getId();
		
		// unauth 
		ezshop.login("Franco", "1234");
		assertThrows(UnauthorizedException.class, () -> ezshop.updateProduct(id, newDescription, newCode, newPrice, newNote));
		ezshop.logout();

		ezshop.login("Marco", "1234");
		
		// invalid id (null, negative, 0)
		assertThrows(InvalidProductIdException.class, () -> ezshop.updateProduct(null, newDescription, newCode, newPrice, newNote));
		assertThrows(InvalidProductIdException.class, () -> ezshop.updateProduct(-1, newDescription, newCode, newPrice, newNote));
		assertThrows(InvalidProductIdException.class, () -> ezshop.updateProduct(0, newDescription, newCode, newPrice, newNote));
		
		// invalid description (null, empty)
		assertThrows(InvalidProductDescriptionException.class, () -> ezshop.updateProduct(id, "", newCode, newPrice, newNote));
		assertThrows(InvalidProductDescriptionException.class, () -> ezshop.updateProduct(id, null, newCode, newPrice, newNote));
		
		// invalid barcode (null, empty, invalid gtin)
		assertThrows(InvalidProductCodeException.class, () -> ezshop.updateProduct(id, newDescription, "", newPrice, newNote));
		assertThrows(InvalidProductCodeException.class, () -> ezshop.updateProduct(id, newDescription, "null", newPrice, newNote));
		assertThrows(InvalidProductCodeException.class, () -> ezshop.updateProduct(id, newDescription, "012345678990", newPrice, newNote));

		// invalid price (negative)
		assertThrows(InvalidPricePerUnitException.class, () -> ezshop.updateProduct(id, newDescription, newCode, -10.5, newNote));
		
		// product doesn't exist
		assertFalse(ezshop.updateProduct(10, newDescription, newCode, newPrice, newNote));
		// barcode already assigned
		assertFalse(ezshop.updateProduct(id, newDescription, "012345678912", newPrice, newNote));
		// correct update
		assertTrue(ezshop.updateProduct(id, newDescription, newCode, newPrice, newNote));
	}
	
	@Test
	public void testDeleteProduct() throws InvalidUsernameException, InvalidPasswordException {
		Integer id = productTypeRepository.findByBarcode("012345678929").getId();
		
		// unauth
		ezshop.login("Franco", "1234");
		assertThrows(UnauthorizedException.class, () -> ezshop.deleteProductType(id));
		ezshop.logout();
		
		ezshop.login("Marco", "1234");
		// invalid product type (null, 0 , negative)
		assertThrows(InvalidProductIdException.class, () -> ezshop.deleteProductType(-1));
		assertThrows(InvalidProductIdException.class, () -> ezshop.deleteProductType(0));
		assertThrows(InvalidProductIdException.class, () -> ezshop.deleteProductType(null));

	}
	
	@Test
	public void testGetAllProducts() throws UnauthorizedException, InvalidUsernameException, InvalidPasswordException, InvalidRoleException, InvalidUserIdException {
		// nobody logged
		assertThrows(UnauthorizedException.class, () -> ezshop.getAllProductTypes());
		
		String[] roles = {"Marco", "Anna", "Franco"}; // Administrator, ShopManager, Cashier
		for (String role : roles) {
			ezshop.login(role, "1234");
			assertTrue(ezshop.getAllProductTypes().stream().allMatch(p -> p instanceof ProductType));
			ezshop.logout();
		}
		
	}
	
	@Test
	public void testGetProductByBarCode() throws InvalidUsernameException, InvalidPasswordException, InvalidProductCodeException, UnauthorizedException {
		String barCode = "012345678912";
		// unauth 
		ezshop.login("Franco", "1234");
		assertThrows(UnauthorizedException.class, () -> ezshop.getProductTypeByBarCode(barCode));
				
		ezshop.login("Marco", "1234");
		// invalid bar code (null, empty, invalid gtin)
		assertThrows(InvalidProductCodeException.class, () -> ezshop.getProductTypeByBarCode(""));
		assertThrows(InvalidProductCodeException.class, () -> ezshop.getProductTypeByBarCode("null"));
		assertThrows(InvalidProductCodeException.class, () -> ezshop.getProductTypeByBarCode("012345678906"));
		
		// inexisting product
		assertNull(ezshop.getProductTypeByBarCode("9999999999994"));
		assertNotNull(ezshop.getProductTypeByBarCode(barCode));
	}
	
	@Test
	public void testGetProductTypesByDescription() throws InvalidUsernameException, InvalidPasswordException, UnauthorizedException {
		String description = "Glasses";
		// unauth 
		ezshop.login("Franco", "1234");
		assertThrows(UnauthorizedException.class, () -> ezshop.getProductTypesByDescription(description));
				
		ezshop.login("Marco", "1234");
		List<ProductType> products = ezshop.getProductTypesByDescription(description);
		
		assertTrue(products.isEmpty() || products.stream().allMatch( p -> p instanceof ProductType));
	}
	
	@Test
	public void testUpdateQuantity() throws InvalidUsernameException, InvalidPasswordException, InvalidProductIdException, UnauthorizedException {
		// unauth 
		ezshop.login("Franco", "1234");
		assertThrows(UnauthorizedException.class, () -> ezshop.updateQuantity(1, 10));
		ezshop.logout();
		
		ezshop.login("Marco", "1234");
		
		// invalid id (negative, 0, null)
		assertThrows(InvalidProductIdException.class, () -> ezshop.updateQuantity(-1, 10));
		assertThrows(InvalidProductIdException.class, () -> ezshop.updateQuantity(0, 10));
		assertThrows(InvalidProductIdException.class, () -> ezshop.updateQuantity(null, 10));
		
		// negative quantity implying negative total quantity 
		assertFalse(ezshop.updateQuantity(1, -200));
		
		// update product without given location
		assertFalse(ezshop.updateQuantity(13, 5));
		
		// correct update
		assertTrue(ezshop.updateQuantity(1, 10));
		
	}
	
	@Test
	public void testUpdatePosition() throws InvalidUsernameException, InvalidPasswordException, InvalidProductIdException, InvalidLocationException, UnauthorizedException {
		Integer productId = productTypeRepository.findByBarcode("012345678929").getId();
		String newPos = "18-C-9";
		
		// unauth 
		ezshop.login("Franco", "1234");
		assertThrows(UnauthorizedException.class, () -> ezshop.updatePosition(productId, newPos));
		ezshop.logout();
		
		ezshop.login("Marco", "1234");
		
		// invalid id (negative, 0, null)
		assertThrows(InvalidProductIdException.class, () -> ezshop.updatePosition(-1, newPos));
		assertThrows(InvalidProductIdException.class, () -> ezshop.updatePosition(0, newPos));
		assertThrows(InvalidProductIdException.class, () -> ezshop.updatePosition(null, newPos));
		
		// invalid location
		assertThrows(InvalidLocationException.class, () -> ezshop.updatePosition(productId, "aa-1-1"));
		assertThrows(InvalidLocationException.class, () -> ezshop.updatePosition(productId, "1-ddc-88"));
		assertThrows(InvalidLocationException.class, () -> ezshop.updatePosition(productId, "1A88"));

		// correct update
		assertTrue(ezshop.updatePosition(productId, newPos));
	}
	
	@Test 
	public void testIssueOrder() throws InvalidUsernameException, InvalidPasswordException, InvalidProductCodeException, InvalidQuantityException, InvalidPricePerUnitException, UnauthorizedException {
		String productCode = "012345678912";
		Integer quantity = 5;
		double pricePerUnit = 1.78;
		
		ezshop.login("Franco", "1234"); // unauth
		assertThrows(UnauthorizedException.class, () -> ezshop.issueOrder(productCode, quantity, pricePerUnit));
		
		ezshop.login("Marco", "1234");
		
		// invalid bar code (null, empty, invalid gtin)
		assertThrows(InvalidProductCodeException.class, () -> ezshop.issueOrder("", quantity, pricePerUnit));
		assertThrows(InvalidProductCodeException.class, () -> ezshop.issueOrder("null", quantity, pricePerUnit));
		assertThrows(InvalidProductCodeException.class, () -> ezshop.issueOrder("012345678906", quantity, pricePerUnit));
		
		// negative or 0 quantity
		assertThrows(InvalidQuantityException.class, () -> ezshop.issueOrder(productCode, -1, pricePerUnit));
		assertThrows(InvalidQuantityException.class, () -> ezshop.issueOrder(productCode, 0, pricePerUnit));

		//negative or 0 price
		assertThrows(InvalidPricePerUnitException.class, () -> ezshop.issueOrder(productCode, quantity, -1.84));
		assertThrows(InvalidPricePerUnitException.class, () -> ezshop.issueOrder(productCode, quantity, 0));
		
		// inexistent product
		assert( ezshop.issueOrder("123456789999", 10, 10.24) == -1 );
		
		// correct order
		Integer orderId = ezshop.issueOrder(productCode, quantity, pricePerUnit);
		assert(orderId != -1);
		assertEquals(orderRepository.find(orderId).getStatus(), "ISSUED");
	}
	
	@Test
	public void testPayOrderFor() throws InvalidUsernameException, InvalidPasswordException, InvalidProductCodeException, InvalidQuantityException, InvalidPricePerUnitException, UnauthorizedException {
		String productCode = "012345678905";
		Integer quantity = 5;
		double pricePerUnit = 1.78;
		
		ezshop.login("Franco", "1234"); // unauth
		assertThrows(UnauthorizedException.class, () -> ezshop.payOrderFor(productCode, quantity, pricePerUnit));
		
		ezshop.login("Marco", "1234");
		
		// invalid bar code (null, empty, invalid gtin)
		assertThrows(InvalidProductCodeException.class, () -> ezshop.payOrderFor("", quantity, pricePerUnit));
		assertThrows(InvalidProductCodeException.class, () -> ezshop.payOrderFor("null", quantity, pricePerUnit));
		assertThrows(InvalidProductCodeException.class, () -> ezshop.payOrderFor("012345678906", quantity, pricePerUnit));
		
		// negative or 0 quantity
		assertThrows(InvalidQuantityException.class, () -> ezshop.payOrderFor(productCode, -1, pricePerUnit));
		assertThrows(InvalidQuantityException.class, () -> ezshop.payOrderFor(productCode, 0, pricePerUnit));

		//negative or 0 price
		assertThrows(InvalidPricePerUnitException.class, () -> ezshop.payOrderFor(productCode, quantity, -1.84));
		assertThrows(InvalidPricePerUnitException.class, () -> ezshop.payOrderFor(productCode, quantity, 0));
		
		// inexistent product
		assert( ezshop.payOrderFor("123456789999", 10, 10.24) == -1 );
		
		// not enough balance
		assert(ezshop.payOrderFor(productCode, quantity*100, pricePerUnit) == -1);
		
		// correct order
		Integer orderId =  ezshop.payOrderFor(productCode, quantity, pricePerUnit);
		assert(orderId != -1);
		assertEquals(orderRepository.find(orderId).getStatus(), "PAYED");
		
	}
	
	@Test
	public void testPayOrder() throws InvalidUsernameException, InvalidPasswordException, InvalidOrderIdException, UnauthorizedException {
		ezshop.login("Franco", "1234"); // unauth
		assertThrows(UnauthorizedException.class, () -> ezshop.payOrder(6));
		
		ezshop.login("Marco", "1234");
		// wrong id
		assertThrows(InvalidOrderIdException.class, () -> ezshop.payOrder(-1));
		assertThrows(InvalidOrderIdException.class, () -> ezshop.payOrder(0));
		assertThrows(InvalidOrderIdException.class, () -> ezshop.payOrder(null));

		// inexistent order
		assertFalse(ezshop.payOrder(8));
		// order not in ISSUED/PAYED state 
		assertFalse(ezshop.payOrder(14));
		// correct pay
		assertTrue(ezshop.payOrder(6));
		assertEquals(orderRepository.find(6).getStatus(), "PAYED");
	}
	
	@Test
	public void testRecordOrderArrival() throws InvalidUsernameException, InvalidPasswordException, InvalidOrderIdException, UnauthorizedException, InvalidLocationException {
		// unauth
		ezshop.login("Franco", "1234");
		assertThrows(UnauthorizedException.class, () -> ezshop.recordOrderArrival(7));
		
		ezshop.login("Marco", "1234");
		
		// wrong id
		assertThrows(InvalidOrderIdException.class, () -> ezshop.recordOrderArrival(-1));
		assertThrows(InvalidOrderIdException.class, () -> ezshop.recordOrderArrival(0));
		assertThrows(InvalidOrderIdException.class, () -> ezshop.recordOrderArrival(null));
		
		// order refers to product without given location
		assertThrows(InvalidLocationException.class, () -> ezshop.recordOrderArrival(40));
		
		// order doesn't exist
		assertFalse(ezshop.recordOrderArrival(50));
		
		// order is not in the PAYED state
		assertFalse(ezshop.recordOrderArrival(6));
		
		// correct register
		assertTrue(ezshop.recordOrderArrival(7));
		assertEquals(orderRepository.find(7).getStatus(), "COMPLETED");		
	}
	
	@Test
	public void testGetAllOrders() throws InvalidUsernameException, InvalidPasswordException, UnauthorizedException {
		ezshop.login("Franco", "1234"); // unauth
		assertThrows(UnauthorizedException.class, () -> ezshop.payOrder(6));
		
		ezshop.login("Marco", "1234");
		assertTrue(ezshop.getAllOrders().stream().allMatch(p -> p instanceof Order));
	}
	
	@Test
	public void testDefineCustomer() throws InvalidUsernameException, InvalidPasswordException, InvalidCustomerNameException, UnauthorizedException {
		String customerName = "Bob";
		
		// unauth (nobody logged)
		assertThrows(UnauthorizedException.class, () -> ezshop.defineCustomer(customerName));
		
		// attempt with cashier (minimum permissions)
		ezshop.login("Franco", "1234");
		// null or empty name
		assertThrows(InvalidCustomerNameException.class, () -> ezshop.defineCustomer(""));
		assertThrows(InvalidCustomerNameException.class, () -> ezshop.defineCustomer(null));
		
		// correct creation
		assert(ezshop.defineCustomer(customerName) != -1);
		
		// duplication attempt
		assert(ezshop.defineCustomer(customerName) == -1);
	}
	
	@Test
	public void testModifyCustomer() throws InvalidUsernameException, InvalidPasswordException {
		String newCustomerName = "John";
		String newCustomerCard = "1111111111";
		
		// unauth (nobody logged)
		assertThrows(UnauthorizedException.class, () -> ezshop.modifyCustomer(1, newCustomerName, newCustomerCard));
		
		// attempt with cashier (minimum permissions)
		ezshop.login("Franco", "1234");
		
		// null or empty name
		assertThrows(InvalidCustomerNameException.class, () -> ezshop.modifyCustomer(1, "", newCustomerCard));
		assertThrows(InvalidCustomerNameException.class, () -> ezshop.modifyCustomer(1, null, newCustomerCard));
		
		// invalid card lenght (notice: null and empty are accepted)
		assertThrows(InvalidCustomerCardException.class, () -> ezshop.modifyCustomer(1, newCustomerName, "11"));
		
	}
	
	@Test
	public void testDeleteCustomer() throws InvalidUsernameException, InvalidPasswordException, InvalidCustomerIdException, UnauthorizedException {
		Integer id = customerRepository.findByName("Fabiana").getId();
		
		// unauth (nobody logged)
		assertThrows(UnauthorizedException.class, () -> ezshop.deleteCustomer(id));
		
		// attempt with cashier (minimum permissions)
		ezshop.login("Franco", "1234");
		
		// not positive id
		assertThrows(InvalidCustomerIdException.class, () -> ezshop.deleteCustomer(-1));
		assertThrows(InvalidCustomerIdException.class, () -> ezshop.deleteCustomer(0));
		assertThrows(InvalidCustomerIdException.class, () -> ezshop.deleteCustomer(null));

		
		// inexistent customer
		assertFalse(ezshop.deleteCustomer(6));
		
		// correct delete
		assertTrue(ezshop.deleteCustomer(id));
	}
	
	@Test
	public void testGetCustomer() throws InvalidUsernameException, InvalidPasswordException, InvalidCustomerIdException, UnauthorizedException {
		Integer id = customerRepository.findByName("Giovanni").getId();
		
		// unauth (nobody logged)
		assertThrows(UnauthorizedException.class, () -> ezshop.getCustomer(id));
		
		// attempt with cashier (minimum permissions)
		ezshop.login("Franco", "1234");
		
		// not positive id
		assertThrows(InvalidCustomerIdException.class, () -> ezshop.getCustomer(-1));
		assertThrows(InvalidCustomerIdException.class, () -> ezshop.getCustomer(0));
		assertThrows(InvalidCustomerIdException.class, () -> ezshop.getCustomer(null));
		
		// inexistent customer
		assertNull(ezshop.getCustomer(6));
		// corrrect get
		assertNotNull(ezshop.getCustomer(id));
	}

	@Test
	public void testGetAllCustomers() throws InvalidUsernameException, InvalidPasswordException, UnauthorizedException {
		// unauth (nobody logged)
		assertThrows(UnauthorizedException.class, () -> ezshop.getAllCustomers());
		// attempt with cashier (minimum permissions)
        ezshop.login("Franco", "1234");
        
        assertTrue(ezshop.getAllCustomers().stream().allMatch(p -> p instanceof Customer));
	}
	
	@Test
	public void testCreateAndAttachCard() throws InvalidUsernameException, InvalidPasswordException, UnauthorizedException, InvalidCustomerIdException, InvalidCustomerCardException {
		Integer customerId = customerRepository.findByName("Gianni").getId();
		
		// unauth (nobody logged)
		assertThrows(UnauthorizedException.class, () -> ezshop.createCard());
		
		// attempt with cashier (minimum permissions)
        ezshop.login("Franco", "1234");
       
        String customerCard = ezshop.createCard();
		assertNotEquals(customerCard, "");
		
		// invalid customer id (null, negative, 0)
		assertThrows(InvalidCustomerIdException.class, () -> ezshop.attachCardToCustomer(customerCard, -1));
		assertThrows(InvalidCustomerIdException.class, () -> ezshop.attachCardToCustomer(customerCard, 0));
		assertThrows(InvalidCustomerIdException.class, () -> ezshop.attachCardToCustomer(customerCard, null));

		// attach
		// customer doesn't exist
		assertFalse(ezshop.attachCardToCustomer(customerCard, 6));
		
		// card already assigned
		assertFalse(ezshop.attachCardToCustomer("4017008113", customerId));
		
		// correct attach
		assertTrue(ezshop.attachCardToCustomer(customerCard, customerId));

	}
	
	@Test
	public void modifyPointsOnCard() throws InvalidUsernameException, InvalidPasswordException, InvalidCustomerCardException, UnauthorizedException {
		String customerCard = "4017008113";
		int pointsToBeAdded = 7;
		
		// unauth (nobody logged)
		assertThrows(UnauthorizedException.class, () -> ezshop.modifyPointsOnCard(customerCard, pointsToBeAdded));
		// attempt with cashier (minimum permissions)
	    ezshop.login("Franco", "1234");
	    
	    // invalid card( null, empty, invalid format)
	    assertThrows(InvalidCustomerCardException.class, () -> ezshop.modifyPointsOnCard("", pointsToBeAdded));
	    assertThrows(InvalidCustomerCardException.class, () -> ezshop.modifyPointsOnCard(null, pointsToBeAdded));
	    assertThrows(InvalidCustomerCardException.class, () -> ezshop.modifyPointsOnCard("11", pointsToBeAdded));
	    
	    // inexistent card number 
	    assertFalse(ezshop.modifyPointsOnCard("0000000000", pointsToBeAdded));
	    
	    // negative points implying negative total points
	    assertFalse(ezshop.modifyPointsOnCard(customerCard, -50));
	    
	    // correct modify
	    assertTrue(ezshop.modifyPointsOnCard(customerCard, pointsToBeAdded));
	}
	

	@Test
	public void testComputeBalance() throws InvalidUsernameException, InvalidPasswordException, UnauthorizedException {
		// nobody logged
		assertThrows(UnauthorizedException.class, () -> ezshop.computeBalance());
		
		// unauth
		ezshop.login("Franco", "1234");
		assertThrows(UnauthorizedException.class, () -> ezshop.computeBalance());
		ezshop.logout();
		
		ezshop.login("Marco", "1234");
		double currentBalance = balanceOperationRepository.findAll()
                .stream()
                .mapToDouble(BalanceOperation::getMoney)
                .sum();
		
		assertEquals(currentBalance, ezshop.computeBalance(), .1);
	}
	
	@Test
	public void testStartEndSaleTransaction() throws UnauthorizedException, InvalidUsernameException, InvalidPasswordException, InvalidTransactionIdException {
		// unauth (nobody logged)
		assertThrows(UnauthorizedException.class, () -> ezshop.startSaleTransaction());
		// cashier 
		ezshop.login("Franco", "1234");
		
		/* --- start ---*/
		Integer transactionId = ezshop.startSaleTransaction();
		assert(transactionId >= 0);
		
		/* --- end ---*/
		// invalid transaction id
		assertThrows(InvalidTransactionIdException.class, () -> ezshop.endSaleTransaction(-1));
		assertThrows(InvalidTransactionIdException.class, () -> ezshop.endSaleTransaction(0));
		assertThrows(InvalidTransactionIdException.class, () -> ezshop.endSaleTransaction(null));

		// inexistent sale transaction
		assertFalse(ezshop.endSaleTransaction(180));
		
		// correct transaction end
		assertTrue(ezshop.endSaleTransaction(transactionId));
		
		// attempt to close it again
		assertFalse(ezshop.endSaleTransaction(transactionId));
	}
	
	@Test
	public void testAddProductToSale() throws InvalidUsernameException, InvalidPasswordException, UnauthorizedException, InvalidQuantityException, InvalidTransactionIdException, InvalidProductCodeException {
		Integer transactionId = 7;
		String productCode = "012345678905";
		int amount = 15;
		
		// unauth (nobody logged)
		assertThrows(UnauthorizedException.class, () -> ezshop.addProductToSale(transactionId, productCode, amount));
		// cashier 
		ezshop.login("Franco", "1234");
		
		// invalid transaction id
		assertThrows(InvalidTransactionIdException.class, () -> ezshop.addProductToSale(0, productCode, amount));
		assertThrows(InvalidTransactionIdException.class, () -> ezshop.addProductToSale(0, productCode, amount));
		assertThrows(InvalidTransactionIdException.class, () -> ezshop.addProductToSale(null, productCode, amount));
		
		// invalid product code
		assertThrows(InvalidProductCodeException.class, () -> ezshop.addProductToSale(transactionId, "", amount));
		assertThrows(InvalidProductCodeException.class, () -> ezshop.addProductToSale(transactionId, "012345678949", amount));
		
		// invalid quantity
		assertThrows(InvalidQuantityException.class, () -> ezshop.addProductToSale(transactionId, productCode, -10));
		

		int newId=ezshop.startSaleTransaction();
		assertTrue(ezshop.addProductToSale(newId, productCode, 1));
		assertFalse(ezshop.addProductToSale(newId, productCode, 1000));
		assertFalse(ezshop.addProductToSale(7, productCode, 1));
	}
	
	@Test
	public void testDeleteProductFromSale() throws InvalidUsernameException, InvalidPasswordException, InvalidQuantityException, InvalidTransactionIdException, UnauthorizedException, InvalidProductCodeException {
		Integer transactionId = 7;  // forse meglio lavorare su un altro (sono quelli del metodo su)
		String productCode = "012345678912"; // forse meglio lavorare su un altro
		int amount = 15;
		
		// unauth (nobody logged)
		assertThrows(UnauthorizedException.class, () -> ezshop.deleteProductFromSale(transactionId, productCode, amount));
		// cashier 
		ezshop.login("Franco", "1234");
		// invalid transaction id
		assertThrows(InvalidTransactionIdException.class, () -> ezshop.deleteProductFromSale(0, productCode, amount));
		assertThrows(InvalidTransactionIdException.class, () -> ezshop.deleteProductFromSale(0, productCode, amount));
		assertThrows(InvalidTransactionIdException.class, () -> ezshop.deleteProductFromSale(null, productCode, amount));
		
		// invalid product code
		assertThrows(InvalidProductCodeException.class, () -> ezshop.deleteProductFromSale(transactionId, "", amount));
		assertThrows(InvalidProductCodeException.class, () -> ezshop.deleteProductFromSale(transactionId, null, amount));
		assertThrows(InvalidProductCodeException.class, () -> ezshop.deleteProductFromSale(transactionId, "012345678949", amount));
		
		// invalid quantity
		assertThrows(InvalidQuantityException.class, () -> ezshop.deleteProductFromSale(transactionId, productCode, -10));
		

	}
	
	@Test
	public void testApplyDiscountRateToProduct() throws InvalidTransactionIdException, InvalidProductCodeException, InvalidDiscountRateException, UnauthorizedException, InvalidPasswordException, InvalidUsernameException, InvalidQuantityException {
		String productCode = "012345678912";
		Double discountRate = 0.1d;

		// unauth (nobody logged)
		assertThrows(UnauthorizedException.class, () -> ezshop.applyDiscountRateToProduct(19, productCode, discountRate));

		// cashier
		ezshop.login("Franco", "1234");

		Integer transactionId = ezshop.startSaleTransaction();
		ezshop.addProductToSale(transactionId, productCode, 1);

		// InvalidTransactionIdException
		assertThrows(InvalidTransactionIdException.class, () -> ezshop.applyDiscountRateToProduct(-1, productCode, discountRate));
		assertThrows(InvalidTransactionIdException.class, () -> ezshop.applyDiscountRateToProduct(0, productCode, discountRate));
		assertThrows(InvalidTransactionIdException.class, () -> ezshop.applyDiscountRateToProduct(null, productCode, discountRate));
		// InvalidProductCodeException
		assertThrows(InvalidProductCodeException.class, () -> ezshop.applyDiscountRateToProduct(transactionId, "", discountRate));
		assertThrows(InvalidProductCodeException.class, () -> ezshop.applyDiscountRateToProduct(transactionId, null, discountRate));
		assertThrows(InvalidProductCodeException.class, () -> ezshop.applyDiscountRateToProduct(transactionId, "012345678949", discountRate));
		// InvalidDiscountRateException
		assertThrows(InvalidDiscountRateException.class, () -> ezshop.applyDiscountRateToProduct(transactionId, productCode, -1d));
		assertThrows(InvalidDiscountRateException.class, () -> ezshop.applyDiscountRateToProduct(transactionId, productCode, 1d));
		assertThrows(InvalidDiscountRateException.class, () -> ezshop.applyDiscountRateToProduct(transactionId, productCode, 2.3d));

		assertFalse(ezshop.applyDiscountRateToProduct(1039495, productCode, discountRate));
		assertTrue(ezshop.applyDiscountRateToProduct(transactionId, productCode, discountRate));
	}
	
	@Test
	public void testApplyDiscountRateToSale() throws InvalidTransactionIdException, InvalidDiscountRateException, UnauthorizedException, InvalidPasswordException, InvalidUsernameException {
		Double discountRate = 0.1d;

		// unauth (nobody logged)
		assertThrows(UnauthorizedException.class, () -> ezshop.applyDiscountRateToSale(19, discountRate));

		// cashier
		ezshop.login("Franco", "1234");

		Integer transactionId = ezshop.startSaleTransaction();

		// InvalidTransactionIdException
		assertThrows(InvalidTransactionIdException.class, () -> ezshop.applyDiscountRateToSale(-1, discountRate));
		assertThrows(InvalidTransactionIdException.class, () -> ezshop.applyDiscountRateToSale(0, discountRate));
		assertThrows(InvalidTransactionIdException.class, () -> ezshop.applyDiscountRateToSale(null, discountRate));
		// InvalidDiscountRateException
		assertThrows(InvalidDiscountRateException.class, () -> ezshop.applyDiscountRateToSale(transactionId, -1d));
		assertThrows(InvalidDiscountRateException.class, () -> ezshop.applyDiscountRateToSale(transactionId, 1d));
		assertThrows(InvalidDiscountRateException.class, () -> ezshop.applyDiscountRateToSale(transactionId, 1.2d));

		assertFalse(ezshop.applyDiscountRateToSale(1039495, discountRate));
		assertTrue(ezshop.applyDiscountRateToSale(transactionId, discountRate));
	}
	
	@Test
	public void testComputePointsForSale() throws InvalidTransactionIdException, UnauthorizedException, InvalidPasswordException, InvalidUsernameException {
		Integer transactionId = 27;

		// unauth (nobody logged)
		assertThrows(UnauthorizedException.class, () -> ezshop.computePointsForSale(transactionId));

		// cashier
		ezshop.login("Franco", "1234");

		// InvalidTransactionIdException
		assertThrows(InvalidTransactionIdException.class, () -> ezshop.computePointsForSale(-1));
		assertThrows(InvalidTransactionIdException.class, () -> ezshop.computePointsForSale(0));
		assertThrows(InvalidTransactionIdException.class, () -> ezshop.computePointsForSale(null));

		assertEquals(ezshop.computePointsForSale(29474), -1);

		assertEquals(ezshop.computePointsForSale(transactionId), 1);
	}

	@Test
	public void testDeleteSaleTransaction() throws InvalidTransactionIdException, UnauthorizedException, InvalidPasswordException, InvalidUsernameException {
		// unauth (nobody logged)
		assertThrows(UnauthorizedException.class, () -> ezshop.deleteSaleTransaction(900));

		ezshop.login("Franco", "1234");

		// invalidTransactionId
		assertThrows(InvalidTransactionIdException.class, ()-> ezshop.deleteSaleTransaction(0));
		assertThrows(InvalidTransactionIdException.class, ()-> ezshop.deleteSaleTransaction(-1));
		assertThrows(InvalidTransactionIdException.class, ()-> ezshop.deleteSaleTransaction(null));


		assertFalse(ezshop.deleteSaleTransaction(25)); //payed
		assertFalse(ezshop.deleteSaleTransaction(900)); //doesn't exist

		int newId= ezshop.startSaleTransaction();
		assertTrue(ezshop.deleteSaleTransaction(newId));
	}
	
	
	@Test
	public void testGetSaleTransaction() throws InvalidTransactionIdException, UnauthorizedException, InvalidPasswordException, InvalidUsernameException {
		int saleTransId = 23;
		// unauth (nobody logged)
		assertThrows(UnauthorizedException.class, () -> ezshop.getSaleTransaction(saleTransId));

		ezshop.login("Franco", "1234");
		// invalidTransactionId
		assertThrows(InvalidTransactionIdException.class, ()-> ezshop.getSaleTransaction(0));
		assertThrows(InvalidTransactionIdException.class, ()-> ezshop.getSaleTransaction(-1));
		assertThrows(InvalidTransactionIdException.class, ()-> ezshop.getSaleTransaction(null));

		assertEquals(saleTransactionRepository.find(saleTransId).getTicketNumber(), ezshop.getSaleTransaction(saleTransId).getTicketNumber());
		int newId = ezshop.startSaleTransaction();

		assertNull(ezshop.getSaleTransaction(900));
	}
	
	@Test
	public void testStartReturnTransaction() throws InvalidTransactionIdException, UnauthorizedException, InvalidPasswordException, InvalidUsernameException {
		Integer transactionId = 27;
		// unauth (nobody logged)
		assertThrows(UnauthorizedException.class, () -> ezshop.startReturnTransaction(transactionId));

		ezshop.login("Franco", "1234");
		// invalidTransactionId
		assertThrows(InvalidTransactionIdException.class, ()-> ezshop.startReturnTransaction(0));
		assertThrows(InvalidTransactionIdException.class, ()-> ezshop.startReturnTransaction(-1));
		assertThrows(InvalidTransactionIdException.class, ()-> ezshop.startReturnTransaction(null));

		assertTrue(ezshop.startReturnTransaction(transactionId)>0);
		assertTrue(ezshop.startReturnTransaction(900)==-1);
	}
	
	@Test
	public void testEndReturnTransaction() throws InvalidTransactionIdException, UnauthorizedException, InvalidPasswordException, InvalidUsernameException {
		//unauth (nobody logged)
		assertThrows(UnauthorizedException.class, () -> ezshop.endReturnTransaction(900, true));

		ezshop.login("Franco", "1234");
		// invalidTransactionId
		assertThrows(InvalidTransactionIdException.class, ()-> ezshop.endReturnTransaction(0, true));
		assertThrows(InvalidTransactionIdException.class, ()-> ezshop.endReturnTransaction(-1, true));
		assertThrows(InvalidTransactionIdException.class, ()-> ezshop.endReturnTransaction(null, true));

		assertFalse(ezshop.endReturnTransaction(12,true));
		int newId= ezshop.startReturnTransaction(27);
		assertTrue(ezshop.endReturnTransaction(newId,false));
	}
	
	
	@Test
	public void testDeleteReturnTransaction() throws InvalidTransactionIdException, UnauthorizedException, InvalidPasswordException, InvalidUsernameException, InvalidQuantityException, InvalidProductCodeException {
		Integer transactionId = 19;
		String creditCard = "5100293991053009";
		String creditCard2 = "4716258050958645";
		String fakeCreditCard = "1234567812345670";
		
		// unauth (nobody logged)
		assertThrows(UnauthorizedException.class, () -> ezshop.deleteReturnTransaction(900));

		ezshop.login("Franco", "1234");
		// invalidTransactionId
		assertThrows(InvalidTransactionIdException.class, ()-> ezshop.deleteReturnTransaction(0));
		assertThrows(InvalidTransactionIdException.class, ()-> ezshop.deleteReturnTransaction(-1));
		assertThrows(InvalidTransactionIdException.class, ()-> ezshop.deleteReturnTransaction(null));

		assertFalse(ezshop.deleteReturnTransaction(900));
		assertFalse(ezshop.deleteReturnTransaction(31));
		int newId= ezshop.startReturnTransaction(27);
		ezshop.returnProduct(newId,"012345678905", 1);
		assertTrue(ezshop.deleteReturnTransaction(newId));
	}
	
	@Test
	public void testReceiveCashPayment() throws InvalidTransactionIdException, InvalidPaymentException, UnauthorizedException, InvalidPasswordException, InvalidUsernameException {

		// unauth (nobody logged)
		Integer transactionId= 19;
		double cash= 0.924;
		assertThrows(UnauthorizedException.class, () -> ezshop.receiveCashPayment(transactionId,cash));

		ezshop.login("Franco", "1234");

		//invalidTransactionId
		assertThrows(InvalidTransactionIdException.class, () -> ezshop.receiveCashPayment(0,100));
		assertThrows(InvalidTransactionIdException.class, () -> ezshop.receiveCashPayment(-1,100));
		assertThrows(InvalidTransactionIdException.class, () -> ezshop.receiveCashPayment(null,100));

		//invalidPayment
		assertThrows(InvalidPaymentException.class, () -> ezshop.receiveCashPayment(transactionId,0));
		assertThrows(InvalidPaymentException.class, () -> ezshop.receiveCashPayment(transactionId,-1));

		assert(ezshop.receiveCashPayment(10, cash) == -1);
		assert(ezshop.receiveCashPayment(transactionId, 0.5) == -1);
		assert(ezshop.receiveCashPayment(transactionId, cash) == 0);
    }
	
	
	@Test
	public void testReceiveCreditCardPayment() throws InvalidTransactionIdException, InvalidCreditCardException, UnauthorizedException, InvalidPasswordException, InvalidUsernameException {
		Integer transactionId=19;
		String creditCard="5100293991053009";
		String creditCard2="4716258050958645";
		String fakeCreditCard="1234567812345670";
		// unauth (nobody logged)
		 assertThrows(UnauthorizedException.class, () -> ezshop.receiveCreditCardPayment(transactionId,creditCard));

		ezshop.login("Franco", "1234");

		//Invalid CreditCard
		assertThrows(InvalidCreditCardException.class,() -> ezshop.receiveCreditCardPayment(transactionId,"oggi"));
		assertThrows(InvalidCreditCardException.class,() -> ezshop.receiveCreditCardPayment(transactionId,""));
		assertThrows(InvalidCreditCardException.class,() -> ezshop.receiveCreditCardPayment(transactionId,null));

		//InvalidTransactionId
		assertThrows(InvalidTransactionIdException.class,() -> ezshop.receiveCreditCardPayment(0,creditCard));
		assertThrows(InvalidTransactionIdException.class,() -> ezshop.receiveCreditCardPayment(-1,creditCard));
		assertThrows(InvalidTransactionIdException.class,() -> ezshop.receiveCreditCardPayment(null,creditCard));

		assertFalse(ezshop.receiveCreditCardPayment(10,creditCard));
		assertFalse(ezshop.receiveCreditCardPayment(transactionId,creditCard2));
		assertFalse(ezshop.receiveCreditCardPayment(transactionId,fakeCreditCard));
		assertTrue(ezshop.receiveCreditCardPayment(transactionId,creditCard));
    }

	
	@Test
	public void testReturnCashPayment() throws InvalidTransactionIdException, UnauthorizedException, InvalidPasswordException, InvalidUsernameException {
		// unauth (nobody logged)
		Integer returnId= 29;
		ReturnTransactionRepository repo = new ReturnTransactionRepository();
		ReturnTransactionImpl returnTransaction = new ReturnTransactionImpl();
		Integer fakeReturnId = repo.create(returnTransaction);
		assertThrows(UnauthorizedException.class, () -> ezshop.returnCashPayment(returnId));

		ezshop.login("Franco", "1234");

		//InvalidTransactionId
		assertThrows(InvalidTransactionIdException.class, () -> ezshop.returnCashPayment(0));
		assertThrows(InvalidTransactionIdException.class, () -> ezshop.returnCashPayment(-1));
		double a = ezshop.returnCashPayment(returnId);
		assert(ezshop.returnCashPayment(100) == -1);

	}
	
	@Test
	public void testReturnCreditCardPayment() throws InvalidTransactionIdException, InvalidCreditCardException, UnauthorizedException, InvalidPasswordException, InvalidUsernameException {
		// unauth (nobody logged)
		Integer returnId=29;
		String creditCard="5100293991053009";
		String fakeCreditCard="1234567812345670";

		ReturnTransactionRepository repo = new ReturnTransactionRepository();
		ReturnTransactionImpl returnTransaction = new ReturnTransactionImpl();
		Integer fakeReturnId = repo.create(returnTransaction);
		assertThrows(UnauthorizedException.class, () -> ezshop.returnCreditCardPayment(returnId,creditCard));

		ezshop.login("Franco", "1234");

		//InvalidTransaction
		assertThrows(InvalidTransactionIdException.class, () -> ezshop.returnCashPayment(0));
		assertThrows(InvalidTransactionIdException.class, () -> ezshop.returnCashPayment(-1));

		//Invalid CreditCard
		assertThrows(InvalidCreditCardException.class,() -> ezshop.returnCreditCardPayment(returnId,"oggi"));
		assertThrows(InvalidCreditCardException.class,() -> ezshop.returnCreditCardPayment(returnId,""));
		assertThrows(InvalidCreditCardException.class,() -> ezshop.returnCreditCardPayment(returnId,null));

		assert(ezshop.returnCreditCardPayment(returnId,creditCard)==1.05);
		assert(ezshop.returnCreditCardPayment(1,creditCard)==-1);
		assert(ezshop.returnCreditCardPayment(returnId,fakeCreditCard)==-1);


    }
	
	@Test
	public void testRecordBalanceUpdate() throws UnauthorizedException, InvalidPasswordException, InvalidUsernameException {
		double toBeAdded=500.99;
		double toBeSubtractedTooMuch=-100000.00;
		// unauth (nobody logged)
		assertThrows(UnauthorizedException.class, () -> ezshop.recordBalanceUpdate(toBeAdded));

		// unauth (cashier)
		ezshop.login("Franco", "1234");
		assertThrows(UnauthorizedException.class, () -> ezshop.recordBalanceUpdate(toBeAdded));
		ezshop.logout();
		ezshop.login("Marco", "1234");

		assertTrue(ezshop.recordBalanceUpdate(toBeAdded));
		assertFalse(ezshop.recordBalanceUpdate(toBeSubtractedTooMuch));
	}
	
	
	@Test
	public void testGetCreditsAndDebits() throws InvalidUsernameException, InvalidPasswordException, UnauthorizedException {
		LocalDate to = LocalDate.of(2021, 5, 25);
		LocalDate from  = LocalDate.now();
		
		// unauth (nobody logged)
		assertThrows(UnauthorizedException.class, () -> ezshop.getCreditsAndDebits(from, to));
		
		// unauth (cashier) 
		ezshop.login("Franco", "1234");
		assertThrows(UnauthorizedException.class, () -> ezshop.getCreditsAndDebits(from, to));
		ezshop.logout();
		
		ezshop.login("Marco", "1234");
		List<BalanceOperation> ops = ezshop.getCreditsAndDebits(from,to );
		List<BalanceOperation> expected = balanceOperationRepository.findAllBetweenDates(to, from);
		
		
		assertEquals(ops, expected);
	}
	
	
	@After
	public void after() {
		ezshop.logout();
	}
	
	

}
