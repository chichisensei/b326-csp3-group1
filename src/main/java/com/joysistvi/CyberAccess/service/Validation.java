package com.joysistvi.CyberAccess.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Set;

// Checks input before it is sent to the database
public class Validation {
    private Validation() {}
    // Database IDs must be positive whole numbers.
    public static int id(int value) {
        if (value <= 0) throw new IllegalArgumentException("ID must be positive.");
        return value;
    }
    // Reject blank or overly long text, then remove surrounding spaces.
    public static String text(String value, String label, int max) {
        if (value == null || value.trim().isEmpty())
            throw new IllegalArgumentException(label + " is required.");
        value = value.trim();
        if (value.length() > max) throw new IllegalArgumentException(label + " is too long (max " + max + ").");
        return value;
    }
    public static String status(String value, Set<String> allowed) {
        if (!allowed.contains(value)) throw new IllegalArgumentException("Choose one of: " + allowed);
        return value;
    }
    // Check the sign, decimal places and database size limit.
    public static BigDecimal decimal(BigDecimal value, String label, boolean positive) {
        if (value == null || (positive ? value.signum() <= 0 : value.signum() < 0))
            throw new IllegalArgumentException(label + (positive ? " must be greater than zero." : " cannot be negative."));
        try { value = value.setScale(2, RoundingMode.UNNECESSARY); }
        catch (ArithmeticException e) { throw new IllegalArgumentException(label + " allows at most 2 decimal places."); }
        if (value.compareTo(new BigDecimal("99999999.99")) > 0)
            throw new IllegalArgumentException(label + " exceeds DECIMAL(10,2).");
        return value;
    }
}
