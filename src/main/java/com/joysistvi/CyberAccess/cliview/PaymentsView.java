package com.joysistvi.CyberAccess.cliview;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class PaymentsView {

   private final List<Payment> payments = new ArrayList<>();
    private int nextId = 1;

    public static class Payment {
        private int id;
        private int transactionId;
        private int userId;
        private BigDecimal amount;
        private String paymentMethod;
        private LocalDateTime paymentDate;
        private String status;

        public Payment(int id, int transactionId, int userId,
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

        public int getId() { return id; }
        public int getTransactionId() { return transactionId; }
        public int getUserId() { return userId; }
        public BigDecimal getAmount() { return amount; }
        public String getPaymentMethod() { return paymentMethod; }
        public LocalDateTime getPaymentDate() { return paymentDate; }
        public String getStatus() { return status; }

        @Override
        public String toString() {
            return "Payment ID: " + id
                    + " | Transaction ID: " + transactionId
                    + " | User ID: " + userId
                    + " | Amount: PHP " + amount
                    + " | Method: " + paymentMethod
                    + " | Date: " + paymentDate
                    + " | Status: " + status;
        }
    }

    public Payment addPayment(int transactionId, int userId,
                              BigDecimal amount, String paymentMethod,
                              String status) {
        validatePayment(transactionId, userId, amount, paymentMethod, status);

        Payment payment = new Payment(
                nextId++,
                transactionId,
                userId,
                amount,
                paymentMethod,
                LocalDateTime.now(),
                status
        );

        payments.add(payment);
        return payment;
    }

    public boolean updatePayment(int id, int transactionId, int userId,
                                 BigDecimal amount, String paymentMethod,
                                 String status) {
        validatePayment(transactionId, userId, amount, paymentMethod, status);

        for (Payment payment : payments) {
            if (payment.getId() == id) {
                payment.transactionId = transactionId;
                payment.userId = userId;
                payment.amount = amount;
                payment.paymentMethod = paymentMethod;
                payment.status = status;
                return true;
            }
        }

        return false;
    }

    public boolean deletePayment(int id) {
        return payments.removeIf(payment -> payment.getId() == id);
    }

    public Payment getPaymentById(int id) {
        for (Payment payment : payments) {
            if (payment.getId() == id) {
                return payment;
            }
        }

        return null;
    }

    public List<Payment> getAllPayments() {
        return new ArrayList<>(payments);
    }

    private void validatePayment(int transactionId, int userId,
                                 BigDecimal amount, String paymentMethod,
                                 String status) {
        if (transactionId <= 0 || userId <= 0) {
            throw new IllegalArgumentException(
                    "Transaction ID and User ID must be positive."
            );
        }

        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException(
                    "Payment amount must be greater than zero."
            );
        }

        if (paymentMethod == null || paymentMethod.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "Payment method is required."
            );
        }

        if (status == null || status.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "Payment status is required."
            );
        }
    }

    public static void main(String[] args) {
        PaymentsView paymentsView = new PaymentsView();

        paymentsView.addPayment(
                101,
                1,
                new BigDecimal("250.00"),
                "Cash",
                "Completed"
        );

        for (Payment payment : paymentsView.getAllPayments()) {
            System.out.println(payment);
        }
    }
  
}

