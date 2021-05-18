package it.polito.ezshop.data;

import it.polito.ezshop.annotations.*;
import it.polito.ezshop.exceptions.*;
import jakarta.validation.constraints.*;

import java.time.LocalDate;
import java.util.List;

public interface EZShopController {
    @FallbackIntValue
    public Integer createUser(
            @NotNull @NotEmpty @Throw(InvalidUsernameException.class) String username,
            @NotNull @NotEmpty @Throw(InvalidPasswordException.class) String password,
            @NotNull @NotEmpty @Pattern(regexp = Role.pattern) @Throw(InvalidRoleException.class) String role
    ) throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException;

    @AcceptRoles({ Role.Administrator })
    @FallbackBooleanValue
    public boolean deleteUser(
            @NotNull @Min(1) @Throw(InvalidUserIdException.class) Integer id
    ) throws InvalidUserIdException, UnauthorizedException;

    @AcceptRoles({ Role.Administrator })
    public List<User> getAllUsers() throws UnauthorizedException;

    @AcceptRoles({ Role.Administrator })
    public User getUser(
            @NotNull @Min(1) @Throw(InvalidUserIdException.class) Integer id
    ) throws InvalidUserIdException, UnauthorizedException;

    @AcceptRoles({ Role.Administrator })
    public boolean updateUserRights(
            @NotNull @Min(1) @Throw(InvalidUserIdException.class) Integer id,
            @NotNull @NotEmpty @Pattern(regexp = Role.pattern) @Throw(InvalidRoleException.class) String role
    ) throws InvalidUserIdException, InvalidRoleException, UnauthorizedException;

    public User login(
            @NotNull @NotEmpty @Throw(InvalidUsernameException.class) String username,
            @NotNull @NotEmpty @Throw(InvalidPasswordException.class) String password
    ) throws InvalidUsernameException, InvalidPasswordException;

    @FallbackBooleanValue
    public boolean logout();

    @AcceptRoles({ Role.Administrator, Role.ShopManager })
    @FallbackIntValue
    public Integer createProductType(
            @NotNull @NotEmpty @Throw(InvalidProductDescriptionException.class) String description,
            @NotNull @NotEmpty @GtinBarcode @Throw(InvalidProductCodeException.class) String productCode,
            @NotNull @Positive double pricePerUnit,
            String note
    ) throws InvalidProductDescriptionException, InvalidProductCodeException, InvalidPricePerUnitException, UnauthorizedException;

    @AcceptRoles({ Role.Administrator, Role.ShopManager })
    @FallbackBooleanValue
    public boolean updateProduct(
            @NotNull @Min(1) @Throw(InvalidProductIdException.class) Integer id,
            @NotNull @NotEmpty @Throw(InvalidProductDescriptionException.class) String newDescription,
            @NotNull @NotEmpty @GtinBarcode @Throw(InvalidProductCodeException.class) String newCode,
            @NotNull @Positive @Throw(InvalidPricePerUnitException.class) double newPrice,
            String newNote
    ) throws InvalidProductIdException, InvalidProductDescriptionException, InvalidProductCodeException, InvalidPricePerUnitException, UnauthorizedException;

    @AcceptRoles({ Role.Administrator, Role.ShopManager })
    @FallbackBooleanValue
    public boolean deleteProductType(
            @NotNull @Min(0) @Throw(InvalidProductIdException.class) Integer id
    ) throws InvalidProductIdException, UnauthorizedException;

    @AcceptRoles({ Role.Administrator, Role.ShopManager, Role.Cashier })
    public List<ProductType> getAllProductTypes() throws UnauthorizedException;

    @AcceptRoles({ Role.Administrator, Role.ShopManager })
    public ProductType getProductTypeByBarCode(
            @NotNull @NotEmpty @GtinBarcode @Throw(InvalidProductCodeException.class) String barCode
    ) throws InvalidProductCodeException, UnauthorizedException;

    @AcceptRoles({ Role.Administrator, Role.ShopManager })
    public List<ProductType> getProductTypesByDescription(String description) throws UnauthorizedException;

