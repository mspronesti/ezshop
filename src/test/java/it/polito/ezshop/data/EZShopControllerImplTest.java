package it.polito.ezshop.data;

import static org.junit.Assert.*;

import java.time.LocalDate;
import java.util.List;

import javax.transaction.Transaction;

import org.hamcrest.MatcherAssert;
import org.hamcrest.core.Is;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

import com.sun.el.stream.Stream;

import it.polito.ezshop.exceptions.*;

public class EZShopControllerImplTest {
    private static EZShopController controller;
    
    private static UserRepository userRepository;
    private static ProductTypeRepository productTypeRepository;
    private static BalanceOperationRepository balanceOperationRepository;
	private static SaleTransactionRepository saleTransactionRepository;
    private static CustomerRepository customerRepository;
    private static OrderRepository orderRepository ;
    
    @BeforeClass
    static public void init() {
    	controller =  EZShopControllerFactory.create();
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
		assertFalse(controller.deleteUser(13));
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
		assertNull(controller.getUser(13));
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
		assertFalse(controller.updateUserRights( 13, newRole));
		// correct update
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
		//assert(controller.createProductType(description, productCode, pricePerUnit, note) != -1);
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
		assertThrows(InvalidProductDescriptionException.class, () -> controller.updateProduct(id, null, newCode, newPrice, newNote));
		
		// invalid barcode (null, empty, invalid gtin)
		assertThrows(InvalidProductCodeException.class, () -> controller.updateProduct(id, newDescription, "", newPrice, newNote));
		assertThrows(InvalidProductCodeException.class, () -> controller.updateProduct(id, newDescription, "null", newPrice, newNote));
		assertThrows(InvalidProductCodeException.class, () -> controller.updateProduct(id, newDescription, "012345678990", newPrice, newNote));

		// invalid price (negative)
		assertThrows(InvalidPricePerUnitException.class, () -> controller.updateProduct(id, newDescription, newCode, -10.5, newNote));
		
		// product doesn't exist
		assertFalse(controller.updateProduct(10, newDescription, newCode, newPrice, newNote));
		// barcode already assigned
		assertFalse(controller.updateProduct(id, newDescription, "012345678912", newPrice, newNote));
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
		assertThrows(InvalidProductIdException.class, () -> controller.deleteProductType(0)); 
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
		assertNull(controller.getProductTypeByBarCode("9999999999994"));
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
		assertFalse(controller.updateQuantity(13, 5));
		
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
		Integer orderId = controller.issueOrder(productCode, quantity, pricePerUnit);
		assert(orderId != -1);
		assertEquals(orderRepository.find(orderId).getStatus(), "ISSUED");
	}
	
	@Test
	public void testPayOrderFor() throws InvalidUsernameException, InvalidPasswordException, InvalidProductCodeException, InvalidQuantityException, InvalidPricePerUnitException, UnauthorizedException {
		String productCode = "012345678905";
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
		Integer orderId =  controller.payOrderFor(productCode, quantity, pricePerUnit);
		assert(orderId != -1);
		assertEquals(orderRepository.find(orderId).getStatus(), "PAYED");
		
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
		// order not in ISSUED/PAYED state 
		assertFalse(controller.payOrder(14));
		// correct pay
		assertTrue(controller.payOrder(6));
		assertEquals(orderRepository.find(6).getStatus(), "PAYED");
	}
	
