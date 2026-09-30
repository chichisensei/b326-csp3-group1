package com.joysistvi.CyberAccess.model;

import java.math.BigDecimal;
import java.util.List;

// Holds the transaction and its list of charges
public record Bill (Transactions transaction, List<TransactionItem> items) {

    // Keep a fixed copy of the charges.
    public Bill {
        items = List.copyOf(items);
    }

    // Add all charge subtotals.
    public BigDecimal total() {
        return items.stream()
                .map(TransactionItem::subtotal)
                .reduce(new BigDecimal("0.00"), BigDecimal::add);
    }
}
