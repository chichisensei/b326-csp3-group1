package com.joysistvi.CyberAccess.cliview;

import com.joysistvi.CyberAccess.controller.RatesController;
import com.joysistvi.CyberAccess.model.Rates;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;
import java.util.Scanner;

public class RatesView {

    private final RatesController controller;
    private final Scanner scanner;

    public RatesView() {
        this(new RatesController(), new Scanner(System.in));
    }

    // Lets the main menu share one Scanner across all views
    public RatesView(RatesController controller, Scanner scanner) {
        this.controller = controller;
        this.scanner = scanner;
    }

    public void show() {
        boolean running = true;
        while (running) {
            System.out.println();
            System.out.println("===== RATES =====");
            System.out.println("1. View all rates");
            System.out.println("2. View active rates");
            System.out.println("3. View rates by type");
            System.out.println("4. Add rate");
            System.out.println("5. Edit rate");
            System.out.println("6. Change price");
            System.out.println("7. Activate / deactivate rate");
            System.out.println("8. Delete rate");
            System.out.println("0. Back");
            System.out.print("Choose: ");

            String choice = scanner.nextLine().trim();
            try {
                switch (choice) {
                    case "1" -> printList(controller.getAll());
                    case "2" -> printList(controller.getActive());
                    case "3" -> viewByType();
                    case "4" -> add();
                    case "5" -> edit();
                    case "6" -> changePrice();
                    case "7" -> toggleStatus();
                    case "8" -> delete();
                    case "0" -> running = false;
                    default -> System.out.println("Invalid choice.");
                }
            } catch (IllegalArgumentException e) {
                System.out.println("Error: " + e.getMessage());
            } catch (SQLIntegrityConstraintViolationException e) {
                System.out.println("Error: this rate is used by other records (e.g. sessions) "
                        + "and cannot be removed. Deactivate it instead.");
            } catch (SQLException e) {
                System.out.println("Database error: " + e.getMessage());
            }
        }
    }

    private void printList(List<Rates> list) {
        if (list.isEmpty()) {
            System.out.println("No rates found.");
            return;
        }
        System.out.println(String.format("%-4s %-28s %-12s %8s %-10s %s",
                "ID", "Rate", "Type", "Price", "Unit", "Status"));
        System.out.println("-".repeat(75));
        list.forEach(System.out::println);
    }

    private void viewByType() throws SQLException {
        System.out.print("Rate type (e.g. Computer, Printing, Photocopy): ");
        printList(controller.getByType(scanner.nextLine()));
    }

    private void add() throws SQLException {
        System.out.print("Rate name: ");
        String name = scanner.nextLine();
        System.out.print("Rate type (e.g. Computer, Printing, Photocopy): ");
        String type = scanner.nextLine();
        BigDecimal price = readPrice("Price: ");
        System.out.print("Unit (e.g. per hour, per page): ");
        String unit = scanner.nextLine();

        // service_id is not linked to a service yet, matching the existing rows (0)
        Rates created = controller.add(0, name, type, price, unit);
        System.out.println("Added: " + created.getRateName() + " (ID " + created.getId() + ")");
    }

    private void edit() throws SQLException {
        int id = readInt("Rate ID to edit: ");
        Rates current = controller.getById(id);
        System.out.println("Press Enter to keep the current value.");

        System.out.print("Name [" + current.getRateName() + "]: ");
        String name = orKeep(scanner.nextLine(), current.getRateName());

        System.out.print("Type [" + current.getRateType() + "]: ");
        String type = orKeep(scanner.nextLine(), current.getRateType());

        System.out.print("Price [" + current.getPrice().toPlainString() + "]: ");
        String priceInput = scanner.nextLine().trim();
        BigDecimal price = priceInput.isEmpty() ? current.getPrice() : parsePrice(priceInput);

        System.out.print("Unit [" + current.getUnit() + "]: ");
        String unit = orKeep(scanner.nextLine(), current.getUnit());

        controller.update(id, current.getServiceId(), name, type, price, unit, current.getStatus());
        System.out.println("Rate updated.");
    }

    private void changePrice() throws SQLException {
        int id = readInt("Rate ID: ");
        Rates current = controller.getById(id);
        System.out.println(current.getRateName() + " is currently " + current.getPrice().toPlainString()
                + " " + current.getUnit() + ".");

        controller.changePrice(id, readPrice("New price: "));
        System.out.println("Price updated.");
    }

    private void toggleStatus() throws SQLException {
        int id = readInt("Rate ID: ");
        Rates current = controller.getById(id);

        String next = Rates.STATUS_ACTIVE.equals(current.getStatus())
                ? Rates.STATUS_INACTIVE : Rates.STATUS_ACTIVE;

        System.out.print(current.getRateName() + " is " + current.getStatus()
                + ". Set to " + next + "? (y/N): ");
        if (scanner.nextLine().trim().equalsIgnoreCase("y")) {
            controller.changeStatus(id, next);
            System.out.println("Status updated.");
        } else {
            System.out.println("Cancelled.");
        }
    }

    private void delete() throws SQLException {
        int id = readInt("Rate ID to delete: ");
        Rates current = controller.getById(id);

        System.out.print("Delete " + current.getRateName() + "? (y/N): ");
        if (scanner.nextLine().trim().equalsIgnoreCase("y")) {
            controller.delete(id);
            System.out.println("Rate deleted.");
        } else {
            System.out.println("Cancelled.");
        }
    }

    // ---------- input helpers ----------

    private String orKeep(String input, String current) {
        return input.isBlank() ? current : input;
    }

    private int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            String line = scanner.nextLine().trim();
            try {
                return Integer.parseInt(line);
            } catch (NumberFormatException e) {
                System.out.println("Please enter a whole number.");
            }
        }
    }

    private BigDecimal readPrice(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return parsePrice(scanner.nextLine().trim());
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private BigDecimal parsePrice(String input) {
        try {
            return new BigDecimal(input);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Please enter a valid amount, e.g. 25 or 12.50.");
        }
    }
}