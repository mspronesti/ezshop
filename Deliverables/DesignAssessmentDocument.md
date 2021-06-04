# Design assessment

The goal of this document is to analyse the structure of your project, compare it with the design delivered
on April 30, discuss whether the design could be improved

# Levelized structure map
```
<Applying Structure 101 to your project, version to be delivered on june 4, produce the Levelized structure map,
with all elements explosed, all dependencies, NO tangles; and report it here as a picture>
```
![](../assets/AssessmentDocument/levelized-structure-map.png)
# Structural over complexity chart
```
<Applying Structure 101 to your project, version to be delivered on june 4, produce the structural over complexity chart; and report it here as a picture>
```
<div align="center">
    <img src="../assets/AssessmentDocument/structural-over-complexity-tangle.png" </img>
</div>


# Size metrics

```
<Report here the metrics about the size of your project, collected using Structure 101>
```

| Metric                                    | Measure |
| ----------------------------------------- | ------- |
| Packages                                  |   7      |
| Classes (outer)                           |     66    |
| Classes (all)                             |     71    |
| NI (number of bytecode instructions)      |    4697     |
| LOC (non comment non blank lines of code) |    2020     |

![](../assets/AssessmentDocument/size-metrics.png)

# Items with XS

```
<Report here information about code tangles and fat packages>
```

| Item | Tangled | Fat  | Size | XS   |
| ---- | ------- | ---- | ---- | ---- |
|  it.polito.ezshop    |   0%      |   6   |4,787      |6      |

![](../assets/AssessmentDocument/items-with-XS.png)

# Package level tangles

```
<Report screen captures of the package-level tangles by opening the items in the "composition perspective" 
(double click on the tangle from the Views->Complexity page)>
```
![](../assets/AssessmentDocument/package-level-tangles2.png)
# Summary analysis
```
<Discuss here main differences of the current structure of your project vs the design delivered on April 30>
<Discuss if the current structure shows weaknesses that should be fixed>
```
## Current structure vs 30th April Design
As regards the differences between our actual structure and the one described in the `DesignDocument` delivered on April 30, we added the `EZShopController` and the `EZShopControllerFactory` to cope with the bounds of the provided interfaces and to avoid duplicating code.

Moreover, we introduced a new package `annotations` for a fancier, easily reusable and more structured validation policy which only requires to properly annotate methods and parameters to deal with exception cases. 

The new design (already provided last delivery, `DesignDocument` v2.0) looks like this


### Packages

```plantuml
@startuml
package it.polito.ezshop {
    package annotations
    package data
    package exceptions
    package utils
}
data -- annotations
data -- exceptions
data -- utils
@enduml
```

