# Design Document 

Authors: Massimiliano Pronesti, Matteo Notarangelo, Davide Mammone, Umberto Pepato 

Date: 30/04/2021

Version: 1.0

# Contents

- [High level design](#package-diagram)
- [Low level design](#class-diagram)
- [Verification traceability matrix](#verification-traceability-matrix)
- [Verification sequence diagrams](#verification-sequence-diagrams)

# Instructions

The design must satisfy the Official Requirements document, notably functional and non functional requirements

# High level design 

The application follows the 3-tier architectural pattern, with each entity having a dedicated `Repository` to interact
with the persistence layer. The Data layer interacts with the repositories to execute collection-related actions, the
View layer with the Java UI library and the Application layer accepts inputs from the UI and converts them to data model
actions.

## Packages

```plantuml
@startuml
package it.polito.ezshop {
    package data
    package view
    package application
}
view -- application
application -- data
@enduml
```

# Low level design

```plantuml
@startuml
left to right direction

package application {

    interface EZShopInterface {
    
    }
    
    class Shop implements EZShopInterface {
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

    class AccountBook {
        double totalBalance
        List<BalanceOperation> operations

        boolean recordBalanceUpdate(double toBeAdded)
        double getBalance()
        List<BalanceOperation> getCreditsAndDebits(LocalDate from, LocalDate to)
    }
    
    AccountBook -- Shop

    class Config {

    }

    class Util {

    }

    class CreditCardCircuit {
    
        boolean isValid(String creditCard)
        boolean isExpired(String creditCard)
        boolean hasEnoughCredit(String creditCard, double requiredBalance)
        boolean processPayment(String creditCard, double requiredBalance)
        boolean returnPayment(String creditCard, double requiredBalance)
    }
}
@enduml
```

```plantuml
@startuml
left to right direction

package data {

    interface Repository<T> {
        +EntityManager getEntityManager()
        +List<T> findAll()
        +T find(Integer id)
        +boolean create(T entity)
        +T update(T entity)
        +boolean delete(T entity)
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
        +boolean create(User user)
        +User update(User user)
        +boolean delete(User user)
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
        +boolean create(Customer user)
        +Customer update(Customer user)
        +boolean delete(Customer user)
    }

    Customer -- CustomerRepository

    class BalanceOperation <<Persistent>> {
        description
        amount
        date
    }

    class Credit extends BalanceOperation {

    }

    class Debit extends BalanceOperation {

    }

    class BalanceOperationRepository implements Repository {
        +List<BalanceOperation> findAll()
        +BalanceOperation find(Integer id)
        +boolean create(BalanceOperation user)
        +BalanceOperation update(BalanceOperation user)
        +boolean delete(BalanceOperation user)
    }

    BalanceOperation -- BalanceOperationRepository

    class LoyaltyCard {
        String id
        Integer points
    }

    class Order <<Persistent>> {
        Integer id
        String barcode
        double pricePerUnit
        Integer quantity
        String status
    }

    class OrderRepository implements Repository {
        +List<Order> findAll()
        +Order find(Integer id)
        +boolean create(Order user)
        +Order update(Order user)
        +boolean delete(Order user)
    }

    Order -- OrderRepository

    class ProductType <<Persistent>> {
        Integer id
        String barcode
        String description
        double pricePerUnit
        String note
        Integer quantity
        Position position
    }

    class ProductTypeRepository implements Repository {
        +List<ProductType> findAll()
        +ProductType find(Integer id)
        +ProductType findByBarcode(String barcode)
        +boolean create(ProductType user)
        +ProductType update(ProductType user)
        +boolean delete(ProductType user)
    }

    ProductType -- ProductTypeRepository

    class Position {
        String aisleID
        String rackID
        String levelID
    }

    class Transaction <<Persistent>> {
    }

    class TransactionRepository implements Repository {
        +List<Transaction> findAll()
        +Transaction find(Integer id)
        +boolean create(Transaction user)
        +Transaction update(Transaction user)
        +boolean delete(Transaction user)
    }

    Transaction -- TransactionRepository

    class ReturnTransaction extends Transaction {
        Integer id
        Integer returnId
        Map<ProductType, Integer> productAndAmount
        boolean commit
    }

    class TransactionItem {
        Integer quantity
        double discountRate
        ProductType product
    }

    class SaleTransaction extends Transaction {
        Integer id
        List<TransactionItem> products
        String date
        String time
        double cost
        String paymentType
        String status
        double discountRate
    }

    Order --|> Debit
    Order "*" - ProductType

    SaleTransaction - "*" ProductType

    LoyaltyCard "0..1" --> Customer

    SaleTransaction "*" -- "0..1" LoyaltyCard

    ProductType - "0..1" Position

    ReturnTransaction "*" - SaleTransaction
    ReturnTransaction "*" - ProductType

    TransactionItem -- SaleTransaction
    TransactionItem -- ProductType

    SaleTransaction --|> Credit
    ReturnTransaction --|> Debit
}

@enduml
```

```plantuml
@startuml
package view {
note: TBD
}
@enduml
```

# Verification traceability matrix

| FR Code | AccountBook | CreditCardCircuit | CustomerRepository | BalanceOperationRepository | LoyalityCard | OrderRepository | ProductTypeRepository|Position|TransactionRepository|TransactionItem| Shop | UserRepository |
| :--------:|:---:|:-----------:|:---------:|:----------------:| :---------------: | :------: | :--------: |:---:|:-----------:|:---------:|:----------------:| :--------: |
| FR1   | | | | |   |   |   | | | |  X |X| 
| FR3   | | | | |   |   | X | | | |  X | |  
| FR4   | | | | |   | X | X |X|X| |  X | |  
| FR5   | | |X| | X |   |   | | | |  X | |  
| FR6   | | | | | X |   | X | |X|X|  X | |  
| FR7   | |X| | |   |   |   | | |X|  X | | 
| FR8   |X| | |X|   |   |   | | | |  X | |  

# Verification sequence diagrams

## Scenario 1-1

```plantuml
@startuml
actor Administrator
    Shop -> ProductTypeRepository: createProductType
    activate ProductTypeRepository

    ProductTypeRepository -> EntityManager: create
    activate EntityManager

    EntityManager --> ProductTypeRepository
    deactivate EntityManager

    ProductTypeRepository --> Shop
    deactivate ProductTypeRepository
@enduml
```

## Scenario 2-1
```plantuml
@startuml
actor Administrator
    Shop -> UserRepository: createUser
    activate UserRepository

    UserRepository -> EntityManager: create
    activate EntityManager

    EntityManager --> UserRepository
    deactivate EntityManager

    UserRepository --> Shop
    deactivate UserRepository
@enduml
```

## Scenario 3-2
```plantuml
@startuml
    actor Administrator
    Shop -> OrderRepository: payOrder
    activate OrderRepository

    OrderRepository -> EntityManager: find
    activate EntityManager

    EntityManager --> OrderRepository
    deactivate EntityManager

    OrderRepository -> EntityManager: update
    activate EntityManager

    EntityManager --> OrderRepository
    deactivate EntityManager

    OrderRepository --> Shop
    deactivate OrderRepository
@enduml
```

## Scenario 4-2
```plantuml
@startuml
    actor Cashier
    Shop -> Shop: createCard
    Shop -> CustomerRepository: attachCardToCustomer
    activate CustomerRepository

    CustomerRepository -> EntityManager: find
    activate EntityManager

    EntityManager --> CustomerRepository
    deactivate EntityManager

    CustomerRepository -> EntityManager: update
    activate EntityManager

    EntityManager --> CustomerRepository
    deactivate EntityManager

    CustomerRepository --> Shop
    deactivate CustomerRepository
@enduml
```

## Scenario 6-5
```plantuml
@startuml
    actor Cashier
    Shop -> TransactionRepository: startSaleTransaction
    activate TransactionRepository
    
    TransactionRepository -> EntityManager: create
    activate EntityManager
    
    EntityManager --> TransactionRepository
    deactivate EntityManager
    
    TransactionRepository --> Shop
    deactivate TransactionRepository
    
    Shop -> ProductTypeRepository: getProductTypeByBarCode
    activate ProductTypeRepository
    
    ProductTypeRepository -> EntityManager: findByBarcode
    activate EntityManager
    
    EntityManager --> ProductTypeRepository
    deactivate EntityManager
    
    ProductTypeRepository --> Shop
    deactivate ProductTypeRepository
    
    Shop -> ProductTypeRepository: addProductToSale
    activate ProductTypeRepository
    
    ProductTypeRepository -> EntityManager: update
    activate EntityManager
    
    EntityManager --> ProductTypeRepository
    deactivate EntityManager
    
    ProductTypeRepository --> TransactionRepository
    deactivate TransactionRepository
    
    TransactionRepository -> EntityManager: update
    activate EntityManager
    
    EntityManager --> TransactionRepository
    deactivate EntityManager
    
    TransactionRepository --> Shop
    deactivate TransactionRepository
    
    Shop -> TransactionRepository: endSaleTransaction
    activate TransactionRepository
    
    TransactionRepository -> EntityManager: update
    activate EntityManager
    
    EntityManager --> TransactionRepository
    deactivate EntityManager
    
    TransactionRepository --> Shop
    deactivate ProductTypeRepository
    
    Shop -> UI: askPaymentType
    activate UI
    
    UI --> Shop
    deactivate UI
    
    Shop -> TransactionRepository: deleteSaleTransaction
    
    TransactionRepository -> EntityManager: delete
    activate EntityManager
    
    EntityManager --> TransactionRepository
    deactivate EntityManager
    
    TransactionRepository --> Shop
    deactivate TransactionRepository
@enduml
```

## Scenario 7-4
```plantuml
@startuml
actor Cashier
@enduml
```

## Scenario 9-1
```plantuml
@startuml
actor ShopManager
@enduml
```
