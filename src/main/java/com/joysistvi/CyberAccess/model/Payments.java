package com.joysistvi.CyberAccess.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Payments {

  private int id;
    private int transactionId;
    private int userId;
    private BigDecimal amount;
    private String paymentMethod;
    private LocalDateTime paymentDate;
    private String status;

    // Default constructor
    public Payments() {
        this.paymentDate = LocalDateTime.now();
        this.status = "Pending";
    }

    // Constructor
    public Payments(int id, int transactionId, int userId,
                    BigDecimal amount, String paymentMethod,
                    LocalDateTime paymentDate, String status) {
        this.id = id;
        this.transactionId = transactionId;
        this.userId = userId;
        this.amount = amount;
        this.paymentMethod = paymentMethod;
        this.paymentDate = paymentDate;
        this.status = status;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(int transactionId) {
        this.transactionId = transactionId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public LocalDateTime getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(LocalDateTime paymentDate) {
        this.paymentDate = paymentDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Payments{" +
                "id=" + id +
                ", transactionId=" + transactionId +
                ", userId=" + userId +
                ", amount=" + amount +
                ", paymentMethod='" + paymentMethod + '\'' +
                ", paymentDate=" + paymentDate +
                ", status='" + status + '\'' +
                '}';
    }
}
