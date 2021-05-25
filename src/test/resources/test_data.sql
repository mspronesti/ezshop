INSERT INTO "BalanceOperationImpl" ("id", "date", "money", "type") VALUES ('10', '1621893600000', '500.0', 'CREDIT'), ('11', '1621893600000', '-200.0', 'DEBIT'), ('18', '1621893600000', '134.0', 'CREDIT'), ('20', '1621893600000', '0.924', 'CREDIT'), ('22', '1621893600000', '4.62', 'CREDIT'), ('24', '1621893600000', '19.95', 'CREDIT'), ('26', '1621893600000', '10.5', 'CREDIT'), ('28', '1621893600000', '10.5', 'CREDIT'), ('30', '1621893600000', '1.05', 'DEBIT'), ('32', '1621893600000', '1.05', 'DEBIT'), ('34', '1621893600000', '12.6', 'DEBIT'), ('36', '1621893600000', '0.924', 'DEBIT');

INSERT INTO "CustomerImpl" ("id", "name", "loyaltyCard_id") VALUES ('3', 'Marcello', '4017008113'), ('4', 'Fabiana', NULL), ('5', 'Giovanni', '3055345153'), ('16', 'Simone', '8819278768'), ('17', 'Gianni', NULL);

INSERT INTO "hibernate_sequence" ("next_val") VALUES ('37');

INSERT INTO "LoyaltyCardImpl" ("id", "points") VALUES ('3055345153', '0'), ('4017008113', '12'), ('8819278768', '24');

INSERT INTO "OrderImpl" ("id", "balanceId", "pricePerUnit", "productCode", "quantity", "status") VALUES ('6', NULL, '0.51', '012345678905', '15', 'ISSUED'), ('7', '11', '10.0', '012345678912', '20', 'PAYED'), ('14', NULL, '0.88', '012345678905', '12', 'ISSUED'), ('15', NULL, '0.99', '012345678905', '2', 'ISSUED');

INSERT INTO "ProductTypeImpl" ("id", "barcode", "description", "note", "aisleID", "levelID", "rackID", "pricePerUnit", "quantity") VALUES ('1', '012345678905', 'Bread', '', '12', '12', 'a', '1.05', '69'), ('2', '012345678912', 'T-shirt', '', NULL, NULL, NULL, '12.99', '0'), ('12', '012345678929', 'Glasses', '', NULL, NULL, NULL, '12.69', '0'), ('13', '012345678936', 'Paper', '', NULL, NULL, NULL, '0.99', '0');

INSERT INTO "SaleTransactionImpl" ("DTYPE", "id", "discountRate", "price", "payment_id", "saleTransaction_id") VALUES ('SaleTransactionImpl', '19', '0.12', '0.924', '20', NULL), ('SaleTransactionImpl', '21', '0.0', '8.316', NULL, NULL), ('SaleTransactionImpl', '23', '0.0', '6.3', '24', NULL), ('SaleTransactionImpl', '25', '0.0', '10.5', '26', NULL), ('SaleTransactionImpl', '27', '0.0', '10.5', '28', NULL), ('ReturnTransactionImpl', '29', '0.0', '1.05', '30', '23'), ('ReturnTransactionImpl', '31', '0.0', '1.05', '32', '19'), ('ReturnTransactionImpl', '33', '0.0', '12.6', '34', '23'), ('ReturnTransactionImpl', '35', '0.0', '0.924', '36', '21');

INSERT INTO "SaleTransactionImpl_entries" ("SaleTransactionImpl_id", "amount", "barcode", "discountRate", "pricePerUnit", "productDescription") VALUES ('25', '10', '012345678905', '0.0', '1.05', ''), ('27', '10', '012345678905', '0.0', '1.05', ''), ('29', '1', '012345678905', '0.0', '1.05', ''), ('19', '1', '012345678905', '0.0', '1.05', ''), ('31', '1', '012345678905', '0.0', '1.05', ''), ('23', '6', '012345678905', '0.0', '1.05', ''), ('33', '12', '012345678905', '0.0', '1.05', ''), ('21', '4', '012345678905', '0.12', '1.05', ''), ('21', '5', '012345678905', '0.12', '1.05', ''), ('35', '1', '012345678905', '0.12', '1.05', '');

INSERT INTO "UserImpl" ("id", "password", "role", "username") VALUES ('1', '$2a$10$qVR41JrVpgNqCysm3QTJneTUz7sZgCzfZCyfSPiIbb6x1DO8mvnJe', 'Administrator', 'Marco'), ('2', '$2a$10$qP93/vZ6ERDLPk0r3pSAsebnVoVJWUCNLBmL2.8U.dCSfbuWxqzMG', 'ShopManager', 'Anna'), ('3', '$2a$10$Mp6s1ENeW9iL2exxzLYJHuRfPmZ5w2B71QWyLYGgFcK.KNUqE0q76', 'Cashier', 'Franco'), ('4', '$2a$10$HKY4oI5Yp.F30mjMWQKVfeeLEs6ExyhWzDryU2CJOJGxQMX4hV24u', 'Cashier', 'Giovanna'), ('5', '$2a$10$tVcWPN/uRFxQ3JEU50/1w.IJG5ozrtTRCTsTQYDuV5x.z4dwVlaqS', 'ShopManager', 'Beatrice');

