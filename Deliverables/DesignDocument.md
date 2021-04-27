# Design Document 


Authors: 

Date:

Version:


# Contents

- [High level design](#package-diagram)
- [Low level design](#class-diagram)
- [Verification traceability matrix](#verification-traceability-matrix)
- [Verification sequence diagrams](#verification-sequence-diagrams)

# Instructions

The design must satisfy the Official Requirements document, notably functional and non functional requirements

# High level design 

The application follows the MVC architectural pattern, with each entity having a dedicated `Repository` to interact
with the persistence layer. The Model layer interacts with the repositories to execute collection-related actions, the
View layer with the Java UI library and the Controller layer accepts inputs from the UI and converts them to data model
actions.

## Packages

```plantuml
@startuml
package model
package view
package controller
package config
package utils
@enduml
```

# Low level design

```plantuml
@startuml
class User {
+ int id
+ String username
+ String password
+ String role 
}

class ProductType {
+ int id
+ String description
+ String productCode
+ double pricePerUnit
+ String note
}

class Product {
+ int id
+ Integer quantity
+ String position
}

class Order {
+ int id
+ String productCode
+ int quantity
+ double pricePerUnit
+ String status
}

class Customer {
+ int id
+ String name
}

class Card {
+ String code
+ int points
}

Customer "1" *-- "1" Card: card >

class Transaction {
+ int id
+ String type
+ double discountRate
+ String status
+ int ticketNumber
}

class TransactionItem {
+ int quantity
+ double discountRate
}

TransactionItem "1" *-- "1" Product: product >

Transaction "1" *-- "0..*" TransactionItem: products >

class PaymentMethod {

}

class CreditCard {
+ String number
}

class Cash {
}

Cash --|> PaymentMethod

CreditCard --|> PaymentMethod

class BalanceOperation {
+ String type
+ int amount
+ LocalDate date
}
@enduml
```

# Verification traceability matrix

\<for each functional requirement from the requirement document, list which classes concur to implement it>











# Verification sequence diagrams 
\<select key scenarios from the requirement document. For each of them define a sequence diagram showing that the scenario can be implemented by the classes and methods in the design>