### Low Level Design
```plantuml
@startuml
left to right direction

package data {

    interface EZShopInterface {
    
    }
    
    class EZShop implements EZShopInterface {
        List<User> users
        List<ProductType> products
        List<Customer> customers
        AccountBook accountBook
        User authenticatedUser
        + void reset();
        + Integer createUser(String username, String password, String role)
        + boolean deleteUser(Integer id)
        + List<User> getAllUsers()
        + User getUser(Integer id)
        + boolean updateUserRights(Integer id, String role)
        + User login(String username, String password)
        + boolean logout();
        + Integer createProductType(String description, String productCode, double pricePerUnit, String note)
        + boolean updateProduct(Integer id, String newDescription, String newCode, double newPrice, String newNote)
        + boolean deleteProductType(Integer id)
        + List<ProductType> getAllProductTypes()
        + ProductType getProductTypeByBarCode(String barCode)
        + List<ProductType> getProductTypesByDescription(String description)
        + boolean updateQuantity(Integer productId, Integer toBeAdded)
        + boolean updatePosition(Integer productId, String newPos)
        + Integer issueOrder(String productCode, Integer quantity, double pricePerUnit)
        + Integer payOrderFor(String productCode, Integer quantity, double pricePerUnit)
        + boolean payOrder(Integer orderId)
        + boolean recordOrderArrival(Integer orderId)
        + List<Order> getAllOrders()
        + Integer defineCustomer(String customerName)
        + boolean modifyCustomer(Integer id, String newCustomerName, String newCustomerCard)
        + boolean deleteCustomer(Integer id)
        + Customer getCustomer(Integer id)
        + List<Customer> getAllCustomers()
        + String createCard()
        + boolean attachCardToCustomer(String customerCard, Integer customerId)
        + boolean modifyPointsOnCard(String customerCard, Integer pointsToBeAdded)
        + Integer startSaleTransaction()
        + boolean addProductToSale(Integer transactionId, String productCode, Integer amount)
        + boolean deleteProductFromSale(Integer transactionId, String productCode, Integer amount)
        + boolean applyDiscountRateToProduct(Integer transactionId, String productCode, double discountRate)
        + boolean applyDiscountRateToSale(Integer transactionId, double discountRate)
        + Integer computePointsForSale(Integer transactionId)
        + boolean endSaleTransaction(Integer transactionId)
        + boolean deleteSaleTransaction(Integer transactionId)
        + SaleTransaction getSaleTransaction(Integer transactionId)
        + Integer startReturnTransaction(Integer transactionId)
        + boolean returnProduct(Integer returnId, String productCode, Integer amount)
        + boolean endReturnTransaction(Integer returnId, boolean commit)
        + boolean deleteReturnTransaction(Integer returnId)
        + double receiveCashPayment(Integer transactionId, double cash)
        + boolean receiveCreditCardPayment(Integer transactionId, String creditCard)
        + double returnCashPayment(Integer returnId)
        + double returnCreditCardPayment(Integer returnId, String creditCard)
        + boolean recordBalanceUpdate(double toBeAdded)
        + List<BalanceOperation> getCreditsAndDebits(LocalDate from, LocalDate to)
        + double computeBalance()
    }
    
    EZShop -- EZShopController

    interface Repository<T> {
        Session getSession()
        T _find(Class<? extends T> entityClass, Serializable id)
        List<T> _findAll(Class<? extends T> entityClass)
        Serializable _create(T entity)
        T _update(T entity)
        void _delete(T entity)
        {abstract} T find(Serializable id)
        {abstract} List<T> findAll()
        {abstract} Serializable create(T entity)
        {abstract} T update(T entity)
        {abstract} void delete(T entity)
    }

    class User <<Persistent>> {
        Integer id
        String role
        String password
        String username
    }

    class UserRepository implements Repository {
        +List<User> findAll()
        +User find(Integer id)
        +User findByUsername(String username)
        +Integer create(User user)
        +User update(User user)
        +void delete(User user)
    }

    User -- UserRepository

    class Customer <<Persistent>> {
        Integer id
        String name
        LoyaltyCard card
    }

    class CustomerRepository implements Repository {
        +List<Customer> findAll()
        +Customer find(Integer id)
        +Customer findByName(String name)
        +Integer create(Customer user)
        +Customer update(Customer user)
        +void delete(Customer user)
    }

    Customer -- CustomerRepository

    class BalanceOperation <<Persistent>> {
        Integer id
        Double money
        String type
        LocalDate date
    }

    class BalanceOperationRepository implements Repository {
        +List<BalanceOperation> findAll()
        +List<BalanceOperation> findAllBetweenDates(LocalDate from, LocalDate to)
        +BalanceOperation find(Integer id)
        +Integer create(BalanceOperation user)
        +BalanceOperation update(BalanceOperation user)
        +void delete(BalanceOperation user)
    }

    BalanceOperation -- BalanceOperationRepository

    class LoyaltyCard {
        String id
        Integer points
    }

    class LoyaltyCardRepository implements Repository {
        +List<LoyaltyCard> findAll()
        +LoyaltyCard find(Integer id)
        +String create(LoyaltyCard user)
        +LoyaltyCard update(LoyaltyCard user)
        +void delete(LoyaltyCard user)
    }

    LoyaltyCard -- LoyaltyCardRepository

    class Order <<Persistent>> {
        Integer id
        Integer balanceId
        String barcode
        String status
        Double pricePerUnit
        Integer quantity
    }

    class OrderRepository implements Repository {
        +List<Order> findAll()
        +Order find(Integer id)
        +Integer create(Order user)
        +Order update(Order user)
        +void delete(Order user)
    }

    Order -- OrderRepository

    class ProductType <<Persistent>> {
        Integer id
        Integer quantity
        String note
        String description
        String barcode
        Double pricePerUnit
        Position position
    }

    class ProductTypeRepository implements Repository {
        +List<ProductType> findAll()
        +ProductType find(Integer id)
        +ProductType findByBarcode(String barcode)
        +Integer create(ProductType user)
        +ProductType update(ProductType user)
        +void delete(ProductType user)
    }

    ProductType -- ProductTypeRepository

    class Position {
        String aisleID
        String rackID
        String levelID
    }

    class SaleTransactionRepository implements Repository {
        +List<SaleTransaction> findAll()
        +SaleTransaction find(Integer id)
        +Integer create(SaleTransaction user)
        +SaleTransaction update(SaleTransaction user)
        +void delete(SaleTransaction user)
    }

    class SaleTransaction {
        Integer id
        List<TicketEntry> entries
        Double discountRate
        Double price
    }
    
    class ReturnTransaction extends SaleTransaction {
        SaleTransaction saleTransaction
    }

    SaleTransaction -- SaleTransactionRepository
    
    class ReturnTransactionRepository implements Repository {
        +List<ReturnTransaction> findAll()
        +ReturnTransaction find(Integer id)
        +Integer create(ReturnTransaction user)
        +ReturnTransaction update(ReturnTransaction user)
        +void delete(ReturnTransaction user)
    }
    
    ReturnTransaction -- ReturnTransactionRepository

    Order "*" - ProductType

    SaleTransaction - "*" ProductType
    ReturnTransaction - "*" ProductType

    LoyaltyCard "0..1" --> Customer

    SaleTransaction "*" -- "0..1" LoyaltyCard

    ProductType - "0..1" Position

    ReturnTransaction "*" - SaleTransaction
    ReturnTransaction "*" - ProductType
    ReturnTransaction "*" - ProductType

    TicketEntry -- SaleTransaction
    TicketEntry -- ProductType
}

@enduml
```

```plantuml
@startuml
package utils {
    class DiscountRateValidator
    class GtinBarcodeValidator
    class HibernateUtil
    class LocationValidator
    class LoyaltyCardIdGenerator
    class PaymentGateway
}
@enduml
```

## Weaknesses Analysis
The structure-over-complexity plot produced by structure101 shows a 96% tangle due to a backward dependency involving our custom java annotation `AcceptRoles` and the `Role` enum, placed in different packages.

Nevertheless, if we place `Role` inside the `annotations` package (and we can because it has no dependency with anything but that annotation) we get a 0% tangle, as shown below.

<div align="center">
    <img src="../assets/AssessmentDocument/structural-over-complexity-no-tangle.png" </img>
</div>


Thereby we believe our design doesn't contain any related weaknesses. 
