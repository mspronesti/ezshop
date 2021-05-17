package it.polito.ezshop.data;

import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

public class ReturnTransactionImpl extends SaleTransactionImpl {
    @OneToOne
    @JoinColumn(name = "saleTransaction_id")
    private SaleTransactionImpl saleTransaction;

    public SaleTransactionImpl getSaleTransaction() {
        return saleTransaction;
    }

    public void setSaleTransaction(SaleTransactionImpl saleTransaction) {
        this.saleTransaction = saleTransaction;
    }
}
