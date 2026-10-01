package com.joysistvi.CyberAccess.controller;

import com.joysistvi.CyberAccess.cliview.PaymentsView;
import java.math.BigDecimal;
import java.util.List;

public class PaymentsController {

  private final PaymentsView paymentsView;

    public PaymentsController() {
        this.paymentsView = new PaymentsView();
    }

    public PaymentsView getPaymentsView() {
        return paymentsView;
    }

    public PaymentsView.Payment addPayment(
            int transactionId,
            int userId,
            BigDecimal amount,
            String paymentMethod,
            String status) {

        return paymentsView.addPayment(
                transactionId,
                userId,
                amount,
                paymentMethod,
                status
        );
    }

    public boolean updatePayment(
            int id,
            int transactionId,
            int userId,
            BigDecimal amount,
            String paymentMethod,
            String status) {

        return paymentsView.updatePayment(
                id,
                transactionId,
                userId,
                amount,
                paymentMethod,
                status
        );
    }

    public boolean deletePayment(int id) {
        return paymentsView.deletePayment(id);
    }

    public PaymentsView.Payment getPaymentById(int id) {
        return paymentsView.getPaymentById(id);
    }

    public List<PaymentsView.Payment> getAllPayments() {
        return paymentsView.getAllPayments();
    }

    public static void main(String[] args) {
        PaymentsController controller = new PaymentsController();

        controller.addPayment(
                101,
                1,
                new BigDecimal("250.00"),
                "Cash",
                "Completed"
        );

        for (PaymentsView.Payment payment : controller.getAllPayments()) {
            System.out.println(payment);
        }
    }
}