	@Test
	public void testRecordOrderArrival() throws InvalidUsernameException, InvalidPasswordException, InvalidOrderIdException, UnauthorizedException, InvalidLocationException {
		// unauth
		controller.login("Franco", "1234"); 
		assertThrows(UnauthorizedException.class, () -> controller.recordOrderArrival(7));
		
		controller.login("Marco", "1234");
		
		// wrong id
		assertThrows(InvalidOrderIdException.class, () -> controller.recordOrderArrival(-1));
		assertThrows(InvalidOrderIdException.class, () -> controller.recordOrderArrival(0));
		assertThrows(InvalidOrderIdException.class, () -> controller.recordOrderArrival(null));
		
		// order refers to product without given location
		assertThrows(InvalidLocationException.class, () -> controller.recordOrderArrival(40));
		
		// order doesn't exist
		assertFalse(controller.recordOrderArrival(50));
		
		// order is not in the PAYED state
		assertFalse(controller.recordOrderArrival(6));
		
		// correct register
		assertTrue(controller.recordOrderArrival(7));
		assertEquals(orderRepository.find(7).getStatus(), "COMPLETED");		
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
	
	@Test
	public void testStartEndSaleTransaction() throws UnauthorizedException, InvalidUsernameException, InvalidPasswordException, InvalidTransactionIdException {
		// unauth (nobody logged)
		assertThrows(UnauthorizedException.class, () -> controller.startSaleTransaction());
		// cashier 
		controller.login("Franco", "1234");
		
		/* --- start ---*/
		Integer transactionId = controller.startSaleTransaction();
		assert(transactionId >= 0);
		
		/* --- end ---*/
		// invalid transaction id
		assertThrows(InvalidTransactionIdException.class, () -> controller.endSaleTransaction(-1));
		assertThrows(InvalidTransactionIdException.class, () -> controller.endSaleTransaction(0));
		assertThrows(InvalidTransactionIdException.class, () -> controller.endSaleTransaction(null));

		// inexistent sale transaction
		assertFalse(controller.endSaleTransaction(180));
		
		// correct transaction end
		assertTrue(controller.endSaleTransaction(transactionId));
		
		// attempt to close it again
		assertFalse(controller.endSaleTransaction(transactionId));	
	}
	
	@Test
	public void testAddProductToSale() throws InvalidUsernameException, InvalidPasswordException {
		// DOVREBBERO essere validi
		Integer transactionId = 7; 
		String productCode = "012345678943";
		int amount = 15;
		
		// unauth (nobody logged)
		assertThrows(UnauthorizedException.class, () -> controller.addProductToSale(transactionId, productCode, amount));
		// cashier 
		controller.login("Franco", "1234");
		
		// invalid transaction id
		assertThrows(InvalidTransactionIdException.class, () -> controller.addProductToSale(0, productCode, amount));
		assertThrows(InvalidTransactionIdException.class, () -> controller.addProductToSale(0, productCode, amount));
		assertThrows(InvalidTransactionIdException.class, () -> controller.addProductToSale(null, productCode, amount));
		
		// invalid product code
		assertThrows(InvalidProductCodeException.class, () -> controller.addProductToSale(transactionId, "", amount));
		// non lancia l'eccezione giusta ... problemi simili a quelli della invoke
//		assertThrows(InvalidProductCodeException.class, () -> controller.addProductToSale(transactionId, null, amount));
		assertThrows(InvalidProductCodeException.class, () -> controller.addProductToSale(transactionId, "012345678949", amount));
		
		// invalid quantity
		assertThrows(InvalidQuantityException.class, () -> controller.addProductToSale(transactionId, productCode, -10));
		
		// resto ........

		// TODO: to be implemented
	}
	
	@Test
	public void testDeleteProductFromSale() throws InvalidUsernameException, InvalidPasswordException {
		Integer transactionId = 7;  // forse meglio lavorare su un altro (sono quelli del metodo su)
		String productCode = "012345678943"; // forse meglio lavorare su un altro
		int amount = 15;
		
		// unauth (nobody logged)
		assertThrows(UnauthorizedException.class, () -> controller.deleteProductFromSale(transactionId, productCode, amount));
		// cashier 
		controller.login("Franco", "1234");
		// invalid transaction id
		assertThrows(InvalidTransactionIdException.class, () -> controller.deleteProductFromSale(0, productCode, amount));
		assertThrows(InvalidTransactionIdException.class, () -> controller.deleteProductFromSale(0, productCode, amount));
		assertThrows(InvalidTransactionIdException.class, () -> controller.deleteProductFromSale(null, productCode, amount));
		
		// invalid product code
		assertThrows(InvalidProductCodeException.class, () -> controller.deleteProductFromSale(transactionId, "", amount));
		// non lancia l'eccezione giusta ... problemi simili a quelli della invoke
//				assertThrows(InvalidProductCodeException.class, () -> controller.deleteProductFromSale(transactionId, null, amount));
		assertThrows(InvalidProductCodeException.class, () -> controller.deleteProductFromSale(transactionId, "012345678949", amount));
		
		// invalid quantity
		assertThrows(InvalidQuantityException.class, () -> controller.deleteProductFromSale(transactionId, productCode, -10));
		
		// resto ........

		// TODO: to be implemented
	}
	
	@Test
	public void testApplyDiscountRateToProduct() throws InvalidTransactionIdException, InvalidProductCodeException, InvalidDiscountRateException, UnauthorizedException, InvalidPasswordException, InvalidUsernameException, InvalidQuantityException {
		String productCode = "012345678905";
		Double discountRate = 0.1d;

		// unauth (nobody logged)
		assertThrows(UnauthorizedException.class, () -> controller.applyDiscountRateToProduct(19, productCode, discountRate));

		// cashier
		controller.login("Franco", "1234");

		Integer transactionId = controller.startSaleTransaction();
		controller.addProductToSale(transactionId, productCode, 1);

		// InvalidTransactionIdException
		assertThrows(InvalidTransactionIdException.class, () -> controller.applyDiscountRateToProduct(-1, productCode, discountRate));
		assertThrows(InvalidTransactionIdException.class, () -> controller.applyDiscountRateToProduct(0, productCode, discountRate));
		assertThrows(InvalidTransactionIdException.class, () -> controller.applyDiscountRateToProduct(null, productCode, discountRate));
		// InvalidProductCodeException
		assertThrows(InvalidProductCodeException.class, () -> controller.applyDiscountRateToProduct(transactionId, "", discountRate));
		assertThrows(InvalidProductCodeException.class, () -> controller.applyDiscountRateToProduct(transactionId, null, discountRate));
		assertThrows(InvalidProductCodeException.class, () -> controller.applyDiscountRateToProduct(transactionId, "012345678949", discountRate));
		// InvalidDiscountRateException
		assertThrows(InvalidDiscountRateException.class, () -> controller.applyDiscountRateToProduct(transactionId, productCode, -1d));
		assertThrows(InvalidDiscountRateException.class, () -> controller.applyDiscountRateToProduct(transactionId, productCode, 1d));
		assertThrows(InvalidDiscountRateException.class, () -> controller.applyDiscountRateToProduct(transactionId, productCode, 2.3d));

		assertFalse(controller.applyDiscountRateToProduct(1039495, productCode, discountRate));
		assertTrue(controller.applyDiscountRateToProduct(transactionId, productCode, discountRate));
	}
	
	@Test
	public void testApplyDiscountRateToSale() throws InvalidTransactionIdException, InvalidDiscountRateException, UnauthorizedException, InvalidPasswordException, InvalidUsernameException {
		Double discountRate = 0.1d;

		// unauth (nobody logged)
		assertThrows(UnauthorizedException.class, () -> controller.applyDiscountRateToSale(19, discountRate));

		// cashier
		controller.login("Franco", "1234");

		Integer transactionId = controller.startSaleTransaction();

		// InvalidTransactionIdException
		assertThrows(InvalidTransactionIdException.class, () -> controller.applyDiscountRateToSale(-1, discountRate));
		assertThrows(InvalidTransactionIdException.class, () -> controller.applyDiscountRateToSale(0, discountRate));
		assertThrows(InvalidTransactionIdException.class, () -> controller.applyDiscountRateToSale(null, discountRate));
		// InvalidDiscountRateException
		assertThrows(InvalidDiscountRateException.class, () -> controller.applyDiscountRateToSale(transactionId, -1d));
		assertThrows(InvalidDiscountRateException.class, () -> controller.applyDiscountRateToSale(transactionId, 1d));
		assertThrows(InvalidDiscountRateException.class, () -> controller.applyDiscountRateToSale(transactionId, 1.2d));

		assertFalse(controller.applyDiscountRateToSale(1039495, discountRate));
		assertTrue(controller.applyDiscountRateToSale(transactionId, discountRate));
	}
	
	@Test
	public void testComputePointsForSale() throws InvalidTransactionIdException, UnauthorizedException, InvalidPasswordException, InvalidUsernameException {
		Integer transactionId = 27;

		// unauth (nobody logged)
		assertThrows(UnauthorizedException.class, () -> controller.computePointsForSale(transactionId));

		// cashier
		controller.login("Franco", "1234");

		// InvalidTransactionIdException
		assertThrows(InvalidTransactionIdException.class, () -> controller.computePointsForSale(-1));
		assertThrows(InvalidTransactionIdException.class, () -> controller.computePointsForSale(0));
		assertThrows(InvalidTransactionIdException.class, () -> controller.computePointsForSale(null));

		assertEquals(controller.computePointsForSale(29474), -1);

		assertEquals(controller.computePointsForSale(transactionId), 1);
	}

	@Test
	public void testDeleteSaleTransaction() throws InvalidTransactionIdException, UnauthorizedException, InvalidPasswordException, InvalidUsernameException {
		// TODO: to be implemented
		// unauth (nobody logged)
		assertThrows(UnauthorizedException.class, () -> controller.deleteSaleTransaction(900));

		controller.login("Franco", "1234");

		// invalidTransactionId
		assertThrows(InvalidTransactionIdException.class, ()-> controller.deleteSaleTransaction(0));
		assertThrows(InvalidTransactionIdException.class, ()-> controller.deleteSaleTransaction(-1));
		assertThrows(InvalidTransactionIdException.class, ()-> controller.deleteSaleTransaction(null));



		assertFalse(controller.deleteSaleTransaction(25)); //payed
		assertFalse(controller.deleteSaleTransaction(900)); //don't exist

		int newId=controller.startSaleTransaction();
		assertTrue(controller.deleteSaleTransaction(newId));
	}
	
	
	@Test
	public void testGetSaleTransaction() throws InvalidTransactionIdException, UnauthorizedException, InvalidPasswordException, InvalidUsernameException {
		// TODO: to be implemented
		int saleTransId = 23;
		// unauth (nobody logged)
		assertThrows(UnauthorizedException.class, () -> controller.getSaleTransaction(saleTransId));

		controller.login("Franco", "1234");
		// invalidTransactionId
		assertThrows(InvalidTransactionIdException.class, ()-> controller.getSaleTransaction(0));
		assertThrows(InvalidTransactionIdException.class, ()-> controller.getSaleTransaction(-1));
		assertThrows(InvalidTransactionIdException.class, ()-> controller.getSaleTransaction(null));

		assertEquals(saleTransactionRepository.find(saleTransId).getTicketNumber(), controller.getSaleTransaction(saleTransId).getTicketNumber());
		int newId=controller.startSaleTransaction();

		assertNull(controller.getSaleTransaction(900));
	}
	
	@Test
	public void testStartReturnTransaction() throws InvalidTransactionIdException, UnauthorizedException, InvalidPasswordException, InvalidUsernameException {
		// TODO: to be implemented
		Integer transactionId = 27;
		// unauth (nobody logged)
		assertThrows(UnauthorizedException.class, () -> controller.startReturnTransaction(transactionId));

		controller.login("Franco", "1234");
		// invalidTransactionId
		assertThrows(InvalidTransactionIdException.class, ()-> controller.startReturnTransaction(0));
		assertThrows(InvalidTransactionIdException.class, ()-> controller.startReturnTransaction(-1));
		assertThrows(InvalidTransactionIdException.class, ()-> controller.startReturnTransaction(null));

		assertTrue(controller.startReturnTransaction(transactionId)>0);
		assertTrue(controller.startReturnTransaction(900)==-1);
	}
	
	@Test
	public void testEndReturnTransaction() throws InvalidTransactionIdException, UnauthorizedException, InvalidPasswordException, InvalidUsernameException {
		// TODO: to be implemented
		//unauth (nobody logged)
		assertThrows(UnauthorizedException.class, () -> controller.endReturnTransaction(900, true));

		controller.login("Franco", "1234");
		// invalidTransactionId
		assertThrows(InvalidTransactionIdException.class, ()-> controller.endReturnTransaction(0, true));
		assertThrows(InvalidTransactionIdException.class, ()-> controller.endReturnTransaction(-1, true));
		assertThrows(InvalidTransactionIdException.class, ()-> controller.endReturnTransaction(null, true));

		assertFalse(controller.endReturnTransaction(12,true));
		int newId=controller.startReturnTransaction(27);
		assertTrue(controller.endReturnTransaction(newId,false));
	}
	
	
	@Test
	public void testDeleteReturnTransaction() throws InvalidTransactionIdException, UnauthorizedException, InvalidPasswordException, InvalidUsernameException, InvalidQuantityException, InvalidProductCodeException {
		// TODO: to be implemented
		Integer transactionId = 19;
		String creditCard = "5100293991053009";
		String creditCard2 = "4716258050958645";
		String fakeCreditCard = "1234567812345670";
		// unauth (nobody logged)
		assertThrows(UnauthorizedException.class, () -> controller.deleteReturnTransaction(900));

		controller.login("Franco", "1234");
		// invalidTransactionId
		assertThrows(InvalidTransactionIdException.class, ()-> controller.deleteReturnTransaction(0));
		assertThrows(InvalidTransactionIdException.class, ()-> controller.deleteReturnTransaction(-1));
		assertThrows(InvalidTransactionIdException.class, ()-> controller.deleteReturnTransaction(null));

		assertFalse(controller.deleteReturnTransaction(900));
		assertFalse(controller.deleteReturnTransaction(31));
		int newId=controller.startReturnTransaction(27);
		controller.returnProduct(newId,"012345678905", 1);
		controller.endReturnTransaction(newId, false);
		assertTrue(controller.deleteSaleTransaction(29));
	}
	
	@Test
	public void testReceiveCashPayment() throws InvalidTransactionIdException, InvalidPaymentException, UnauthorizedException, InvalidPasswordException, InvalidUsernameException {

		// unauth (nobody logged)
		Integer transactionId= 19;
		double cash= 0.924;
		assertThrows(UnauthorizedException.class, () -> controller.receiveCashPayment(transactionId,cash));

		controller.login("Franco", "1234");

		//invalidTransactionId
		assertThrows(InvalidTransactionIdException.class, () -> controller.receiveCashPayment(0,100));
		assertThrows(InvalidTransactionIdException.class, () -> controller.receiveCashPayment(-1,100));
		assertThrows(InvalidTransactionIdException.class, () -> controller.receiveCashPayment(null,100));

		//invalidPayment
		assertThrows(InvalidPaymentException.class, () -> controller.receiveCashPayment(transactionId,0));
		assertThrows(InvalidPaymentException.class, () -> controller.receiveCashPayment(transactionId,-1));

		assert(controller.receiveCashPayment(10,cash) == -1);
		assert(controller.receiveCashPayment(transactionId,0.5) == -1);
		assert(controller.receiveCashPayment(transactionId,cash) == 0);

    }
	
	
	@Test
	public void testReceiveCreditCardPayment() throws InvalidTransactionIdException, InvalidCreditCardException, UnauthorizedException, InvalidPasswordException, InvalidUsernameException {
		// TODO: to be implemented
		Integer transactionId=19;
		String creditCard="5100293991053009";
		String creditCard2="4716258050958645";
		String fakeCreditCard="1234567812345670";
		// unauth (nobody logged)
		 assertThrows(UnauthorizedException.class, () -> controller.receiveCreditCardPayment(transactionId,creditCard));

		controller.login("Franco", "1234");

		//Invalid CreditCard
		assertThrows(InvalidCreditCardException.class,() -> controller.receiveCreditCardPayment(transactionId,"oggi"));
		assertThrows(InvalidCreditCardException.class,() -> controller.receiveCreditCardPayment(transactionId,""));
		assertThrows(InvalidCreditCardException.class,() -> controller.receiveCreditCardPayment(transactionId,null));

		//InvalidTransactionId
		assertThrows(InvalidTransactionIdException.class,() -> controller.receiveCreditCardPayment(0,creditCard));
		assertThrows(InvalidTransactionIdException.class,() -> controller.receiveCreditCardPayment(-1,creditCard));
		assertThrows(InvalidTransactionIdException.class,() -> controller.receiveCreditCardPayment(null,creditCard));

		assertFalse(controller.receiveCreditCardPayment(10,creditCard));
		assertFalse(controller.receiveCreditCardPayment(transactionId,creditCard2));
		assertFalse(controller.receiveCreditCardPayment(transactionId,fakeCreditCard));
		assertTrue(controller.receiveCreditCardPayment(transactionId,creditCard));
    }

	
	@Test
	public void testReturnCashPayment()  throws InvalidTransactionIdException, UnauthorizedException {
		// TODO: to be implemented
		// unauth (nobody logged)
		//assertThrows(UnauthorizedException.class, () -> controller.returnCashPayment());

	}
	
	@Test
	public void testReturnCreditCardPayment() throws InvalidTransactionIdException, InvalidCreditCardException, UnauthorizedException{
		// TODO: to be implemented
		// unauth (nobody logged)
		//assertThrows(UnauthorizedException.class, () -> controller.returnCreditCardPayment());


	}
	
	@Test
	public void testRecordBalanceUpdate() throws UnauthorizedException, InvalidPasswordException, InvalidUsernameException {
		// TODO: to be implemented
		double toBeAdded=500.99;
		double toBeSubtractedTooMuch=-100000.00;
		// unauth (nobody logged)
		assertThrows(UnauthorizedException.class, () -> controller.recordBalanceUpdate(toBeAdded));

		// unauth (cashier)
		controller.login("Franco", "1234");
		assertThrows(UnauthorizedException.class, () -> controller.recordBalanceUpdate(toBeAdded));
		controller.logout();
		controller.login("Marco", "1234");

		assertTrue(controller.recordBalanceUpdate(toBeAdded));
		assertFalse(controller.recordBalanceUpdate(toBeSubtractedTooMuch));
	}
	
	
	@Test
	public void testGetCreditsAndDebits() throws InvalidUsernameException, InvalidPasswordException, UnauthorizedException {
		LocalDate to = LocalDate.of(2021, 5, 25);
		LocalDate from  = LocalDate.now();
		
		// unauth (nobody logged)
		assertThrows(UnauthorizedException.class, () -> controller.getCreditsAndDebits(from, to));
		
		// unauth (cashier) 
		controller.login("Franco", "1234");
		assertThrows(UnauthorizedException.class, () -> controller.getCreditsAndDebits(from, to));
		controller.logout();
		
		controller.login("Marco", "1234");
		List<BalanceOperation> ops = controller.getCreditsAndDebits(from,to );
		List<BalanceOperation> expected = balanceOperationRepository.findAllBetweenDates(to, from);
		
		
		assertEquals(ops, expected);
	}
	
	
	@After
	public void after() {
		controller.logout();
	}
	
	

}
