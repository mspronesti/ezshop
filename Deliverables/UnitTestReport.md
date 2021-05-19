Unit Testing Documentation template

Authors: Massimiliano Pronesti, Matteo Notarangelo, Davide Mammone, Umberto Pepato

Date: 14/05/2021

Version: 1.0





# Black Box Unit Tests

```
<Define here criteria, predicates and the combination of predicates for each function of each class.
Define test cases to cover all equivalence classes and boundary conditions.
In the table, report the description of the black box test case and the correspondence with the JUnit black box test case name/number>
```



## Class BalanceOperationImpl



### Method setBalanceId

Criteria for method **setBalanceId**:

**Predicates for method setBalanceId:**

| Criterion | Predicate |
| --------- | --------- |


**Boundaries for method setBalanceId**:

| Criterion | Boundary values |
| --------- | --------------- |

 **Combination of predicates for method setBalanceId**

| Criterion | Valid/Invalid | Description of the test case | JUnit test case                                              |
| --------- | ------------- | ---------------------------- | ------------------------------------------------------------ |
| *         | **Valid**     | T1(313124)                   | it.polito.ezshop.acceptanceTest.BalanceOperationTest.testSetBalanceId |



### Method setDate

**Criteria for method setDate:**


**Predicates for method setDate:**

| Criterion | Predicate |
| --------- | --------- |


**Boundaries for method setDate**:

| Criterion | Boundary values |
| --------- | --------------- |


 **Combination of predicates for method setDate**

| Criterion | Valid/Invalid | Description of the test case | JUnit test case                                              |
| --------- | ------------- | ---------------------------- | ------------------------------------------------------------ |
| *         | **Valid**     | T1({LocalDateObj})           | it.polito.ezshop.acceptanceTest.BalanceOperationTest.testSetDate |



### Method setMoney

**Criteria for method setMoney:**

**Predicates for method setMoney:**

| Criterion | Predicate |
| --------- | --------- |


**Boundaries for method setMoney**:

| Criterion | Boundary values |
| --------- | --------------- |


 **Combination of predicates for method setMoney**

| Criterion | Valid/Invalid | Description of the test case | JUnit test case                                              |
| --------- | ------------- | ---------------------------- | ------------------------------------------------------------ |
| *         | **Valid**     | T1(31.90)                    | it.polito.ezshop.acceptanceTest.BalanceOperationTest.testSetMoney |




### Method setType

**Criteria for method setType:**

**Predicates for method setType:**

| Criterion | Predicate |
| --------- | --------- |


**Boundaries for method setType**:

| Criterion | Boundary values |
| --------- | --------------- |


 **Combination of predicates for method setType**

| Criterion | Valid/Invalid | Description of the test case   | JUnit test case                                              |
| --------- | ------------- | ------------------------------ | ------------------------------------------------------------ |
| *         | **Valid**     | T4("DEBIT") <br />T5("CREDIT") | it.polito.ezshop.acceptanceTest.BalanceOperationTest.testSetType |




## Class CustomerImpl



### Method setId

Criteria for method **setId**:

**Predicates for method setId:**

| Criterion | Predicate |
| --------- | --------- |


**Boundaries for method setId**:

| Criterion | Boundary values |
| --------- | --------------- |

 **Combination of predicates for method setId**

| Criterion | Valid/Invalid | Description of the test case | JUnit test case                                        |
| --------- | ------------- | ---------------------------- | ------------------------------------------------------ |
| *         | **Valid**     | T1(414276)                   | it.polito.ezshop.acceptanceTest.CustomerTest.testSetId |




### Method setCustomerName

Criteria for method **setCustomerName**:


**Predicates for method setCustomerName:**

| Criterion | Predicate |
| --------- | --------- |


**Boundaries for method setCustomerName**:

| Criterion | Boundary values |
| --------- | --------------- |


 **Combination of predicates for method setCustomerName**

