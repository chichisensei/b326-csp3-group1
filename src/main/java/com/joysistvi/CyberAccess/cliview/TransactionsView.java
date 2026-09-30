package com.joysistvi.CyberAccess.cliview;

import com.joysistvi.CyberAccess.controller.TransactionsController;
import java.math.BigDecimal;
import java.sql.SQLException;

// Shows the transaction menu, history and charge options
public class TransactionsView {
    private final TransactionsController controller;
    private final BillView bills;
    private final ServicesView services;
    private final ConsoleInput input;
    public TransactionsView(TransactionsController controller, BillView bills, ServicesView services, ConsoleInput input) {
        this.controller = controller; this.bills = bills; this.services = services; this.input = input;
    }
    public void show() {
        while (true) {
            System.out.println("\nTRANSACTIONS: 1 Create | 2 History | 3 View bill | 4 Add charge | 5 Add service");
            System.out.println("6 Remove charge | 7 Close bill | 8 Cancel transaction | 9 Add saved rate | 0 Back");
            try {
                switch (input.number("Choose: ")) {
                    case 0 -> { return; }
                    case 1 -> System.out.println("Created transaction #" + controller.create(input.number("Customer/user ID: ")));
                    case 2 -> history();
                    case 3 -> bills.show(input.number("Transaction ID: "));
                    case 4 -> addCharge(false);
                    case 5 -> addCharge(true);
                    case 6 -> {
                        int id = input.number("Transaction ID: ");
                        bills.show(id);
                        int item = input.number("Charge/item ID to remove: ");
                        if (input.confirm("Remove this charge")) { controller.removeItem(id, item); System.out.println("Charge removed."); }
                    }
                    case 7 -> {
                        int id = input.number("Transaction ID: "); bills.show(id);
                        if (input.confirm("Finalize this bill and stop further edits (does not record payment)")) {
                            controller.close(id); System.out.println("Bill closed.");
                        }
                    }
                    case 8 -> {
                        int id = input.number("Transaction ID: ");
                        if (input.confirm("Cancel this open transaction and keep it in history")) {
                            controller.cancel(id); System.out.println("Transaction cancelled.");
                        }
                    }
                    case 9 -> {
                        var rates = controller.activeRates();
                        if (rates.isEmpty()) { System.out.println("No active linked rates. Run the optional demo service setup, or link rates.service_id in phpMyAdmin."); break; }
                        for (var rate : rates) System.out.printf("Rate #%d | %s | %s | PHP %s %s%n",
                                rate.id(), rate.serviceName(), rate.rateName(), rate.price(), rate.unit());
                        int id = input.number("Transaction ID: ");
                        int rateId = input.number("Rate ID: ");
                        int item = controller.addRate(id, rateId, input.decimal("Quantity: "));
                        System.out.println("Added charge #" + item); bills.show(id);
                    }
                    default -> System.out.println("Choose a listed option.");
                }
            } catch (IllegalArgumentException e) { System.out.println(e.getMessage()); }
            catch (SQLException e) { System.out.println("Database error: " + e.getMessage()); }
        }
    }
    private void history() throws SQLException {
        int userId = input.number("Customer ID (0 for all): ");
        var rows = controller.history(userId == 0 ? null : userId);
        if (rows.isEmpty()) System.out.println("No transactions found.");
        for (var row : rows) System.out.printf("#%d | Customer %d | %s | %s | PHP %s%n",
                row.id(), row.userId(), row.createdAt(), row.status(), row.total().toPlainString());
    }
    private void addCharge(boolean fromService) throws SQLException {
        int id = input.number("Transaction ID: ");
        Integer serviceId = null;
        String description = null;
        if (fromService) { services.list(); serviceId = input.number("Active service ID: "); }
        else description = input.text("Charge description (e.g. Computer usage): ");
        BigDecimal quantity = input.decimal("Quantity (e.g. 2 or 1.50): ");
        String unit = input.text("Unit (hour/page/piece): ");
        BigDecimal price = input.decimal("Price per unit in PHP (use your agreed rate): ");
        int itemId = fromService ? controller.addService(id, serviceId, quantity, unit, price)
                : controller.addCharge(id, description, quantity, unit, price);
        System.out.println("Added charge #" + itemId); bills.show(id);
    }
}
