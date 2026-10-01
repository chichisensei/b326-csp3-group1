package com.joysistvi.CyberAccess.service;

import com.joysistvi.CyberAccess.model.Payments;
import com.joysistvi.CyberAccess.repo.PaymentsRepo;
import com.joysistvi.CyberAccess.repo.PaymentsRepoImpl;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class PaymentsServiceImpl {

  private final PaymentsRepo paymentsRepo;

    public PaymentsServiceImpl() {
        this.paymentsRepo = new PaymentsRepoImpl();
    }

    public PaymentsServiceImpl(PaymentsRepo paymentsRepo) {
        this.paymentsRepo = paymentsRepo;
    }

    // Retrieve all payments
    public List<Payments> getAllPayments() throws SQLException {
        return paymentsRepo.findAll();
    }

    // Retrieve a payment by ID
    public Optional<Payments> getPaymentById(int id) throws SQLException {
        if (id <= 0) {
            throw new IllegalArgumentException(
                    "Payment ID must be greater than zero."
            );
        }

        return paymentsRepo.findById(id);
    }

    // Retrieve payments by transaction ID
    public List<Payments> getPaymentsByTransactionId(int transactionId)
            throws SQLException {
        if (transactionId <= 0) {
            throw new IllegalArgumentException(
                    "Transaction ID must be greater than zero."
            );
        }

        return paymentsRepo.findByTransactionId(transactionId);
    }

    // Retrieve payments by user ID
    public List<Payments> getPaymentsByUserId(int userId)
            throws SQLException {
        if (userId <= 0) {
            throw new IllegalArgumentException(
                    "User ID must be greater than zero."
            );
        }

        return paymentsRepo.findByUserId(userId);
    }

    // Retrieve payments by status
    public List<Payments> getPaymentsByStatus(String status)
            throws SQLException {
        if (status == null || status.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "Payment status is required."
            );
        }

        return paymentsRepo.findByStatus(status.trim());
    }

    // Check if a payment exists
    public boolean paymentExists(int id) throws SQLException {
        if (id <= 0) {
            throw new IllegalArgumentException(
                    "Payment ID must be greater than zero."
            );
        }

        return paymentsRepo.existsById(id);
    }

    // Create a payment
    public void addPayment(Payments payment) throws SQLException {
        validatePayment(payment);
        paymentsRepo.save(payment);
    }

    // Update a payment
    public boolean updatePayment(Payments payment) throws SQLException {
        validatePayment(payment);

        if (payment.getId() <= 0) {
            throw new IllegalArgumentException(
                    "Payment ID must be greater than zero."
            );
        }

        return paymentsRepo.update(payment);
    }

    // Update payment status
    public boolean updatePaymentStatus(int id, String status)
            throws SQLException {
        if (id <= 0) {
            throw new IllegalArgumentException(
                    "Payment ID must be greater than zero."
            );
        }

        if (status == null || status.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "Payment status is required."
            );
        }

        return paymentsRepo.updateStatus(id, status.trim());
    }

    // Delete a payment
    public boolean deletePayment(int id) throws SQLException {
        if (id <= 0) {
            throw new IllegalArgumentException(
                    "Payment ID must be greater than zero."
            );
        }

        return paymentsRepo.delete(id);
    }

    // Validate payment details
    private void validatePayment(Payments payment) {
        if (payment == null) {
            throw new IllegalArgumentException(
                    "Payment information cannot be null."
            );
        }

        if (payment.getTransactionId() <= 0) {
            throw new IllegalArgumentException(
                    "Transaction ID must be greater than zero."
            );
        }

        if (payment.getUserId() <= 0) {
            throw new IllegalArgumentException(
                    "User ID must be greater than zero."
            );
        }

        if (payment.getAmount() == null
                || payment.getAmount().signum() <= 0) {
            throw new IllegalArgumentException(
                    "Payment amount must be greater than zero."
            );
        }

        if (payment.getPaymentMethod() == null
                || payment.getPaymentMethod().trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "Payment method is required."
            );
        }

        if (payment.getStatus() == null
                || payment.getStatus().trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "Payment status is required."
            );
        }
    }
}
