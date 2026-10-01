package com.joysistvi.CyberAccess.cliview;

import com.joysistvi.CyberAccess.controller.FeedbackController;
import com.joysistvi.CyberAccess.model.Feedback;

import java.util.List;
import java.util.Scanner;

public class FeedbackView {

    private final FeedbackController feedbackController;
    private final Scanner scanner;

    public FeedbackView(FeedbackController feedbackController, Scanner scanner) {
        this.feedbackController = feedbackController;
        this.scanner = scanner;
    }

    public void run() {
        int choice;

        do {
            clearScreen();
            printMenu();
            choice = promptChoice();

            switch (choice) {
                case 1 -> viewAllFeedback();
                case 2 -> addFeedback();
                case 3 -> deleteFeedback();
                case 0 -> System.out.println("Returning to dashboard...");
                default -> System.out.println("Invalid choice. Try again.");
            }

            if (choice != 0) {
                System.out.print("\nPress Enter to continue...");
                scanner.nextLine();
            }

        } while (choice != 0);
    }

    private void printMenu() {
        System.out.println("\n===== FEEDBACK =====");
        System.out.println("1. View All Feedback");
        System.out.println("2. Add Feedback");
        System.out.println("3. Delete Feedback");
        System.out.println("0. Back");
    }

    private int promptChoice() {
        System.out.print("Choice: ");
        return readInt();
    }

    private void viewAllFeedback() {
        List<Feedback> feedbackList =
                feedbackController.handleViewAllFeedback();

        printFeedback(feedbackList);
    }

    private void addFeedback() {
        System.out.print("User ID: ");
        int userId = readInt();

        System.out.print("Comment: ");
        String comment = scanner.nextLine();

        Feedback feedback = new Feedback(comment, userId);

        boolean success =
                feedbackController.handleAddFeedback(feedback);

        System.out.println(
                success
                        ? "Feedback added successfully."
                        : "Failed to add feedback."
        );
    }

    private void deleteFeedback() {
        System.out.print("Feedback ID to delete: ");
        int id = readInt();

        boolean success =
                feedbackController.handleDeleteFeedback(id);

        System.out.println(
                success
                        ? "Feedback deleted successfully."
                        : "Failed to delete feedback."
        );
    }

    private void printFeedback(List<Feedback> feedbackList) {

        if (feedbackList.isEmpty()) {
            System.out.println("No feedback found.");
            return;
        }

        String border =
                "+" + "-".repeat(6) +
                        "+" + "-".repeat(32) +
                        "+" + "-".repeat(10) + "+";

        System.out.println(border);
        System.out.printf(
                "| %-4s | %-30s | %-8s |%n",
                "ID",
                "Comment",
                "User ID"
        );
        System.out.println(border);

        for (Feedback feedback : feedbackList) {
            System.out.printf(
                    "| %-4d | %-30s | %-8d |%n",
                    feedback.getId(),
                    feedback.getComment(),
                    feedback.getUserId()
            );
        }

        System.out.println(border);
    }

    private void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    private int readInt() {
        while (!scanner.hasNextInt()) {
            System.out.print("Please enter a valid number: ");
            scanner.next();
        }

        int value = scanner.nextInt();
        scanner.nextLine();

        return value;
    }
}