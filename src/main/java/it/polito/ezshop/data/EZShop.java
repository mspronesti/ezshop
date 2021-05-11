package it.polito.ezshop.data;

import it.polito.ezshop.exceptions.*;
import org.springframework.security.crypto.bcrypt.BCrypt;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


public class EZShop implements EZShopInterface {

    private final BalanceOperationRepository balanceOperationRepository;
    private final CustomerRepository customerRepository;
    private final OrderRepository orderRepository;
    private final ProductTypeRepository productTypeRepository;
    private final SaleTransactionRepository saleTransactionRepository;
    private final UserRepository userRepository;
    private User loggedUser;

    public EZShop() {
        balanceOperationRepository = new BalanceOperationRepository();
        customerRepository = new CustomerRepository();
        orderRepository = new OrderRepository();
        productTypeRepository = new ProductTypeRepository();
        saleTransactionRepository = new SaleTransactionRepository();
        userRepository = new UserRepository();
        loggedUser = null;
    }

    @Override
    public void reset() {

    }

    @Override
    public Integer createUser(String username, String password, String role) throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException {
    	if(username == null || username.isEmpty()) {
    		throw new InvalidUsernameException();
    	}
    	if(password == null || password.isEmpty()) {
    		throw new InvalidPasswordException();
    	}
    	if(!isValidRole(role)) { 
    		throw new InvalidRoleException();
    	}
    	
    	User user = new UserImpl();
        user.setUsername(username);
        user.setPassword(BCrypt.hashpw(password, BCrypt.gensalt()));
        user.setRole(role);
        return userRepository.create(user);
    }

    @Override
    public boolean deleteUser(Integer id) throws InvalidUserIdException, UnauthorizedException {
    	if(!isValidId(id)) { 
    		throw new InvalidUserIdException();
    	}
    	if(!isAuthorized(loggedUser.getRole())) {
			throw new UnauthorizedException();
    	}
    	
    	User user = this.userRepository.find(id);
    	if(user != null) {
        	this.userRepository.delete(user);
        	return true;
        }
    	
    	return false;    
}

    @Override
    public List<User> getAllUsers() throws UnauthorizedException {
    	return userRepository.findAll().stream().collect(Collectors.toList());
    }

    @Override
    public User getUser(Integer id) throws InvalidUserIdException, UnauthorizedException {
    	if(!isValidId(id)) { 
    		throw new InvalidUserIdException();
    	}
    	if(!isAuthorized(loggedUser.getRole())) {
			throw new UnauthorizedException();
    	}
  	
    	return this.userRepository.find(id);
    }

    @Override
    public boolean updateUserRights(Integer id, String role) throws InvalidUserIdException, InvalidRoleException, UnauthorizedException {
    	if(!isValidId(id)) {
    		throw new InvalidUserIdException();
    	}
    	if(!isValidRole(role)) {
    		throw new InvalidRoleException();
    	}
    	if(!isAuthorized(loggedUser.getRole())) {
			throw new UnauthorizedException();
    	}
    	
    	User user = this.userRepository.find(id); 
    	if (user != null) {
        	user.setRole(role);
        	return true;
        }
     
    	return false;
    }

    @Override
    public User login(String username, String password) throws InvalidUsernameException, InvalidPasswordException {
    	User user = this.userRepository.findByUsername(username);
        if (user == null) {
            throw new InvalidUsernameException();
        }
        if (!BCrypt.checkpw(password, user.getPassword())) {
            throw new InvalidPasswordException();
        }
        
        loggedUser = user;
        return user;
    }

    @Override
    public boolean logout() {
    	// loggedUser = null;
        return false;
    }

    @Override
    public Integer createProductType(String description, String productCode, double pricePerUnit, String note) throws InvalidProductDescriptionException, InvalidProductCodeException, InvalidPricePerUnitException, UnauthorizedException {
        return null; // lascio per dopo: devo vedere la validazione del bar code al link mandato su slack
    }

    @Override
    public boolean updateProduct(Integer id, String newDescription, String newCode, double newPrice, String newNote) throws InvalidProductIdException, InvalidProductDescriptionException, InvalidProductCodeException, InvalidPricePerUnitException, UnauthorizedException {
        return false;
    }

    @Override
    public boolean deleteProductType(Integer id) throws InvalidProductIdException, UnauthorizedException {
    	if(!isValidId(id)) {
    		throw new InvalidProductIdException();
    	}
    	// se non c'è nessuno loggato, come ho accesso qui ?
    	if(loggedUser == null || !isAuthorized(loggedUser.getRole())) { 
    		throw new UnauthorizedException();
    	}
        return false;
    }

    @Override
    public List<ProductType> getAllProductTypes() throws UnauthorizedException {
        return productTypeRepository.findAll().stream().collect(Collectors.toList());
    }