    @AcceptRoles({ Role.Administrator, Role.ShopManager })
    @FallbackBooleanValue
    public boolean updateQuantity(
            @NotNull @Min(1) @Throw(InvalidProductIdException.class) Integer productId,
            int toBeAdded
    ) throws InvalidProductIdException, UnauthorizedException;

    @AcceptRoles({ Role.Administrator, Role.ShopManager })
    @FallbackBooleanValue
    public boolean updatePosition(
            @NotNull @Min(1) @Throw(InvalidProductIdException.class) Integer productId,
            String newPos
    ) throws InvalidProductIdException, InvalidLocationException, UnauthorizedException;

    @AcceptRoles({ Role.Administrator, Role.ShopManager })
    @FallbackIntValue
    public Integer issueOrder(
            @NotNull @NotEmpty @GtinBarcode @Throw(InvalidProductCodeException.class) String productCode,
            @NotNull @Min(1) @Throw(InvalidQuantityException.class) int quantity,
            @NotNull @Positive @Throw(InvalidPricePerUnitException.class) double pricePerUnit
    ) throws InvalidProductCodeException, InvalidQuantityException, InvalidPricePerUnitException, UnauthorizedException;

    @AcceptRoles({ Role.Administrator, Role.ShopManager })
    @FallbackIntValue
    public Integer payOrderFor(
            @NotNull @NotEmpty @GtinBarcode @Throw(InvalidProductCodeException.class) String productCode,
            @NotNull @Min(1) @Throw(InvalidQuantityException.class) int quantity,
            @NotNull @Positive @Throw(InvalidPricePerUnitException.class) double pricePerUnit
    ) throws InvalidProductCodeException, InvalidQuantityException, InvalidPricePerUnitException, UnauthorizedException;

    @AcceptRoles({ Role.Administrator, Role.ShopManager })
    @FallbackBooleanValue
    public boolean payOrder(
            @NotNull @Min(1) @Throw(InvalidOrderIdException.class) Integer orderId
    ) throws InvalidOrderIdException, UnauthorizedException;

    @AcceptRoles({ Role.Administrator, Role.ShopManager })
    @FallbackBooleanValue
    public boolean recordOrderArrival(
            @NotNull @Min(1) @Throw(InvalidOrderIdException.class) Integer orderId
    ) throws InvalidOrderIdException, UnauthorizedException, InvalidLocationException;

    @AcceptRoles({ Role.Administrator, Role.ShopManager })
    public List<Order> getAllOrders() throws UnauthorizedException;

    @AcceptRoles({ Role.Administrator, Role.ShopManager, Role.Cashier })
    @FallbackIntValue
    public Integer defineCustomer(
            @NotNull @NotEmpty @Throw(InvalidCustomerNameException.class) String customerName
    ) throws InvalidCustomerNameException, UnauthorizedException;

    @AcceptRoles({ Role.Administrator, Role.ShopManager, Role.Cashier })
    @FallbackBooleanValue
    public boolean modifyCustomer(
            @NotNull @Min(1) @Throw(InvalidCustomerIdException.class) Integer id, // TODO(@umbo) missing in interface
            @NotNull @NotEmpty @Throw(InvalidCustomerNameException.class) String newCustomerName,
            @NotNull @NotEmpty @Pattern(regexp = "^\\d{10}$") String newCustomerCard
    ) throws InvalidCustomerNameException, InvalidCustomerCardException, InvalidCustomerIdException, UnauthorizedException;

    @AcceptRoles({ Role.Administrator, Role.ShopManager, Role.Cashier })
    @FallbackBooleanValue
    public boolean deleteCustomer(
            @NotNull @Min(1) @Throw(InvalidCustomerIdException.class) Integer id
    ) throws InvalidCustomerIdException, UnauthorizedException;

    @AcceptRoles({ Role.Administrator, Role.ShopManager, Role.Cashier })
    public Customer getCustomer(
            @NotNull @Min(1) @Throw(InvalidCustomerIdException.class) Integer id
    ) throws InvalidCustomerIdException, UnauthorizedException;

    @AcceptRoles({ Role.Administrator, Role.ShopManager, Role.Cashier })
    public List<Customer> getAllCustomers() throws UnauthorizedException;

    @AcceptRoles({ Role.Administrator, Role.ShopManager, Role.Cashier })
    @FallbackStringValue
    public String createCard() throws UnauthorizedException;

