# Requirements Document 

Authors: Massimiliano Pronesti, Matteo Notarangelo, Davide Mammone, Umberto Pepato

Date: 21/04/2021

Version:

# Contents

- [Essential description](#essential-description)
- [Stakeholders](#stakeholders)
- [Context Diagram and interfaces](#context-diagram-and-interfaces)
	+ [Context Diagram](#context-diagram)
	+ [Interfaces](#interfaces) 
	
- [Stories and personas](#stories-and-personas)
- [Functional and non functional requirements](#functional-and-non-functional-requirements)
	+ [Functional Requirements](#functional-requirements)
	+ [Non functional requirements](#non-functional-requirements)
- [Use case diagram and use cases](#use-case-diagram-and-use-cases)
	+ [Use case diagram](#use-case-diagram)
	+ [Use cases](#use-cases)
    	+ [Relevant scenarios](#relevant-scenarios)
- [Glossary](#glossary)
- [System design](#system-design)
- [Deployment diagram](#deployment-diagram)


# Essential description

Small shops require a simple application to support the owner or manager. A small shop (ex a food shop) occupies 50-200 square meters, sells 500-2000 different item types, has one or a few cash registers.

EZShop is a software application to:
* manage sales
* manage inventory
* manage customers
* support accounting

# Stakeholders

| Stakeholder name  | Description | 
| ----------------- |:-----------:|
|   Owner    | Uses the application to manage inventory, introduce sales, manage expenses, trace earnings        |
| Manager |  Uses the application to manage inventory, introduce sales, manages expenses, trace earnings on behalf of owner|
| Developer | Develops and maintain the application, introduce news feature to improve usability, fixes bugs
| Cashier | Uses the applications to scan items, produce receipt, produce coupons  |
| Item | Product sold by store |
| Inventory | Provides list of items |
| Credit Card System | Handles payments via credit cards |
# Context Diagram and interfaces

## Context Diagram

```plantuml
@startuml
skinparam packageStyle rectangle
left to right direction


actor :Manager: as m
actor :Owner: as o
actor :Item: as i
actor :Inventory: as iv
actor Cashier as c
actor :Credit card system: as ccs

m --|> c
o --|> m

rectangle System{
(EZShop) as ez	

i -- ez 
ez -- iv
c -- ez
ez -- ccs

}

@enduml
```


## Interfaces

| Actor | Logical Interface | Physical Interface  |
| ------------- |:-------------:| -----:|
|   Manager, Owner    | Web GUI  | Touchscreen on smartphone or tablet, keyboard screen mouse on pc|
| Cashier | Web GUI | Touchscreen on smartphone or tablet   |
|  Inventory |  Web Services | Internet connection |
| Item |  Bar code | Laser beam |
| Credit Card System | Visa direct APIs https://developer.visa.com/capabilities/visa_direct/docs-getting-started | Credit card reader|


# Stories and personas

## Anna

Anna is a 53 years old shop owner and manager. She is marryed and has a son, lives in Milan and is fond of travelling. She started her shop some years ago, but noticed she took too much time in sorting inventory and searching for what she should buy from suppliers. She then tried EZshop and even if she was not so confortable with applications, learned how to use it without much trouble. Now she doesn't only find immediatly what item her shop miss, but also check every morning the weekly account to find if sales are growing.

## Mark

Mark is a 28 years old shop manager in Munich. After he got his degree in economics and management he started working for a chain of small computer and electronic stores, but he doesn't own the shop. He is still new to his role as a manger and EZshop simplify a lot his work. The application helps him remember all the usual customer, the items he need to order from suppliers and which products are on sale. He will be also valued by his annual accounting, so he pays great attention in the weekly as well as the montly accounting. 

## Jean

Jean is a 23 years old cashier who works in a medium-size shop in Lyon. He has been doing this work for the past 2 years, but since his first day at work some things have changed. Prior to the introduction of EZshop he used to take care of the different action related to sales mostly by himself, but nowadays the application greatly helps him. He has an account which he was given by the shop manager after a brief training on how to use the application. 

## Lucy

Lucy is a 36 years old shop owner in Glasgow, she owns a little tailor's shop and she is the only person working there, so she has to take care of the cash register. Because of this she is very busy and every little spare time is a great achivement, as such she uses all functions of EZshop application and especially like the inventory and sales features. 




# Functional and non functional requirements

## Functional Requirements

| ID        | Description  |
| ------------- |:-------------:| 
| FR1 | Authorize and authenticate |
| &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;FR1.1 | Login user |
| &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;FR1.2 | Logout user |
| &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;FR1.3 | Sign up   |
| &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;FR1.3.1 | Create account |
| &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;FR1.3.2 | Associate account with new store |
| &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;FR1.3.3 | Add or update User |
| &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;FR1.3.4 | Remove User |
|  FR2     |  Manage Inventory |
|  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;FR2.1   |  Add or modify new Item |
|  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;FR2.2 |  Delete Item |
|  FR3 |  Read Item |
|  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;FR3.1 |  Find Item |
|  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;FR3.2 |  Filter Item |
|  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;FR3.3 |  Sort Item |
|  FR4     |  Manage Accounting |
| &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;FR4.1 | Show daily accounting |
| &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;FR4.2 | Show weekly accounting |
| &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;FR4.3 | Show montly accounting |
| &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;FR4.4 | Show annual accounting |
| &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;FR4.5 | Add transaction |
| &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;FR4.5.1 | Add expense |
| &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;FR4.5.2 | Add income |
|  FR5     |  Manage Sales      |
|  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;FR5.1 | Scan Fidelity card (if any) |
|  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;FR5.2 | Scan item |
|  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;FR5.3 | Produce receipt  |
|  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;FR5.4 | Remove item from receipt |
|  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;FR5.5 | Handle item return |
|  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;FR5.6 | Provide coupon |
| &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;FR5.7  | Handle payment |
| &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;FR5.7.1 | Handle cash payment |
| &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;FR5.7.2 | Handle credit card payment |
|  FR6   |  Manage Customer |
|  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;FR6.1 | Produce fidelity card | 

## Non Functional Requirements

| ID        | Type (efficiency, reliability, ..)           | Description  | Refers to |
| ------------- |:-------------:| :-----:| -----:|
|  NFR1     | Space | Inventory should storage at least 2000 Item types | FR2 |
|  NFR2     | Performance | All function should be executed in < 1sec | All FR |
|  NFR3     | Usability | Training time should take < 20min | All FR |
|  NFR4     | Usability | Trained person should take < 10sec for adding a new Item type | FR2.1 |
|  NFR5     | Interoperability | Some info should be saved on server in case a user changes device | FR2, FR4 | 
|  NFR6     | Reliability | Manage sales function should be aviable (even if only payment by cash is aviable) even if no internet connection is aviable | FR5 | 
|  NFR7     | Legislative | The system should check if the return is legally doable in the current situation (date, cupon/cash) | FR5.5/FR5.6 | 
|  NFR8     | Localisation | The currrency is Euro | All FR | 
|  NFR9     | Privacy | Personal data of one user should not be accessed by other users and users with lower authentication level should not have access to higher authentication level function | All FR, especially FR1 | 
|  NFR10     | Portability | The application should be accessed by PC on major operating systems, and by tablet or smarthphone running on Android or iOS | All FR | 



# Use case diagram and use cases


## Use case diagram
```plantuml
@startuml
left to right direction

actor :Item: as it
actor :User: as u
actor :Inventory: as i
actor :Manager: as m
actor :Owner: as o
actor :Credit Card System: as ccs

o --|> m
m --|> u

u --> (Manage sales)


u --> (Manage user account)

u --> (Manage customer)

m --> (Manage inventory)

(Manage sales) --> ccs

(Manage inventory) .> (Create new item) :include 
(Manage inventory) .> (Update item) :include 
(Manage inventory) .> (Read item) :include 
(Manage inventory) .> (Remove item) :include
(Remove item) <. (Manage sales) :include


(Find item)  <. (Read item):include
(Filter item) <. (Read item):include 
(Sort item)  <. (Read item):include

(Manage inventory) --> i

(Manage sales) --> it
(Manage accounting) <-- m
@enduml
```

### Use case 1, UC1 - CREATE A USER ACCOUNT
use case 1, UC1 

| Actors Involved        | End user |
| ------------- |:-------------:| 
|  Precondition     | Device is connected to the internet, application is ON |  
|  Post condition     | Existence of an account connected to the shop |
|  Nominal Scenario     | New user creates a new account U and populates its fields |
|  Variants     | A user can create only one account, this is checked through the email (one email, one account at most) |

##### Scenario 1.1
| Scenario 1.1 | Nominal case |
| ------------- |:-------------:| 
|  Precondition     | Device is connected to the internet, application is ON |
|  Post condition     | End user is logged in |
| Step#        | Description  |
|  1     | End user taps on login  |  
|  2     | End user inserts username and password |
|  3     | End user is logged in |

### Use case 2, UC2 - MODIFY USER ACCOUNT 

| Actors Involved        | End user |
| ------------- |:-------------:| 
|  Precondition     | Account user exists |  
|  Post condition     | - |
|  Nominal Scenario     | User modifies one or more fields of his account |
|  Variants     | A user can modify only his/her account. A manager can modify any account |

### Use case 3, UC3 - REMOVE USER ACCOUNT

| Actors Involved        | End user |
| ------------- |:-------------:| 
|  Precondition     | Account user exists |  
|  Post condition     | Account user deleted from the system |
|  Nominal Scenario     | User selects an account to delete |
|  Variants     | A user can delete only his/her account. A manager can delete any account |

### Use case 4, UC4 - CREATE A NEW ITEM

| Actors Involved        | End user |
| ------------- |:-------------:| 
|  Precondition     | End user is logged in. Item does not exist |  
|  Post condition     | Item has been created |
|  Nominal Scenario     | The user creates a new item in the system; he enters all the fields of an item |

### Use case 5, UC5 - UPDATE ITEM 

| Actors Involved        | End user |
| ------------- |:-------------:| 
|  Precondition     | End user is logged in. Item exists |  
|  Post condition     | - |
|  Nominal Scenario     | User modifies one or more fields of the item |


### Use case 6, UC6 - DELETE ITEM

| Actors Involved        | End user |
| ------------- |:-------------:| 
|  Precondition     | Item exists |  
|  Post condition     | Item deleted from the system |
|  Nominal Scenario     | User selects an item to delete |


### Use case 7, UC7 - READ ITEM

| Actors Involved        | End user |
| ------------- |:-------------:| 
|  Precondition     | Items that user wants to read exist |  
|  Post condition     | Item has been read |
|  Nominal Scenario     | User selects one or more item to read |

##### Scenario 7.1
| Scenario 7.1 | filter case |
| ------------- |:-------------:| 
|  Precondition     | Items that user wants to filter exist |
|  Post condition     | Items have been filtered  |
| Step#        | Description  |
|  1     | End user opens the inventory |  
|  2     | End user filters items |

##### Scenario 7.2
| Scenario 7.2 | find case |
| ------------- |:-------------:| 
|  Precondition     | Items that user wants to find exist |
|  Post condition     | Items have been found |
| Step#        | Description  |
|  1     | End user opens the inventory |  
|  2     | End user finds the selected items |

##### Scenario 7.3
| Scenario 7.3 | sort case |
| ------------- |:-------------:| 
|  Precondition     | Items that user wants to sort exist |
|  Post condition     | Items have been sorted |
| Step#        | Description  |
|  1     | End user opens the inventory |  
|  2     | End user sorts items |

##### Scenario 7.4
| Scenario 7.4 | exeption case  (there's no item to read) |
| ------------- |:-------------:| 
|  Precondition     | Item that user wants to read doesn't exist |
|  Post condition     | - |
| Step#        | Description  |
|  1     | End user opens the inventory |  
|  2     | End user tries to read items |
|  3     | App raises an error because there's no item to read telling the end user to add items first |


### Use case 8, UC8 - MANAGE SALES

| Actors Involved        | item, cashier |
| ------------- |:-------------:| 
|  Precondition     | There's a cashier at the cash register  |  
|  Post condition     | Income is increased. One or more instances of the bought items has been removed from the inventory (depending on how many instances of the same item is bought) |
|  Nominal Scenario     | Cashier manages the sale of one or more items |
|  Variants     | Customer can pay with cash (in case, he can have the right amount of money, or he can have less/more than needed) or with a credit card |

##### Scenario 8.1

| Scenario 8.1 | Nominal case |
| ------------- |:-------------:| 
|  Precondition     |Scan is valid, customor has fidelity card |
|  Post condition     | Items has been sold |
|        |   Income has increased    |
|        |   Quantity of item in inventory is reduced   |
| Step#        | Description  |
|  1     | Cashier scans fidelity card |
|  2     | Cashier scans item 1 |  
|  3     | Cashier scans item 2 |
|  ..    | Until last item |
|  4     | Customer pays the correct amount |
|  5     | Cashier inserts money in the cash register |
|  6     | Cash register prints the receipt |
|  7     | Cashier gives the receipt to the customer |
|  8     | Cashier provides coupon to the customer  |

##### Scenario 8.2
| Scenario 8.2 | Nominal case |
| ------------- |:-------------:| 
|  Precondition     |Scan is valid, customor doesn't have fidelity card |
|  Post condition     | Items has been sold |
|        |   Income has increased    |
|        |   Quantity of item in inventory is reduced   |
| Step#        | Description  |
|  1     | Cashier scans item 1 |  
|  2     | Cashier scans item 2 |
|  ..    | Until last item |
|  3     | Customer pays the correct amount |
|  4     | Cashier inserts money in the cash register |
|  5     | Cash register prints the receipt |
|  6     | Cashier gives the receipt to the customer |

### Use case 9, UC9 - MANAGE CUSTOMER

| Actors Involved        | Cashier |
| ------------- |:-------------:| 
|  Precondition     | Customer doesn't own a fidelity card |  
|  Post condition     | Customer has fidelity card |
|  Nominal Scenario     | Cashier provides fidelity card to the customer |

#### Scenario 9.1
| Scenario 9.1 | Nominal case |
| ------------- |:-------------:| 
|  Precondition     | Customer doesn't own a fidelity card |
|  Post condition     | Customer has fidelity card |
| Step#        | Description  |
|  1     | Cashier makes the customer fill a form with his personal data  |  
|  2     | Cashier makes the customer sign a module |
|  3     | Cashier gives the customer the fidelity card |

### Use case 10, UC10 - MANAGE ACCOUNTING

| Actors Involved        | Manager |
| ------------- |:-------------:| 
|  Precondition     | There's at least one transaction that needs to be added into the accounting |  
|  Post condition     | The accounting is up to date |
|  Nominal Scenario     | The manager adds expenses and income to the accounting |

#### Scenario 10.1
| Scenario 10.1 | Nominal case |
| ------------- |:-------------:| 
|  Precondition     | There's at least one transaction that needs to be added into the accounting |
|  Post condition     | The accounting is up to date |
| Step#        | Description  |
|  1     | The manager adds the expenses to the accounting   |  
|  2     | The manager adds the income to the accounting |
|  3     | The manager check if the accounting is up to date by checking the daily accounting |



# Glossary
```plantuml
@startuml

note "size ranges from 50 to 200 sqm" as N1
N1 -- Store

class EZShop

class User {
  + account_name
  + account_pwd
  + email	
  + permission_level
}

class Store {
  + name
  + address
  + size
}

class CashRegister{	
  + acceptsCash
  + acceptsCreditCard
}

class Owner

class Customer {
  + name
  + surname
  + email
}

class FidelityCard {
  + ID
  + n_points
  + expiry_date	
}

class Item {
 + ID
 + variant
 + quantity
}

class ItemType {
 + name
 + tags
 + description
 + photo
 + category
 + ID
 + price
}

class Catalogue {

}

class Inventory {	
}

class Bookkeeping{
}

class SaleTransaction{
  + date
  + amount
}
ItemType "*" --o SaleTransaction
SaleTransaction "*" -- Bookkeeping

Owner -up-|> User
EZShop -- "*" Store
User  -- "*" Store
EZShop -- Catalogue
Catalogue -- "*" ItemType
ItemType -- Item

Item  "*" --  Inventory

EZShop --"*" User

Store  --  "1..*" CashRegister 
FidelityCard "0..1" --  Customer
Store  -- "*" FidelityCard
Store --Inventory 


@enduml
```


# System Design
```plantuml
@startuml
top to bottom direction
class CashRegister
class Computer
class Printer 
class CreditCardReader
class EZshop 
class BarcodeReader 

Computer --o CashRegister
BarcodeReader -- Computer
EZshop--Computer
Printer--o CashRegister
CreditCardReader --o CashRegister
@enduml
```

# Deployment Diagram 
```plantuml
@startuml
artifact "EZShop Application" as ezs
node "Server" as s
node "PC client" as pc
node "Mobile client" as phone
s -- ezs
s -- "*" pc
s -- "*" phone
@enduml
```
