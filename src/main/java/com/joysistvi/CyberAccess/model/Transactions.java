package com.joysistvi.CyberAccess.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

// Holds the details of one transaction.
public record Transactions(
        int id,
        int userId,
        LocalDateTime createdAt,
        String status,
        BigDecimal total
) {}