    @AcceptRoles({ Role.Administrator, Role.ShopManager, Role.Cashier })
    @FallbackBooleanValue
    public boolean attachCardToCustomer(
            @NotNull @NotEmpty @Pattern(regexp = "^\\d{10}$") String customerCard,
            @NotNull @Min(1) @Throw(InvalidCustomerIdException.class) Integer customerId
    ) throws InvalidCustomerIdException, InvalidCustomerCardException, UnauthorizedException;

    @AcceptRoles({ Role.Administrator, Role.ShopManager, Role.Cashier })
    @FallbackBooleanValue
    public boolean modifyPointsOnCard(
            @NotNull @NotEmpty @Pattern(regexp = "^\\d{10}$") String customerCard,
            @NotNull int pointsToBeAdded
    ) throws InvalidCustomerCardException, UnauthorizedException;

    @AcceptRoles({ Role.Administrator, Role.ShopManager, Role.Cashier })
    public Integer startSaleTransaction() throws UnauthorizedException;

    @AcceptRoles({ Role.Administrator, Role.ShopManager, Role.Cashier })
    @FallbackBooleanValue
    public boolean addProductToSale(
            @NotNull @Min(1) @Throw(InvalidTransactionIdException.class) Integer transactionId,
            @NotNull @NotEmpty @GtinBarcode @Throw(InvalidProductCodeException.class) String productCode,
            @NotNull @Min(0) @Throw(InvalidQuantityException.class) int amount // TODO(@umbo) is this right?
    ) throws InvalidTransactionIdException, InvalidProductCodeException, InvalidQuantityException, UnauthorizedException;

    @AcceptRoles({ Role.Administrator, Role.ShopManager, Role.Cashier })
    @FallbackBooleanValue
    public boolean deleteProductFromSale(
            @NotNull @Min(1) @Throw(InvalidTransactionIdException.class) Integer transactionId,
            @NotNull @NotEmpty @GtinBarcode @Throw(InvalidProductCodeException.class) String productCode,
            @NotNull @Min(0) @Throw(InvalidQuantityException.class) int amount // TODO(@umbo) is this right?
    ) throws InvalidTransactionIdException, InvalidProductCodeException, InvalidQuantityException, UnauthorizedException;

    @AcceptRoles({ Role.Administrator, Role.ShopManager, Role.Cashier })
    @FallbackBooleanValue
    public boolean applyDiscountRateToProduct(
            @NotNull @Min(1) @Throw(InvalidTransactionIdException.class) Integer transactionId,
            @NotNull @NotEmpty @GtinBarcode @Throw(InvalidProductCodeException.class) String productCode,
            @NotNull @DiscountRate @Throw(InvalidDiscountRateException.class) double discountRate
    ) throws InvalidTransactionIdException, InvalidProductCodeException, InvalidDiscountRateException, UnauthorizedException;

    @AcceptRoles({ Role.Administrator, Role.ShopManager, Role.Cashier })
    @FallbackBooleanValue
    public boolean applyDiscountRateToSale(
            @NotNull @Min(1) @Throw(InvalidTransactionIdException.class) Integer transactionId,
            @NotNull @DiscountRate @Throw(InvalidDiscountRateException.class) double discountRate
    ) throws InvalidTransactionIdException, InvalidDiscountRateException, UnauthorizedException;

    @AcceptRoles({ Role.Administrator, Role.ShopManager, Role.Cashier })
    @FallbackIntValue
    public int computePointsForSale(
            @NotNull @Min(1) @Throw(InvalidTransactionIdException.class) Integer transactionId
    ) throws InvalidTransactionIdException, UnauthorizedException;

    @AcceptRoles({ Role.Administrator, Role.ShopManager, Role.Cashier })
    @FallbackBooleanValue
    public boolean endSaleTransaction(
            @NotNull @Min(1) @Throw(InvalidTransactionIdException.class) Integer transactionId
    ) throws InvalidTransactionIdException, UnauthorizedException;

    @AcceptRoles({ Role.Administrator, Role.ShopManager, Role.Cashier })
    @FallbackBooleanValue
    public boolean deleteSaleTransaction(
            @NotNull @Min(1) @Throw(InvalidTransactionIdException.class) Integer transactionId
    ) throws InvalidTransactionIdException, UnauthorizedException;

