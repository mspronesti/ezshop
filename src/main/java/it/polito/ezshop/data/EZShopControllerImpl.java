package it.polito.ezshop.data;

import it.polito.ezshop.annotations.*;
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
import java.util.stream.Collectors;


public class EZShopControllerImpl implements EZShopController {
    private final BalanceOperationRepository balanceOperationRepository = new BalanceOperationRepository();
    private final CustomerRepository customerRepository = new CustomerRepository();
    private final LoyaltyCardRepository loyaltyCardRepository = new LoyaltyCardRepository();
    private final OrderRepository orderRepository = new OrderRepository();
    private final ProductTypeRepository productTypeRepository = new ProductTypeRepository();
    private final ReturnTransactionRepository returnTransactionRepository = new ReturnTransactionRepository();
    private final SaleTransactionRepository saleTransactionRepository = new SaleTransactionRepository();
    private final UserRepository userRepository = new UserRepository();
    private User loggedUser = null;
    private final Map<Integer, SaleTransactionImpl> openSaleTransactions = new HashMap<>();
    private final Map<Integer, ReturnTransactionImpl> openReturnTransactions = new HashMap<>();
    
    public void reset() {
    	// maybe deleteAll inside each repository to avoid N queries ?
    	this.productTypeRepository.findAll().forEach(productTypeRepository::delete);
    	this.orderRepository.findAll().forEach(orderRepository::delete);
    	this.returnTransactionRepository.findAll().forEach(returnTransactionRepository::delete);
    	this.saleTransactionRepository.findAll().forEach(saleTransactionRepository::delete);
    	this.balanceOperationRepository.findAll().forEach(balanceOperationRepository::delete);
    }
    
    public Integer createUser(String username, String password, String role) throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException {
        if(userRepository.findByUsername(username) != null) {
        	// user with given username already exists
        	return -1;
        }
    	
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
        ProductType product = new ProductTypeImpl();
        product.setProductDescription(description);
        product.setPricePerUnit(pricePerUnit);
        product.setNote(note);
        product.setBarCode(productCode);
        try {
            return productTypeRepository.create(product);
        } catch (Exception exception) {
            return -1;
        }
    }

