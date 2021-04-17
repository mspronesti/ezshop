# Requirements Document 

Authors: Massimiliano Pronesti, Matteo Notarangelo, Davide Mammone, Umberto Pepato

Date:

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
| Cashier | ... |
| Cash Register | ... |
| Inventory | ... |
# Context Diagram and interfaces

## Context Diagram

```plantuml
@startuml
skinparam packageStyle rectangle
left to right direction


actor :Manager: as m
actor :Owner: as o
actor :Cash Register: as cr
actor :Inventory: as iv
actor Cashier as c


m --|> c
o --|> m

rectangle System{
(EZShop) as ez	

cr --> ez 
ez <-- iv
c -- ez

}

@enduml
```


## Interfaces

| Actor | Logical Interface | Physical Interface  |
| ------------- |:-------------:| -----:|
|   End User    | Web GUI  | Touch Screen on Smartphone,keyboard and mouse on pc|
|  Inventory |  Web Services | Internet Connection |
| Cash register |  Web Services | Internet Connection |
 


# Stories and personas
\<A Persona is a realistic impersonation of an actor. Define here a few personas and describe in plain text how a persona interacts with the system>

\<Persona is-an-instance-of actor>

\<stories will be formalized later as scenarios in use cases>


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
|  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;FR2.2 |  Delete Item |
|  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;FR3 |  Read Item |
|  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;FR3.1 |  Find Item |
|  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;FR3.2 |  Filter Item |
|  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;FR3.3 |  Sort Item |
|  FR4     |  Manage Accounting |
| &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;FR4.1 | Show daily accounting |
| &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;FR4.2 | Show weekly accounting |
| &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;FR4.3 | Show montly accounting |
| &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;FR4.4 | Show annual accounting |
| &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;FR4.5 | Add transaction |
| &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;FR4.5.1 | Add expense |
| &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;FR4.5.2 | Add income |
|  FR5     |  Manage Sales      |
|  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;FR5.1 | Scan item |
|  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;FR5.2 | Produce receipt  |
|  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;FR5.3 | remove item from receipt |
|  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;FR5.4 | return (reso) |
|  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;FR5.5 | provide coupon |
|  FR6   |  Manage Customer |
|  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;FR6.1 | fidelity card | 

## Non Functional Requirements

\<Describe constraints on functional requirements>

| ID        | Type (efficiency, reliability, ..)           | Description  | Refers to |
| ------------- |:-------------:| :-----:| -----:|
|  NFR1     |   |  | |
|  NFR2     | |  | |
|  NFR3     | | | |
| NFRx .. | | | | 


# Use case diagram and use cases


## Use case diagram

### Use case 1, UC1
use case 1, UC1 - CREATE A USER ACCOUNT

| Actors Involved        | End user |
| ------------- |:-------------:| 
|  Precondition     | device is connected to the internet, application in ON |  
|  Post condition     | Existence of an account connected to the shop |
|  Nominal Scenario     | New user creates a new account U and populates its fields. |
|  Variants     | A user can create only one account, this is checked through the email (one email, one account at most). |

##### Scenario 1.1 

| Scenario 1.1 | Nominal case |
| ------------- |:-------------:| 
|  Precondition     | device is connected to the internet, application in ON |
|  Post condition     | End user is logged in |
| Step#        | Description  |
|  1     | End user taps on login  |  
|  2     | End user inserts username and password |
|  3     | End user is logged in |

### Use case 2, UC2
use case 2, UC2 - MODIFY USER ACCOUNT

| Actors Involved        | End user |
| ------------- |:-------------:| 
|  Precondition     | Account user exists |  
|  Post condition     | - |
|  Nominal Scenario     | User modifies one or more fields of his account |
|  Variants     | A user can Modify only his/her account. a Manager can modify any account |

### Use case 3, UC3

use case 3, UC3 - REMOVE USER ACCOUNT

| Actors Involved        | End user |
| ------------- |:-------------:| 
|  Precondition     | Account user exists |  
|  Post condition     | Account user deleted from the system |
|  Nominal Scenario     | User selects an account to delete |
|  Variants     | A user can delete only his/her account. a Manager can delete any account |

### Use case 4, UC4
use case 4, UC4 - CREATE A NEW ITEM

| Actors Involved        | End user |
| ------------- |:-------------:| 
|  Precondition     | End user is logged in. Item does not exist |  
|  Post condition     | Item has been created |
|  Nominal Scenario     | the user creates a new item in the system; he enters all the fields of an item |

