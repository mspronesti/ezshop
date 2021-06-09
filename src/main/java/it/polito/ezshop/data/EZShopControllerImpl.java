package it.polito.ezshop.data;

import it.polito.ezshop.annotations.*;
import it.polito.ezshop.exceptions.*;
import it.polito.ezshop.utils.PaymentGateway;
import jakarta.validation.*;
import jakarta.validation.constraints.*;
import jakarta.validation.executable.ExecutableValidator;

import org.hibernate.exception.JDBCConnectionException;
import org.hibernate.validator.constraints.CreditCardNumber;
import org.springframework.security.crypto.bcrypt.BCrypt;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * EZShopController is a delegate for all EZShop's method implementations. It is initialized by means of a dedicated
 * factory (EZShopControllerFactory), which wraps it in a Dynamic Proxy in order to automatically validate method
 * parameters - using Hibernate Validator - and edge cases (ie database connection not working) in order to provide
 * automatic fallback methods.
 * Custom annotations and validators are used for this purpose (see the annotations and utils packages).
 */
public class EZShopControllerImpl implements EZShopController {
    private final BalanceOperationRepository balanceOperationRepository = new BalanceOperationRepository();
    private final CustomerRepository customerRepository = new CustomerRepository();
    private final LoyaltyCardRepository loyaltyCardRepository = new LoyaltyCardRepository();
    private final OrderRepository orderRepository = new OrderRepository();
    private final ProductRepository productRepository = new ProductRepository();
    private final ProductTypeRepository productTypeRepository = new ProductTypeRepository();
    private final ReturnTransactionRepository returnTransactionRepository = new ReturnTransactionRepository();
    private final SaleTransactionRepository saleTransactionRepository = new SaleTransactionRepository();
    private final UserRepository userRepository = new UserRepository();
    private User loggedUser;
    private final Map<Integer, SaleTransactionImpl> openSaleTransactions = new HashMap<>();
    private final Map<Integer, ReturnTransactionImpl> openReturnTransactions = new HashMap<>();
    private final PaymentGateway paymentGateway = new PaymentGateway();

    public void reset() {
        this.productRepository.findAll().forEach(productRepository::delete);
        this.productTypeRepository.findAll().forEach(productTypeRepository::delete);
        this.orderRepository.findAll().forEach(orderRepository::delete);
        this.returnTransactionRepository.findAll().forEach(returnTransactionRepository::delete);
        this.saleTransactionRepository.findAll().forEach(saleTransactionRepository::delete);
        this.balanceOperationRepository.findAll().forEach(balanceOperationRepository::delete);
        this.customerRepository.findAll().forEach(customerRepository::delete);
        this.loyaltyCardRepository.findAll().forEach(loyaltyCardRepository::delete);
        this.userRepository.findAll().forEach(userRepository::delete);
    }

    @Override
    @FallbackIntValue
    public Integer createUser(
            @NotNull @NotEmpty @Throw(InvalidUsernameException.class) String username,
            @NotNull @NotEmpty @Throw(InvalidPasswordException.class) String password,
            @NotNull @NotEmpty @Pattern(regexp = Role.PATTERN) @Throw(InvalidRoleException.class) String role
    ) throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException {
        if (userRepository.findByUsername(username) != null) {
            // user with given username already exists
            return -1;
        }

        User user = new UserImpl();
        user.setUsername(username);
        user.setPassword(BCrypt.hashpw(password, BCrypt.gensalt()));
        user.setRole(role);
        return userRepository.create(user);
    }

    @Override
    @AcceptRoles({Role.Administrator})
    @FallbackBooleanValue
    public boolean deleteUser(
            @NotNull @Min(1) @Throw(InvalidUserIdException.class) Integer id
    ) throws InvalidUserIdException, UnauthorizedException {
        User user = userRepository.find(id);
        if (user != null) {
            userRepository.delete(user);
            return true;
        }
        return false;
    }

    @Override
    @AcceptRoles({Role.Administrator})
    public List<User> getAllUsers() throws UnauthorizedException {
        return userRepository.findAll();
    }

    @Override
    @AcceptRoles({Role.Administrator})
    public User getUser(
            @NotNull @Min(1) @Throw(InvalidUserIdException.class) Integer id
    ) throws InvalidUserIdException, UnauthorizedException {
        return userRepository.find(id);
    }