| Criterion | Valid/Invalid | Description of the test case | JUnit test case                                              |
| --------- | ------------- | ---------------------------- | ------------------------------------------------------------ |
| *         | **Valid**     | T5("MarcoA)                  | it.polito.ezshop.acceptanceTest.CustomerTest.testSetCustomerName |




### Method setCustomerCard

Criteria for method **setCustomerCard**:


**Predicates for method setCustomerCard:**

| Criterion | Predicate |
| --------- | --------- |


**Boundaries for method setCustomerCard**:

| Criterion | Boundary values |
| --------- | --------------- |


 **Combination of predicates for method setCustomerCard**

| Criterion | Valid/Invalid | Description of the test case | JUnit test case                                              |
| --------- | ------------- | ---------------------------- | ------------------------------------------------------------ |
| *         | **Valid**     | T5("14148")                  | it.polito.ezshop.acceptanceTest.CustomerTest.testSetCustomerCard |





### Method setPoints

Criteria for method **setPoints**:

**Predicates for method setPoints:**

| Criterion | Predicate |
| --------- | --------- |


**Boundaries for method setPoints**:

| Criterion | Boundary values |
| --------- | --------------- |


 **Combination of predicates for method setPoints**

| Criterion | Valid/Invalid | Description of the test case | JUnit test case                                            |
| --------- | ------------- | ---------------------------- | ---------------------------------------------------------- |
| *         | **Valid**     | T1(14)                       | it.polito.ezshop.acceptanceTest.CustomerTest.testSetPoints |




## Class LoyalityCardImpl



### Method setId

Criteria for method **setId**:

**Predicates for method setId:**

| Criterion | Predicate |
| --------- | --------- |


**Boundaries for method setId**:

| Criterion | Boundary values |
| --------- | --------------- |


 **Combination of predicates for method setId**

| Criterion | Valid/Invalid | Description of the test case | JUnit test case                                            |
| --------- | ------------- | ---------------------------- | ---------------------------------------------------------- |
| *         | **Valid**     | T1(14213)                    | it.polito.ezshop.acceptanceTest.LoyalitiCardTest.testSetId |




### Method setPoints

Criteria for method **setPoints**:

**Predicates for method setPoints:**

| Criterion | Predicate |
| --------- | --------- |


**Boundaries for method setPoints**:

| Criterion | Boundary values |
| --------- | --------------- |


 **Combination of predicates for method setPoints**

| Criterion | Valid/Invalid | Description of the test case | JUnit test case                                              |
| --------- | ------------- | ---------------------------- | ------------------------------------------------------------ |
| *         | **Valid**     | T1(50)                       | it.polito.ezshop.acceptanceTest.LoyalitiCardTest.testSetPoints |




### Method setCustomer

Criteria for method **setCustomer**:

**Predicates for method setCustomer:**

| Criterion | Predicate |
| --------- | --------- |


**Boundaries for method setCustomer**:

| Criterion | Boundary values |
| --------- | --------------- |

 **Combination of predicates for method setCustomer**

| Criterion | Valid/Invalid | Description of the test case | JUnit test case                                              |
| --------- | ------------- | ---------------------------- | ------------------------------------------------------------ |
| *         | **Valid**     | T1({CustomerObj})            | it.polito.ezshop.acceptanceTest.LoyalitiCardTest.testSetCustomer |






## Class OrderImpl



### Method setOrderId

Criteria for method **setOrderId**:


**Predicates for method setOrderId:**

| Criterion | Predicate |
| --------- | --------- |


**Boundaries for method setOrderId**:

| Criterion | Boundary values |
| --------- | --------------- |

 **Combination of predicates for method setOrderId**

| Criterion | Valid/Invalid | Description of the test case | JUnit test case                                          |
| --------- | ------------- | ---------------------------- | -------------------------------------------------------- |
| *         | **Valid**     | T1(16537)                    | it.polito.ezshop.acceptanceTest.OrderTest.testSetOrderId |



### Method setBalanceId

Criteria for method **setBalanceId**:


**Predicates for method setBalanceId:**

| Criterion | Predicate |
| --------- | --------- |


**Boundaries for method setBalanceId**:

| Criterion | Boundary values |
| --------- | --------------- |


 **Combination of predicates for method setBalanceId**

| Criterion | Valid/Invalid | Description of the test case | JUnit test case                                            |
| --------- | ------------- | ---------------------------- | ---------------------------------------------------------- |
| *         | **Valid**     | T1(43123)                    | it.polito.ezshop.acceptanceTest.OrderTest.testSetBalanceId |



### Method setStatus

Criteria for method **setStatus**:

**Predicates for method setStatus:**

| Criterion | Predicate |
| --------- | --------- |


**Boundaries for method setStatus**:

| Criterion | Boundary values |
| --------- | --------------- |



 **Combination of predicates for method setStatus**

| Criterion | Valid/Invalid | Description of the test case | JUnit test case                                         |
| --------- | ------------- | ---------------------------- | ------------------------------------------------------- |
| *         | **Valid**     | T5("Pending")                | it.polito.ezshop.acceptanceTest.OrderTest.testSetStatus |



### Method setProductCode

Criteria for method **setProductCode**:

**Predicates for method setProductCode:**

| Criterion | Predicate |
| --------- | --------- |


**Boundaries for method setProductCode**:

| Criterion | Boundary values |
| --------- | --------------- |


 **Combination of predicates for method setProductCode**

| Criterion | Valid/Invalid | Description of the test case | JUnit test case                                              |
| --------- | ------------- | ---------------------------- | ------------------------------------------------------------ |
| *         | **Valid**     | T5("01DA2")                  | it.polito.ezshop.acceptanceTest.OrderTest.testSetProductCode |




### Method setPricePerUnit

**Criteria for method setPricePerUnit:**
	

- Validity of pricePerUnit

- pricePerUnit is a number

- Sign of pricePerUnit

  

**Predicates for method setPricePerUnit:**

| Criterion | Predicate |
| --------- | --------- |

**Boundaries for method setPricePerUnit**:

| Criterion | Boundary values |
| --------- | --------------- |


 **Combination of predicates for method setPricePerUnit**

| Criterion | Valid/Invalid | Description of the test case | JUnit test case                                              |
| --------- | ------------- | ---------------------------- | ------------------------------------------------------------ |
| *         | **Valid**     | T1(12.50)                    | it.polito.ezshop.acceptanceTest.OrderTest.testSetPricePerUnit |



### Method setQuantity

**Criteria for method setQuantity:**

**Predicates for method setQuantity:**

| Criterion | Predicate |
| --------- | --------- |

**Boundaries for method setQuantity**:

| Criterion | Boundary values |
| --------- | --------------- |

 **Combination of predicates for method setQuantity**

| Criterion | Valid/Invalid | Description of the test case | JUnit test case                                           |
| --------- | ------------- | ---------------------------- | --------------------------------------------------------- |
| *         | **Valid**     | T1(6)                        | it.polito.ezshop.acceptanceTest.OrderTest.testSetQuantity |




## Class ProductTypeImpl



### Method setId

**Criteria for method setId:**

**Predicates for method setId:**

| Criterion | Predicate |
| --------- | --------- |


**Boundaries for method setId**:

| Criterion | Boundary values |
| --------- | --------------- |


 **Combination of predicates for method setId**

| Criterion | Valid/Invalid | Description of the test case | JUnit test case                                           |
| --------- | ------------- | ---------------------------- | --------------------------------------------------------- |
| *         | **Valid**     | T1(3214)                     | it.polito.ezshop.acceptanceTest.ProductTypeTest.testSetId |



### Method setQuantity

**Criteria for method setQuantity:**

**Predicates for method setQuantity:**

| Criterion | Predicate |
| --------- | --------- |

**Boundaries for method setQuantity**:

| Criterion | Boundary values |
| --------- | --------------- |

 **Combination of predicates for method setQuantity**

| Criterion | Valid/Invalid | Description of the test case | JUnit test case                                              |
| --------- | ------------- | ---------------------------- | ------------------------------------------------------------ |
| *         | **Valid**     | T1(4)                        | it.polito.ezshop.acceptanceTest.ProductTypeTest.testSetQuantity |



### Method setLocation

**Criteria for method setLocation:**


**Predicates for method setLocation:**

| Criterion | Predicate |
| --------- | --------- |

**Boundaries for method setLocation**:

| Criterion | Boundary values |
| --------- | --------------- |

 **Combination of predicates for method setLocation**

| Criterion | Valid/Invalid | Description of the test case | JUnit test case                                              |
| --------- | ------------- | ---------------------------- | ------------------------------------------------------------ |
| *         | **Valid**     | T1("3-a-12")                 | it.polito.ezshop.acceptanceTest.ProductTypeTest.testSetLocation |



### Method setNote

**Criteria for method setNote:**

**Predicates for method setNote:**

| Criterion | Predicate |
| --------- | --------- |

**Boundaries for method setNote**:

| Criterion | Boundary values |
| --------- | --------------- |

 **Combination of predicates for method setNote**

| Criterion | Valid/Invalid | Description of the test case | JUnit test case                                             |
| --------- | ------------- | ---------------------------- | ----------------------------------------------------------- |
| *         | **Valid**     | T1("Blue version")           | it.polito.ezshop.acceptanceTest.ProductTypeTest.testSetNote |




### Method setProductDescription

**Criteria for method setProductDescription:**

**Predicates for method setProductDescription:**

| Criterion | Predicate |
| --------- | --------- |


**Boundaries for method setProductDescription**:

| Criterion | Boundary values |
| --------- | --------------- |


 **Combination of predicates for method setProductDescription**

| Criterion | Valid/Invalid | Description of the test case | JUnit test case                                              |
| --------- | ------------- | ---------------------------- | ------------------------------------------------------------ |
| *         | **Valid**     | T1("Blue t-shirt")           | it.polito.ezshop.acceptanceTest.ProductTypeTest.testSetProductDescription |




### Method setBarCode

**Criteria for method setBarCode:**

**Predicates for method setBarCode:**

| Criterion | Predicate |
| --------- | --------- |

**Boundaries for method setBarCode**:

| Criterion | Boundary values |
| --------- | --------------- |
|           |                 |

 **Combination of predicates for method setBarCode**

| Criterion | Valid/Invalid | Description of the test case | JUnit test case                                              |
| --------- | ------------- | ---------------------------- | ------------------------------------------------------------ |
| *         | **Valid**     | T1("13148419")               | it.polito.ezshop.acceptanceTest.ProductTypeTest.testSetBarcode |



### Method setPricePerUnit

**Criteria for method setPricePerUnit:**

**Predicates for method setPricePerUnit:**

| Criterion | Predicate |
| --------- | --------- |


**Boundaries for method setPricePerUnit**:

| Criterion | Boundary values |
| --------- | --------------- |


 **Combination of predicates for method setPricePerUnit**

| Criterion | Valid/Invalid | Description of the test case | JUnit test case                                              |
| --------- | ------------- | ---------------------------- | ------------------------------------------------------------ |
| *         | **Valid**     | T1(15.24)                    | it.polito.ezshop.acceptanceTest.ProductTypeTest.testSetPricePerUnit |



## Class ReturnTransactionImpl



### Method setSaleTransaction

**Criteria for method setSaleTransaction:**

**Predicates for method setSaleTransaction:**

| Criterion | Predicate |
| --------- | --------- |

**Boundaries for method setSaleTransaction**:

| Criterion | Boundary values |
| --------- | --------------- |


 **Combination of predicates for method setSaleTransaction**

| Criterion | Valid/Invalid | Description of the test case | JUnit test case                                              |
| --------- | ------------- | ---------------------------- | ------------------------------------------------------------ |
| *         | Valid         | T1({saleTransactionObj})     | it.polito.ezshop.acceptanceTest.ReturnTransactionTest.testSetSaleTransaction |



## Class SaleTransactionImpl



### Method setTicketNumber

**Criteria for method setTicketNumber:**

**Predicates for method setTicketNumber:**

| Criterion | Predicate |
| --------- | --------- |

**Boundaries for method setTicketNumber**:

| Criterion | Boundary values |
| --------- | --------------- |


 **Combination of predicates for method getTicketNumber**

| Criterion | Valid/Invalid | Description of the test case | JUnit test case                                              |
| --------- | ------------- | ---------------------------- | ------------------------------------------------------------ |
| *         | **Valid**     | T1(64)                       | it.polito.ezshop.acceptanceTest.SaleTransactionTest.testSetTicketNumber |



### Method setEntries

**Criteria for method setEntries:**

**Predicates for method setEntries:**

| Criterion | Predicate |
| --------- | --------- |

**Boundaries for method setEntries**:

| Criterion | Boundary values |
| --------- | --------------- |


 **Combination of predicates for method setEntries**

| Criterion | Valid/Invalid | Description of the test case | JUnit test case                                              |
| --------- | ------------- | ---------------------------- | ------------------------------------------------------------ |
| *         | **Valid**     | T1(List<{TicketEntryObj}>)   | it.polito.ezshop.acceptanceTest.SaleTransactionTest.testSetEntries |



### Method setDiscountRate

**Criteria for method setDiscountRate:**

**Predicates for method setDiscountRate:**

| Criterion | Predicate |
| --------- | --------- |


**Boundaries for method setDiscountRate**:

| Criterion | Boundary values |
| --------- | --------------- |


 **Combination of predicates for method setDiscountRate**

| Criterion | Valid/Invalid | Description of the test case | JUnit test case                                              |
| --------- | ------------- | ---------------------------- | ------------------------------------------------------------ |
| *         | **Valid**     | T1(12.50)                    | it.polito.ezshop.acceptanceTest.SaleTransactionTest.testSetDiscountRate |




### Method setPrice

**Criteria for method setPrice:**

**Predicates for method setPrice:**

| Criterion | Predicate |
| --------- | --------- |


**Boundaries for method setPrice**:

| Criterion | Boundary values |
| --------- | --------------- |

 **Combination of predicates for method setPrice**

| Criterion | Valid/Invalid | Description of the test case | JUnit test case                                              |
| --------- | ------------- | ---------------------------- | ------------------------------------------------------------ |
| *         | **Valid**     | T1(24.99)                    | it.polito.ezshop.acceptanceTest.SaleTransactionTest.testSetPrice |



### Method setPayment

**Criteria for method setPayment:**

**Predicates for method setPayment:**

| Criterion | Predicate |
| --------- | --------- |

**Boundaries for method setPayment**:

| Criterion | Boundary values |
| --------- | --------------- |

 **Combination of predicates for method setPayment**

| Criterion | Valid/Invalid | Description of the test case  | JUnit test case                                              |
| --------- | ------------- | ----------------------------- | ------------------------------------------------------------ |
| *         | **Valid**     | T1({BalanceOperationImplObj}) | it.polito.ezshop.acceptanceTest.SaleTransactionTest.testSetPayment |



## Class TicketEntryImpl



### Method setBarCode

Criteria for method **setBarCode**:

**Predicates for method setBarCode:**

| Criterion | Predicate |
| --------- | --------- |


**Boundaries for method setBarCode**:

| Criterion | Boundary values |
| --------- | --------------- |

 **Combination of predicates for method setBarCode**

| Criterion | Valid/Invalid | Description of the test case | JUnit test case                                              |
| --------- | ------------- | ---------------------------- | ------------------------------------------------------------ |
| *         | **Valid**     | T5("313151")                 | it.polito.ezshop.acceptanceTest.TicketEntryTest.testSetBarcode |



### Method setProductDescription

Criteria for method **setProductDescription**:

**Predicates for method setProductDescription:**

| Criterion | Predicate |
| --------- | --------- |

**Boundaries for method setProductDescription**:

| Criterion | Boundary values |
| --------- | --------------- |

 **Combination of predicates for method setProductDescription**

| Criterion | Valid/Invalid | Description of the test case | JUnit test case                                              |
| --------- | ------------- | ---------------------------- | ------------------------------------------------------------ |
| *         | **Valid**     | T1("Yellow T-shirt")         | it.polito.ezshop.acceptanceTest.TicketEntryTest.testSetProductDescription |




### Method setAmount

**Criteria for method setAmount:**

**Predicates for method setAmount:**

| Criterion | Predicate |
| --------- | --------- |

**Boundaries for method setAmount**:

| Criterion | Boundary values |
| --------- | --------------- |


 **Combination of predicates for method setAmount**

| Criterion | Valid/Invalid | Description of the test case | JUnit test case                                              |
| --------- | ------------- | ---------------------------- | ------------------------------------------------------------ |
| *         | **Valid**     | T1(12)                       | it.polito.ezshop.acceptanceTest.TicketEntryTest.testSetAmount |



### Method setPricePerUnit

**Criteria for method setPricePerUnit:**

**Predicates for method setPricePerUnit:**

| Criterion | Predicate |
| --------- | --------- |


**Boundaries for method setPricePerUnit**:

| Criterion | Boundary values |
| --------- | --------------- |

 **Combination of predicates for method setPricePerUnit**

| Criterion | Valid/Invalid | Description of the test case | JUnit test case                                              |
| --------- | ------------- | ---------------------------- | ------------------------------------------------------------ |
| *         | **Valid**     | T1(6.24)                     | it.polito.ezshop.acceptanceTest.TicketEntryTest.testSetPricePerUnit |



### Method setDiscountRate

Criteria for method **setDiscountRate**:


**Predicates for method setDiscountRate:**

| Criterion | Predicate |
| --------- | --------- |


**Boundaries for method setDiscountRate**:

| Criterion | Boundary values |
| --------- | --------------- |

 **Combination of predicates for method setDiscountRate**

| Criterion | Valid/Invalid | Description of the test case | JUnit test case                                              |
| --------- | ------------- | ---------------------------- | ------------------------------------------------------------ |
| *         | **Valid**     | T1(33.33)                    | it.polito.ezshop.acceptanceTest.TicketEntryTest.testSetDiscountRate |



## Class UserImpl



### Method setId

**Criteria for method setId:**

**Predicates for method setId:**

| Criterion | Predicate |
| --------- | --------- |

**Boundaries for method setId**:

| Criterion | Boundary values |
| --------- | --------------- ||

 **Combination of predicates for method setId**

| Criterion | Valid/Invalid | Description of the test case | JUnit test case                                    |
| --------- | ------------- | ---------------------------- | -------------------------------------------------- |
| *         | **Valid**     | T1(2312)                     | it.polito.ezshop.acceptanceTest.UserTest.testSetId |



### Method setUsername

**Criteria for method setUsername:**

**Predicates for method setUsername:**

| Criterion | Predicate |
| --------- | --------- |

**Boundaries for method setUsername**:

| Criterion | Boundary values |
| --------- | --------------- |

 **Combination of predicates for method setUsername**

| Criterion | Valid/Invalid | Description of the test case | JUnit test case                                          |
| --------- | ------------- | ---------------------------- | -------------------------------------------------------- |
| *         | **Valid**     | T1("FilippoA")               | it.polito.ezshop.acceptanceTest.UserTest.testSetUsername |



### Method setPassword

**Criteria for method setPassword:**

**Predicates for method setPassword:**

| Criterion | Predicate |
| --------- | --------- |


**Boundaries for method setPassword**:

| Criterion | Boundary values |
| --------- | --------------- |
|           |                 |

 **Combination of predicates for method setPassword**

| Criterion | Valid/Invalid | Description of the test case | JUnit test case                                          |
| --------- | ------------- | ---------------------------- | -------------------------------------------------------- |
| *         | **Valid**     | T1("Fe29dqh^ad3_ad")         | it.polito.ezshop.acceptanceTest.UserTest.testSetPassword |



### Method setRole

**Criteria for method setRole:**

**Predicates for method setRole:**

| Criterion | Predicate |
| --------- | --------- |

**Boundaries for method setRole**:

| Criterion | Boundary values |
| --------- | --------------- |

 **Combination of predicates for method setRole**

| Criterion | Valid/Invalid | Description of the test case | JUnit test case                                      |
| --------- | ------------- | ---------------------------- | ---------------------------------------------------- |
| *         | **Valid**     | T1("Cashier")                | it.polito.ezshop.acceptanceTest.UserTest.testSetRole |


## Class DiscountRateValidator


### Method isValid

Criteria for method **isValid**:

- Sign of double

- Double is valid

**Predicates for method isValid:**

| Criterion       | Predicate    |
| --------------- | ------------ |
| Sign of double  | >=0d && <1   |
|                 | <0d \|\| >=1 |
| Double is valid | Valid        |
|                 | null         |

**Boundaries for method isValid**:

| Criterion      | Boundary values |
| -------------- | --------------- |
| Sign of double | -Inf, 0d,1, Inf |

 **Combination of predicates for method isValid**

| Double is valid | Sign of double         | Valid/Invalid | Description of the test case | JUnit test case                                              |
| --------------- | ---------------------- | ------------- | ---------------------------- | ------------------------------------------------------------ |
| Valid           | [0d,1)                 | **Valid**     | T1(0.5d) -> true             | it.polito.ezshop.acceptanceTests.DiscountRateValidatorTest.testIsValid |
| ""              | (-inf,0d) \|\| [1,inf) | Valid         | T2(24d) -> false             | it.polito.ezshop.acceptanceTests.DiscountRateValidatorTest.testIsValid |
| null            | -                      | Invalid       | T3() -> NullPointer          | it.polito.ezshop.acceptanceTests.DiscountRateValidatorTest.testIsValid |


## Class GtinBarcodeValidator


### Method isValid

Criteria for method **isValid**:

- length of String
- Is a number
- String is valid

**Predicates for method isValid:**

| Criterion       | Predicate                   |
| --------------- | --------------------------- |
| String length   | [12, 14]                    |
|                 | [0, 11]  \|\|  [15, MaxInt) |
|                 | null                        |
| Is a number     | Number                      |
|                 | Not a number                |
| String is valid | Valid                       |
|                 | Invalid                     |

**Boundaries for method isValid**:

| Criterion | Boundary values |
| --------- | --------------- |

 **Combination of predicates for method isValid**

| String length             | Is a number | String is valid | Valid/Invalid | Description of the test case | JUnit test case                                              |
| ------------------------- | ----------- | --------------- | ------------- | ---------------------------- | ------------------------------------------------------------ |
| [12 ,14]                  | Yes         | Valid           | **Valid**     | T1("012345678905") -> true   | it.polito.ezshop.acceptanceTests.GtBarcodeValidatorTest.testIsValid |
| ""                        | ""          | Invalid         | Valid         | T2("333333333333") -> false  | it.polito.ezshop.acceptanceTests.GtBarcodeValidatorTest.testIsValid |
| ""                        | No          | -               | Valid         | T2("ciao") -> false          | it.polito.ezshop.acceptanceTests.GtBarcodeValidatorTest.testIsValid |
| [0, 11]  \|\|[15, MaxInt) | -           | -               | Valid         | T2("12") -> false            | it.polito.ezshop.acceptanceTests.GtBarcodeValidatorTest.testIsValid |
| null                      | -           | -               | Invalid       | T5(null) -> NullPointer      | it.polito.ezshop.acceptanceTests.GtBarcodeValidatorTest.testIsValid |


## Class LocationValidator


### Method isValid

Criteria for method **isValid**:

- String length
- String is valid

**Predicates for method isValid:**

| Criterion       | Predicate |
| --------------- | --------- |
| String length   | !=0       |
|                 | Empty     |
|                 | null      |
| String is valid | Valid     |
|                 | Invalid   |

**Boundaries for method isValid**:

| Criterion | Boundary values |
| --------- | --------------- |

 **Combination of predicates for method isValid**

| String length | String is valid | Valid/Invalid | Description of the test case | JUnit test case                                              |
| ------------- | --------------- | ------------- | ---------------------------- | ------------------------------------------------------------ |
| !0            | Valid           | Valid         | T1("1-a-1") -> true          | it.polito.ezshop.acceptanceTests.LocationValidatorTest.testIsValid |
| ""            | Invalid         | Valid         | T2("3131ad") - > false       | it.polito.ezshop.acceptanceTests.LocationValidatorTest.testIsValid |
| Empty         | -               | Valid         | T3("") -> true               | it.polito.ezshop.acceptanceTests.LocationValidatorTest.testIsValid |
| null          | -               | Valid         | T4(null) -> false            | it.polito.ezshop.acceptanceTests.LocationValidatorTest.testIsValid |




# White Box Unit Tests

### Test cases definition



### Method setBalanceId

| Unit name    | JUnit test case  |
| ------------ | ---------------- |
| setBalanceId | testSetBalanceId |

### Code coverage report



![](assets/WhiteBoxTesting/balanceId.png)



| Test case         | Node coverage | Node coverage cumulative | Edge coverage | Path |
| ----------------- | ------------- | ------------------------ | ------------- | ---- |
| setBalanceId("3") | 1             | 100%                     | -             | 1    |



### Loop coverage analysis

| Unit name | Loop rows | Number of iterations | JUnit test case |
| --------- | --------- | -------------------- | --------------- |
| -         | -         | -                    | -               |



### Method setDate

| Unit name | JUnit test case |
| --------- | --------------- |
| setDate   | testSetDate     |

### Code coverage report

![](assets/WhiteBoxTesting/date.png)



| Test case             | Node coverage | Node coverage cumulative | Edge coverage | Path |
| --------------------- | ------------- | ------------------------ | ------------- | ---- |
| setDate("2021-18-10") | 1             | 100%                     | -             | 1    |



### Loop coverage analysis

| Unit name | Loop rows | Number of iterations | JUnit test case |
| --------- | --------- | -------------------- | --------------- |
| -         | -         | -                    | -               |



### Method setMoney

| Unit name | JUnit test case |
| --------- | --------------- |
| setMoney  | testSetMoney    |

### Code coverage report

![](assets/WhiteBoxTesting/money.png)



| Test case       | Node coverage | Node coverage cumulative | Edge coverage | Path |
| --------------- | ------------- | ------------------------ | ------------- | ---- |
| setMoney("299") | 1             | 100%                     | -             | 1    |



### Loop coverage analysis

| Unit name | Loop rows | Number of iterations | JUnit test case |
| --------- | --------- | -------------------- | --------------- |
| -         | -         | -                    | -               |



### Method setType

| Unit name | JUnit test case |
| --------- | --------------- |
| setType   | testSetType     |

### Code coverage report

![](assets/WhiteBoxTesting/type.png)



| Test case         | Node coverage | Node coverage cumulative | Edge coverage | Path |
| ----------------- | ------------- | ------------------------ | ------------- | ---- |
| setType("CREDIT") | 1             | 100%                     | -             | 1    |



### Loop coverage analysis

| Unit name | Loop rows | Number of iterations | JUnit test case |
| --------- | --------- | -------------------- | --------------- |
| -         | -         | -                    | -               |



## Class Customer

### Method setCustomerName

| Unit name       | JUnit test case     |
| --------------- | ------------------- |
| setCustomerName | testSetCustomerName |

### Code coverage report

![](assets/WhiteBoxTesting/customerName.png)



| Test case                     | Node coverage | Node coverage cumulative | Edge coverage | Path |
| ----------------------------- | ------------- | ------------------------ | ------------- | ---- |
| setType("Hanamichi Sakuragi") | 1             | 100%                     | -             | 1    |



### Loop coverage analysis

| Unit name | Loop rows | Number of iterations | JUnit test case |
| --------- | --------- | -------------------- | --------------- |
| -         | -         | -                    | -               |



### Method setCustomerCard

| Unit name       | JUnit test case     |
| --------------- | ------------------- |
| setCustomerCard | testSetCustomerCard |

### Code coverage report

![](assets/WhiteBoxTesting/customerCard.png)



| Test case          | Node coverage | Node coverage cumulative | Edge coverage | Path |
| ------------------ | ------------- | ------------------------ | ------------- | ---- |
| setType("AB000CD") | 1             | 100%                     | -             | 1    |



### Loop coverage analysis

| Unit name | Loop rows | Number of iterations | JUnit test case |
| --------- | --------- | -------------------- | --------------- |
| -         | -         | -                    | -               |



### Method setId

| Unit name | JUnit test case |
| --------- | --------------- |
| setId     | testSetId       |

### Code coverage report

![](assets/WhiteBoxTesting/id.png)



| Test case  | Node coverage | Node coverage cumulative | Edge coverage | Path |
| ---------- | ------------- | ------------------------ | ------------- | ---- |
| setId("1") | 1             | 100%                     | -             | 1    |



### Loop coverage analysis

| Unit name | Loop rows | Number of iterations | JUnit test case |
| --------- | --------- | -------------------- | --------------- |
| -         | -         | -                    | -               |



### Method setPoints

| Unit name | JUnit test case |
| --------- | --------------- |
| setPoints | testSetPoints   |

### Code coverage report

![](assets/WhiteBoxTesting/points.png)



| Test case       | Node coverage | Node coverage cumulative | Edge coverage | Path |
| --------------- | ------------- | ------------------------ | ------------- | ---- |
| setPoints("43") | 1             | 100%                     | -             | 1    |



### Loop coverage analysis

| Unit name | Loop rows | Number of iterations | JUnit test case |
| --------- | --------- | -------------------- | --------------- |
| -         | -         | -                    | -               |



## Class Order

### 

### Method setBalanceId

| Unit name    | JUnit test case  |
| ------------ | ---------------- |
| setBalanceId | testSetBalanceId |

### Code coverage report



![](assets/WhiteBoxTesting/balanceId.png)



| Test case         | Node coverage | Node coverage cumulative | Edge coverage | Path |
| ----------------- | ------------- | ------------------------ | ------------- | ---- |
| setBalanceId("3") | 1             | 100%                     | -             | 1    |



### Loop coverage analysis

| Unit name | Loop rows | Number of iterations | JUnit test case |
| --------- | --------- | -------------------- | --------------- |
| -         | -         | -                    | -               |



### Method setProductCode

| Unit name      | JUnit test case    |
| -------------- | ------------------ |
| setProductCode | testSetProductCode |

### Code coverage report



![](assets/WhiteBoxTesting/productCode.png)



| Test case              | Node coverage | Node coverage cumulative | Edge coverage | Path |
| ---------------------- | ------------- | ------------------------ | ------------- | ---- |
| setProductCode("0003") | 1             | 100%                     | -             | 1    |



### Loop coverage analysis

| Unit name | Loop rows | Number of iterations | JUnit test case |
| --------- | --------- | -------------------- | --------------- |
| -         | -         | -                    | -               |



### Method setPricePerUnit

| Unit name       | JUnit test case     |
| --------------- | ------------------- |
| setPricePerUnit | testSetPricePerUnit |

### Code coverage report



![](assets/WhiteBoxTesting/priceperunit.png)



| Test case             | Node coverage | Node coverage cumulative | Edge coverage | Path |
| --------------------- | ------------- | ------------------------ | ------------- | ---- |
| setPricePerUnit("39") | 1             | 100%                     | -             | 1    |



### Loop coverage analysis

| Unit name | Loop rows | Number of iterations | JUnit test case |
| --------- | --------- | -------------------- | --------------- |
| -         | -         | -                    | -               |



### Method setQuantity

| Unit name   | JUnit test case |
| ----------- | --------------- |
| setQuantity | testSetQuantity |

### Code coverage report



![](assets/WhiteBoxTesting/quantity.png)



| Test case          | Node coverage | Node coverage cumulative | Edge coverage | Path |
| ------------------ | ------------- | ------------------------ | ------------- | ---- |
| setQuantity("100") | 1             | 100%                     | -             | 1    |



### Loop coverage analysis

| Unit name | Loop rows | Number of iterations | JUnit test case |
| --------- | --------- | -------------------- | --------------- |
| -         | -         | -                    | -               |



### Method setStatus

| Unit name | JUnit test case |
| --------- | --------------- |
| setStatus | testSetStatus   |

### Code coverage report



![](assets/WhiteBoxTesting/status.png)



| Test case                | Node coverage | Node coverage cumulative | Edge coverage | Path |
| ------------------------ | ------------- | ------------------------ | ------------- | ---- |
| setStatus("validStatus") | 1             | 100%                     | -             | 1    |



### Loop coverage analysis

| Unit name | Loop rows | Number of iterations | JUnit test case |
| --------- | --------- | -------------------- | --------------- |
| -         | -         | -                    | -               |



### Method setOrderId

| Unit name  | JUnit test case |
| ---------- | --------------- |
| setOrderId | testSetOrderId  |

### Code coverage report



![](assets/WhiteBoxTesting/orderId.png)



| Test case          | Node coverage | Node coverage cumulative | Edge coverage | Path |
| ------------------ | ------------- | ------------------------ | ------------- | ---- |
| setOrderId("0003") | 1             | 100%                     | -             | 1    |



### Loop coverage analysis

| Unit name | Loop rows | Number of iterations | JUnit test case |
| --------- | --------- | -------------------- | --------------- |
| -         | -         | -                    | -               |



## Class ProductType



### Method setQuantity

| Unit name   | JUnit test case |
| ----------- | --------------- |
| setQuantity | testSetQuantity |

### Code coverage report



![](assets/WhiteBoxTesting/quantity.png)



| Test case          | Node coverage | Node coverage cumulative | Edge coverage | Path |
| ------------------ | ------------- | ------------------------ | ------------- | ---- |
| setQuantity("100") | 1             | 100%                     | -             | 1    |



### Loop coverage analysis

| Unit name | Loop rows | Number of iterations | JUnit test case |
| --------- | --------- | -------------------- | --------------- |
| -         | -         | -                    | -               |



### Method setLocation

| Unit name   | JUnit test case |
| ----------- | --------------- |
| setLocation | testSetLocation |

### Code coverage report



![](assets/WhiteBoxTesting/location.png)



| Test case             | Node coverage | Node coverage cumulative | Edge coverage | Path |
| --------------------- | ------------- | ------------------------ | ------------- | ---- |
| setLocation("3-a-12") | 1             | 100%                     | -             | 1    |



### Loop coverage analysis

| Unit name | Loop rows | Number of iterations | JUnit test case |
| --------- | --------- | -------------------- | --------------- |
| -         | -         | -                    | -               |



### Method setProductDescription

| Unit name             | JUnit test case           |
| --------------------- | ------------------------- |
| setProductDescription | testSetProductDescription |

### Code coverage report



![](assets/WhiteBoxTesting/productDescription.png)



| Test case                             | Node coverage | Node coverage cumulative | Edge coverage | Path |
| ------------------------------------- | ------------- | ------------------------ | ------------- | ---- |
| setProductDescription("blue t-shirt") | 1             | 100%                     | -             | 1    |



### Loop coverage analysis

| Unit name | Loop rows | Number of iterations | JUnit test case |
| --------- | --------- | -------------------- | --------------- |
| -         | -         | -                    | -               |



### Method setBarCode

| Unit name  | JUnit test case |
| ---------- | --------------- |
| setBarCode | testSetBarCode  |

### Code coverage report



![](assets/WhiteBoxTesting/barCode.png)



| Test case              | Node coverage | Node coverage cumulative | Edge coverage | Path |
| ---------------------- | ------------- | ------------------------ | ------------- | ---- |
| setBarCode("13148419") | 1             | 100%                     | -             | 1    |



### Loop coverage analysis

| Unit name | Loop rows | Number of iterations | JUnit test case |
| --------- | --------- | -------------------- | --------------- |
| -         | -         | -                    | -               |



### Method setPricePerUnit

| Unit name       | JUnit test case     |
| --------------- | ------------------- |
| setPricePerUnit | testSetPricePerUnit |

### Code coverage report



![](assets/WhiteBoxTesting/priceperunit.png)



| Test case             | Node coverage | Node coverage cumulative | Edge coverage | Path |
| --------------------- | ------------- | ------------------------ | ------------- | ---- |
| setPricePerUnit("39") | 1             | 100%                     | -             | 1    |



### Loop coverage analysis

| Unit name | Loop rows | Number of iterations | JUnit test case |
| --------- | --------- | -------------------- | --------------- |
| -         | -         | -                    | -               |



### Method setId

| Unit name | JUnit test case |
| --------- | --------------- |
| setId     | testSetId       |

### Code coverage report

![desktop/money](assets/WhiteBoxTesting/id.png)



| Test case    | Node coverage | Node coverage cumulative | Edge coverage | Path |
| ------------ | ------------- | ------------------------ | ------------- | ---- |
| setId("001") | 1             | 100%                     | -             | 1    |



### Loop coverage analysis

| Unit name | Loop rows | Number of iterations | JUnit test case |
| --------- | --------- | -------------------- | --------------- |
| -         | -         | -                    | -               |



## Class SaleTransaction



### Method setTicketNumber

| Unit name       | JUnit test case     |
| --------------- | ------------------- |
| setTicketNumber | testSetTicketNumber |

### Code coverage report



![](assets/WhiteBoxTesting/ticketnumber.png)



| Test case             | Node coverage | Node coverage cumulative | Edge coverage | Path |
| --------------------- | ------------- | ------------------------ | ------------- | ---- |
| setTicketNumber("10") | 1             | 100%                     | -             | 1    |



### Loop coverage analysis

| Unit name | Loop rows | Number of iterations | JUnit test case |
| --------- | --------- | -------------------- | --------------- |
| -         | -         | -                    | -               |



### Method setEntries //void -> da cambiare

| Unit name  | JUnit test case |
| ---------- | --------------- |
| setEntries | testSetEntries  |

### Code coverage report



![](assets/WhiteBoxTesting/entries.png)



| Test case              | Node coverage | Node coverage cumulative | Edge coverage | Path |
| ---------------------- | ------------- | ------------------------ | ------------- | ---- |
| setEntries("FilippoS") | 1             | 100%                     | -             | 1    |



### Loop coverage analysis

| Unit name | Loop rows | Number of iterations | JUnit test case |
| --------- | --------- | -------------------- | --------------- |
| -         | -         | -                    | -               |



### Method setDiscountRate    //void -> da cambiare

| Unit name       | JUnit test case     |
| --------------- | ------------------- |
| setDiscountRate | testSetDiscountRate |

### Code coverage report



![](assets/WhiteBoxTesting/discountrate.png)



| Test case              | Node coverage | Node coverage cumulative | Edge coverage | Path |
| ---------------------- | ------------- | ------------------------ | ------------- | ---- |
| setDiscountRate("0.5") | 1             | 100%                     | -             | 1    |



### Loop coverage analysis

| Unit name | Loop rows | Number of iterations | JUnit test case |
| --------- | --------- | -------------------- | --------------- |
| -         | -         | -                    | -               |



### Method setPrice

| Unit name | JUnit test case |
| --------- | --------------- |
| setPrice  | testSetPrice    |

### Code coverage report



![](assets/WhiteBoxTesting/price.png)



| Test case      | Node coverage | Node coverage cumulative | Edge coverage | Path |
| -------------- | ------------- | ------------------------ | ------------- | ---- |
| setPrice("29") | 1             | 100%                     | -             | 1    |



### Loop coverage analysis

| Unit name | Loop rows | Number of iterations | JUnit test case |
| --------- | --------- | -------------------- | --------------- |
| -         | -         | -                    | -               |



## Class TicketEntry

### Method setBarCode

| Unit name  | JUnit test case |
| ---------- | --------------- |
| setBarCode | testSetBarCode  |

### Code coverage report



![](assets/WhiteBoxTesting/barcode.png)



| Test case              | Node coverage | Node coverage cumulative | Edge coverage | Path |
| ---------------------- | ------------- | ------------------------ | ------------- | ---- |
| setBarCode("13148419") | 1             | 100%                     | -             | 1    |



### Loop coverage analysis

| Unit name | Loop rows | Number of iterations | JUnit test case |
| --------- | --------- | -------------------- | --------------- |
| -         | -         | -                    | -               |



### Method setProductDescription

| Unit name             | JUnit test case           |
| --------------------- | ------------------------- |
| setProductDescription | testSetProductDescription |

### Code coverage report



![](assets/WhiteBoxTesting/productdescription.png)



| Test case                             | Node coverage | Node coverage cumulative | Edge coverage | Path |
| ------------------------------------- | ------------- | ------------------------ | ------------- | ---- |
| setProductDescription("blue t-shirt") | 1             | 100%                     | -             | 1    |



### Loop coverage analysis

| Unit name | Loop rows | Number of iterations | JUnit test case |
| --------- | --------- | -------------------- | --------------- |
| -         | -         | -                    | -               |



### Method setAmount

| Unit name | JUnit test case |
| --------- | --------------- |
| setAmount | testSetAmount   |

### Code coverage report



![](assets/WhiteBoxTesting/amount.png)



| Test case        | Node coverage | Node coverage cumulative | Edge coverage | Path |
| ---------------- | ------------- | ------------------------ | ------------- | ---- |
| setAmount("100") | 1             | 100%                     | -             | 1    |



### Loop coverage analysis

| Unit name | Loop rows | Number of iterations | JUnit test case |
| --------- | --------- | -------------------- | --------------- |
| -         | -         | -                    | -               |



### Method setPricePerUnit

| Unit name       | JUnit test case     |
| --------------- | ------------------- |
| setPricePerUnit | testSetPricePerUnit |

### Code coverage report



![](assets/WhiteBoxTesting/priceperunit.png)



| Test case             | Node coverage | Node coverage cumulative | Edge coverage | Path |
| --------------------- | ------------- | ------------------------ | ------------- | ---- |
| setPricePerUnit("39") | 1             | 100%                     | -             | 1    |



### Loop coverage analysis

| Unit name | Loop rows | Number of iterations | JUnit test case |
| --------- | --------- | -------------------- | --------------- |
| -         | -         | -                    | -               |



### Method setDiscountRate

| Unit name       | JUnit test case     |
| --------------- | ------------------- |
| setDiscountRate | testSetDiscountRate |

### Code coverage report



![](assets/WhiteBoxTesting/discountrate.png)



| Test case              | Node coverage | Node coverage cumulative | Edge coverage | Path |
| ---------------------- | ------------- | ------------------------ | ------------- | ---- |
| setDiscountRate("0.5") | 1             | 100%                     | -             | 1    |



### Loop coverage analysis

| Unit name | Loop rows | Number of iterations | JUnit test case |
| --------- | --------- | -------------------- | --------------- |
| -         | -         | -                    | -               |



## Class User



### Method setId

| Unit name | JUnit test case |
| --------- | --------------- |
| setId     | testSetId       |

### Code coverage report

![](assets/WhiteBoxTesting/id.png)



| Test case  | Node coverage | Node coverage cumulative | Edge coverage | Path |
| ---------- | ------------- | ------------------------ | ------------- | ---- |
| setId("1") | 1             | 100%                     | -             | 1    |



### Loop coverage analysis

| Unit name | Loop rows | Number of iterations | JUnit test case |
| --------- | --------- | -------------------- | --------------- |
| -         | -         | -                    | -               |

### Method setUsername

| Unit name   | JUnit test case |
| ----------- | --------------- |
| setUsername | testSetUsername |

### Code coverage report

![](assets/WhiteBoxTesting/username.png)



| Test case               | Node coverage | Node coverage cumulative | Edge coverage | Path |
| ----------------------- | ------------- | ------------------------ | ------------- | ---- |
| setUsername("AlbertoN") | 1             | 100%                     | -             | 1    |



### Loop coverage analysis

| Unit name | Loop rows | Number of iterations | JUnit test case |
| --------- | --------- | -------------------- | --------------- |
| -         | -         | -                    | -               |



### Method setPassword

| Unit name   | JUnit test case |
| ----------- | --------------- |
| setPassword | testSetPassword |

### Code coverage report

![](assets/WhiteBoxTesting/password.png)



| Test case                     | Node coverage | Node coverage cumulative | Edge coverage | Path |
| ----------------------------- | ------------- | ------------------------ | ------------- | ---- |
| setPassword("Fe29dqh^ad3_ad") | 1             | 100%                     | -             | 1    |



### Loop coverage analysis

| Unit name | Loop rows | Number of iterations | JUnit test case |
| --------- | --------- | -------------------- | --------------- |
| -         | -         | -                    | -               |



### Method setRole

| Unit name | JUnit test case |
| --------- | --------------- |
| setRole   | testSetRole     |

### Code coverage report

![](assets/WhiteBoxTesting/role.png)



| Test case          | Node coverage | Node coverage cumulative | Edge coverage | Path |
| ------------------ | ------------- | ------------------------ | ------------- | ---- |
| setRole("Cashier") | 1             | 100%                     | -             | 1    |



### Loop coverage analysis

| Unit name | Loop rows | Number of iterations | JUnit test case |
| --------- | --------- | -------------------- | --------------- |
| -         | -         | -                    | -               |

## Class DiscountRateValidator

### Method isValid

| Unit name | JUnit test case |
| --------- | --------------- |
| isValid   | testIsValid     |

### Code coverage report

![](assets/WhiteBoxTesting/DiscountValidator.png)



| Test case     | Node coverage | Node coverage cumulative | Edge coverage | Path | Number of test cases needed to cover all nodes |
| ------------- | ------------- | ------------------------ | ------------- | ---- | ---------------------------------------------- |
| isValid("0d") | 1-2           | 66%                      | 1-2           | 1    | -                                              |
| isValid("1")  | 1-3           | 100%                     | 1-3           | 1    | -                                              |
| **total**     | 1-2-3         | 100%                     | 100%          | 2    | 2                                              |



### Loop coverage analysis

| Unit name | Loop rows | Number of iterations | JUnit test case |
| --------- | --------- | -------------------- | --------------- |
| -         | -         | -                    | -               |



## Class GtinBarcodeValidator

### Method isValid

| Unit name | JUnit test case |
| --------- | --------------- |
| isValid   | testIsValid     |

### Code coverage report

![](assets/WhiteBoxTesting/GtinBarcodeValidator.png)



| Test case               | Node coverage | Node coverage cumulative | Edge coverage | Path | Number of test cases needed to cover all nodes |
| ----------------------- | ------------- | ------------------------ | ------------- | ---- | ---------------------------------------------- |
| isValid("^\\d{12,14}$") | 1-2           | 33%                      | 1-2           | 1    | -                                              |
| isValid("...")          | 1-3-4-6       | 66%                      | 1-3 3-4 4-6   | 1    | -                                              |
| isValid                 | 1-3-4-5       | 100%                     | 1-3 3-4 4-5   | 1    |                                                |
| **total**               | 1-2-3-4-5-6   | 100%                     | 100%          | 3    | 3                                              |



### Loop coverage analysis

| Unit name | Loop rows | Number of iterations | JUnit test case |
| --------- | --------- | -------------------- | --------------- |
| -         | -         | -                    | -               |



## Class LocationValidator

### Method isValid

| Unit name | JUnit test case |
| --------- | --------------- |
| isValid   | testIsValid     |

### Code coverage report

![](assets/WhiteBoxTesting/locationValidator.png)



| Test case                        | Node coverage | Node coverage cumulative | Edge coverage | Path | Number of test cases needed to cover all nodes |
| -------------------------------- | ------------- | ------------------------ | ------------- | ---- | ---------------------------------------------- |
| isValid("null")<br />isValid("") | 1-2           | 66%                      | 1-2           | 1    | -                                              |
| isValid("...")                   | 1-3           | 100%                     | 1-3           | 1    | -                                              |
| **total**                        | 1-2-3         | 100%                     | 100%          | 2    | 2                                              |



### Loop coverage analysis

| Unit name | Loop rows | Number of iterations | JUnit test case |
| --------- | --------- | -------------------- | --------------- |
| -         | -         | -                    | -               |