    @AcceptRoles({ Role.Administrator, Role.ShopManager, Role.Cashier })
    public SaleTransaction getSaleTransaction(
            @NotNull @Min(1) @Throw(InvalidTransactionIdException.class) Integer transactionId
    ) throws InvalidTransactionIdException, UnauthorizedException;

    @AcceptRoles({ Role.Administrator, Role.ShopManager, Role.Cashier })
    @FallbackIntValue
    public Integer startReturnTransaction(
            @NotNull @Min(1) @Throw(InvalidTransactionIdException.class) Integer transactionId
    ) throws InvalidTransactionIdException, UnauthorizedException;

    @AcceptRoles({ Role.Administrator, Role.ShopManager, Role.Cashier })
    @FallbackBooleanValue
    public boolean returnProduct(
            @NotNull @Min(1) @Throw(InvalidTransactionIdException.class) Integer returnId,
            @NotNull @NotEmpty @GtinBarcode @Throw(InvalidProductCodeException.class) String productCode,
            @NotNull @Min(0) @Throw(InvalidQuantityException.class) int amount
    ) throws InvalidTransactionIdException, InvalidProductCodeException, InvalidQuantityException, UnauthorizedException;

    @AcceptRoles({ Role.Administrator, Role.ShopManager, Role.Cashier })
    @FallbackBooleanValue
    public boolean endReturnTransaction(
            @NotNull @Min(1) @Throw(InvalidTransactionIdException.class) Integer returnId,
            boolean commit
    ) throws InvalidTransactionIdException, UnauthorizedException;

    @AcceptRoles({ Role.Administrator, Role.ShopManager, Role.Cashier })
    @FallbackBooleanValue
    public boolean deleteReturnTransaction(
            @NotNull @Min(1) @Throw(InvalidTransactionIdException.class) Integer returnId
    ) throws InvalidTransactionIdException, UnauthorizedException;

    @AcceptRoles({ Role.Administrator, Role.ShopManager, Role.Cashier })
    @FallbackIntValue
    public double receiveCashPayment(
            @NotNull @Min(1) @Throw(InvalidTransactionIdException.class) Integer transactionId,
            @NotNull @Positive @Throw(InvalidPaymentException.class) double cash
    ) throws InvalidTransactionIdException, InvalidPaymentException, UnauthorizedException;

    @AcceptRoles({ Role.Administrator, Role.ShopManager, Role.Cashier })
    @FallbackBooleanValue
    public boolean receiveCreditCardPayment(
            @NotNull @Min(1) @Throw(InvalidTransactionIdException.class) Integer transactionId,
            @NotNull @NotEmpty @Throw(InvalidCreditCardException.class) String creditCard
    ) throws InvalidTransactionIdException, InvalidCreditCardException, UnauthorizedException;

    @AcceptRoles({ Role.Administrator, Role.ShopManager, Role.Cashier })
    @FallbackIntValue
    public double returnCashPayment(
            @NotNull @Min(1) @Throw(InvalidTransactionIdException.class) Integer returnId
    ) throws InvalidTransactionIdException, UnauthorizedException;

    @AcceptRoles({ Role.Administrator, Role.ShopManager, Role.Cashier })
    @FallbackBooleanValue
    public double returnCreditCardPayment(
            @NotNull @Min(1) @Throw(InvalidTransactionIdException.class) Integer returnId,
            @NotNull @NotEmpty @Throw(InvalidCreditCardException.class) String creditCard
    ) throws InvalidTransactionIdException, InvalidCreditCardException, UnauthorizedException;

    @AcceptRoles({ Role.Administrator, Role.ShopManager })
    @FallbackBooleanValue
    public boolean recordBalanceUpdate(double toBeAdded) throws UnauthorizedException;

    @AcceptRoles({ Role.Administrator, Role.ShopManager })
    public List<BalanceOperation> getCreditsAndDebits(LocalDate from, LocalDate to) throws UnauthorizedException;

    @AcceptRoles({ Role.Administrator, Role.ShopManager })
    public double computeBalance() throws UnauthorizedException;
}
