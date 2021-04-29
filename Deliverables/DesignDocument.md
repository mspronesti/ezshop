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

<discuss architectural styles used, if any>
<report package diagram>






# Low level design

<for each package, report class diagram>









# Verification traceability matrix


| FR Code | AccountBook | CreditCardCircuit | Customer | BalanceOperation | LoyalityCard | Order | ProductTYpe|Position|ReturnTransaction|ProductQuantityAndDiscount| SaleTransaction | Shop | User |
| :--------:|:---:|:-----------:|:---------:|:----------------:| :---------------: | :------: | :--------: |:---:|:-----------:|:---------:|:----------------:| :--------: |:---:|
| FR1   | | | | |   |   |   | | | | |  X |X| 
| FR3   | | | | |   |   | X | | | | |  X | |  
| FR4   | | | | |   | X | X |X| |X| |  X | |  
| FR5   | | |X| | X |   |   | | | | |  X | |  
| FR6   | | | | | X |   | X | |X|X|X|  X | |  
| FR7   | |X| | |   |   |   | | | |X|  X | | 
| FR8   |X| | |X|   |   |   | | | | |  X | |  

\<for each functional requirement from the requirement document, list which classes concur to implement it>











# Verification sequence diagrams 
\<select key scenarios from the requirement document. For each of them define a sequence diagram showing that the scenario can be implemented by the classes and methods in the design>