    @Override
    @AcceptRoles({Role.Administrator})
    public boolean updateUserRights(
            @NotNull @Min(1) @Throw(InvalidUserIdException.class) Integer id,
            @NotNull @NotEmpty @Pattern(regexp = Role.PATTERN) @Throw(InvalidRoleException.class) String role
    ) throws InvalidUserIdException, InvalidRoleException, UnauthorizedException {
        User user = userRepository.find(id);
        if (user != null) {
            user.setRole(role);
            userRepository.update(user);
            return true;
        }
        return false;
    }

    @Override
    public User login(
            @NotNull @NotEmpty @Throw(InvalidUsernameException.class) String username,
            @NotNull @NotEmpty @Throw(InvalidPasswordException.class) String password
    ) throws InvalidUsernameException, InvalidPasswordException {
        User user = userRepository.findByUsername(username);
        if (user != null && BCrypt.checkpw(password, user.getPassword())) {
            loggedUser = user;
            return user;
        }
        return null;
    }

    @Override
    @FallbackBooleanValue
    public boolean logout() {
        if (loggedUser != null) {
            loggedUser = null;
            return true;
        }
        return false;
    }

    @Override
    @AcceptRoles({Role.Administrator, Role.ShopManager})
    @FallbackIntValue
    public Integer createProductType(
            @NotNull @NotEmpty @Throw(InvalidProductDescriptionException.class) String description,
            @NotNull @NotEmpty @GtinBarcode @Throw(InvalidProductCodeException.class) String productCode,
            @NotNull @Positive @Throw(InvalidPricePerUnitException.class) double pricePerUnit,
            String note
    ) throws InvalidProductDescriptionException, InvalidProductCodeException, InvalidPricePerUnitException, UnauthorizedException {
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

    @Override
    @AcceptRoles({Role.Administrator, Role.ShopManager})
    @FallbackBooleanValue
    public boolean updateProduct(
            @NotNull @Min(1) @Throw(InvalidProductIdException.class) Integer id,
            @NotNull @NotEmpty @Throw(InvalidProductDescriptionException.class) String newDescription,
            @NotNull @NotEmpty @GtinBarcode @Throw(InvalidProductCodeException.class) String newCode,
            @NotNull @Positive @Throw(InvalidPricePerUnitException.class) double newPrice,
            String newNote
    ) throws InvalidProductIdException, InvalidProductDescriptionException, InvalidProductCodeException, InvalidPricePerUnitException, UnauthorizedException {
        ProductType product = productTypeRepository.find(id);
        if (product != null) {
            try {
                product.setProductDescription(newDescription);
                product.setBarCode(newCode);
                product.setPricePerUnit(newPrice);
                product.setNote(newNote);
                productTypeRepository.update(product);
                return true;
            } catch (Exception exception) {
                return false;
            }
        }
        return false;
    }

    @Override
    @AcceptRoles({Role.Administrator, Role.ShopManager})
    @FallbackBooleanValue
    public boolean deleteProductType(
            @NotNull @Min(1) @Throw(InvalidProductIdException.class) Integer id
    ) throws InvalidProductIdException, UnauthorizedException {
        ProductType productType = productTypeRepository.find(id);
        if (productType != null) {
            productTypeRepository.delete(productType);
            return true;
        }
        return false;
    }

    @Override
    @AcceptRoles({Role.Administrator, Role.ShopManager, Role.Cashier})
    public List<ProductType> getAllProductTypes() throws UnauthorizedException {
        return productTypeRepository.findAll();
    }

    @Override
    @AcceptRoles({Role.Administrator, Role.ShopManager})
    public ProductType getProductTypeByBarCode(
            @NotNull @NotEmpty @GtinBarcode @Throw(InvalidProductCodeException.class) String barCode
    ) throws InvalidProductCodeException, UnauthorizedException {
        return productTypeRepository.findByBarcode(barCode);
    }

    @Override
    @AcceptRoles({Role.Administrator, Role.ShopManager})
    public List<ProductType> getProductTypesByDescription(String description) throws UnauthorizedException {
        return productTypeRepository.findAll().stream()
                .filter(t -> t.getProductDescription().contains(description == null ? "" : description))
                .collect(Collectors.toList());
    }

    @Override
    @AcceptRoles({Role.Administrator, Role.ShopManager})
    @FallbackBooleanValue
    public boolean updateQuantity(
            @NotNull @Min(1) @Throw(InvalidProductIdException.class) Integer productId,
            int toBeAdded
    ) throws InvalidProductIdException, UnauthorizedException {
        ProductType product = this.productTypeRepository.find(productId);
        if (product != null) {
            int newQuantity = product.getQuantity() + toBeAdded;
            if (newQuantity >= 0 && !product.getLocation().isEmpty()) {
                product.setQuantity(newQuantity);
                productTypeRepository.update(product);
                return true;
            }
        }
        return false;
    }

    @Override
    @AcceptRoles({Role.Administrator, Role.ShopManager})
    @FallbackBooleanValue
    public boolean updatePosition(
            @NotNull @Min(1) @Throw(InvalidProductIdException.class) Integer productId,
            @Location @Throw(InvalidLocationException.class) String newPos
    ) throws InvalidProductIdException, InvalidLocationException, UnauthorizedException {
        ProductType product = this.productTypeRepository.find(productId);
        if (product != null && !isAssignedPosition(newPos)) {
            // resets or sets position
            product.setLocation(newPos == null || newPos.isEmpty() ? "" : newPos);
            productTypeRepository.update(product);
            return true;
        }
        return false;
    }

    @Override
    @AcceptRoles({Role.Administrator, Role.ShopManager})
    @FallbackIntValue
    public Integer issueOrder(
            @NotNull @NotEmpty @GtinBarcode @Throw(InvalidProductCodeException.class) String productCode,
            @NotNull @Min(1) @Throw(InvalidQuantityException.class) int quantity,
            @NotNull @Positive @Throw(InvalidPricePerUnitException.class) double pricePerUnit
    ) throws InvalidProductCodeException, InvalidQuantityException, InvalidPricePerUnitException, UnauthorizedException {
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

    @Override
    @AcceptRoles({Role.Administrator, Role.ShopManager})
    @FallbackIntValue
    public Integer payOrderFor(
            @NotNull @NotEmpty @GtinBarcode @Throw(InvalidProductCodeException.class) String productCode,
            @NotNull @Min(1) @Throw(InvalidQuantityException.class) int quantity,
            @NotNull @Positive @Throw(InvalidPricePerUnitException.class) double pricePerUnit
    ) throws InvalidProductCodeException, InvalidQuantityException, InvalidPricePerUnitException, UnauthorizedException {
        ProductType product = this.productTypeRepository.findByBarcode(productCode);
        if (product == null) {
            // can't order an unexisting product 
            return -1;
        }

        Integer orderId = issueOrder(productCode, quantity, pricePerUnit);
        try {
            if (!payOrder(orderId)) {
                return -1;
            }
        } catch (Exception e) {
            return -1;
        }

        return orderId;
    }

    @Override
    @AcceptRoles({Role.Administrator, Role.ShopManager})
    @FallbackBooleanValue
    public boolean payOrder(
            @NotNull @Min(1) @Throw(InvalidOrderIdException.class) Integer orderId
    ) throws InvalidOrderIdException, UnauthorizedException {
        Order order = orderRepository.find(orderId);
        if (order != null) {
            String status = order.getStatus();
            if (status.equals(OrderImpl.Status.PAYED.name()))
                return true;

            if (status.equals(OrderImpl.Status.ISSUED.name())) {
                double currentBalance = computeBalance();
                double toBeAdded = -1 * order.getPricePerUnit() * order.getQuantity();
                if (currentBalance + toBeAdded < 0) {
                    return false;
                }

                BalanceOperationImpl operation = new BalanceOperationImpl();
                operation.setDate(LocalDate.now());
                operation.setType(toBeAdded > 0 ? BalanceOperationImpl.Type.CREDIT : BalanceOperationImpl.Type.DEBIT);
                operation.setMoney(toBeAdded);
                balanceOperationRepository.create(operation);

                // set payed
                order.setStatus(OrderImpl.Status.PAYED.name());
                order.setBalanceId(operation.getBalanceId());
                orderRepository.update(order);
                return true;
            }
        }
        return false;
    }

    @Override
    @AcceptRoles({Role.Administrator, Role.ShopManager})
    @FallbackBooleanValue
    public boolean recordOrderArrival(
            @NotNull @Min(1) @Throw(InvalidOrderIdException.class) Integer orderId
    ) throws InvalidOrderIdException, UnauthorizedException, InvalidLocationException {
        Order order = orderRepository.find(orderId);
        if (order != null) {
            String status = order.getStatus();
            ProductType product = productTypeRepository.findByBarcode(order.getProductCode());

            if (product.getLocation().isEmpty())
                throw new InvalidLocationException();

            if (status.equals(OrderImpl.Status.COMPLETED.name()))
                return true;

            if (status.equals(OrderImpl.Status.PAYED.name())) {
                // set COMPLETED
                order.setStatus(OrderImpl.Status.COMPLETED.name());
                orderRepository.update(order);

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

    @Override
    @AcceptRoles({Role.Administrator, Role.ShopManager})
    @FallbackBooleanValue
    public boolean recordOrderArrivalRFID(
            @NotNull @Min(1) @Throw(InvalidOrderIdException.class) Integer orderId,
            @NotNull @Pattern(regexp = ProductImpl.RFIDPATTERN) @Throw(InvalidRFIDException.class) String RFIDfrom
    ) throws InvalidOrderIdException, UnauthorizedException, InvalidLocationException, InvalidRFIDException {
        Order order = orderRepository.find(orderId);
        if (order != null) {
            String status = order.getStatus();
            int orderQuantity = order.getQuantity();

            ProductType productType = productTypeRepository.findByBarcode(order.getProductCode());


            if (productType.getLocation().isEmpty())
                throw new InvalidLocationException();

            if (status.equals(OrderImpl.Status.COMPLETED.name()))
                return true;

            if (status.equals(OrderImpl.Status.PAYED.name())) {
                // set COMPLETED
                order.setStatus(OrderImpl.Status.COMPLETED.name());
                orderRepository.update(order);

                // associating RFIDs
                for (int i = 0; i < orderQuantity; ++i) {
                    Product product = new ProductImpl();
                    // increment RFID and left pad with 0s
                    product.setId(String.format("%012d", Integer.parseInt(RFIDfrom) + i));
                    product.setProductType((ProductTypeImpl) productType);
                    productRepository.create(product);
                }

                if (productType != null) {
                    // update quantity
                    productType.setQuantity(productType.getQuantity() + orderQuantity);
                    productTypeRepository.update(productType);
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    @AcceptRoles({Role.Administrator, Role.ShopManager})
    public List<Order> getAllOrders() throws UnauthorizedException {
        return orderRepository.findAll();
    }

    @Override
    @AcceptRoles({Role.Administrator, Role.ShopManager, Role.Cashier})
    @FallbackIntValue
    public Integer defineCustomer(
            @NotNull @NotEmpty @Throw(InvalidCustomerNameException.class) String customerName
    ) throws InvalidCustomerNameException, UnauthorizedException {
        if (customerRepository.findByName(customerName) != null) {
            // customer with given name already exists
            return -1;
        }
        Customer customer = new CustomerImpl();
        customer.setCustomerName(customerName);
        return customerRepository.create(customer);
    }

    @Override
    @AcceptRoles({Role.Administrator, Role.ShopManager, Role.Cashier})
    @FallbackBooleanValue
    public boolean modifyCustomer(
            @NotNull @Min(1) @Throw(InvalidCustomerIdException.class) Integer id,
            @NotNull @NotEmpty @Throw(InvalidCustomerNameException.class) String newCustomerName,
            @Pattern(regexp = LoyaltyCardImpl.PATTERN) @Throw(InvalidCustomerCardException.class) String newCustomerCard
    ) throws InvalidCustomerNameException, InvalidCustomerCardException, InvalidCustomerIdException, UnauthorizedException {
        Customer customer = customerRepository.find(id);
        if (customer != null) {
            if (newCustomerCard != null) {
                customer.setCustomerCard(newCustomerCard);
            }
            customer.setCustomerName(newCustomerName);
            customerRepository.update(customer);
            return true;
        }
        return false;
    }

    @Override
    @AcceptRoles({Role.Administrator, Role.ShopManager, Role.Cashier})
    @FallbackBooleanValue
    public boolean deleteCustomer(
            @NotNull @Min(1) @Throw(InvalidCustomerIdException.class) Integer id
    ) throws InvalidCustomerIdException, UnauthorizedException {
        Customer customer = customerRepository.find(id);
        if (customer != null) {
            customerRepository.delete(customer);
            return true;
        }
        return false;
    }

    @Override
    @AcceptRoles({Role.Administrator, Role.ShopManager, Role.Cashier})
    public Customer getCustomer(
            @NotNull @Min(1) @Throw(InvalidCustomerIdException.class) Integer id
    ) throws InvalidCustomerIdException, UnauthorizedException {
        return customerRepository.find(id);
    }

    @Override
    @AcceptRoles({Role.Administrator, Role.ShopManager, Role.Cashier})
    public List<Customer> getAllCustomers() throws UnauthorizedException {
        return this.customerRepository.findAll();
    }

    @Override
    @AcceptRoles({Role.Administrator, Role.ShopManager, Role.Cashier})
    @FallbackStringValue
    public String createCard() throws UnauthorizedException {
        LoyaltyCard loyaltyCard = new LoyaltyCardImpl();
        return this.loyaltyCardRepository.create(loyaltyCard);
    }

    @Override
    @AcceptRoles({Role.Administrator, Role.ShopManager, Role.Cashier})
    @FallbackBooleanValue
    public boolean attachCardToCustomer(
            @NotNull @NotEmpty @Pattern(regexp = LoyaltyCardImpl.PATTERN) @Throw(InvalidCustomerCardException.class) String customerCard,
            @NotNull @Min(1) @Throw(InvalidCustomerIdException.class) Integer customerId
    ) throws InvalidCustomerIdException, InvalidCustomerCardException, UnauthorizedException {
        Customer customer = this.customerRepository.find(customerId);
        LoyaltyCard loyaltyCard = this.loyaltyCardRepository.find(customerCard);
        if (customer != null && (loyaltyCard == null || loyaltyCard.getCustomer() == null)) {
            customer.setCustomerCard(customerCard);
            this.customerRepository.update(customer);
            return true;
        }
        return false;
    }

    @Override
    @AcceptRoles({Role.Administrator, Role.ShopManager, Role.Cashier})
    @FallbackBooleanValue
    public boolean modifyPointsOnCard(
            @NotNull @NotEmpty @Pattern(regexp = LoyaltyCardImpl.PATTERN) @Throw(InvalidCustomerCardException.class) String customerCard,
            int pointsToBeAdded
    ) throws InvalidCustomerCardException, UnauthorizedException {
        LoyaltyCard loyaltyCard = this.loyaltyCardRepository.find(customerCard);
        if (loyaltyCard != null) {
            int pointsToUpdate = loyaltyCard.getPoints() + pointsToBeAdded;
            if (pointsToUpdate < 0)
                return false;

            loyaltyCard.setPoints(pointsToUpdate);
            this.loyaltyCardRepository.update(loyaltyCard);
            return true;
        }
        return false;
    }

    @Override
    @AcceptRoles({Role.Administrator, Role.ShopManager, Role.Cashier})
    public Integer startSaleTransaction() throws UnauthorizedException {
        SaleTransactionImpl saleTransaction = new SaleTransactionImpl();
        saleTransaction.setPrice(0.0d);
        Integer id = saleTransactionRepository.create(saleTransaction);
        openSaleTransactions.put(id, saleTransaction);
        return id;
    }

    @Override
    @AcceptRoles({Role.Administrator, Role.ShopManager, Role.Cashier})
    @FallbackBooleanValue
    public boolean addProductToSale(
            @NotNull @Min(1) @Throw(InvalidTransactionIdException.class) Integer transactionId,
            @NotNull @NotEmpty @GtinBarcode @Throw(InvalidProductCodeException.class) String productCode,
            @NotNull @Min(0) @Throw(InvalidQuantityException.class) int amount
    ) throws InvalidTransactionIdException, InvalidProductCodeException, InvalidQuantityException, UnauthorizedException {
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
            entry.setProductDescription(product.getProductDescription());
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

    @Override
    @AcceptRoles({Role.Administrator, Role.ShopManager, Role.Cashier})
    @FallbackBooleanValue
    public boolean addProductToSaleRFID(
            @NotNull @Min(1) @Throw(InvalidTransactionIdException.class) Integer transactionId,
            @NotNull @Pattern(regexp = ProductImpl.RFIDPATTERN) @Throw(InvalidRFIDException.class) String RFID
    ) throws InvalidTransactionIdException, InvalidRFIDException, InvalidQuantityException, UnauthorizedException {
        ProductImpl product = (ProductImpl) productRepository.find(RFID);
        if (product == null || product.getStatus() != ProductImpl.Status.AVAILABLE) {
            return false;
        }
        SaleTransactionImpl saleTransaction = openSaleTransactions.get(transactionId);
        if (saleTransaction == null) {
            return false;
        }
        product.setStatus(ProductImpl.Status.SELLING);
        productRepository.update(product);
        saleTransaction.getProducts().add(product);
        try {
            return addProductToSale(transactionId, product.getProductType().getBarCode(), 1);
        } catch (InvalidProductCodeException e) {
            return false;
        }
    }

    @Override
    @AcceptRoles({Role.Administrator, Role.ShopManager, Role.Cashier})
    @FallbackBooleanValue
    public boolean deleteProductFromSale(
            @NotNull @Min(1) @Throw(InvalidTransactionIdException.class) Integer transactionId,
            @NotNull @NotEmpty @GtinBarcode @Throw(InvalidProductCodeException.class) String productCode,
            @NotNull @Min(0) @Throw(InvalidQuantityException.class) int amount
    ) throws InvalidTransactionIdException, InvalidProductCodeException, InvalidQuantityException, UnauthorizedException {
        SaleTransactionImpl saleTransaction = openSaleTransactions.get(transactionId);
        if (saleTransaction == null) {
            return false;
        }
        TicketEntry entry = saleTransaction.getEntryByBarcode(productCode);
        ProductType product = productTypeRepository.findByBarcode(productCode);
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

    @Override
    @AcceptRoles({Role.Administrator, Role.ShopManager, Role.Cashier})
    @FallbackBooleanValue
    public boolean deleteProductFromSaleRFID(
            @NotNull @Min(1) @Throw(InvalidTransactionIdException.class) Integer transactionId,
            @NotNull @Pattern(regexp = ProductImpl.RFIDPATTERN) @Throw(InvalidRFIDException.class) String RFID
    ) throws InvalidTransactionIdException,
            InvalidRFIDException, InvalidQuantityException, UnauthorizedException {
        Product product = productRepository.find(RFID);
        if (product == null || product.getStatus() != ProductImpl.Status.SELLING) {
            return false;
        }
        SaleTransactionImpl saleTransaction = openSaleTransactions.get(transactionId);
        if (saleTransaction == null) {
            return false;
        }
        saleTransaction.getProducts().removeIf(p -> {
            if (p.getId().equals(product.getId())) {
                p.setStatus(ProductImpl.Status.AVAILABLE);
                productRepository.update(p);
                return true;
            }
            return false;
        });
        try {
            return deleteProductFromSale(transactionId, product.getProductType().getBarCode(), 1);
        } catch (InvalidProductCodeException e) {
            return false;
        }
    }

    @Override
    @AcceptRoles({Role.Administrator, Role.ShopManager, Role.Cashier})
    @FallbackBooleanValue
    public boolean applyDiscountRateToProduct(
            @NotNull @Min(1) @Throw(InvalidTransactionIdException.class) Integer transactionId,
            @NotNull @NotEmpty @GtinBarcode @Throw(InvalidProductCodeException.class) String productCode,
            @NotNull @DiscountRate @Throw(InvalidDiscountRateException.class) double discountRate
    ) throws InvalidTransactionIdException, InvalidProductCodeException, InvalidDiscountRateException, UnauthorizedException {
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
        return true;
    }

    @Override
    @AcceptRoles({Role.Administrator, Role.ShopManager, Role.Cashier})
    @FallbackBooleanValue
    public boolean applyDiscountRateToSale(
            @NotNull @Min(1) @Throw(InvalidTransactionIdException.class) Integer transactionId,
            @NotNull @DiscountRate @Throw(InvalidDiscountRateException.class) double discountRate
    ) throws InvalidTransactionIdException, InvalidDiscountRateException, UnauthorizedException {
        SaleTransactionImpl saleTransaction = openSaleTransactions.get(transactionId);
        if (saleTransaction == null) {
            return false;
        }
        saleTransaction.setDiscountRate(discountRate);
        saleTransaction.updatePrice();
        return true;
    }

    @Override
    @AcceptRoles({Role.Administrator, Role.ShopManager, Role.Cashier})
    @FallbackIntValue
    public int computePointsForSale(
            @NotNull @Min(1) @Throw(InvalidTransactionIdException.class) Integer transactionId
    ) throws InvalidTransactionIdException, UnauthorizedException {
        SaleTransactionImpl saleTransaction = openSaleTransactions.get(transactionId);
        if (saleTransaction == null) {
            saleTransaction = (SaleTransactionImpl) saleTransactionRepository.find(transactionId);
            if (saleTransaction == null) {
                return -1;
            }
        }
        return (int) Math.floor(saleTransaction.getPrice() / 10);
    }

    @Override
    @AcceptRoles({Role.Administrator, Role.ShopManager, Role.Cashier})
    @FallbackBooleanValue
    public boolean endSaleTransaction(
            @NotNull @Min(1) @Throw(InvalidTransactionIdException.class) Integer transactionId
    ) throws InvalidTransactionIdException, UnauthorizedException {
        SaleTransactionImpl saleTransaction = openSaleTransactions.get(transactionId);
        if (saleTransaction == null) {
            return false;
        }
        saleTransaction.getProducts().forEach(p -> {
            p.setStatus(ProductImpl.Status.SOLD);
            productRepository.update(p);
        });
        openSaleTransactions.remove(transactionId);
        saleTransactionRepository.update(saleTransaction);
        return true;
    }

    @Override
    @AcceptRoles({Role.Administrator, Role.ShopManager, Role.Cashier})
    @FallbackBooleanValue
    public boolean deleteSaleTransaction(
            @NotNull @Min(1) @Throw(InvalidTransactionIdException.class) Integer transactionId
    ) throws InvalidTransactionIdException, UnauthorizedException {
        SaleTransactionImpl saleTransaction = (SaleTransactionImpl) saleTransactionRepository.find(transactionId);
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
        saleTransaction.getProducts().forEach(p -> {
            p.setStatus(ProductImpl.Status.AVAILABLE);
            productRepository.update(p);
        });
        openSaleTransactions.remove(transactionId);
        saleTransactionRepository.delete(saleTransaction);
        return true;
    }

    @Override
    @AcceptRoles({Role.Administrator, Role.ShopManager, Role.Cashier})
    public SaleTransaction getSaleTransaction(
            @NotNull @Min(1) @Throw(InvalidTransactionIdException.class) Integer transactionId
    ) throws InvalidTransactionIdException, UnauthorizedException {
        return openSaleTransactions.containsKey(transactionId) ? null : saleTransactionRepository.find(transactionId);
    }

    @Override
    @AcceptRoles({Role.Administrator, Role.ShopManager, Role.Cashier})
    @FallbackIntValue
    public Integer startReturnTransaction(
            @NotNull @Min(1) @Throw(InvalidTransactionIdException.class) Integer transactionId
    ) throws InvalidTransactionIdException, UnauthorizedException {
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

    @Override
    @AcceptRoles({Role.Administrator, Role.ShopManager, Role.Cashier})
    @FallbackBooleanValue
    public boolean returnProduct(
            @NotNull @Min(1) @Throw(InvalidTransactionIdException.class) Integer returnId,
            @NotNull @NotEmpty @GtinBarcode @Throw(InvalidProductCodeException.class) String productCode,
            @NotNull @Min(1) @Throw(InvalidQuantityException.class) int amount
    ) throws InvalidTransactionIdException, InvalidProductCodeException, InvalidQuantityException, UnauthorizedException {
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

    @Override
    @AcceptRoles({Role.Administrator, Role.ShopManager, Role.Cashier})
    @FallbackBooleanValue
    public boolean returnProductRFID(
            @NotNull @Min(1) @Throw(InvalidTransactionIdException.class) Integer returnId,
            @NotNull @Pattern(regexp = ProductImpl.RFIDPATTERN) @Throw(InvalidRFIDException.class) String RFID
    ) throws InvalidTransactionIdException, InvalidRFIDException, UnauthorizedException {
        ProductImpl product = (ProductImpl) productRepository.find(RFID);
        if (product == null || product.getStatus() != ProductImpl.Status.SOLD) {
            return false;
        }
        ReturnTransactionImpl returnTransaction = openReturnTransactions.get(returnId);
        if (returnTransaction == null) {
            return false;
        }
        returnTransaction.getProducts().add(product);
        try {
            return returnProduct(returnId, product.getProductType().getBarCode(), 1);
        } catch (InvalidProductCodeException | InvalidQuantityException e) {
            return false;
        }
    }

    @Override
    @AcceptRoles({Role.Administrator, Role.ShopManager, Role.Cashier})
    @FallbackBooleanValue
    public boolean endReturnTransaction(
            @NotNull @Min(1) @Throw(InvalidTransactionIdException.class) Integer returnId,
            boolean commit
    ) throws InvalidTransactionIdException, UnauthorizedException {
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
        returnTransaction.getProducts().forEach(p -> {
            p.setStatus(ProductImpl.Status.AVAILABLE);
            productRepository.update(p);
        });
        saleTransaction.updatePrice();
        saleTransactionRepository.update(saleTransaction);
        returnTransactionRepository.update(returnTransaction);
        return true;
    }

    @Override
    @AcceptRoles({Role.Administrator, Role.ShopManager, Role.Cashier})
    @FallbackBooleanValue
    public boolean deleteReturnTransaction(
            @NotNull @Min(1) @Throw(InvalidTransactionIdException.class) Integer returnId
    ) throws InvalidTransactionIdException, UnauthorizedException {
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
        returnTransaction.getProducts().forEach(p -> {
            p.setStatus(ProductImpl.Status.SOLD);
            productRepository.update(p);
        });
        saleTransaction.updatePrice();
        saleTransactionRepository.update(saleTransaction);
        returnTransactionRepository.delete(returnTransaction);
        return true;
    }

    @Override
    @AcceptRoles({Role.Administrator, Role.ShopManager, Role.Cashier})
    @FallbackIntValue
    public double receiveCashPayment(
            @NotNull @Min(1) @Throw(InvalidTransactionIdException.class) Integer transactionId,
            @NotNull @Positive @Throw(InvalidPaymentException.class) double cash
    ) throws InvalidTransactionIdException, InvalidPaymentException, UnauthorizedException {
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

    @Override
    @AcceptRoles({Role.Administrator, Role.ShopManager, Role.Cashier})
    @FallbackBooleanValue
    public boolean receiveCreditCardPayment(
            @NotNull @Min(1) @Throw(InvalidTransactionIdException.class) Integer transactionId,
            @NotNull @NotEmpty @CreditCardNumber @Throw(InvalidCreditCardException.class) String creditCard
    ) throws InvalidTransactionIdException, InvalidCreditCardException, UnauthorizedException {
        SaleTransactionImpl saleTransaction = (SaleTransactionImpl) saleTransactionRepository.find(transactionId);
        if (saleTransaction == null) {
            return false;
        }
        double price = saleTransaction.getPrice();
        boolean b = paymentGateway.requestPayment(creditCard, price);
        if (b) {
            BalanceOperationImpl operation = new BalanceOperationImpl();
            operation.setDate(LocalDate.now());
            operation.setType(BalanceOperationImpl.Type.CREDIT);
            operation.setMoney(price);
            balanceOperationRepository.create(operation);
            return true;
        }
        return false;
    }

    @Override
    @AcceptRoles({Role.Administrator, Role.ShopManager, Role.Cashier})
    @FallbackIntValue
    public double returnCashPayment(
            @NotNull @Min(1) @Throw(InvalidTransactionIdException.class) Integer returnId
    ) throws InvalidTransactionIdException, UnauthorizedException {
        ReturnTransactionImpl returnTransaction = returnTransactionRepository.find(returnId);
        if (returnTransaction == null || returnTransaction.getPayment() != null) {
            return -1;
        }
        BalanceOperationImpl payment = new BalanceOperationImpl();
        payment.setDate(LocalDate.now());
        payment.setType(BalanceOperationImpl.Type.DEBIT);
        double price = returnTransaction.getPrice();
        payment.setMoney(price);
        balanceOperationRepository.create(payment);
        returnTransaction.setPayment(payment);
        returnTransactionRepository.update(returnTransaction);
        return price;
    }

    @Override
    @AcceptRoles({Role.Administrator, Role.ShopManager, Role.Cashier})
    @FallbackBooleanValue
    public double returnCreditCardPayment(
            @NotNull @Min(1) @Throw(InvalidTransactionIdException.class) Integer returnId,
            @NotNull @NotEmpty @CreditCardNumber @Throw(InvalidCreditCardException.class) String creditCard
    ) throws InvalidTransactionIdException, InvalidCreditCardException, UnauthorizedException {
        ReturnTransactionImpl returnTransaction = returnTransactionRepository.find(returnId);
        if (returnTransaction == null) {
            return -1;
        }
        double price = returnTransaction.getPrice();
        if (paymentGateway.requestAccreditation(creditCard, price)) {
            BalanceOperationImpl operation = new BalanceOperationImpl();
            operation.setDate(LocalDate.now());
            operation.setType(BalanceOperationImpl.Type.DEBIT);
            operation.setMoney(price);
            balanceOperationRepository.create(operation);
            return price;
        }
        return -1;
    }

    @Override
    @AcceptRoles({Role.Administrator, Role.ShopManager})
    @FallbackBooleanValue
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

    @Override
    @AcceptRoles({Role.Administrator, Role.ShopManager})
    public List<BalanceOperation> getCreditsAndDebits(LocalDate from, LocalDate to) throws UnauthorizedException {
        LocalDate startDate = from,
                endDate = to;
        if (from != null && to != null && from.isAfter(to)) {
            startDate = to;
            endDate = from;
        }
        return balanceOperationRepository.findAllBetweenDates(startDate, endDate);
    }

    @Override
    @AcceptRoles({Role.Administrator, Role.ShopManager})
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

                try {
                    return method.invoke(target, args);
                } catch (InvocationTargetException e) {
                    throw e.getCause();
                }

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