    @Override
    public ProductType getProductTypeByBarCode(String barCode) throws InvalidProductCodeException, UnauthorizedException {
        return null;
    }

    @Override
    public List<ProductType> getProductTypesByDescription(String description) throws UnauthorizedException {
        return productTypeRepository.findAll().stream()
        		.filter(t -> t.getProductDescription().contains(description))
        		.collect(Collectors.toList());
    }

    @Override
    public boolean updateQuantity(Integer productId, int toBeAdded) throws InvalidProductIdException, UnauthorizedException {
        if(!isValidId(productId)) {
        	throw new InvalidProductIdException();
        }
        if(!isValidRole(loggedUser.getRole())) {
        	throw new UnauthorizedException();
        }
        
        ProductType product = this.productTypeRepository.find(productId);
        int newQuantity = product.getQuantity() + toBeAdded;
        
        if(newQuantity >= 0 && !product.getLocation().isEmpty()) { 
        	product.setQuantity(newQuantity);
        	return true;
        }
        
    	return false;
    }

    @Override
    public boolean updatePosition(Integer productId, String newPos) throws InvalidProductIdException, InvalidLocationException, UnauthorizedException {
        if(!isValidId(productId)) {
        	throw new InvalidProductIdException();
        }
        if(!isAuthorized(loggedUser.getRole())) {
        	throw new UnauthorizedException();
        }
        if(!Pattern.matches("^[0-9]+-[A-Za-z]-[0-9]*$", newPos)) {
        	throw new InvalidLocationException();
        }
        
        ProductType product = this.productTypeRepository.find(productId);
    	
        if(product != null && !isAssignedPosition(newPos)) {
        	// resets or sets position
    		product.setLocation(newPos.isEmpty() || newPos == null ? "" : newPos);
    		return true;
    	}
        
        return false;
    }

    @Override
    public Integer issueOrder(String productCode, int quantity, double pricePerUnit) throws InvalidProductCodeException, InvalidQuantityException, InvalidPricePerUnitException, UnauthorizedException {
        return null;
    }

    @Override
    public Integer payOrderFor(String productCode, int quantity, double pricePerUnit) throws InvalidProductCodeException, InvalidQuantityException, InvalidPricePerUnitException, UnauthorizedException {
        return null;
    }

    @Override
    public boolean payOrder(Integer orderId) throws InvalidOrderIdException, UnauthorizedException {
        return false;
    }

    @Override
    public boolean recordOrderArrival(Integer orderId) throws InvalidOrderIdException, UnauthorizedException, InvalidLocationException {
        return false;
    }

    @Override
    public List<Order> getAllOrders() throws UnauthorizedException {
        return null;
    }

    @Override
    public Integer defineCustomer(String customerName) throws InvalidCustomerNameException, UnauthorizedException {
        if(customerName == null || customerName.isEmpty())
        	throw new InvalidCustomerNameException();
        
        Customer customer = new CustomerImpl();
        customer.setCustomerName(customerName);
        return customerRepository.create(customer);
    }

    @Override
    public boolean modifyCustomer(Integer id, String newCustomerName, String newCustomerCard) throws InvalidCustomerNameException, InvalidCustomerCardException, InvalidCustomerIdException, UnauthorizedException {
        return false;
    }

    @Override
    public boolean deleteCustomer(Integer id) throws InvalidCustomerIdException, UnauthorizedException {
    	if(!isValidId(id)) { 
    		throw new InvalidCustomerIdException();
    	}
    	if(!isAuthorized(loggedUser.getRole())) {
    		throw new UnauthorizedException();
    	}
    	
    	Customer customer = this.customerRepository.find(id);
    	if(customer != null) {
        	this.customerRepository.delete(customer);
        	return true;
        }
    	
    	return false;    
    }

    @Override
    public Customer getCustomer(Integer id) throws InvalidCustomerIdException, UnauthorizedException {
    	if(!isValidId(id)) { 
    		throw new InvalidCustomerIdException();
    	}
    	if(!isAuthorized(loggedUser.getRole())) {
			throw new UnauthorizedException();
    	}
  	
    	return this.customerRepository.find(id);
    }

    @Override
    public List<Customer> getAllCustomers() throws UnauthorizedException {
    	if(!isAuthorized(loggedUser.getRole())) {
			throw new UnauthorizedException();
    	}
        return this.customerRepository.findAll().stream().collect(Collectors.toList());
    }

    @Override
    public String createCard() throws UnauthorizedException {
        return null;
    }

    @Override
    public boolean attachCardToCustomer(String customerCard, Integer customerId) throws InvalidCustomerIdException, InvalidCustomerCardException, UnauthorizedException {
        return false;
    }

