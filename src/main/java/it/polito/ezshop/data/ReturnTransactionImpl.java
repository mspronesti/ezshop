package it.polito.ezshop.data;

import javax.persistence.Entity;
import javax.persistence.OneToOne;

@Entity
public class ReturnTransactionImpl extends SaleTransactionImpl {
    @OneToOne
    private SaleTransactionImpl saleTransaction;

    public SaleTransactionImpl getSaleTransaction() {
        return saleTransaction;
    }

    public void setSaleTransaction(SaleTransactionImpl saleTransaction) {
        this.saleTransaction = saleTransaction;
    }
}
