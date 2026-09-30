package com.joysistvi.CyberAccess.model;

import java.math.BigDecimal;
import java.math.RoundingMode;

// Holds one charge on a bill: quantity, unit and price
public record TransactionItem (int id, int transactionId, Integer serviceId,
                               String description, BigDecimal quantity,
                               String unit, BigDecimal unitPrice) {
    // Subtotal = quantity x price, rounded to 2 decimal places.
    public BigDecimal subtotal() {
        return quantity.multiply(unitPrice).setScale(2, RoundingMode.HALF_UP);
    }
}

