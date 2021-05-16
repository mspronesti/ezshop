package it.polito.ezshop.data;

import it.polito.ezshop.annotations.*;
import it.polito.ezshop.data.ProductType;
import it.polito.ezshop.data.ProductTypeImpl;
import it.polito.ezshop.exceptions.*;
import jakarta.validation.*;
import jakarta.validation.executable.ExecutableValidator;
import org.hibernate.exception.JDBCConnectionException;
import org.springframework.security.crypto.bcrypt.BCrypt;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class EZShopControllerImpl implements EZShopController {
    private final BalanceOperationRepository balanceOperationRepository = new BalanceOperationRepository();
    private final CustomerRepository customerRepository = new CustomerRepository();
    private final LoyaltyCardRepository loyaltyCardRepository = new LoyaltyCardRepository();
    private final OrderRepository orderRepository = new OrderRepository();
    private final ProductTypeRepository productTypeRepository = new ProductTypeRepository();
    private final SaleTransactionRepository saleTransactionRepository = new SaleTransactionRepository();
    private final UserRepository userRepository = new UserRepository();
    private User loggedUser = null;

    public Integer createUser(String username, String password, String role) throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException {
        User user = new UserImpl();
        user.setUsername(username);
        user.setPassword(BCrypt.hashpw(password, BCrypt.gensalt()));
        user.setRole(role);
        return userRepository.create(user);
    }

    public boolean deleteUser(Integer id) throws InvalidUserIdException, UnauthorizedException {
        User user = userRepository.find(id);
        if (user != null) {
            userRepository.delete(user);
            return true;
        }
        return false;
    }

    public List<User> getAllUsers() throws UnauthorizedException {
        return userRepository.findAll();
    }

    public User getUser(Integer id) throws InvalidUserIdException, UnauthorizedException {
        return userRepository.find(id);
    }

    public boolean updateUserRights(Integer id, String role) throws InvalidUserIdException, InvalidRoleException, UnauthorizedException {
        User user = userRepository.find(id);
        if (user != null) {
            user.setRole(role);
            userRepository.update(user);
            return true;
        }
        return false;
    }

    public User login(String username, String password) throws InvalidUsernameException, InvalidPasswordException {
        User user = userRepository.findByUsername(username);
        if (user != null && BCrypt.checkpw(password, user.getPassword())) {
            loggedUser = user;
            return user;
        }
        return null;
    }

    public boolean logout() {
        if (loggedUser != null) {
            loggedUser = null;
            return true;
        }
        return false;
    }

    public Integer createProductType(String description, String productCode, double pricePerUnit, String note) throws InvalidProductDescriptionException, InvalidProductCodeException, InvalidPricePerUnitException, UnauthorizedException {
    	 if(!isValidGTIN(productCode)) {
         	throw new InvalidProductCodeException();
         }
    	
    	ProductType product = new ProductTypeImpl();
        product.setProductDescription(description);
        product.setPricePerUnit(pricePerUnit);
        product.setNote(note);
    	
    	return productTypeRepository.create(product);
    }

    public boolean updateProduct(Integer id, String newDescription, String newCode, double newPrice, String newNote) throws InvalidProductIdException, InvalidProductDescriptionException, InvalidProductCodeException, InvalidPricePerUnitException, UnauthorizedException {
        if(!isValidGTIN(newCode)) {
        	throw new InvalidProductCodeException();
        }
        
    	ProductType product = productTypeRepository.find(id);
    	if(product != null) {
    		product.setProductDescription(newDescription);
    		product.setBarCode(newCode);
    		product.setPricePerUnit(newPrice);
    		product.setNote(newNote);
    		return true;
    	}
    	return false;
    }

    public boolean deleteProductType(Integer id) throws InvalidProductIdException, UnauthorizedException {   	 
        ProductType productType = productTypeRepository.find(id);
        if (productType != null) {
            productTypeRepository.delete(productType);
            return true;
        }
        return false;
    }

    public List<ProductType> getAllProductTypes() throws UnauthorizedException {
        return productTypeRepository.findAll();
    }
    
    public ProductType getProductTypeByBarCode(String barCode) throws InvalidProductCodeException, UnauthorizedException {
    	 if(!isValidGTIN(barCode)) {
         	throw new InvalidProductCodeException();
        }
    	 
    	return productTypeRepository.findByBarcode(barCode);
    }

    public List<ProductType> getProductTypesByDescription(String description) throws UnauthorizedException {
        return productTypeRepository.findAll().stream()
                .filter(t -> t.getProductDescription().contains(description))
                .collect(Collectors.toList());
    }

    public boolean updateQuantity(Integer productId, int toBeAdded) throws InvalidProductIdException, UnauthorizedException {
    	ProductType product = this.productTypeRepository.find(productId);
        int newQuantity = product.getQuantity() + toBeAdded;
        if (newQuantity >= 0 && !product.getLocation().isEmpty()) {
            product.setQuantity(newQuantity);
            productTypeRepository.update(product);
            return true;
        }
        return false;
    }

    public boolean updatePosition(Integer productId, String newPos) throws InvalidProductIdException, InvalidLocationException, UnauthorizedException {
        ProductType product = this.productTypeRepository.find(productId);
        if (product != null && !isAssignedPosition(newPos)) {
            // resets or sets position
            product.setLocation(newPos == null || newPos.isEmpty() ? "" : newPos);
            productTypeRepository.update(product);
            return true;
        }
        return false;
    }

    public Integer issueOrder(String productCode, int quantity, double pricePerUnit) throws InvalidProductCodeException, InvalidQuantityException, InvalidPricePerUnitException, UnauthorizedException {
    	if(!isValidGTIN(productCode)) {
          throw new InvalidProductCodeException();
        }
    	 
    	Order order = new OrderImpl();
        order.setPricePerUnit(pricePerUnit);
        order.setQuantity(quantity);
        order.setProductCode(productCode);
    	return orderRepository.create(order);
    }

    public Integer payOrderFor(String productCode, int quantity, double pricePerUnit) throws InvalidProductCodeException, InvalidQuantityException, InvalidPricePerUnitException, UnauthorizedException {
        return null;
    }

    public boolean payOrder(Integer orderId) throws InvalidOrderIdException, UnauthorizedException {
        return false;
    }

    public boolean recordOrderArrival(Integer orderId) throws InvalidOrderIdException, UnauthorizedException, InvalidLocationException {
        return false;
    }

    public List<Order> getAllOrders() throws UnauthorizedException {
        return orderRepository.findAll();
    }

    public Integer defineCustomer(String customerName) throws InvalidCustomerNameException, UnauthorizedException {
        Customer customer = new CustomerImpl();
        customer.setCustomerName(customerName);
        return customerRepository.create(customer);
    }

    public boolean modifyCustomer(Integer id, String newCustomerName, String newCustomerCard) throws InvalidCustomerNameException, InvalidCustomerCardException, InvalidCustomerIdException, UnauthorizedException {
        try {
        	Customer customer = customerRepository.find(id);
        	if(customer != null) {
        		customer.setCustomerCard(newCustomerCard); // se null/empty reset/keep ? 
        		customer.setCustomerName(newCustomerName);
        		return true;
        	}
        }
        catch(JDBCConnectionException exception){
        	// db unreachable
        	return false;
        }
        
        return false;
    }

    public boolean deleteCustomer(Integer id) throws InvalidCustomerIdException, UnauthorizedException {
        Customer customer = customerRepository.find(id);
        if (customer != null) {
            customerRepository.delete(customer);
            return true;
        }
        return false;
    }

    public Customer getCustomer(Integer id) throws InvalidCustomerIdException, UnauthorizedException {
        return customerRepository.find(id);
    }

    public List<Customer> getAllCustomers() throws UnauthorizedException {
        return this.customerRepository.findAll();
    }

    public String createCard() throws UnauthorizedException {
        LoyaltyCard loyaltyCard = new LoyaltyCardImpl();
        return this.loyaltyCardRepository.create(loyaltyCard);
    }

    public boolean attachCardToCustomer(String customerCard, Integer customerId) throws InvalidCustomerIdException, InvalidCustomerCardException, UnauthorizedException {
        Customer customer = this.customerRepository.find(customerId);
        if (customer != null) {
            customer.setCustomerCard(customerCard);
            this.customerRepository.update(customer);
            return true;
        }
        return false;
    }

    public boolean modifyPointsOnCard(String customerCard, int pointsToBeAdded) throws InvalidCustomerCardException, UnauthorizedException {
        LoyaltyCard loyaltyCard = this.loyaltyCardRepository.find(customerCard);
        if (loyaltyCard != null) {
            loyaltyCard.setPoints( loyaltyCard.getPoints() + pointsToBeAdded);
        	this.loyaltyCardRepository.update(loyaltyCard);
            return true;
        }
        return false;
    }

    public Integer startSaleTransaction() throws UnauthorizedException {
        SaleTransaction saleTransaction = new SaleTransactionImpl();
        return this.saleTransactionRepository.create(saleTransaction);
    }

    public boolean addProductToSale(Integer transactionId, String productCode, int amount) throws InvalidTransactionIdException, InvalidProductCodeException, InvalidQuantityException, UnauthorizedException {
        SaleTransaction saleTransaction = this.saleTransactionRepository.find(transactionId);
        if (saleTransaction == null) { // TODO started and open?
            return false;
        }
        ProductType productType = this.productTypeRepository.find(productCode);
        if (productType == null) {
            return false; // TODO shouldn't this be InvalidProductCodeException?
        }
        TicketEntry entry = saleTransaction.getEntries()
                .stream()
                .filter(e -> e.getBarCode().equals(productType.getBarCode()))
                .findFirst()
                .orElseGet(() -> {
                    TicketEntry newEntry = new TicketEntryImpl();
                    newEntry.setBarCode(productCode);
                    newEntry.setAmount(0);
                    newEntry.setPricePerUnit(productType.getPricePerUnit());
                    return newEntry;
                });
        int newAmount = entry.getAmount() + amount;
        if (productType.getQuantity() >= newAmount) {
            entry.setAmount(newAmount);
            productType.setQuantity(productType.getQuantity() - amount);
            if (!saleTransaction.getEntries().contains(entry)) {
                saleTransaction.getEntries().add(entry);
            }
            this.saleTransactionRepository.update(saleTransaction);
            this.productTypeRepository.update(productType);
            return true;
        }
        return false;
    }

    public boolean deleteProductFromSale(Integer transactionId, String productCode, int amount) throws InvalidTransactionIdException, InvalidProductCodeException, InvalidQuantityException, UnauthorizedException {
        return false;
    }

    public boolean applyDiscountRateToProduct(Integer transactionId, String productCode, double discountRate) throws InvalidTransactionIdException, InvalidProductCodeException, InvalidDiscountRateException, UnauthorizedException {
        return false;
    }

    public boolean applyDiscountRateToSale(Integer transactionId, double discountRate) throws InvalidTransactionIdException, InvalidDiscountRateException, UnauthorizedException {
        return false;
    }

    public int computePointsForSale(Integer transactionId) throws InvalidTransactionIdException, UnauthorizedException {
        return 0;
    }

    public boolean endSaleTransaction(Integer transactionId) throws InvalidTransactionIdException, UnauthorizedException {
        return false;
    }

    public boolean deleteSaleTransaction(Integer transactionId) throws InvalidTransactionIdException, UnauthorizedException {
        return false;
    }

    public SaleTransaction getSaleTransaction(Integer transactionId) throws InvalidTransactionIdException, UnauthorizedException {
        return null;
    }

    public Integer startReturnTransaction(Integer transactionId) throws InvalidTransactionIdException, UnauthorizedException {
        return null;
    }

    public boolean returnProduct(Integer returnId, String productCode, int amount) throws InvalidTransactionIdException, InvalidProductCodeException, InvalidQuantityException, UnauthorizedException {
        return false;
    }

    public boolean endReturnTransaction(Integer returnId, boolean commit) throws InvalidTransactionIdException, UnauthorizedException {
        return false;
    }

    public boolean deleteReturnTransaction(Integer returnId) throws InvalidTransactionIdException, UnauthorizedException {
        return false;
    }

    public double receiveCashPayment(Integer transactionId, double cash) throws InvalidTransactionIdException, InvalidPaymentException, UnauthorizedException {
        return 0;
    }

    public boolean receiveCreditCardPayment(Integer transactionId, String creditCard) throws InvalidTransactionIdException, InvalidCreditCardException, UnauthorizedException {
        return false;
    }

    public double returnCashPayment(Integer returnId) throws InvalidTransactionIdException, UnauthorizedException {
        return 0;
    }

    public double returnCreditCardPayment(Integer returnId, String creditCard) throws InvalidTransactionIdException, InvalidCreditCardException, UnauthorizedException {
        return 0;
    }

    public boolean recordBalanceUpdate(double toBeAdded) throws UnauthorizedException {
        return false;
    }

    public List<BalanceOperation> getCreditsAndDebits(LocalDate from, LocalDate to) throws UnauthorizedException {
        return null;
    }

    public double computeBalance() throws UnauthorizedException {
        return 0;
    }

    private static final ValidatorFactory validatorFactory = Validation.byDefaultProvider()
            .configure()
            .parameterNameProvider(new ParameterNameProvider() {
                @Override
                public List<String> getParameterNames(Constructor<?> constructor) {
                    return Arrays.stream(constructor.getParameters())
                            .map(Parameter::getName)
                            .collect(Collectors.toList());
                }

                @Override
                public List<String> getParameterNames(Method method) {
                    return Arrays.stream(method.getParameters())
                            .map(Parameter::getName)
                            .collect(Collectors.toList());
                }
            })
            .buildValidatorFactory();
    private static final ExecutableValidator validator = validatorFactory.getValidator().forExecutables();

    static class ActionHandler implements InvocationHandler {
        private final EZShopControllerImpl target;

        public ActionHandler(EZShopControllerImpl target) {
            this.target = target;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            AcceptRoles rolesAnnotation = method.getAnnotation(AcceptRoles.class);
            if (rolesAnnotation != null) {
                if (target.loggedUser != null) {
                    List<String> acceptedRoles = Arrays.stream(rolesAnnotation.value())
                            .map(Enum::name)
                            .collect(Collectors.toList());
                    if (!acceptedRoles.contains(target.loggedUser.getRole())) {
                        throw new UnauthorizedException();
                    }
                } else {
                    throw new UnauthorizedException();
                }
            }
            try {
                Object[] params;
                if (args != null) {
                    params = args;
                } else {
                    params = new Object[]{};
                }
                Set<ConstraintViolation<Object>> violations = validator.validateParameters(target, method, params);
                if (!violations.isEmpty()) {
                    for (ConstraintViolation<Object> violation : violations) {
                        Iterator<Path.Node> propertyPath = violation.getPropertyPath().iterator();
                        Path.MethodNode methodNode = propertyPath.next().as(Path.MethodNode.class);
                        String parameterName = propertyPath.next().as(Path.ParameterNode.class).getName();
                        Optional<Parameter> parameter = Arrays.stream(method.getParameters())
                                .filter(p -> p.getName().equals(parameterName))
                                .findFirst();
                        if (parameter.isPresent()) {
                            Throw exceptionAnnotation = parameter.get().getAnnotation(Throw.class);
                            if (exceptionAnnotation != null) {
                                Class<? extends Throwable> parameterConstraintException = exceptionAnnotation.value();
                                throw parameterConstraintException.getDeclaredConstructor().newInstance();
                            }
                        }
                    }
                }
                return method.invoke(target, args);
            } catch (JDBCConnectionException exception) {
                FallbackStringValue fallbackStringAnnotation = method.getAnnotation(FallbackStringValue.class);
                if (fallbackStringAnnotation != null) {
                    return fallbackStringAnnotation.value();
                }
                FallbackIntValue fallbackIntAnnotation = method.getAnnotation(FallbackIntValue.class);
                if (fallbackIntAnnotation != null) {
                    return fallbackIntAnnotation.value();
                }
                FallbackBooleanValue fallbackBooleanAnnotation = method.getAnnotation(FallbackBooleanValue.class);
                if (fallbackBooleanAnnotation != null) {
                    return fallbackBooleanAnnotation.value();
                }
            }
            return null;
        }
    }

    /**
     * This methods checks whether a given location
     * is already used for some product inside store
     *
     * @param location to check
     * @return true if used, false otherwise
     */
    private boolean isAssignedPosition(String location) {
        return this.productTypeRepository.findAll().stream().anyMatch(p -> p.getLocation().equals(location));
    }

    /**
     * This method checks whether a given card Id
     * is already associated to some customer
     *
     * @param customerCard
     * @return true if cardId in use, false otherwise
     */
    private boolean isAssignedCardId(String customerCard) {
        return this.customerRepository.findAll().stream().anyMatch(p -> p.getCustomerCard().equals(customerCard));
    }
    
    /**
     * Checks wether a provided string of digits corresponds to a valid 
     * GTIN bar code
     * @param string to check
     * @return true if gtin, false otherwise
     */
    private boolean isValidGTIN (String gtin) {
    	if(!Pattern.matches("^\\d{12,14}$", gtin)) 
    		return false;
    	
    	AtomicInteger index = new AtomicInteger();
    	
    	// create string from input excluding last digit
    	String code = gtin.substring(0, gtin.length() - 1);
    	// extracting last digit 
    	int checkDigit = gtin.charAt(gtin.length() - 1) ^ '0';
    	
    	// fill with appropriate number of initial zeros
    	code = String.format("%0" + (17 - code.length() )+"d%s",0 , code);	
		// compute sum of the digits after processing 
    	int sum = Integer.valueOf(code.chars()
					     			  .map(p -> p ^ '0') // char to int 
					     			  .map(p -> index.getAndIncrement() % 2 == 0 ? p * 3 : p)
					     			  .sum());
		
    	return (sum + 9)/10 * 10 - sum == checkDigit;
    }
    	
}