    public boolean updateProduct(Integer id, String newDescription, String newCode, double newPrice, String newNote) throws InvalidProductIdException, InvalidProductDescriptionException, InvalidProductCodeException, InvalidPricePerUnitException, UnauthorizedException {
        ProductType product = productTypeRepository.find(id);
        if (product != null) {
            product.setProductDescription(newDescription);
            product.setBarCode(newCode);
            product.setPricePerUnit(newPrice);
            product.setNote(newNote);
            productTypeRepository.update(product);
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
        ProductType product = this.productTypeRepository.findByBarcode(productCode);
        if (product == null) {
            // can't order an unexisting product
            return -1;
        }
        Order order = new OrderImpl();
        order.setPricePerUnit(pricePerUnit);
        order.setQuantity(quantity);
        order.setProductCode(productCode);
        order.setStatus(OrderImpl.Status.ISSUED.name());
        return orderRepository.create(order);
    }

    public Integer payOrderFor(String productCode, int quantity, double pricePerUnit) throws InvalidProductCodeException, InvalidQuantityException, InvalidPricePerUnitException, UnauthorizedException {
        ProductType product = this.productTypeRepository.findByBarcode(productCode);
        if (product == null) {
            // can't order an unexisting product 
            return -1;
        }
        
        Integer orderId = issueOrder(productCode, quantity, pricePerUnit);
        try {
        	if(!payOrder(orderId)) {
        		return -1;
        	}
        }catch(Exception e) {
        	return -1;
        }
        
        return orderId;
    }

    public boolean payOrder(Integer orderId) throws InvalidOrderIdException, UnauthorizedException {
        Order order = orderRepository.find(orderId);
        if (order != null) {
            String status = order.getStatus();
            if(status.equals(OrderImpl.Status.PAYED.name()))
            	return true;
            
            if (status.equals(OrderImpl.Status.ISSUED.name())) {
            	boolean payed =  recordBalanceUpdate(-1 * order.getPricePerUnit() * order.getQuantity());
            	if(payed) {
            		// set payed
            		order.setStatus(OrderImpl.Status.PAYED.name());
            		orderRepository.update(order);
            		return true;
            	}
            }
        }
        return false;
    }

    public boolean recordOrderArrival(Integer orderId) throws InvalidOrderIdException, UnauthorizedException, InvalidLocationException {
        Order order = orderRepository.find(orderId);
        if (order != null) {
            String status = order.getStatus();

            if (status.equals(OrderImpl.Status.COMPLETED.name()))
                return true;

            if (status.equals(OrderImpl.Status.PAYED.name())) {
                // set COMPLETED
            	order.setStatus(OrderImpl.Status.COMPLETED.name());
                orderRepository.update(order);
                
                ProductType product = productTypeRepository.findByBarcode(order.getProductCode());
                if (product != null) {
                	// update quantity
                    product.setQuantity(product.getQuantity() + order.getQuantity());
                    productTypeRepository.update(product);
                    return true;
                }
            }
        }
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
        Customer customer = customerRepository.find(id);
        if (customer != null) {
            customer.setCustomerCard(newCustomerCard); // se null/empty reset/keep ?
            customer.setCustomerName(newCustomerName);
            customerRepository.update(customer);
            return true;
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
        if (pointsToBeAdded < 0) {
            return false;
        }
        LoyaltyCard loyaltyCard = this.loyaltyCardRepository.find(customerCard);
        if (loyaltyCard != null) {
            loyaltyCard.setPoints(loyaltyCard.getPoints() + pointsToBeAdded);
            this.loyaltyCardRepository.update(loyaltyCard);
            return true;
        }
        return false;
    }

    public Integer startSaleTransaction() throws UnauthorizedException {
        SaleTransactionImpl saleTransaction = new SaleTransactionImpl();
        Integer id = saleTransactionRepository.create(saleTransaction);
        openSaleTransactions.put(id, saleTransaction);
        return id;
    }

    public boolean addProductToSale(Integer transactionId, String productCode, int amount) throws InvalidTransactionIdException, InvalidProductCodeException, InvalidQuantityException, UnauthorizedException {
        SaleTransactionImpl saleTransaction = openSaleTransactions.get(transactionId);
        if (saleTransaction == null) {
            return false;
        }
        ProductType product = productTypeRepository.findByBarcode(productCode);
        if (product == null) {
            return false;
        }
        TicketEntry entry = saleTransaction.getEntryByBarcode(productCode);
        if (entry == null) {
            entry = new TicketEntryImpl();
            entry.setBarCode(productCode);
            entry.setAmount(0);
            entry.setPricePerUnit(product.getPricePerUnit());
        }
        int newAmount = entry.getAmount() + amount;
        if (product.getQuantity() >= amount) {
            entry.setAmount(newAmount);
            product.setQuantity(product.getQuantity() - amount);
            productTypeRepository.update(product);
            if (!saleTransaction.getEntries().contains(entry)) {
                saleTransaction.getEntries().add(entry);
            }
            saleTransaction.updatePrice();
            return true;
        }
        return false;
    }

    public boolean deleteProductFromSale(Integer transactionId, String productCode, int amount) throws InvalidTransactionIdException, InvalidProductCodeException, InvalidQuantityException, UnauthorizedException {
        SaleTransactionImpl saleTransaction = openSaleTransactions.get(transactionId);
        if (saleTransaction == null) {
            return false;
        }
        TicketEntry entry = saleTransaction.getEntryByBarcode(productCode);
        ProductType product = productTypeRepository.find(productCode);
        if (product == null || entry == null || amount > entry.getAmount()) {
            return false;
        }
        int newAmount = entry.getAmount() - amount;
        if (newAmount == 0) {
            saleTransaction.getEntries().remove(entry);
        } else {
            entry.setAmount(newAmount);
        }
        product.setQuantity(product.getQuantity() + amount);
        saleTransaction.updatePrice();
        productTypeRepository.update(product);
        return true;
    }

    public boolean applyDiscountRateToProduct(Integer transactionId, String productCode, double discountRate) throws InvalidTransactionIdException, InvalidProductCodeException, InvalidDiscountRateException, UnauthorizedException {
        SaleTransactionImpl saleTransaction = openSaleTransactions.get(transactionId);
        if (saleTransaction == null) {
            return false;
        }
        TicketEntry entry = saleTransaction.getEntryByBarcode(productCode);
        if (entry == null) {
            return false;
        }
        entry.setDiscountRate(discountRate);
        saleTransaction.updatePrice();
        saleTransactionRepository.update(saleTransaction);
        return true;
    }

    public boolean applyDiscountRateToSale(Integer transactionId, double discountRate) throws InvalidTransactionIdException, InvalidDiscountRateException, UnauthorizedException {
        SaleTransactionImpl saleTransaction = openSaleTransactions.get(transactionId);
        if (saleTransaction == null) {
            return false;
        }
        saleTransaction.setDiscountRate(discountRate);
        saleTransaction.updatePrice();
        saleTransactionRepository.update(saleTransaction);
        return true;
    }

    public int computePointsForSale(Integer transactionId) throws InvalidTransactionIdException, UnauthorizedException {
        SaleTransactionImpl saleTransaction = openSaleTransactions.get(transactionId);
        if (saleTransaction == null) {
            saleTransaction = (SaleTransactionImpl) saleTransactionRepository.find(transactionId);
            if (saleTransaction == null) {
                return -1;
            }
        }
        return (int) Math.floor(saleTransaction.getPrice() / 10);
    }

    public boolean endSaleTransaction(Integer transactionId) throws InvalidTransactionIdException, UnauthorizedException {
        SaleTransactionImpl saleTransaction = openSaleTransactions.get(transactionId);
        if (saleTransaction == null) {
            return false;
        }
        saleTransactionRepository.update(saleTransaction);
        openSaleTransactions.remove(transactionId);
        return true;
    }

    public boolean deleteSaleTransaction(Integer transactionId) throws InvalidTransactionIdException, UnauthorizedException {
        SaleTransactionImpl saleTransaction = openSaleTransactions.get(transactionId);
        if (saleTransaction == null) {
            return false;
        }
        for (TicketEntry entry : saleTransaction.getEntries()) {
            ProductType product = productTypeRepository.findByBarcode(entry.getBarCode());
            if (product == null) {
                return false;
            }
            product.setQuantity(product.getQuantity() + entry.getAmount());
            productTypeRepository.update(product);
        }
        openSaleTransactions.remove(transactionId);
        return true;
    }

    public SaleTransaction getSaleTransaction(Integer transactionId) throws InvalidTransactionIdException, UnauthorizedException {
        return saleTransactionRepository.find(transactionId);
    }

    public Integer startReturnTransaction(Integer transactionId) throws InvalidTransactionIdException, UnauthorizedException {
        SaleTransaction saleTransaction = saleTransactionRepository.find(transactionId);
        if (saleTransaction == null) {
            return -1;
        }
        ReturnTransactionImpl returnTransaction = new ReturnTransactionImpl();
        returnTransaction.setSaleTransaction((SaleTransactionImpl) saleTransaction);
        Integer id = returnTransactionRepository.create(returnTransaction);
        openReturnTransactions.put(id, returnTransaction);
        return id;
    }

    public boolean returnProduct(Integer returnId, String productCode, int amount) throws InvalidTransactionIdException, InvalidProductCodeException, InvalidQuantityException, UnauthorizedException {
        ReturnTransactionImpl returnTransaction = openReturnTransactions.get(returnId);
        if (returnTransaction == null) {
            return false;
        }
        SaleTransactionImpl saleTransaction = returnTransaction.getSaleTransaction();
        if (saleTransaction == null) {
            return false;
        }
        TicketEntry entry = saleTransaction.getEntryByBarcode(productCode);
        if (entry == null || entry.getAmount() < amount) {
            return false;
        }
        TicketEntry returnEntry = new TicketEntryImpl();
        returnEntry.setBarCode(productCode);
        returnEntry.setAmount(amount);
        returnEntry.setDiscountRate(entry.getDiscountRate());
        returnEntry.setPricePerUnit(entry.getPricePerUnit());
        returnTransaction.getEntries().add(returnEntry);
        returnTransaction.updatePrice();
        return true;
    }

    public boolean endReturnTransaction(Integer returnId, boolean commit) throws InvalidTransactionIdException, UnauthorizedException {
        ReturnTransactionImpl returnTransaction = openReturnTransactions.get(returnId);
        if (returnTransaction == null) {
            return false;
        }
        if (!commit) {
            openReturnTransactions.remove(returnTransaction.getTicketNumber());
            return true;
        }
        SaleTransactionImpl saleTransaction = returnTransaction.getSaleTransaction();
        if (saleTransaction == null) {
            return false;
        }
        for (TicketEntry returnEntry : returnTransaction.getEntries()) {
            TicketEntry saleEntry = saleTransaction.getEntryByBarcode(returnEntry.getBarCode());
            ProductType product = productTypeRepository.findByBarcode(returnEntry.getBarCode());
            if (saleEntry == null || product == null) {
                return false;
            }
            int newAmount = saleEntry.getAmount() - returnEntry.getAmount();
            if (newAmount == 0) {
                saleTransaction.getEntries().remove(saleEntry);
                continue;
            }
            saleEntry.setAmount(newAmount);
            product.setQuantity(product.getQuantity() + returnEntry.getAmount());
            productTypeRepository.update(product);
        }
        saleTransaction.updatePrice();
        saleTransactionRepository.update(saleTransaction);
        returnTransactionRepository.update(returnTransaction);
        return true;
    }

    public boolean deleteReturnTransaction(Integer returnId) throws InvalidTransactionIdException, UnauthorizedException {
        ReturnTransactionImpl returnTransaction = returnTransactionRepository.find(returnId);
        if (returnTransaction == null || returnTransaction.getPayment() != null) {
            return false;
        }
        SaleTransactionImpl saleTransaction = returnTransaction.getSaleTransaction();
        if (saleTransaction == null) {
            return false;
        }
        for (TicketEntry returnEntry : returnTransaction.getEntries()) {
            TicketEntry saleEntry = saleTransaction.getEntryByBarcode(returnEntry.getBarCode());
            if (saleEntry == null) {
                saleEntry = new TicketEntryImpl();
                saleEntry.setAmount(returnEntry.getAmount());
                saleEntry.setDiscountRate(returnEntry.getDiscountRate());
                saleEntry.setBarCode(returnEntry.getBarCode());
                saleEntry.setPricePerUnit(returnEntry.getPricePerUnit());
                saleTransaction.getEntries().add(saleEntry);
            } else {
                saleEntry.setAmount(saleEntry.getAmount() + returnEntry.getAmount());
            }
            ProductType product = productTypeRepository.findByBarcode(returnEntry.getBarCode());
            if (product == null) {
                return false;
            }
            product.setQuantity(product.getQuantity() + returnEntry.getAmount());
            productTypeRepository.update(product);
        }
        saleTransaction.updatePrice();
        saleTransactionRepository.update(saleTransaction);
        returnTransactionRepository.delete(returnTransaction);
        return true;
    }

    public double receiveCashPayment(Integer transactionId, double cash) throws InvalidTransactionIdException, InvalidPaymentException, UnauthorizedException {
        SaleTransactionImpl saleTransaction = (SaleTransactionImpl) saleTransactionRepository.find(transactionId);
        if (saleTransaction == null) {
            return -1;
        }
        double price = saleTransaction.getPrice();
        if (price > cash) {
            return -1;
        }
        BalanceOperationImpl payment = new BalanceOperationImpl();
        payment.setDate(LocalDate.now());
        payment.setType(BalanceOperationImpl.Type.CREDIT);
        payment.setMoney(price);
        balanceOperationRepository.create(payment);
        saleTransaction.setPayment(payment);
        saleTransactionRepository.update(saleTransaction);
        return cash - price;
    }

    public boolean receiveCreditCardPayment(Integer transactionId, String creditCard) throws InvalidTransactionIdException, InvalidCreditCardException, UnauthorizedException {
        // TODO(@umbo) waiting for an answer about creditcards.txt
        return false;
    }

    public double returnCashPayment(Integer returnId) throws InvalidTransactionIdException, UnauthorizedException {
        ReturnTransactionImpl returnTransaction = returnTransactionRepository.find(returnId);
        if (returnTransaction == null || returnTransaction.getPayment() != null) {
            return -1;
        }
        BalanceOperationImpl payment = new BalanceOperationImpl();
        payment.setDate(LocalDate.now());
        payment.setType(BalanceOperationImpl.Type.DEBIT);
        // TODO(@umbo) waiting for an answer about a preliminary balance check
        double price = returnTransaction.getPrice();
        payment.setMoney(price);
        balanceOperationRepository.create(payment);
        returnTransaction.setPayment(payment);
        returnTransactionRepository.update(returnTransaction);
        return price;
    }

    public double returnCreditCardPayment(Integer returnId, String creditCard) throws InvalidTransactionIdException, InvalidCreditCardException, UnauthorizedException {
        // TODO(@umbo) waiting for an answer about creditcards.txt
        return 0;
    }

    public boolean recordBalanceUpdate(double toBeAdded) throws UnauthorizedException {
        double currentBalance = computeBalance();
        if (currentBalance + toBeAdded < 0) {
            return false;
        }
        BalanceOperationImpl operation = new BalanceOperationImpl();
        operation.setDate(LocalDate.now());
        operation.setType(toBeAdded > 0 ? BalanceOperationImpl.Type.CREDIT : BalanceOperationImpl.Type.DEBIT);
        operation.setMoney(toBeAdded);
        balanceOperationRepository.create(operation);
        return true;
    }

    public List<BalanceOperation> getCreditsAndDebits(LocalDate from, LocalDate to) throws UnauthorizedException {
        LocalDate startDate = from,
                endDate = to;
        if (from != null && to != null && from.isAfter(to)) {
            startDate = to;
            endDate = from;
        }
        return balanceOperationRepository.findAllBetweenDates(startDate, endDate);
    }

    public double computeBalance() throws UnauthorizedException {
        return balanceOperationRepository.findAll()
                .stream()
                .mapToDouble(BalanceOperation::getMoney)
                .sum();
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
        return productTypeRepository.findAll().stream().anyMatch(p -> p.getLocation().equals(location));
    }
}