### Use case 5, UC5
Use case 5, UC5 - UPDATE ITEM

| Actors Involved        | End user |
| ------------- |:-------------:| 
|  Precondition     | End user is logged in. Item exists |  
|  Post condition     | - |
|  Nominal Scenario     | User modifies one or more fields of the item |


### Use case 6, UC6
use case 6, UC6 - DELETE ITEM

| Actors Involved        | End user |
| ------------- |:-------------:| 
|  Precondition     | Item exists |  
|  Post condition     | Item deleted from the system |
|  Nominal Scenario     | User selects an item to delete |


### Use case 7, UC7
use case 7, UC7 - READ ITEM

| Actors Involved        | End user |
| ------------- |:-------------:| 
|  Precondition     | Items that user wants to read exist |  
|  Post condition     | Item has been read |
|  Nominal Scenario     | User selects one or more item to read |


| Scenario 7.1 | filter case |
| ------------- |:-------------:| 
|  Precondition     | Items that user wants to filter exist |
|  Post condition     | Items have been filtered  |
| Step#        | Description  |
|  1     | End user opens the inventory |  
|  2     | End user filters items |

| Scenario 7.2 | find case |
| ------------- |:-------------:| 
|  Precondition     | Items that user wants to find exist |
|  Post condition     | Items have been found |
| Step#        | Description  |
|  1     | End user opens the inventory |  
|  2     | End user finds the selected items |

| Scenario 7.3 | sort case |
| ------------- |:-------------:| 
|  Precondition     | Items that user wants to sort exist |
|  Post condition     | Items have been sorted |
| Step#        | Description  |
|  1     | End user opens the inventory |  
|  2     | End user sorts items |

| Scenario 7.4 | exeption case  (there's no item to read) |
| ------------- |:-------------:| 
|  Precondition     | Item that user wants  to read doesn't exist |
|  Post condition     | - |
| Step#        | Description  |
|  1     | End user opens the inventory |  
|  2     | End user tries to read items |
|  3     | App raises an error because there's no item to read telling the end user to add items first |


### Use case 8, UC8
Use case 8, UC8 - MANAGE SALES
| Actors Involved        | cash register, cashier |
| ------------- |:-------------:| 
|  Precondition     | there's a cashier at the cash register  |  
|  Post condition     | income is increased. one or more instances of the bought items has been removed from the catalogue (depending on how many instances of the same item is bought) |
|  Nominal Scenario     | cashier manages the sell of one or more items |
|  Variants     | customer can pay with cash (in case, he can have the right amount of money, or he can have less/more than needed) or with a CreditCard |


| Scenario 8.1 | Nominal case |
| ------------- |:-------------:| 
|  Precondition     |scan is valid |
|  Post condition     | items has been selled |
|        |   income has increased    |
|        |   quantity of item in inventory is reduced   |
| Step#        | Description  |
|  1     | cashier scans item 1 |  
|  2     | cashier scans item 2 |
|  ..    | until last item |
|  3     | customer pays the correct amount |
|  4     | cashier inserts money in the cash register |
|  5     | cash register prints the receipt |
|  6     | cashier gives the receipt to the customer |
|  7     | cashier provides coupon to the customer |

### Use case 9, UC9
use case 9, UC9 - MANAGE CUSTOMER

| Actors Involved        | Cashier |
| ------------- |:-------------:| 
|  Precondition     | Customer has a fidelity card |  
|       | Cashier has already printed the receipt | 
|  Post condition     | - |
|  Nominal Scenario     | Cashier provides coupon to the customer (depending on how much he/her has spent) |



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
 + quantity
 + price
}

class Inventory {	
}


Owner -up-|> User
EZShop -- "*" Store
User  -- "*" Store

Item  "*" --  Inventory

EZShop --"*" User

Store  --  "1..*" CashRegister 
FidelityCard "0..1" --  Customer
Store  -- "*" FidelityCard
Store --Inventory 

@enduml
```


# System Design
\<describe here system design>

\<must be consistent with Context diagram>

# Deployment Diagram 
```plantuml
@startuml
artifact "EZShop Application" as ezs
node "server" as s
node "PC client" as pc
node "Mobile client" as phone
s -- ezs
s -- "*" pc
s -- "*" phone
@enduml
```