    @Override
    public boolean modifyPointsOnCard(String customerCard, int pointsToBeAdded) throws InvalidCustomerCardException, UnauthorizedException {
        return false;
    }

    @Override
    public Integer startSaleTransaction() throws UnauthorizedException {
        return null;
    }

    @Override
    public boolean addProductToSale(Integer transactionId, String productCode, int amount) throws InvalidTransactionIdException, InvalidProductCodeException, InvalidQuantityException, UnauthorizedException {
        return false;
    }

    @Override
    public boolean deleteProductFromSale(Integer transactionId, String productCode, int amount) throws InvalidTransactionIdException, InvalidProductCodeException, InvalidQuantityException, UnauthorizedException {
        return false;
    }

    @Override
    public boolean applyDiscountRateToProduct(Integer transactionId, String productCode, double discountRate) throws InvalidTransactionIdException, InvalidProductCodeException, InvalidDiscountRateException, UnauthorizedException {
        return false;
    }

    @Override
    public boolean applyDiscountRateToSale(Integer transactionId, double discountRate) throws InvalidTransactionIdException, InvalidDiscountRateException, UnauthorizedException {
        return false;
    }

    @Override
    public int computePointsForSale(Integer transactionId) throws InvalidTransactionIdException, UnauthorizedException {
        return 0;
    }

    @Override
    public boolean endSaleTransaction(Integer transactionId) throws InvalidTransactionIdException, UnauthorizedException {
        return false;
    }

    @Override
    public boolean deleteSaleTransaction(Integer saleNumber) throws InvalidTransactionIdException, UnauthorizedException {
        return false;
    }

    @Override
    public SaleTransaction getSaleTransaction(Integer transactionId) throws InvalidTransactionIdException, UnauthorizedException {
        return null;
    }

    @Override
    public Integer startReturnTransaction(Integer saleNumber) throws /*InvalidTicketNumberException,*/InvalidTransactionIdException, UnauthorizedException {
        return null;
    }

    @Override
    public boolean returnProduct(Integer returnId, String productCode, int amount) throws InvalidTransactionIdException, InvalidProductCodeException, InvalidQuantityException, UnauthorizedException {
        return false;
    }

    @Override
    public boolean endReturnTransaction(Integer returnId, boolean commit) throws InvalidTransactionIdException, UnauthorizedException {
        return false;
    }

    @Override
    public boolean deleteReturnTransaction(Integer returnId) throws InvalidTransactionIdException, UnauthorizedException {
        return false;
    }

    @Override
    public double receiveCashPayment(Integer ticketNumber, double cash) throws InvalidTransactionIdException, InvalidPaymentException, UnauthorizedException {
        return 0;
    }

    @Override
    public boolean receiveCreditCardPayment(Integer ticketNumber, String creditCard) throws InvalidTransactionIdException, InvalidCreditCardException, UnauthorizedException {
        return false;
    }

    @Override
    public double returnCashPayment(Integer returnId) throws InvalidTransactionIdException, UnauthorizedException {
        return 0;
    }

    @Override
    public double returnCreditCardPayment(Integer returnId, String creditCard) throws InvalidTransactionIdException, InvalidCreditCardException, UnauthorizedException {
        return 0;
    }

    @Override
    public boolean recordBalanceUpdate(double toBeAdded) throws UnauthorizedException {
        return false;
    }

    @Override
    public List<BalanceOperation> getCreditsAndDebits(LocalDate from, LocalDate to) throws UnauthorizedException {
        return null;
    }

    @Override
    public double computeBalance() throws UnauthorizedException {
        return 0;
    }
    
    /**
     * This method checks whether a provided role is valid
     * @param role: the role to check
     * @return true if valid role, false otherwise
     */
    private boolean isValidRole(String role) {
    	List<String> roles = new ArrayList<>(List.of("Administrator", "Cashier", "ShopManager"));
    	return role != null && !role.isEmpty() && roles.contains(role);
    }
    
    /**
     * 
     * @param role
     * @return
     */
    private boolean isAuthorized(String role) {
    	return role.equals("Administrator") || role.equals("ShopManager");
    	// oppure !role.equals("Cashier") ma lo trovo meno leggibile
    }
    
    /**
     * This methods checks whether a provided id is
     * not null and greater than 0
     * @param id: id to check
     * @return true if valid, false otherwise
     */
    private boolean isValidId(Integer id) {
    	return id != null && id >= 0;
    }
    
    /**
     * This methods checks whether a given location
     * is already used for some product inside store
     * @param location to check
     * @return true if used, false otherwise
     */
    private boolean isAssignedPosition(String location) {
    	return this.productTypeRepository.findAll().stream().anyMatch(p -> p.getLocation().equals(location));
    }
}
