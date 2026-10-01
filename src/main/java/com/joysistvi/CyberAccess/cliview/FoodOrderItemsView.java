package com.joysistvi.CyberAccess.cliview;

import com.joysistvi.CyberAccess.controller.FoodOrderItemsController;
import com.joysistvi.CyberAccess.model.FoodOrderItems;

import java.util.List;
import java.util.Scanner;

public class FoodOrderItemsView {

    private final FoodOrderItemsController controller;
    private final Scanner scanner;

    public FoodOrderItemsView(
            FoodOrderItemsController controller,
            Scanner scanner) {

        this.controller = controller;
        this.scanner = scanner;
    }

    public void run() {

        int choice;

        do {

            printMenu();

            choice = promptChoice();

            switch (choice) {

                case 1 -> viewAll();

                case 2 -> viewByOrder();

                case 3 -> addItem();

                case 4 -> deleteItem();

                case 0 ->
                        System.out.println(
                                "Returning to dashboard..."
                        );

                default ->
                        System.out.println(
                                "Invalid choice. Try again."
                        );
            }

            if (choice != 0) {

                System.out.print(
                        "\nPress Enter to continue..."
                );

                scanner.nextLine();
            }

        } while (choice != 0);
    }

    private void printMenu() {

        System.out.println("\n===== FOOD ORDER ITEMS =====");

        System.out.println("1. View All Food Order ");
        System.out.println("2. View Items by Order ID");
        System.out.println("3. Add Food Order ");
        System.out.println("4. Delete Food Order ");
        System.out.println("0. Back");
    }

    private int promptChoice() {

        System.out.print("Choice: ");

        return readInt();
    }

    private void viewAll() {

        printHeader("ALL FOOD ORDER ITEMS");

        List<FoodOrderItems> items =
                controller.handleGetAll();

        printItems(items);
    }

    private void viewByOrder() {

        printHeader("FOOD ITEMS BY ORDER");

        System.out.print("Order ID: ");

        int orderId = readInt();

        List<FoodOrderItems> items =
                controller.handleGetByOrderId(orderId);

        printItems(items);
    }

    private void addItem() {

        printHeader("ADD FOOD ORDER ITEM");

        System.out.print("Order ID: ");
        int orderId = readInt();

        System.out.print("Food Item ID: ");
        int foodItemId = readInt();

        System.out.print("Quantity: ");
        int quantity = readInt();

        System.out.print("Unit Price: ");
        double unitPrice = readDouble();

        FoodOrderItems item =
                new FoodOrderItems(
                        orderId,
                        foodItemId,
                        quantity,
                        unitPrice
                );

        boolean success =
                controller.handleAdd(item);

        System.out.println(
                success
                        ? "Food order item added successfully."
                        : "Failed to add food order item."
        );
    }

    private void deleteItem() {

        printHeader("DELETE FOOD ORDER ITEM");

        System.out.print(
                "Food Order Item ID: "
        );

        int id = readInt();

        boolean success =
                controller.handleDelete(id);

        System.out.println(
                success
                        ? "Food order item deleted successfully."
                        : "Food order item not found."
        );
    }

    private void printItems(
            List<FoodOrderItems> items) {

        if (items.isEmpty()) {

            System.out.println(
                    "No food order items found."
            );

            return;
        }

        String border =
                "+" + "-".repeat(6) +
                        "+" + "-".repeat(10) +
                        "+" + "-".repeat(14) +
                        "+" + "-".repeat(10) +
                        "+" + "-".repeat(14) + "+";

        System.out.println(border);

        System.out.printf(
                "| %-4s | %-8s | %-12s | %-8s | %-12s |%n",
                "ID",
                "Order ID",
                "Food Item ID",
                "Quantity",
                "Unit Price"
        );

        System.out.println(border);

        for (FoodOrderItems item : items) {

            System.out.printf(
                    "| %-4d | %-8d | %-12d | %-8d | %-12.2f |%n",
                    item.getId(),
                    item.getOrderId(),
                    item.getFoodItemId(),
                    item.getQuantity(),
                    item.getUnitPrice()
            );
        }

        System.out.println(border);
    }

    private void printHeader(String title) {

        System.out.println(
                "\n----- " + title + " -----"
        );
    }

    private int readInt() {

        while (!scanner.hasNextInt()) {

            System.out.print(
                    "Please enter a valid number: "
            );

            scanner.next();
        }

        int value = scanner.nextInt();

        scanner.nextLine();

        return value;
    }

    private double readDouble() {

        while (!scanner.hasNextDouble()) {

            System.out.print(
                    "Please enter a valid amount: "
            );

            scanner.next();
        }

        double value = scanner.nextDouble();

        scanner.nextLine();

        return value;
    }
}